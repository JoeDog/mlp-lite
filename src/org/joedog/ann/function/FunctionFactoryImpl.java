package org.joedog.ann.function;

public class FunctionFactoryImpl implements FunctionFactory {

  public FunctionFactoryImpl () {
  }

  private String getCallingMethodName() {
    StackTraceElement callingFrame = Thread.currentThread().getStackTrace()[4];
    return callingFrame.getMethodName();
  }

  public Function getFunction (int type) {
    switch(type) {
      case Function.GAUSSIAN:
        return Gaussian.getInstance();
      case Function.HEAVISIDE:
        return Heaviside.getInstance();
      case Function.HYPERBOLIC:
        return Hyperbolic.getInstance();
      case Function.LOGISTIC:
        return Logistic.getInstance();
      case Function.RELU:
        return ReLU.getInstance();
      case Function.SIGMOIDAL:
        return Sigmoidal.getInstance();
      default:
        return null;
    }
  }
}
