package org.joedog.ann;

import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;

public class Connection {
  private UUID   uuid     = null;
  private double weight   = 0.0;
  private double momentum = 0.0;
  private Neuron nsrc     = null;
  private Neuron ndst     = null;

  public Connection(Neuron src, Neuron dst) {
    this(src, dst, Util.randomNetworkWeight());
  }

  public Connection(Neuron src, Neuron dst, double weight) {
    src.addOutputConnection(this);
    dst.addInputsConnection(this);

    this.uuid   = UUID.randomUUID();
    this.nsrc   = src;
    this.ndst   = dst;
    this.weight = weight;
  }
 
  public void reweight(double eta, double alpha) {
    double tmp     = (eta * this.nsrc.getOutputValue() * this.ndst.getError());
    this.weight   += tmp + (alpha * this.momentum);
    this.momentum  = tmp;
  }

  public String getUUID() {
    return this.uuid.toString();
  }

  public Neuron sourceNeuron() {
    return this.nsrc;
  }

  public Neuron destinationNeuron() {
    return this.ndst;
  }

  @XmlAttribute(name="weight")
  public double getWeight() {
    return this.weight;
  }

  @XmlAttribute(name="neuronIndex")
  public int getIndex() {
    return this.nsrc.getNeuronIndex();
  }

  @XmlAttribute(name="layerIndex")
  public int getLayerIndex() {
    return this.nsrc.getLayerIndex();
  }

  public String toString() {
    return this.uuid.toString();
  }
}
