package eg;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.*;

public class Calculator extends ColumnFixture {

    public float volts;
    public String key;

    public static HP35 hp = new HP35();

    public boolean points() {
        return false;
    }

    public boolean flash() {
        return false;
    }

    public float watts() {
        return 0.5f;
    }

    public void reset () {
        key = null;
    }

    public void execute () throws Exception {
        if (key != null) {
            hp.key(key);
        }
    }

    public ScientificDouble x() {
        return new ScientificDouble(hp.r[0]);
    }

    public ScientificDouble y() {
        return new ScientificDouble(hp.r[1]);
    }

    public ScientificDouble z() {
        return new ScientificDouble(hp.r[2]);
    }

    public ScientificDouble t() {
        return new ScientificDouble(hp.r[3]);
    }


    static class HP35 {

        double r[] = {0,0,0,0};
        double s=0;

        public void key(String key) throws Exception {
            if (numeric(key))               {push(Double.parseDouble(key));}
            else if (key.equals("enter"))   {push();}
            else if (key.equals("+"))       {push(pop()+pop());}
            else if (key.equals("-"))       {double t=pop(); push(pop()-t);}
            else if (key.equals("*"))       {push(pop()*pop());}
            else if (key.equals("/"))       {double t=pop(); push(pop()/t);}
            else if (key.equals("x^y"))     {push(Math.exp(Math.log(pop())*pop()));}
            else if (key.equals("clx"))     {r[0]=0;}
            else if (key.equals("clr"))     {r[0]=r[1]=r[2]=r[3]=0;}
            else if (key.equals("chs"))     {r[0]=-r[0];}
            else if (key.equals("x<>y"))    {double t=r[0]; r[0]=r[1]; r[1]=t;}
            else if (key.equals("r!"))      {r[3]=pop();}
            else if (key.equals("sto"))     {s=r[0];}
            else if (key.equals("rcl"))     {push(s);}
            else if (key.equals("sqrt"))    {push(Math.sqrt(pop()));}
            else if (key.equals("ln"))      {push(Math.log(pop()));}
            else if (key.equals("sin"))     {push(Math.sin(Math.toRadians(pop())));}
            else if (key.equals("cos"))     {push(Math.cos(Math.toRadians(pop())));}
            else if (key.equals("tan"))     {push(Math.tan(Math.toRadians(pop())));}
            else {throw new Exception("can't do key: "+key);
            }
        }

        boolean numeric (String key) {
            return key.length()>= 1 &&
                (Character.isDigit(key.charAt(0)) ||
                    (key.length()>= 2 &&
                        (key.charAt(0) == '-' &&
                            Character.isDigit(key.charAt(1)))));
        }

        void push() {
            for (int i=3; i>0; i--) {
                r[i] = r[i-1];
            }
        }

        void push(double value) {
            push();
            r[0] = value;
        }

        double pop() {
            double result = r[0];
            for (int i=0; i<3; i++) {
                r[i] = r[i+1];
            }
            return result;
        }
    }
}