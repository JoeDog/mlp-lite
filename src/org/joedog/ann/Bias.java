package org.joedog.ann;

import javax.xml.bind.annotation.XmlAttribute;

public class Bias {
  private Neuron neuron; 
  private double value;
  private double weight; 
  private double  momentum; // momentum delta weight

  public Bias(Neuron neuron) {
    this(neuron, 1.0, 0.0);
  }

  public Bias(Neuron neuron, double value) {
    this(neuron, value, 0.0);
  }

  public Bias(Neuron neuron, double value, double weight) {
    this.neuron   = neuron;
    this.value    = value;
    this.weight   = weight;
    this.momentum = 0.0;
    this.neuron.setBias(this);
  }

  public Bias(Neuron neuron, double value, double weight, double momentum) {
    this.neuron   = neuron;
    this.value    = value;
    this.weight   = weight;
    this.momentum = 0.0;
    this.neuron.setBias(this);
  }

  public void remove() {
    this.neuron.setBias(null);
  }

  public void reweight(double eta, double alpha) {
    double tmp     = eta * this.value * this.neuron.getError();
    this.weight  += tmp + (alpha * this.momentum);
    this.momentum = tmp;
  }

  public Neuron getNeuron() {
    return this.neuron;
  }

  @XmlAttribute(name="value")
  public double getValue() {
    return this.value;
  }

  @XmlAttribute(name="weight")
  public double getWeight() {
    return this.weight;
  }
  
  public String asString() {
    return "bv: "+this.value+", bw: "+this.weight+", bm: "+this.momentum;
  }
  public String toString() {
    return "Bias(v: "+this.value+" w: "+this.weight+" m: "+this.momentum+")";
  }
}

