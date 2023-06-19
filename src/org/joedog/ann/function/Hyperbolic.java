package org.joedog.ann.function;

public class Hyperbolic extends Function {
  private static  Hyperbolic  _instance = null;
  private static  Object      mutex     = new Object();

  private Hyperbolic () {

  }

  public synchronized static Hyperbolic getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Hyperbolic();
        }
      }
    }
    return _instance;
  }

  public double evaluate(double value) {
    return Math.tanh(value);
  }

  public double derivate(double value) {
    //return (1 - Math.pow(value, 2));
    return (double) (1 - Math.pow(evaluate(value), 2));
  }
}
