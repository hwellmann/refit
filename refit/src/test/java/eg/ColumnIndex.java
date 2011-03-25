// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import fit.*;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.*;

public class ColumnIndex extends RowFixture {

    Parse rows;

    public void doRows(Parse rows) {
        this.rows = rows;
        super.doRows(rows);
    }

    public Class<?> getTargetClass() {
        return Column.class;
    }

    public Object[] query() throws ClassNotFoundException {
        // first find what classes are mentioned in the table
        Set<String> names = new HashSet<String>();
        int column=0;
        for (Parse cell=rows.parts; cell != null; column++, cell = cell.more) {
            if (cell.text().equals("className")) {
                break;
            }
        }
        for (Parse row = rows.more; row != null; row = row.more) {
            names.add(row.at(0,column).text());
        }
        // then find the columns in these classes
        ArrayList<Column> columns = new ArrayList<Column>();
        for (Iterator<String> i=names.iterator(); i.hasNext(); ) {
            Class<?> each = Class.forName((String)i.next());
            Field f[] = each.getFields();
            for (int j=0; j<f.length; j++) {
                if(f[j].getModifiers()==1) {
                    columns.add(new Column(f[j]));
                }
            }
            Method m[] = each.getMethods();
            for (int j=0; j<m.length; j++) {
                if(m[j].getParameterTypes().length == 0 && m[j].getModifiers()==1) {
                    columns.add(new Column(m[j]));
                }
            }
        }
        return columns.toArray();
    }

    public Object parse (String text, Class<?> type) throws Exception {
        if (type.equals(Class.class)) {return parseClass(text);}
        return super.parse(text, type);
    }

    Class<?> parseClass(String name) throws Exception {
        if (name.equals("byte")) {return Byte.TYPE;}
        if (name.equals("short")) {return Short.TYPE;}
        if (name.equals("int")) {return Integer.TYPE;}
        if (name.equals("long")) {return Long.TYPE;}
        if (name.equals("float")) {return Float.TYPE;}
        if (name.equals("double")) {return Double.TYPE;}
        if (name.equals("char")) {return Character.TYPE;}
        return Class.forName(name);
    }

    public class Column {
        public Object column;
        public Class<?> className;
        public String columnName;
        public Class<?> columnType;

        Column (Field f) {
            column = f;
            className = f.getDeclaringClass();
            columnName = f.getName();
            columnType = f.getType();
        }

        Column (Method m) {
            column = m;
            className = m.getDeclaringClass();
            columnName = m.getName() + "()";
            columnType = m.getReturnType();
        }
    }
}


