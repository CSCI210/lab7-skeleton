// MyXfloat.java
//   sample Xfloat program

public class MyXfloat extends Xfloat {

  public MyXfloat(){super();}
  public MyXfloat(float f){super(f);}
  public MyXfloat(byte sign, byte exp, int man){super(sign, exp, man);}
  
  private String toBinaryString(int val, int len) {
      len = Math.min(len, 32);
      char[] ans = new char[len];
      int mask = 1;
      for (int i = 0; i < len; i++) {
        ans[len-(i+1)] = (char)(((val & mask) >>> i) + (int)'0');
        mask <<= 1;
      }
      return new String(ans);
  }
  
  private boolean xLy(int x, int y) {
      int xabs = Math.abs(x);
      int yabs = Math.abs(y);
//      System.out.println(xabs);
//      System.out.println(yabs);
      return (x < y);

  }
  
  private int absDiff(int x, int y) {
      return Math.abs((Math.abs(x) - Math.abs(y)));
  }
  
  private boolean isXfloatZero(Xfloat x) {
      return (x.sign == 0) && (x.exp == 0) && (x.man == 0);
  }

  public Xfloat xadd(Xfloat y) {
      
      //check if any are zero, return other if so, also if both zero, then return zero
      if (isXfloatZero(this)) {
	  return y;
      }
      if (isXfloatZero(y)) {
	  return this;
      }
      
      if (isXfloatZero(this) && isXfloatZero(y)) {
	  return new MyXfloat(0);
      }
      
    //set up temps
      Xfloat xtemp = new MyXfloat(this.toFloat());
      Xfloat ytemp = new MyXfloat(y.toFloat());
      
      //figure out which exponent is bigger abs value wise and then sub tract the big - small, then that value is the shift
      

//      System.out.println(xtemp.exp+" "+ytemp.exp);
//      
//      System.out.println(xLy(xtemp.exp, ytemp.exp));
      
      
      //add hidden bit to man, Good, does this
      xtemp.man = xtemp.man + 0b100000000000000000000000;
      ytemp.man = ytemp.man + 0b100000000000000000000000;
      
      
      //Normalization
      if (xLy(Byte.toUnsignedInt(xtemp.exp), Byte.toUnsignedInt(ytemp.exp))) {
	  xtemp.man >>>= (absDiff(Byte.toUnsignedInt(xtemp.exp), Byte.toUnsignedInt(ytemp.exp)));
          xtemp.exp += (absDiff(Byte.toUnsignedInt(xtemp.exp), Byte.toUnsignedInt(ytemp.exp)));
          
//	  System.out.println(toBinaryString(xtemp.man, 32));
//
//          System.out.println(toBinaryString(ytemp.man, 32));
//          
//          System.out.println(toBinaryString(xtemp.exp, 8));
//
//          System.out.println(toBinaryString(ytemp.exp, 8));

      } else {
	  ytemp.man >>>= (absDiff(Byte.toUnsignedInt(xtemp.exp), Byte.toUnsignedInt(ytemp.exp)));
          ytemp.exp += (absDiff(Byte.toUnsignedInt(xtemp.exp), Byte.toUnsignedInt(ytemp.exp)));
          
//          System.out.println(toBinaryString(xtemp.man, 32));
//
//          System.out.println(toBinaryString(ytemp.man, 32));
//          
//          System.out.println(toBinaryString(xtemp.exp, 8));
//
//          System.out.println(toBinaryString(ytemp.exp, 8));

      }
      
      //if negative, do twos compliment on mantissa, (might be wrong to do this >) ensure only 24 bits remain
      if (xtemp.sign == 1) {
	  xtemp.man = ~xtemp.man;
	  //System.out.println(toBinaryString(xtemp.man, 32));

          //System.out.println(toBinaryString(ytemp.man, 32));
	  xtemp.man = xtemp.man + 1;
	  //if (ytemp.sign == 1 && xtemp.sign == 1) {
	      //xtemp.man = xtemp.man & 0b00000000111111111111111111111111;
	  //}
      }
      
      
      if (ytemp.sign == 1) {
	  ytemp.man = ~ytemp.man;
	  ytemp.man = ytemp.man + 1;
	  //if (ytemp.sign == 1 && xtemp.sign == 1) {
	      //ytemp.man = ytemp.man & 0b00000000111111111111111111111111;
	  //}
      }
      

      //Add the MANS
      int mansum = xtemp.man + ytemp.man;
      
      //System.out.println(toBinaryString(mansum, 32));

      
      //check if result is negative?? this might not work, its the first one
      if (toBinaryString(mansum,32).substring(0, 1).equalsIgnoreCase("1")) {
	  mansum = ~mansum;
	  mansum = mansum + 1;
      }
      
      
      //find leftmost 1, 
      // 0000 0000 1.000 0000 0000 0000 0000 0000
      int i = 0;
      if (mansum != 0) {
	  while (!toBinaryString(mansum,32).substring(i, i+1).equals("1")) {
	      i++;
	  }
	  //i--;
	  //System.out.println(i);
	  
	  
	  //int pos = 32 - i; ????
	      
	  if (i < 8) {
	      mansum >>= (8 - i);
	      xtemp.exp += (8 - i);
	      
	  } else if (i > 8) {
	      mansum <<= (i - 8);
	      xtemp.exp -= (i - 8);
	  }
	  
      }	else {
	  return new MyXfloat(0);
      }
      

      //i wanna lowkey not do this anymore i have been working so long on it
      int signbit = 0;
      
      if (xLy(Byte.toUnsignedInt(this.exp), Byte.toUnsignedInt(y.exp))) {
	  signbit = y.sign;
      } else if (!xLy(Byte.toUnsignedInt(this.exp), Byte.toUnsignedInt(y.exp)) && 
	      !(Byte.toUnsignedInt(this.exp) == Byte.toUnsignedInt(y.exp))) {
	  signbit = this.sign;
      } else if (this.man > y.man){
	  signbit = this.sign;
      } else if (this.man < y.man) {
	  signbit = y.sign;
      } else {
	  signbit = this.sign;
      }
      
      mansum = mansum & 0b00000000011111111111111111111111;
      
      return new MyXfloat((byte)signbit,(byte)xtemp.exp,mansum);
  }
  public Xfloat xmult(Xfloat y) {
      
      if (isXfloatZero(this)) {
	  return new MyXfloat(0);
      }
      if (isXfloatZero(y)) {
	  return new MyXfloat(0);
      }
      
      if (isXfloatZero(this) && isXfloatZero(y)) {
	  return new MyXfloat(0);
      }
      
      Xfloat xtemp = new MyXfloat(this.toFloat());
      Xfloat ytemp = new MyXfloat(y.toFloat());
      
      xtemp.man = xtemp.man | Xfloat.BMASK;
      ytemp.man = ytemp.man | Xfloat.BMASK;
      
      long xman = (long) xtemp.man;
      long yman = (long) ytemp.man;
      
      long manprod = xman * yman;
      
      System.out.println(Long.toBinaryString(manprod));

      
      byte expsum = (byte) (xtemp.exp + ytemp.exp);
      
      expsum = (byte) (expsum + (byte) 0b10000001);
      
      int i = 0;
      if (manprod != 0) {
	  while (!Long.toBinaryString(manprod).substring(i, i+1).equals("1")) {
	      i++;
	  }   
	  if (i < 23) {
	      manprod >>= (23 - i);
	      xtemp.exp += (23 - i);
	      
	  } else if (i > 23) {
	      manprod <<= (i - 23);
	      xtemp.exp -= (i - 23);
	  }
	  
      }	else {
	  return new MyXfloat(0);
      }
      
      manprod = manprod & 0b00000000011111111111111111111111;

      byte signx = (byte) ((~xtemp.sign & ytemp.sign) | (xtemp.sign & ~ytemp.sign));
      
      return new MyXfloat(signx,expsum,(int)manprod);
      
  		
  }

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
    System.out.println("     "+new MyXfloat((float) (x * y))+" "+(x*y));

    System.out.println("x+y: "+wf+" "+wf.toFloat());
    System.out.println("     "+new MyXfloat((float) (x + y))+" "+(x+y));

  }
}
