package org.joedog.ann;

import org.joedog.ann.function.*;

public class OutputLayer extends Layer {
  private String type = "OutputLayer";

  public OutputLayer(MLP mlp, Function function, int count) {
    super(mlp, function, count);
  }  

  public String getType() {
    return this.type;
  }

  public Value[] getOutputValues() {
    Value[] out = new Value[this.count];
    int i = 0;
    for (Neuron n : neurons) {
      out[i] = new Value.ValueBuilder().asAnalog(n.getOutputValue());
      i++;
    }
    return out;
  }

  public boolean calculateTargetError(Value[] target) {
    if (target.length != this.count) return false; 
    int i = 0;
    for (Neuron n : neurons) {
      n.calculateError(target[i]);
      i++; 
    } 
    return true;
  }
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(type+" Size: "+this.count+" ("+this.neurons.size()+")\n");
    for (Neuron n : neurons) {
      sb.append(n.toString());
    }
    return sb.toString();
  }
}
