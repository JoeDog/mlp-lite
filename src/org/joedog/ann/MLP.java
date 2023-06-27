package org.joedog.ann;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.namespace.QName;

import org.joedog.ann.function.*;
import org.joedog.util.io.FileUtils;
import org.joedog.util.config.INI;

@XmlRootElement
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

  public MLP() {}

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
          new Bias(n, this.bval);
        } 
      }
    }
  } 
  
  private MLP(String file) {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    try {
      Layer layer    = null; 
      int   index    = 0;
      XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));    
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();
          switch (startElement.getName().getLocalPart()) {
            case "mlp":
               Attribute oeta = startElement.getAttributeByName(new QName("eta"));
               if (oeta != null) {
                 this.eta = Double.parseDouble(oeta.getValue());
               }
               Attribute oalpha = startElement.getAttributeByName(new QName("alpha"));
               if (oalpha != null) {
                 this.alpha = Double.parseDouble(oalpha.getValue());
               }
               Attribute ogain = startElement.getAttributeByName(new QName("gain"));
               if (ogain != null) {
                 this.gain = Double.parseDouble(ogain.getValue());
               }
               break;
            case "layer":
              String type  = null;
              int    func  = -1;
              int    count = -1;
              Attribute otype = startElement.getAttributeByName(new QName("type"));
              if (otype != null) {
                type = otype.getValue();
              }
              Attribute ofunc = startElement.getAttributeByName(new QName("function"));
              if (ofunc != null) {
                func = Integer.parseInt(ofunc.getValue());
              }
              Attribute ocount = startElement.getAttributeByName(new QName("count"));
              if (ocount != null) {
                count = Integer.parseInt(ocount.getValue());
              }
              switch (type) {
                case "InputLayer":
                  layer  = new InputLayer(this, count);
                  break;
                case "HiddenLayer":
                  layer  = new Layer(this, new FunctionFactoryImpl().getFunction(func), count);
                  break;
                case "OutputLayer":
                  layer = new OutputLayer(this, new FunctionFactoryImpl().getFunction(func), count);
                  break;
              }
              break;
            case "neuron":
              Attribute opos = startElement.getAttributeByName(new QName("position"));
              if (opos != null) {
                index = Integer.parseInt(opos.getValue());
              }
              break;
            case "bias":
              double val = 0.00;
              double wgt = 0.00;
              Attribute oval = startElement.getAttributeByName(new QName("value"));
              if (oval != null) {
                val = Double.parseDouble(oval.getValue());
              }
              Attribute owgt = startElement.getAttributeByName(new QName("weight"));
              if (owgt != null) {
                wgt = Double.parseDouble(owgt.getValue());
              }
              Neuron n = layer.getNeuron(index);
              if (n != null) {
                new Bias(n, val, wgt);
              }
              break;
            case "connection":
              int    nidx = 0;
              int    lidx = 0;
              double wght = 0.00;
              Attribute onidx = startElement.getAttributeByName(new QName("neuronIndex"));
              if (onidx != null) {
                nidx = Integer.parseInt(onidx.getValue());
              }
              Attribute olidx = startElement.getAttributeByName(new QName("layerIndex"));
              if (olidx != null) {
                lidx = Integer.parseInt(olidx.getValue());
              }
              Attribute owght = startElement.getAttributeByName(new QName("weight"));
              if (owght != null) {
                wght = Double.parseDouble(owght.getValue());
              }
              Neuron src = this.getLayer(lidx).getNeuron(nidx);
              Neuron dst = layer.getNeuron(index);
              new Connection(src, dst, wght);
              break;
          } 
        }
        if (nextEvent.isEndElement()) {
          EndElement endElement = nextEvent.asEndElement();
          if (endElement.getName().getLocalPart().equals("layer")) {
            //index = 0;
          }
        }
      }
    } catch (Exception e) {e.printStackTrace();}
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
  
  public synchronized static MLP getInstance(String file) {
    if (_instance == null) {
      synchronized(mutex) {
        if (! FileUtils.exists(file)) {
          throw new IllegalArgumentException(String.format("Config file (%s) does not exist.", file));
        }  
        _instance = new MLP(file);
      }
    }
    return _instance;
  }

  @XmlAttribute(name="gain")
  public double getGain() {
    return this.gain;
  }

  public void setGain(double gain) {
    this.gain = gain;
  }

  @XmlAttribute(name="eta")
  public double getEta() {
    return this.eta;
  }

  public void setEta(double eta) {
    this.eta = eta;
  }

  @XmlAttribute(name="alpha")
  public double getAlpha() {
    return this.alpha;
  }

  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }

  public int getLayerIndex(Layer layer) {
    return this.layers.indexOf(layer);
  }

  public Value[] predict(Value[] values) {
    if (this._rote(values, null, false)) {
      return this.getOutputLayer().getOutputValues();
    }
    return null;
  }

  public double MSE() {
    return this.getOutputLayer().MSE();
  }

  public double MAE() {
    return this.getOutputLayer().MAE();
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
    int orig = count;
    while (count > 0) {
      int i = Util.randomIndex(this.examples.size());
      if (! this.learn(this.examples.get(i).getInputs(), this.examples.get(i).getTarget())) {
        return false;
      }
      if (count % 5000000 == 0) {
        System.out.printf("Count: %d, MSE: %.10f, MAE: %.10f\n", orig-count, this.MSE(), this.MAE());
      }
      if (this.MSE() <= 0.00000000001 && this.MAE() < 0.00000001) {
        System.out.printf("Done: MSE: %.10f, MAE: %.10f\n", this.MSE(), this.MAE());
        return true;
      }
      count--;
    }
    System.out.printf("Count complete: MSE: %.10f, MAE: %.10f\n", this.MSE(), this.MAE());
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

  @XmlElement(name="layer")
  public ArrayList<Layer> getLayers(){
    return this.layers;
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

  public void save(String file) {
    try {
      JAXBContext context    = JAXBContext.newInstance(MLP.class);
      Marshaller  marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(this, new FileOutputStream(file));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  } 

  public String toString() {
    int index = 1;
    StringBuffer sb = new StringBuffer();
    sb.append(String.format("eta       = %1.4f\n", this.eta));
    sb.append(String.format("alpha     = %1.4f\n", this.alpha));
    sb.append(String.format("gain      = %1.4f\n", this.gain));
    sb.append(String.format("mae       = %1.4f\n", this.mae));
    sb.append(String.format("bval      = %1.4f\n", this.bval));
    sb.append(String.format("count     = %d\n",    this.layers.size()));
    for (Layer layer : this.layers) {
      String section = layer.getType();
      if (section.startsWith("Hidden")) {
        section = section+"-"+index;
        index++;
      }
      sb.append(String.format("[%s]\n", section));
      sb.append(String.format("  function = %d\n", layer.getFunction()));
      sb.append(String.format("  count    = %d\n", layer.size()));
      int i = 1;
      for (Neuron n : layer.getNeurons()) {  
        Bias b = n.getBias();
        String bias = (b == null) ? "" : b.asString();
        String cmma = (b == null) ? "" : ", ";
        sb.append(
          String.format(
            "  neuron-%d = (v: %1.18f, o: %1.18f, d: %1.18f, e: %1.18f%s%s)\n", 
            i, n.getInputValue(), n.getOutputValue(), n.getDelta(), n.getError(), cmma, bias
          )
        );
        i++;
      }
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
