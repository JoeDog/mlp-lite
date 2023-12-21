package org.joedog.ann.data;

import java.util.List;
import java.util.ArrayList;

public class Range {
  private int min = 0;
  private int max = 0;
  private List<Value> observers = new ArrayList<Value>();

  public Range() {
    this(0, 10000);
  }
 
  public Range(int min, int max)  {
    this.min = min;
    this.max = max;
  }

  public int min() {
    return this.min;
  }

  public int max() {
    return this.max;
  }

  public void update(int min, int max) {
    int  tin = this.min;
    int  tax = this.max;
    this.min = min;
    this.max = max; 
    if (tin != min || tax != max) {
      this._notify();
    }  
  } 

  public void register(Value observer) {
    this.observers.add(observer);
  }

  public void unregister(Value observer) {
    this.observers.remove(observer);
  }

  public String toString() {
    return "["+this.min+", "+this.max+"]";
  }

  private void _notify() {
    for (Value observer : this.observers) {
      observer.recalibrate();
    }
  } 
}
