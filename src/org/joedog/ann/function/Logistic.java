package org.joedog.ann.function;

public class Logistic extends Function {
  private static  Logistic _instance = null;
  private static  Object   mutex     = new Object();

  private Logistic () {

  }

  public synchronized static Logistic getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Logistic();
        }
      }
    }
    return _instance;
  }

  public double evaluate(double value) {
    return (double) (1 / (1+Math.exp(-value)));
  }

  public double derivate(double value) {
    return evaluate(value) * (1 - evaluate(value));
  }
}
