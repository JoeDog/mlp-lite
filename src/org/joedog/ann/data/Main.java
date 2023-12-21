package org.joedog.ann.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
  public static void main(String [] args) {
    List <Value> values = new ArrayList<Value>();
    Range range = new Range(0, 10); 
    for (int i = 0; i < 20; i++) {
      values.add(new Value((double)i, range));
    }
    for (int i = 0; i < values.size(); i++) {
      System.out.println(values.get(i).toString()); 
    }
    double d = 1.00;
    System.out.println((d - range.min()) / (range.max() - range.min()));

    Dataset ds = new Dataset(0, 10);
    for (int i = 0; i < 20; i++) {
      ds.add(i+1);
    } 

    for (Value v : ds.getData()) {
      System.out.println(v.asInt());
    }
  }
}
