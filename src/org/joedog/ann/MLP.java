package org.joedog.ann;

import java.util.Arrays;
import java.util.ArrayList;

import org.joedog.ann.function.*;

public class MLP {
  private double        eta       = 0.30;
  private double        alpha     = 0.75;
  private double        gain      = 0.99;
  private double        mae       = 0.02;
  private double        bval      = 1.0;
  ArrayList<Layer>      layers    = new ArrayList<Layer>();
  ArrayList<Example>    examples  = new ArrayList<Example>();
  private static MLP    _instance = null;
  private static Object mutex     = new Object();

  private MLP(int[] count, Function function, boolean auto) {
    for (int i = 0; i < count.length; i++) {
      Layer layer = null;
      if (i == 0) {
        layer = new InputLayer(this, count[i]);
      } else { 
        if (i == count.length-1) {
          layer = new OutputLayer(this, function, count[i]);
        } else {
          layer = new Layer(this, function, count[i]);
        }
        if (auto) {
          this._autoConnect(this.getLayer(i-1), layer);
        }
        for (Neuron n : layer.getNeurons()) {
          new Bias(n, bval);
        } 
      }
    }
  } 

  public synchronized static MLP getInstance(int[] count, Function function, boolean auto) {
    if (_instance == null) {
      synchronized(mutex) {
         if (count.length < 2) {
           throw new IllegalArgumentException("The layer count must be at least three.");
        }
        for (int i : count) {
           if (i < 1) throw new IllegalArgumentException("A layer must have at least one Neuron");
        }
        if (_instance == null) {
          _instance = new MLP(count, function, auto);
        }
      }
    }
    return _instance;
  } 

  public double gain() {
    return gain;
  }

  public void setGain(double gain) {
    this.gain = gain;
  }

  public Value[] predict(Value[] values) {
    if (this._rote(values, null, false)) {
      return this.getOutputLayer().getOutputValues();
    }
    return null;
  }

  private boolean _rote(Value[] inputs, Value[] target, boolean training) {
    if (! this.isConstructed()) return false;
    InputLayer inner = this.getInputLayer();
    if (inner.setInputValues(inputs)) {
      this._propagateSignal();
    } 
    if (target == null) return ! training;

    OutputLayer outer = this.getOutputLayer();
    if (outer.calculateTargetError(target)) {
      if (! training) return true;
      return this._backPropagateError();
    }
    return false;
  }

  private boolean _backPropagateError() {
    Bias bias = null;
    if (! this.isConstructed()) return false;

    int i = this.layers.size()-1; 
    while (i >= 0) {
      for (Neuron n : this.layers.get(i).getNeurons()) {
        if (i < this.layers.size()-1) {
          if (i > 0) n.calculateError();
          for (Connection c : n.getOutputConnections()) {
            c.reweight(this.eta, this.alpha);
          }
          bias = n.getBias();
          if (bias != null) {
            bias.reweight(this.eta, this.alpha);
          } 
        }
      }
      i--;
    }
    return true;
  }

  private boolean _propagateSignal() {
    if (this.isConstructed()) {
      int i = 1;
      while (i < this.layers.size()) {
        for (Neuron n : this.layers.get(i).getNeurons()) {
          n.calculateOutput();
        } 
        i++;
      }
      return true;
    }
    return false;
  }

  public boolean learn(Value[] inputs, Value[] target) {
    if (inputs.length > 0) {
      return this._rote(inputs, target, true);
    }
    return false;
  }

  public boolean learnByExample(int count) {
    while (count > 0) {
      int i = Util.randomIndex(this.examples.size());
      if (! this.learn(this.examples.get(i).getInputs(), this.examples.get(i).getTarget())) {
        return false;
      }
      count--;
    }
    return true;
  }

  public boolean isConstructed() {
    return (this.getInputLayer() != null && this.getOutputLayer() != null);
  }

  public boolean addExample(Value[] inputs, Value[] target) {
    if (! this.isConstructed()) return false;
    if (inputs.length > (this.getInputLayer()).size())  return false;
    if (target.length > (this.getOutputLayer()).size()) return false;
    this.examples.add(new Example(inputs, target));
    return true;
  }

  public void addLayer(Layer layer) {
    this.layers.add(layer);
  }

  public Layer getLayer(int index) {
    return this.layers.get(index);
  }

  public InputLayer getInputLayer() {
    if (this.layers.size() > 0) {
      InputLayer tmp = (InputLayer)this.layers.get(0);
      if (tmp instanceof InputLayer) {
        return tmp;
      }
    }
    return null;
  }

  public OutputLayer getOutputLayer() {
    if (this.layers.size() > 0) {
      OutputLayer tmp = (OutputLayer)this.layers.get(this.layers.size()-1);
      if (tmp instanceof OutputLayer) {
        return tmp;
      }
    }
    return null;
  }

  public int getLayerIndex(Layer layer) {
    return this.layers.lastIndexOf(layer);
  }

  public boolean contains(Layer layer) {
    if (layer == null) {
      return false;
    }
    for (Layer l : this.layers) {
      if (layer.getUUID().equals(l.getUUID())) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("MLP with "+this.layers.size()+" layers\n");
    for (Layer l : this.layers) {
      sb.append(l.toString());
    }
    return sb.toString();
  }

  private void _autoConnect(Layer src, Layer dst) {
    if (src != dst) {
      for (Neuron nsrc : src.getNeurons()) {
        for (Neuron ndst : dst.getNeurons()) {
          new Connection(nsrc, ndst);
        }
      }
    }
  }
}
