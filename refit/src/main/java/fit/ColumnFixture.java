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

public class ColumnFixture extends Fixture {

    protected TypeAdapter columnBindings[];
    protected boolean hasExecuted = false;

    // Traversal ////////////////////////////////

    public void doRows(Parse rows) {
        bind(rows.parts);
        super.doRows(rows.more);
    }

    public void doRow(Parse row) {
        hasExecuted = false;
        try {
            reset();
            super.doRow(row);
            if (!hasExecuted) {
                execute();
            }
        } catch (Exception e) {
            exception (row.leaf(), e);
        }
    }

    public void doCell(Parse cell, int column) {
        TypeAdapter a = columnBindings[column];
        try {
            String text = cell.text();
            if (text.equals("")) {
                check(cell, a);
            } else if (a == null) {
                ignore(cell);
            } else if (a.field != null) {
                a.set(a.parse(text));
            } else if (a.method != null) {
                check(cell, a);
            }
        } catch(Exception e) {
            exception(cell, e);
        }
    }

    public void check(Parse cell, TypeAdapter a) {
        if (!hasExecuted) {
            try {
                execute();
            } catch (Exception e) {
                exception (cell, e);
            }
            hasExecuted = true;
        }
        super.check(cell, a);
    }

    public void reset() throws Exception {
        // about to process first cell of row
    }

    public void execute() throws Exception {
        // about to process first method call of row
    }

    // Utility //////////////////////////////////

    protected void bind (Parse heads) {
        columnBindings = new TypeAdapter[heads.size()];
        for (int i=0; heads!=null; i++, heads=heads.more) {
            String name = heads.text();
            String suffix = "()";
            try {
                if (name.equals("")) {
                    columnBindings[i] = null;
                } else if (name.endsWith(suffix)) {
                    columnBindings[i] = bindMethod(name.substring(0,name.length()-suffix.length()));
                } else {
                    columnBindings[i] = bindField(name);
                }
            }
            catch (Exception e) {
                exception (heads, e);
            }
        }

    }

    protected TypeAdapter bindMethod (String name) throws Exception {
        return TypeAdapter.on(this, getTargetClass().getMethod(camel(name)));
    }

    protected TypeAdapter bindField (String name) throws Exception {
        return TypeAdapter.on(this, getTargetClass().getField(camel(name)));
    }

    protected Class<?> getTargetClass() {
        return getClass();
    }
}
