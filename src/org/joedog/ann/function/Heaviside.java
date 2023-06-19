package org.joedog.ann.function;

public class Heaviside extends Function {
  private static  Heaviside  _instance = null;
  private static  Object     mutex     = new Object();

  private Heaviside () {

  }

  public synchronized static Heaviside getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Heaviside();
        }
      }
    }
    return _instance;
  }

  public double evaluate(double value) {
    if (value >= 0.0) {
      return 1.0;
    } else {
      return 0.0;
    }
  }

  public double derivate(double value) {
    return 1.0;
  }
}
