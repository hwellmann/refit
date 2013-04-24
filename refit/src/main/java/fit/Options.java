/*
 * Copyright 2013 Harald Wellmann
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

/**
 * Options for customizing Fit execution.
 * 
 * @author Harald Wellmann
 * @version $Rev: 36713 $ $Date: 2011-08-02 12:04:03 +0200 (Di, 02 Aug 2011) $
 * @since 24.04.2013
 */
public class Options {

    private static Boolean keepSmartQuotes;

    /**
     * Should the parser keep smart quotes in HTML cells? The default is false, i.e. typographic
     * quotes will be replaced by regular quotes.
     * 
     * @return true if smart quotes shall be kept
     */
    public static boolean isKeepSmartQuotes() {
        if (keepSmartQuotes == null) {
            String property = System.getProperty("fit.keepSmartQuotes", "false");
            keepSmartQuotes = Boolean.parseBoolean(property);
        }
        return keepSmartQuotes;
    }
}
