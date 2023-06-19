package org.joedog.ann;

public class InputLayer extends Layer {
  private String type = "InputLayer";

  public InputLayer(MLP mlp, int count) {
    super(mlp, null, count);
  }  

  public boolean setInputValues(Value[] values) {
    if (values.length != this.count) return false;
    for (int i = 0; i < this.count; i++) {
      (this.neurons.get(i)).setOutputValue(values[i]);
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
