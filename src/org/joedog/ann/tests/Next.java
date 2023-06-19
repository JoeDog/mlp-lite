package org.joedog.ann.tests;

import org.joedog.ann.*;
import org.joedog.ann.function.*;

public class Next {
  
  public static void main(String [] args) {
    MLP mlp = MLP.getInstance(new int[]{3,10,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);
    Value [] val = new Value[20];
    for (int i = 1; i < val.length; i++) {
      val[i-1] = new Value.ValueBuilder().inRange(0, 100, i);
    }
    for (int i = 0; i < 16; i++) {
      System.out.printf(
        "%d, %d, %d => %d\n", 
        val[i].asInt(), val[i+1].asInt(), val[i+2].asInt(), val[i+3].asInt()
      );
      mlp.addExample(new Value[]{val[i], val[i+1], val[i+2]}, new Value[]{val[i+3]});
    }
    mlp.learnByExample(4000000);
    System.out.println(mlp.toString());
    System.out.println("LEARNED :");
    System.out.printf( "  inputs: 3,  4,  5 output: %d\n", mlp.predict(
      new Value[]{val[2], val[3], val[4]})[0].asInt()
    );
    System.out.printf( "  inputs: 7,  8,  9 output: %d\n", mlp.predict(
      new Value[]{val[6], val[7], val[8]})[0].asInt()
    );
    System.out.printf( "  inputs: 9, 10, 11 output: %d\n", mlp.predict(
      new Value[]{val[8], val[9], val[10]})[0].asInt()
    );
  }
}

