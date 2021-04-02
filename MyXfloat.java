// MyXfloat.java
//   sample Xfloat program

public class MyXfloat extends Xfloat {

  public MyXfloat(){super();}
  public MyXfloat(float f){super(f);}
  public MyXfloat(byte sign, byte exp, int man){super(sign, exp, man);}

  public Xfloat xadd(Xfloat y) {return new MyXfloat((byte)0,(byte)0,0);}
  public Xfloat xmult(Xfloat y) {return new MyXfloat((byte)0,(byte)0,0);}

  public static void main(String arg[]) {
    if (arg.length < 2) return;
    float x = Float.valueOf(arg[0]).floatValue(),
          y = Float.valueOf(arg[1]).floatValue();
    Xfloat xf, yf, zf, wf;
    xf = new MyXfloat(x);
    yf = new MyXfloat(y);    
    zf = xf.xmult(yf);
    wf = xf.xadd(yf);
    System.out.println("x:   "+xf+" "+x);
    System.out.println("y:   "+yf+" "+y);
    System.out.println("x*y: "+zf+" "+zf.toFloat());
    System.out.println("x+y: "+wf+" "+wf.toFloat());
  }
}
