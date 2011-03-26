// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import fit.ColumnFixture;
import fit.Fixture;
import fit.Parse;
import fit.RowFixture;


public class AllPairs extends AllCombinations {

    public int rank;
    public int steps=0;
    public Map<String, Item> toItem = new HashMap<String, Item>();
    public List<Var> vars = new ArrayList<Var>();
    public SortedSet<Pair> pairs = new TreeSet<Pair>();

    protected void combinations() {
        populate();
        generate();
    }

    // Populate /////////////////////////////////

    protected void populate() {
        doAllVars();
        doAllVarPairs();
    }

    protected void doAllVars() {
        rank = 0;
        for (int i=0; i<lists.size(); i++) {
            List<File> files = lists.get(i);
            Var var = new Var(i, files);
            vars.add(var);
            doAllItems(var, files);
        }
    }

    protected void doAllItems(Var var, List<File> files) {
        for (int i=0; i<files.size(); i++) {
            Item item = new Item (var, i, rank++);
            toItem.put((files.get(i)).getName(), item);
            var.items.add(item);
        }
    }

    protected void doAllVarPairs() {
        for (int i=0; i<vars.size(); i++) {
            for (int j=i+1; j<vars.size(); j++) {
                doAllItemPairs((Var)vars.get(i), (Var)vars.get(j));
            }
        }
    }

    protected void doAllItemPairs(Var vl, Var vr) {
        for (int l=0; l<vl.size(); l++) {
            for (int r=0; r<vr.size(); r++) {
                pairs.add(new Pair(vl.get(l), vr.get(r)));
            }
        }
    }

    // Generate /////////////////////////////////

    protected void generate() {
        while(((Pair)pairs.first()).used == 0) {
            emit(nextCase());
        }
    }

    private Item[] nextCase() {
        Item slug[] = new Item[vars.size()];
        while (!isFull(slug)) {
            Pair p=nextFit(slug);
            fill(slug, p);
        }
        return slug;
    }

    protected void fill(Item[] slug, Pair pair) {
        slug[pair.left.var.index] = pair.left;
        slug[pair.right.var.index] = pair.right;
        pair.used++;
        pairs.add(pair);
    }

    protected boolean isFull (Item[]slug) {
        for (int i=0; i<slug.length; i++) {
            if (slug[i]==null) {
                return false;
            }
        }
        return true;
    }

    protected Pair nextFit(Item[] slug) {
        List<Pair> hold = new ArrayList<Pair>();
        Pair pair;
        while(!(pair=nextPair()).isFit(slug)) {
            hold.add(pair);
        }
        pairs.addAll(hold);
        return pair;
    }

    protected Pair nextPair() {
        Pair first = (Pair)pairs.first();
        pairs.remove(first);
        steps++;
        return first;
    }

    protected void emit (Item[] slug) {
        List<File> combination = new ArrayList<File>();
        for (int i=0; i<slug.length; i++) {
            combination.add(slug[i].file());
        }
        doCase(combination);
    }

    // Helper Classes ///////////////////////////

    public class Var {
        List<File> files;
        int index;
        List<Item> items = new ArrayList<Item>();
        Var (int index, List<File> files)     {this.index = index; this.files = files;}
        int size()                      {return items.size();}
        Item get(int index)             {return (Item)items.get(index);}
    }

    public class Item {
        Var var;
        int index;
        int rank;
        Item (Var var, int i, int n)    {this.var = var; index = i; rank=n;}
        File file()                     {return (File)var.files.get(index);}
        public String toString()        {return file().getName();}
        boolean isFit(Item[]slug)       {return slug[var.index]==null || slug[var.index]==this;}
    }

    public class Pair implements Comparable<Pair> {
        public Item left, right;
        public int used = 0;
        Pair (Item left, Item right)    {this.left=left; this.right=right;}
        public String toString()        {return left+"-"+right+" ("+used+")";}
        boolean isFit(Item[]slug)       {return left.isFit(slug) && right.isFit(slug);}
        public int rank()               {return rank*(rank*used+left.rank)+right.rank;}
        public int compareTo(Pair o)  {return rank()-((Pair)o).rank();}
    }

    // Self Test Classes ////////////////////////

    public static AllPairs fut;
    public static List<Item[]> cases;

    public static class Setup extends Fixture {

        public void doTable(Parse table) {
            fut = new AllPairs();
            cases = new ArrayList<Item[]>();
            super.doTable(table);
            fut.populate();
        }

        public void doCell(Parse cell, int i) {
            if (!cell.text().equals("")) {
                while(fut.lists.size()<=i) {
                    fut.lists.add(new ArrayList<File>());
                }
                (fut.lists.get(i)).add(new File(cell.text()));
                right(cell);
            }
        }
    }


    public static class Cases extends RowFixture {
        public static class Case {
            public int number;
            public Item[] items;
            Case(int n, Item[] i) {number=n; items=i;}
        }
        public Object[] query() {
            while((fut.pairs.first()).used==0) {
                cases.add(fut.nextCase());
            }
            List<Case> result = new ArrayList<Case>();
            for (int i=0; i<cases.size(); i++) {
                result.add(new Case(i+1, cases.get(i)));
            }
            return result.toArray();
        }
        public Class<?> getTargetClass(){
            return Case.class;
        }
    }

    public static class Pairs extends RowFixture {
        public Object[] query () {
            return fut.pairs.toArray();
        }
        public Class<?> getTargetClass() {
            return Pair.class;
        }
        public Object parse (String s, Class<?> type) throws Exception {
            if (s.equals("null"))                   {return null;}
            if (type.equals(Item.class))            {return fut.toItem.get(s);}
            return super.parse(s, type);
        }
    }

    public static class Step extends ColumnFixture {
        static Pair next;
        static Item slug[] = new Item[3];
        static List<Pair> hold = new ArrayList<Pair>();

        public String next() {
            next=fut.nextPair();
            hold.add(next);
            return next.toString();
        }

        public String nextFit() {
            next=fut.nextFit(slug);
            hold.add(next);
            return next.toString();
        }

        public int rank() {
            return next.rank();
        }

        public boolean isFit() {
            boolean isFit = next.isFit(slug);
            if (isFit) {
                fut.fill(slug, next);
                fut.pairs.addAll(hold);
                hold=new ArrayList<Pair>();
            }
            return isFit;
        }

        public int hold() {
            return hold.size();
        }

        public Item[] slug() {
            return slug;
        }

        public boolean isFull() {
            boolean result = fut.isFull(slug);
            if(result){
                cases.add(slug);
                slug=new Item[3];
            }
            return result;
        }

        public Object parse (String s, Class<?> type) throws Exception {
            if (s.equals("null"))                   {return null;}
            if (type.equals(Item.class))            {return fut.toItem.get(s);}
            return super.parse(s, type);
        }
    }

    public static class Stats extends ColumnFixture {
        public int[] items;
        public int pairs;
        public int steps;
        public long msec;

        public int cases() {
            AllPairs ap = new AllPairs();
            setup(ap);
            ap.populate();
            return generate(ap);
        }

        void setup(AllPairs ap) {
            int name=0;
            for (int i=0; i<items.length; i++) {
                List<File> l = new ArrayList<File>();
                for (int j=0; j<items[i]; j++) {
                    l.add(new File(Integer.toString(name++)));
                }
                ap.lists.add(l);
            }
        }

        int generate(AllPairs ap) {
            int cases=0;
            msec = System.currentTimeMillis();
            while(((Pair)ap.pairs.first()).used == 0) {
                ap.nextCase();
                cases++;
            }
            pairs=ap.pairs.size();
            steps=ap.steps;
            msec = System.currentTimeMillis()-msec;
            return cases;
        }
    }
}