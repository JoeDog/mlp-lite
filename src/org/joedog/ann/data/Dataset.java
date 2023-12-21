package org.joedog.ann.data;

import java.util.List;
import java.util.ArrayList;

public class Dataset {
  private Range       range = null;
  private List<Value> data  = new ArrayList<Value>();

  public Dataset(int min, int max) {
    this.range = new Range(min, max); 
  }

  public Dataset(Range range) {
    this.range = range;
  }

  public void add(double value) {
    this.data.add(new Value(value, this.range)); 
  }

  public Value get(int index) {
    return this.data.get(index);
  }

  public int size() {
    return this.data.size();
  }

  public List<Value> getData() {
    return this.data;
  }
}
