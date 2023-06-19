package org.joedog.ann.function;

public abstract class Function {
  public static final int GAUSSIAN   = 1;
  public static final int HEAVISIDE  = 2;
  public static final int HYPERBOLIC = 3;
  public static final int LOGISTIC   = 4;
  public static final int RELU       = 5;
  public static final int SIGMOIDAL  = 6; 

  public Function () {
  }

  public abstract double evaluate(double value);
  public abstract double derivate(double value);
}
