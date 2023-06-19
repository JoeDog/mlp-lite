package org.joedog.ann.tests;
  
import org.joedog.ann.*;
import org.joedog.ann.function.*;

public class Numbers {
  /**
   * Given input n, produce the next five consecutive numbers.
   */
  public static void main(String [] args) {
    int lo =  9995;
    int hi = 10000;
    Value [] nums = new Value.ValueBuilder().range(lo, hi);
    for (int i = 0; i < nums.length; i++) {
      System.out.printf("%d\n", nums[i].asInt());
    }
  }
}
