package org.joedog.ann;

import java.util.Random;
import java.math.RoundingMode;
import java.math.BigDecimal;

import org.joedog.ann.data.*;

public final class Util {

  public static double round(double number, int precision) {
    if (precision < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(Double.toString(number));
    bd = bd.setScale(precision, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static double truncateDouble(double number, int precision) {
    double result = number;
    String arg = "" + number;
    int idx = arg.indexOf('.');
    if (idx!=-1) {
      if (arg.length() > idx+precision) {
        arg = arg.substring(0,idx+precision+1);
        arg = arg.replace("-", "");
        arg = arg.replace("E", "");
        arg = arg.replace("e", "");
        result  = Double.parseDouble(arg);
      }
    }
    return result ;
  }

  public static double randomNetworkWeight() {
    int      min  = -1;
    int      max  =  1;
    Random     r  = new Random();
    return ((double) ((Math.random() * (max - min)) + min));
  } 
  
  public static float randomFloat() {
    Random r  = new Random();
    float  f  = r.nextFloat();
    return f;
  }

  public static double randomDouble() {
    Random r  = new Random();
    double d  = r.nextDouble();
    return d;
  }

  public static int randomIndex(int max) {
    return (int)(Util.randomDouble()*max);
  }
}
