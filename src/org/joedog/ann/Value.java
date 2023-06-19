package org.joedog.ann;

public class Value {
  public enum Type {
    BOOLEAN,
    INTEGER,
    DOUBLE,
    FLOAT,
    STRING
  }
  private int    min   = -1;
  private int    max   = -1;
  private Type   type  = null;
  private double value = -1.0;

  public Value(int min, int max, double d) {
    this.min   = min;
    this.max   = max;
    this.type  = Type.DOUBLE;
    if (d <= this.min) {
      this.value = 0.0;
    } else if (d >= this.max) {
      this.value = 1.0;
    } else {
      this.value = this.normalize(d);
    }
  }

  public Value(boolean b) {
    this.min   = 0;
    this.max   = 1;
    this.type  = Type.BOOLEAN;
    this.value = this.normalize((b==true)?1.0:0.0);
  }

  public double getValue() {
    return this.value;
  }

  public boolean asBoolean() {
    return (this.value > 0.499) ? true : false;
  }

  public int asInt() {
    return (int)this.denormalize();
  }

  public int asInt(int min, int max) {
    this.min = min;
    this.max = max;
    return (int)this.denormalize();
  }

  public double asDouble() {
    return this.denormalize();
  }

  public int asDouble(int min, int max) {
    this.min = min;
    this.max = max;
    return (int)this.denormalize();
  }

  public void recalibrate(int min, int max) {
    this.min = min;
    this.max = max;
    this.value = normalize(this.value);
  }

  private double normalize(double value) {
    return ((value - this.min) / (this.max - this.min));
  }

  private double denormalize() {
    return (this.value * (this.max - this.min) + this.min);
  }  

  public String toString() {
    return "Value: min="+this.min+", max="+this.max+", value="+this.value;
  }

  public static class ValueBuilder {
    /**
     * Builder
     * Value val = new Value.ValueBuilder().fromBoolean(true);
     */
    public Value inRange(int lo, int hi, int value) {
      return new Value(lo, hi, (double)value);
    }

    public Value[] range(int lo, int hi) {
      int     n = lo;
      int     r = (hi-lo)+1;
      Value[] v = new Value[r];

      for (int i = 0; i < r; i++) {
        v[i] = new Value(lo, hi, (double)n);
        n++;
      }
      return v;
    }

    public Value inRange(int lo, int hi, double value) {
      return new Value(lo, hi, value);
    }

    public Value asBoolean(boolean value) {
      return new Value(value);
    }

    public Value asInt(int value) {
      return new Value(0, 1000000, (double)value);
    }
 
    public Value asAnalog(double value) {
      return new Value(0, 1, value);
    }
  }
}
