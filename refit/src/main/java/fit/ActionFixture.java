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

import java.lang.reflect.Method;

public class ActionFixture extends Fixture {
    protected Parse cells;
    public static Fixture actor;

    // Traversal ////////////////////////////////

    public void doCells(Parse cells) {
        this.cells = cells;
        try {
            Method action = getClass().getMethod(cells.text());
            action.invoke(this);
        }
        catch (Exception e) {
            exception(cells, e);
        }
    }

    // Actions //////////////////////////////////

    public void start() throws Exception {
        actor = getFixtureInstanceOf(cells.more.text());
    }

    public void enter() throws Exception {
        Method method = method(1);
        Class<?> type = method.getParameterTypes()[0];
        String text = cells.more.more.text();
        Object args[] = { TypeAdapter.on(actor, type).parse(text) };
        method.invoke(actor, args);
    }

    public void press() throws Exception {
        method(0).invoke(actor);
    }

    public void check() throws Exception {
        TypeAdapter adapter = TypeAdapter.on(actor, method(0));
        check(cells.more.more, adapter);
    }

    // Utility //////////////////////////////////

    protected Method method(int args) throws NoSuchMethodException {
        return method(camel(cells.more.text()), args);
    }

    protected Method method(String test, int args) throws NoSuchMethodException {
        Method methods[] = actor.getClass().getMethods();
        Method result = null;
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (m.getName().equals(test) && m.getParameterTypes().length == args) {
                if (result == null) {
                    result = m;
                }
                else {
                    throw new NoSuchMethodException("too many implementations");
                }
            }
        }
        if (result == null) {
            throw new NoSuchMethodException();
        }
        return result;
    }
}
