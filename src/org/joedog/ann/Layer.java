package org.joedog.ann;

import java.util.ArrayList;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

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
    this.uuid     = UUID.randomUUID();
    this.count    = count;
    this.mlp.addLayer(this);
    for (int i = 0; i < this.count; i++) {
      this.neurons.add(new Neuron(this));
    }
  }

  @XmlAttribute(name="type")
  public String getType() {
    return this.type;
  }

  @XmlAttribute(name="function") 
  public int getFunction() {
    return (this.isFunctional()) ? this.function.function() : -1;
  }

  @XmlAttribute(name="count")
  public int getCount() {
    return this.size();
  }

  public int size() {
    return this.neurons.size();
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
    return this.mlp.getGain();
  }

  public String getUUID() {
    return this.uuid.toString();
  }

  public Neuron getNeuron(int index) {
    if (index >= 0 && index < this.size()) {
      return this.neurons.get(index);
    }
    System.out.println("RETURNING NULL: "+index);
    return null;
  }

  public void addNeuron(Neuron neuron) {
    if (! this.contains(neuron)) {
      this.neurons.add(neuron);
      this.count = this.neurons.size();
    }
  }

  @XmlElement(name="neuron") 
  public ArrayList<Neuron> getNeurons() {
    return this.neurons;
  }

  public int getNeuronIndex(Neuron neuron) {
    return this.neurons.indexOf(neuron);
  }

  public int getLayerIndex() {
    return this.mlp.getLayerIndex(this);
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

  public double MSE() { // mean sq error
    double mse = 0.00;
    if (this.size() == 0) return mse; 
    for (Neuron n : this.neurons) {
      mse += Math.pow(n.getError(), 2);
    }
    return mse / this.size();
  }

  public double MAE() { // mean abs error
    double mae = 0.00;
    if (this.size() == 0) return mae; 
    for (Neuron n : this.neurons) {
      mae += Math.abs(n.getError());
    } 
    return mae / this.size();
  }
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(type+" Size: "+this.size()+" ("+this.neurons.size()+")\n");
    for (Neuron n : this.neurons) {
      sb.append(n.toString());
    }
    return sb.toString();
  } 
}
