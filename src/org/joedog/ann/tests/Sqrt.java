package org.joedog.ann.tests;

import org.joedog.ann.*;
import org.joedog.ann.function.*;

/**
 * @author Jeffrey Fulmer
 */
public class Sqrt {
  /**
   */
  public Sqrt() {
    MLP mlp = MLP.getInstance(new int[]{1,2,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);  
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(1)}, new Value[]{new Value.ValueBuilder().inRange(0,10,1)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(4)}, new Value[]{new Value.ValueBuilder().inRange(0,10,2)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(9)}, new Value[]{new Value.ValueBuilder().inRange(0,10,3)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(16)}, new Value[]{new Value.ValueBuilder().inRange(0,10,4)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(25)}, new Value[]{new Value.ValueBuilder().inRange(0,10,5)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(36)}, new Value[]{new Value.ValueBuilder().inRange(0,10,6)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(49)}, new Value[]{new Value.ValueBuilder().inRange(0,10,7)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(64)}, new Value[]{new Value.ValueBuilder().inRange(0,10,8)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(89)}, new Value[]{new Value.ValueBuilder().inRange(0,10,9)}
    );
    mlp.addExample(
      new Value[]{new Value.ValueBuilder().asInt(100)}, new Value[]{new Value.ValueBuilder().inRange(0,10,10)}
    );
    mlp.learnByExample(4000000);
    System.out.println(mlp.toString());
    System.out.println("LEARNED :");
    System.out.printf( "  - sqrt(49) = %.14f\n", mlp.predict(
      new Value[]{new Value.ValueBuilder().asInt(49)})[0].asInt()
    );
    System.out.printf( "  - sqrt(25) = %.14f\n", mlp.predict(
      new Value[]{new Value.ValueBuilder().asInt(49)})[0].asInt()
    );
  }

  public static void main(String[] args) {
    Sqrt sqrt = new Sqrt();
  }
}
