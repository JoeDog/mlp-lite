package org.joedog.ann;

import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Neuron {
  private Layer     parent = null; 
  private Bias      bias   = null; // inputBias
  private double    inval  = 0.0;  // computedInput
  private double    outval = 0.0;  // computedOutput
  private double    delta  = 0.0;  // computedDeltaError
  private double    error  = 0.0;  // computedSignalError
  private UUID      uuid   = null;
  private ArrayList<Connection> inputs = new ArrayList<Connection>(); 
  private ArrayList<Connection> output = new ArrayList<Connection>(); 
 
  public Neuron(Layer parent) {
    this.uuid   = UUID.randomUUID();
    this.parent = parent;
  }

  public Neuron(Layer parent, double v, double o, double d, double e) {
    this.uuid   = UUID.randomUUID();
    this.parent = parent;
    this.inval  = v;
    this.outval = o; 
    this.delta  = d;
    this.error  = e;
  }

  public void addOutputConnection(Connection conn) {
    this.output.add(conn);
  }

  public ArrayList<Connection> getOutputConnections() {
    return this.output;
  }
  
  public void addInputsConnection(Connection conn) {
    this.inputs.add(conn);
  }

  @XmlElement(name="connection")
  public ArrayList<Connection> getInputConnections() {
    return this.inputs;
  }
  
  public void setBias(Bias bias) {
    this.bias = bias;
  }

  @XmlElement(name="bias")
  public Bias getBias() {
    return this.bias;
  }

  @XmlAttribute(name="position")
  public int getNeuronIndex() {
    return this.parent.getNeuronIndex(this);
  }
  
  public int getLayerIndex() {
    return this.parent.getLayerIndex();
  }

  public void setOutputValue(Value value) {
    this.outval = value.getValue();
  }

  //@XmlAttribute(name="output")
  public double getOutputValue() {
    return this.outval;
  } 

  //@XmlAttribute(name="input")
  public double getInputValue() {
    return this.inval;
  } 

  //@XmlAttribute(name="error")
  public double getError() {
    return this.error;
  }

  //@XmlAttribute(name="delta")
  public double getDelta() {
    return this.delta;
  }

  //@XmlElement(name="uuid")
  public String getUUID() {
    return this.uuid.toString();
  }

  public void calculateOutput() {
    this._calculateInput();
    if (this.parent.isFunctional()) {
      this.outval = this.parent.evaluate(this.inval *this.parent.parentGain());
    } 
  }

  public void calculateError() {
    this.calculateError(null);
  }

  public void calculateError(Value target) {
    if (target != null) {
      this.delta = target.getValue() - this.getOutputValue(); 
    } else {
      this.delta = 0.0;
      for (Connection c : this.output) {
        this.delta += (c.destinationNeuron().getError() * c.getWeight());
      }
    }
    if (this.parent.isFunctional()) {
      this.error = this.delta * this.parent.parentGain() * this.parent.derivate(this.inval);
      if (this.error > 1.0) {
        this.error = Util.truncateDouble(this.error*0.0001, 15);
      }
    }
  }

  private void _calculateInput() {
    double sum = 0.0;
    for (Connection c : this.inputs) {
      sum += c.sourceNeuron().getOutputValue() * c.getWeight();
    }
    if (this.bias != null) {
      sum += (this.bias.getValue() * this.bias.getWeight());
    } 
    this.inval = sum;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Neuron: ["+this.uuid.toString()+"]\n");
    sb.append(" - value: "+Util.round(this.inval, 10)+", ");
    sb.append("output: "+Util.round(this.outval, 10)+", ");
    sb.append("delta: "+Util.round(this.delta, 10)+", ");
    sb.append("error: "+Util.round(this.error, 10)+"\n");
    if (this.bias != null) {
      sb.append(" - bias:  ");
      sb.append(this.bias.toString()+"\n");
    }
    return sb.toString();
  }
}
