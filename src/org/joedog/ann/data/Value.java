package org.joedog.ann.data;

import java.util.List;
import java.util.ArrayList;

public class Value {
  public enum Type {
    BOOLEAN,
    INTEGER,
    DOUBLE,
    FLOAT,
    STRING
  };
  private Range  range = null;
  private Type   type  = null;
  private double value = -1.0;
  private double input = -1.0;

  public Value(double value, Range range, Type type) {
    this.range = range;
    this.type  = type;
    if (this.type != Type.BOOLEAN) {
      if (value <= this.range.min()) {
        this.value = 0.00; // We should throw an exception
      } else if (value >= this.range.max()) {
        this.range.update(this.range.min(), (int)value+1000);
      }
    }
    this.value = this.normalize(value);
    this.input = value;
    this.range.register(this);
  }

  public Value(double value, Range range) {
    this(value, range, Type.DOUBLE);
    this.input = value;
  }

  public Value(double value) {
    this(value, new Range(0, (int)value+1000), Type.DOUBLE);
    this.input = value;
  }

  public Value(boolean value) {
    this(value==true?1.0:0.0, new Range(0, 1), Type.BOOLEAN);
    System.out.println("boolean");
    this.value = this.normalize((value==true)?1.0:0.0);
    this.input = (value==true) ? 1.0 : 0.0;
  }

  public double getValue() {
    return (double)this.value;
  }

  public boolean asBoolean() {
    return (this.value > 0.499) ? true : false;
  }

  public int asInt() {
    return (int)Math.ceil(this.denormalize());
  }

  public int fromAnalog() {
    return (int)Math.ceil(this.value*100.1);
  }

  public double asDouble() {
    return (double)this.denormalize();
  }

  public void recalibrate() {
    this.value = normalize(this.input);
  }

  private double normalize(double value) {
    //System.out.printf("value: %f, Min: %d, Max: %d\n", value, this.range.min(), this.range.max());
    return ((value - this.range.min()) / (this.range.max() - this.range.min()));
  }

  private double denormalize() {
    //System.out.printf("denorm: %3.4f\n", this.value * (this.max - this.min) + this.min);
    return (this.value * (this.range.max() - this.range.min()) + this.range.min());
  }  

  public String toString() {
    return "Value: min="+this.range.min()+", max="+this.range.max()+", value="+this.value;
  }

  public static class ValueBuilder {
    public List<Value> inRange(int lo, int hi) {
      int n = lo;
      int r = (hi-lo)+1;
      Range        range = new Range(lo, hi);
      List <Value> list  = new ArrayList<Value>();

      for (int i = 0; i < r; i++) {
        list.add(i, new Value((double)n, range));
        n++; 
      }
      return list;
    }

    public Value inRange(int value, int lo, int hi) {
      return this.inRange((double)value, lo, hi);
    }

    public Value inRange(double value, int lo, int hi) {
      return new Value(value, new Range(lo, hi));
    }

    public Value asBoolean(boolean value) {
      return new Value((value==true)?1.0:0.0, new Range(0,1), Type.BOOLEAN);
    }

    public Value asInt(double value) {
      Range range = new Range(0, 1000000);
      return new Value(value, range);
    }

   public Value asInt(double value, Range range) {
     return new Value(value, range);
   }

   public Value asInt(double value, int lo, int hi) {
     Range range = new Range(lo, hi);
     return new Value(value, range);
   }
 
    public Value asAnalog(double value) {
      return new Value(value, new Range(0, 1));
    }

    public Value asAnalog(int value) {
      return this.asAnalog((double)value);
    }
  }
}
