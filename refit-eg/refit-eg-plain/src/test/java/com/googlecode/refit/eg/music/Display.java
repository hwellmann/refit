// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.eg.music;

import java.util.Date;

public class Display extends fit.RowFixture {

    public Class<?> getTargetClass() {
        return Music.class;
    }

    public Object[] query() {
        return MusicLibrary.displayContents();
    }

    @SuppressWarnings("rawtypes")
	public Object parse (String s, Class type) throws Exception {
        if (type.equals(Date.class))    {return Music.dateFormat.parse(s);}
        return super.parse (s, type);
    }

}
