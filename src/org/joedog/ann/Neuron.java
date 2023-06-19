package org.joedog.ann;

import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.UUID;

public class Neuron {
  private Layer     parent = null; // self._parentLayer
  private Bias      bias   = null; // self._inputBias
  private double    inval  = 0.0; // self._computedInput
  private double    outval = 0.0; // self._computedOutput
  private double    delta  = 0.0; // self._computedDeltaError
  private double    error  = 0.0; // self._computedSignalError
  private UUID      uuid   = null;
  private ArrayList<Connection> inputs = new ArrayList<Connection>(); // self._inputConnections
  private ArrayList<Connection> output = new ArrayList<Connection>(); // self._outputConnections
 
  public Neuron(Layer parent) {
    this.uuid   = UUID.randomUUID();
    this.parent = parent;
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

  public ArrayList<Connection> getInputConnections() {
    return this.inputs;
  }
  
  public void setBias(Bias bias) {
    this.bias = bias;
  }

  public Bias getBias() {
    return this.bias;
  }

  public void setOutputValue(Value value) {
    this.outval = value.getValue();
  }

  public double getOutputValue() {
    return this.outval;
  } 

  public void calculateOutput() {
    this._calculateInput();
    if (this.parent.isFunctional()) {
      double in = this.inval*this.parent.parentGain();
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

  public double getError() {
    // signal error
    return this.error;
  }

  public String getUUID() {
    return this.uuid.toString();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Neuron: ["+this.uuid.toString()+"]\n");
    sb.append(" - value: "+Util.round(this.inval, 10)+", ");
    sb.append("output: "+Util.round(this.outval, 10)+", ");
    sb.append("delta: "+Util.round(this.delta, 10)+", ");
    sb.append("error: "+Util.round(this.error, 10)+"\n");
    /*sb.append(" - inputs:  [");
    for (int i = 0; i < this.inputs.size(); i++) {
      String sep = (i == this.inputs.size()-1) ? "" : ",";
      sb.append(this.inputs.get(i).toString()+sep);
    } 
    sb.append("]\n");
    sb.append(" - output:  [");
    for (int i = 0; i < this.output.size(); i++) {
      String sep = (i == this.output.size()-1) ? "" : ",";
      sb.append(this.output.get(i).toString()+sep);
    }
    sb.append("]\n");*/
    if (this.bias != null) {
      sb.append(" - bias:  ");
      sb.append(this.bias.toString()+"\n");
    }
    return sb.toString();
  }
}
