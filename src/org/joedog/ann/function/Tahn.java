package org.joedog.ann.function;

public class Tahn extends Function {
  private static  Tahn       _instance = null;
  private static  Object     mutex     = new Object();

  private Tahn () {

  }

  public synchronized static Tahn getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Tahn();
        }
      }
    }
    return _instance;
  }

  public int function() {
    return Function.TAHN;
  }

  public double evaluate(double input) {
    return Math.tanh(input);
  }
   
  public double derivate(double input) {
    return 1 / Math.pow(Math.cosh(input), 2);
  }
}
