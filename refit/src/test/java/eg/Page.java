// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import fit.*;
import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class Page extends RowFixture {
    static URL url;
    static String text;

    // actions //////////////////////////////////

    public void location(String url) throws Exception {
        Page.url = new URL(url);
        Page.text = get(Page.url);
    }

    public String title() throws Exception {
        return new Parse(text, new String[]{"title"}).text();
    }

    public void link (String label) throws Exception {
        Parse links = new Parse (text, new String[]{"a"});
        while (!links.text().startsWith(label)) {
            links = links.more;
        }
        StringTokenizer tokens = new StringTokenizer(links.tag, "<> =\"");
        while (!tokens.nextToken().toLowerCase().equals("href")) {};
        url = new URL(url, tokens.nextToken());
        text = get(url);
    }

    // rows /////////////////////////////////////

    public Class<?> getTargetClass() {
        return Row.class;
    }

    public Object[] query() throws Exception {
        String tags[] = {"wiki", "table", "tr", "td"};
        Parse rows = new Parse(text, tags).at(0, 0, 2);
        Row result[] = new Row[rows.size()];
        for (int i=0; i<result.length; i++) {
            result[i] = new Row(rows);
            rows = rows.more;
        }
        return result;
    }

    public class Row {
        Parse cells;

        Row(Parse row) {
            this.cells = row.parts;
        }

        public String numerator() {
            return cells.at(0).text();
        }

        public String denominator() {
            return cells.at(1).text();
        }

        public String quotient() {
            return cells.at(2).text();
        }
    }


    // utilities ////////////////////////////////

    private String get(URL url) throws IOException {
        Page.url = url;
        InputStream stream =  (InputStream) url.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer(10000);
        for (String line; (line = reader.readLine()) != null; ) {
            buffer.append(line);
            buffer.append('\n');
        }
        return buffer.toString();
    }



}