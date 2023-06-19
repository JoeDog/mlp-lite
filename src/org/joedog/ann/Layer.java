package org.joedog.ann;

import java.util.ArrayList;
import java.util.UUID;

import org.joedog.ann.function.*;

public class Layer {
  private   MLP               mlp      = null;
  private   UUID              uuid     = null;
  protected int               count    = 0; 
  private   Function          function = null;
  protected ArrayList<Neuron> neurons  = new ArrayList<Neuron>();
  private   String            type     = "HiddenLayer";

  public Layer(MLP mlp) {
     this(mlp, null, 0);
   }
   
  public Layer(MLP mlp, Function function) {
    this(mlp, function, 0);
  }
  
  public Layer(MLP mlp, Function function, int count) {
    this.mlp      = mlp;
    this.function = function;
    this.count    = count;
    this.uuid     = UUID.randomUUID();
    this.mlp.addLayer(this);
    for (int i = 0; i < this.count; i++) {
      this.neurons.add(new Neuron(this));
    }
  }

  public boolean isFunctional() {
    return (this.function != null);
  }

  public double evaluate(double value) { 
    return this.function.evaluate(value);
  }

  public double derivate(double value) {
    return this.function.derivate(value);
  }

  public double parentGain() {
    return this.mlp.gain();
  }

  public int size() {
    return this.neurons.size();
  }

  public String getUUID() {
    return this.uuid.toString();
  }

  public Neuron getNeuron(int index) {
    if (index >= 0 && index < this.size()) {
      return this.neurons.get(index);
    }
    return null;
  }

  public void addNeuron(Neuron neuron) {
    if (! this.contains(neuron)) {
      this.neurons.add(neuron);
    }
  }

  public ArrayList<Neuron> getNeurons() {
    return this.neurons;
  }

  public boolean contains(Neuron neuron) {
    if (neuron == null) {
      return false;
    }
    for (Neuron n : this.neurons) {
      if (neuron.getUUID().equals(n.getUUID())) {
        return true;
      } 
    } 
    return false;
  }
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(type+" Size: "+this.count+" ("+this.neurons.size()+")\n");
    for (Neuron n : this.neurons) {
      sb.append(n.toString());
    }
    return sb.toString();
  } 
}
