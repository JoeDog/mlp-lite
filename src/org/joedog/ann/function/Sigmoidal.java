package org.joedog.ann.function;

import org.joedog.ann.Util;

public class Sigmoidal extends Function {
  private static  Sigmoidal  _instance = null;
  private static  Object     mutex     = new Object();

  private Sigmoidal () {

  }

  public synchronized static Sigmoidal getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Sigmoidal();
        }
      }
    }
    return _instance;
  }

  public double evaluate(double input) {
    double e = Math.exp(-input);
    return 1 / (1 + e);
  }
    
  public double derivate(double input) {
    double e = Math.exp(input);
    double p = Math.pow(1 + Math.exp(input), 2); 
    return (e/p);
  }
}
