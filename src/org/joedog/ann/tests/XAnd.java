package org.joedog.ann.tests;

import org.joedog.ann.*;
import org.joedog.ann.function.*;

public class XAnd {
  /**
   * | A | B | Out
   * +---+---+----
   * | 0 | 0 | 0
   * +---+---+---
   * | 0 | 1 | 0
   * +---+---+---
   * | 1 | 0 | 0
   * +---+---+---
   * | 1 | 1 | 1
   * +---+---+---
   */
  public static void main(String [] args) {
    MLP mlp = MLP.getInstance(new int[]{2,2,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);
    Value trusie = new Value.ValueBuilder().asBoolean(true);
    Value falsie = new Value.ValueBuilder().asBoolean(false);
    mlp.addExample(new Value[]{falsie, falsie}, new Value[]{falsie});
    mlp.addExample(new Value[]{falsie, trusie}, new Value[]{falsie});
    mlp.addExample(new Value[]{trusie, falsie}, new Value[]{falsie});
    mlp.addExample(new Value[]{trusie, trusie}, new Value[]{trusie});
    mlp.learnByExample(4000000);
    System.out.println(mlp.toString());
    System.out.println("LEARNED :");
    System.out.printf( "  - 0 xand 0 = %s\n", mlp.predict(new Value[]{falsie, falsie})[0].asBoolean());
    System.out.printf( "  - 0 xand 1 = %s\n", mlp.predict(new Value[]{falsie, trusie})[0].asBoolean());
    System.out.printf( "  - 1 xand 0 = %s\n", mlp.predict(new Value[]{trusie, falsie})[0].asBoolean());
    System.out.printf( "  - 1 xand 1 = %s\n", mlp.predict(new Value[]{trusie, trusie})[0].asBoolean());
  }
}

