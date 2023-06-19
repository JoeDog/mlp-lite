package org.joedog.ann;

import java.util.ArrayList;

public class Example { 
  private Value[] inputs;
  private Value[] target;

  public Example() {

  }

  public Example(Value[] inputs, Value[] target) {
    this.inputs = inputs;
    this.target = target;
  }

  public void add(Value[] inputs, Value[] target) {
    this.inputs = inputs;
    this.target = target;
  }

  public Value[] getInputs() {
    return this.inputs;
  }

  public Value[] getTarget() {
    return this.target;
  }
}
