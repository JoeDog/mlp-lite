package org.joedog.ann.function;

public class ReLU extends Function {
  private static  ReLU     _instance = null;
  private static  Object   mutex     = new Object();

  private ReLU () {

  }

  public synchronized static ReLU getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new ReLU();
        }
      }
    }
    return _instance;
  }

  public int function() {
    return Function.RELU;
  }

  public double evaluate(double input) {
    return 1 / (1 + Math.exp(-input));
  }
    
  public double derivate(double input) {
    return Math.exp(input) / Math.pow(1 + Math.exp(input), 2);
  }
}
