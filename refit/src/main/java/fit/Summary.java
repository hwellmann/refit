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

public class Summary extends Fixture {
    public static String countsKey = "counts";

    public void doTable(Parse table) {
        getSummary().put(countsKey, counts());
        SortedSet<String> keys = new TreeSet<String>(getSummary().keySet());
        table.parts.more = rows(keys.iterator());
    }

    protected Parse rows(Iterator<?> keys) {
        if (keys.hasNext()) {
            Object key = keys.next();
            Parse result = tr(td(key.toString(), td(getSummary().get(key).toString(), null)), rows(keys));
            if (key.equals(countsKey)) {
                mark(result);
            }
            return result;
        }
        else {
            return null;
        }
    }

    protected Parse tr(Parse parts, Parse more) {
        return new Parse("tr", null, parts, more);
    }

    protected Parse td(String body, Parse more) {
        return new Parse("td", info(body), null, more);
    }

    protected void mark(Parse row) {
        // mark summary good/bad without counting beyond here
        Counts official = getCounts();
        setCounts(new Counts());
        Parse cell = row.parts.more;
        if (official.wrong + official.exceptions > 0) {
            wrong(cell);
        }
        else {
            right(cell);
        }
        setCounts(official);
    }

}
