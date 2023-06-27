package org.joedog.ann.function;

import org.joedog.ann.Util;

public class Gaussian extends Function {
  private static  Gaussian  _instance = null;
  private static  Object     mutex     = new Object();

  private Gaussian () {

  }

  public synchronized static Gaussian getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Gaussian();
        }
      }
    }
    return _instance;
  }

  public int function() {
    return Function.GAUSSIAN;
  }

  public double evaluate(double x) {
    double d = Math.pow(-x, 2);
    double f = 1 / (Math.exp(d));
    if (f > 0.99999999) System.out.println("GREATER THAN 1.0 FROM "+x);
    return f;
  }

  public double derivate(double x) {
    return (double)-2 * x * evaluate(x);
  }
}
