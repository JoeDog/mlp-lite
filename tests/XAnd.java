package tests;

import org.joedog.ann.*;
import org.joedog.ann.data.*;
import org.joedog.ann.function.*;
import org.joedog.util.io.FileUtils;

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
    MLP mlp      = null;
    Value trusie = new Value.ValueBuilder().asBoolean(true);
    Value falsie = new Value.ValueBuilder().asBoolean(false);

    if (FileUtils.exists("xand.xml")) {
      mlp = MLP.getInstance("xand.xml"); 
      System.out.println("TRAINED :");
    } else {
      mlp = MLP.getInstance(new int[]{2,2,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);
      mlp.addExample(new Value[]{falsie, falsie}, new Value[]{falsie});
      mlp.addExample(new Value[]{falsie, trusie}, new Value[]{falsie});
      mlp.addExample(new Value[]{trusie, falsie}, new Value[]{falsie});
      mlp.addExample(new Value[]{trusie, trusie}, new Value[]{trusie});
      mlp.learnByExample(4000000);
      mlp.save("xand.xml");
      System.out.println("LEARNED :");
    }
    System.out.printf( "  - 0 xand 0 = %s\n", mlp.predict(new Value[]{falsie, falsie})[0].asBoolean());
    System.out.printf( "  - 0 xand 1 = %s\n", mlp.predict(new Value[]{falsie, trusie})[0].asBoolean());
    System.out.printf( "  - 1 xand 0 = %s\n", mlp.predict(new Value[]{trusie, falsie})[0].asBoolean());
    System.out.printf( "  - 1 xand 1 = %s\n", mlp.predict(new Value[]{trusie, trusie})[0].asBoolean());
  }
}

