/*
 * Copyright 2011 Harald Wellmann
 * Copyright (c) 2002, 2008 Cunningham & Cunningham, Inc.
 *
 * This file is part of reFit.
 * 
 * reFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * reFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with reFit.  If not, see <http://www.gnu.org/licenses/>.
 */
package fit;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
abstract public class RowFixture extends ColumnFixture {

    public Object results[];
    public List<Parse> missing = new LinkedList<Parse>();
    public List surplus = new LinkedList();


    public void doRows(Parse rows) {
        try {
            bind(rows.parts);
            results = query();
            match(list(rows.more), list(results), 0);
            Parse last = rows.last();
            last.more = buildRows(surplus.toArray());
            mark(last.more, "surplus");
            mark(missing.iterator(), "missing");
        } catch (Exception e) {
            exception (rows.leaf(), e);
        }
    }

    abstract public Object[] query() throws Exception;  // get rows to be compared
    abstract public Class getTargetClass();             // get expected type of row

    protected void match(List expected, List computed, int col) {
        if (col >= columnBindings.length) {
            check (expected, computed);
        } else if (columnBindings[col] == null) {
            match (expected, computed, col+1);
        } else {
            Map eMap = eSort(expected, col);
            Map cMap = cSort(computed, col);
            Set keys = union(eMap.keySet(),cMap.keySet());
            for (Iterator i=keys.iterator(); i.hasNext(); ) {
                Object key = i.next();
                List eList = (List)eMap.get(key);
                List cList = (List)cMap.get(key);
                if (eList == null) {
                    surplus.addAll(cList);
                } else if (cList == null) {
                    missing.addAll(eList);
                } else if (eList.size()==1 && cList.size()==1) {
                    check(eList, cList);
                } else {
                    match(eList, cList, col+1);
                }
            }
        }
    }

    protected List<Parse> list (Parse rows) {
        List<Parse> result = new LinkedList<Parse>();
        while (rows != null) {
            result.add(rows);
            rows = rows.more;
        }
        return result;
    }

    protected List<?> list (Object[] rows) {
        List<Object> result = new LinkedList<Object>();
        for (int i=0; i<rows.length; i++) {
            result.add(rows[i]);
        }
        return result;
    }

    protected Map eSort(List<Parse> list, int col) {
        TypeAdapter a = columnBindings[col];
        Map result = new HashMap(list.size());
        for (Iterator<Parse> i=list.iterator(); i.hasNext(); ) {
            Parse row = i.next();
            Parse cell = row.parts.at(col);
            try {
                Object key = a.parse(cell.text());
                bin(result, key, row);
            } catch (Exception e) {
                exception(cell, e);
                for (Parse rest=cell.more; rest!=null; rest=rest.more) {
                    ignore(rest);
                }
            }
        }
        return result;
    }

    protected Map cSort(List list, int col) {
        TypeAdapter a = columnBindings[col];
        Map result = new HashMap(list.size());
        for (Iterator<?> i=list.iterator(); i.hasNext(); ) {
            Object row = i.next();
            try {
                a.target = row;
                Object key = a.get();
                bin(result, key, row);
            } catch (Exception e) {
                // surplus anything with bad keys, including null
                surplus.add(row);
            }
        }
        return result;
    }

    protected void bin (Map map, Object key, Object row) {
        if (key.getClass().isArray()) {
            key = Arrays.asList((Object[])key);
        }
        if (map.containsKey(key)) {
            ((List)map.get(key)).add(row);
        } else {
            List list = new LinkedList();
            list.add(row);
            map.put(key, list);
        }
    }

    protected Set union (Set a, Set b) {
        Set result = new HashSet();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    protected void check (List<Parse> eList, List<Parse> cList) {
        if (eList.size()==0) {
            surplus.addAll(cList);
            return;
        }
        if (cList.size()==0) {
            missing.addAll(eList);
            return;
        }
        Parse row = eList.remove(0);
        Parse cell = row.parts;
        Object obj = cList.remove(0);
        for (int i=0; i<columnBindings.length && cell!=null; i++) {
            TypeAdapter a = columnBindings[i];
            if (a != null) {
                a.target = obj;
            }
            check(cell, a);
            cell = cell.more;
        }
        check (eList, cList);
    }

    protected void mark(Parse rows, String message) {
        String annotation = label(message);
        while (rows != null) {
            wrong(rows.parts);
            rows.parts.addToBody(annotation);
            rows = rows.more;
        }
    }

    protected void mark(Iterator<Parse> rows, String message) {
        String annotation = label(message);
        while (rows.hasNext()) {;
            Parse row = rows.next();
            wrong(row.parts);
            row.parts.addToBody(annotation);
        }
    }

    protected Parse buildRows(Object[] rows) {
        Parse root = new Parse(null ,null, null, null);
        Parse next = root;
        for (int i=0; i<rows.length; i++) {
            next = next.more = new Parse("tr", null, buildCells(rows[i]), null);
        }
        return root.more;
    }

    protected Parse buildCells(Object row) {
        if (row == null) {
            Parse nil = new Parse("td", "null", null, null);
            nil.addToTag(" colspan="+columnBindings.length);
            return nil;
        }
        Parse root = new Parse(null, null, null, null);
        Parse next = root;
        for (int i=0; i<columnBindings.length; i++) {
            next = next.more = new Parse("td", "&nbsp;", null, null);
            TypeAdapter a = columnBindings[i];
            if (a == null) {
                ignore (next);
            } else {
                try {
                    a.target = row;
                    info(next, a.toString(a.get()));
                } catch (Exception e) {
                    exception(next, e);
                }
            }
        }
        return root.more;
    }
}
