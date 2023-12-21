package tests;

import org.joedog.ann.*;
import org.joedog.ann.data.*;
import org.joedog.ann.function.*;
import org.joedog.util.io.FileUtils;

/**
 * @author Jeffrey Fulmer
 */
public class XOr {
  /**
   * | A | B | Out
   * +---+---+----
   * | F | F | F
   * +---+---+---
   * | F | T | T
   * +---+---+---
   * | T | F | T
   * +---+---+---
   * | T | T | F
   * +---+---+---
   */
  public XOr() {
    MLP mlp      = null;
    Value trusie = new Value.ValueBuilder().asBoolean(true);
    Value falsie = new Value.ValueBuilder().asBoolean(false);

    if (! FileUtils.exists("xor.xml")) {
      mlp = MLP.getInstance(new int[]{2,2,2,2,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);  
      mlp.addExample(new Value[]{falsie, falsie}, new Value[]{falsie});
      mlp.addExample(new Value[]{falsie, trusie}, new Value[]{trusie});
      mlp.addExample(new Value[]{trusie, falsie}, new Value[]{trusie});
      mlp.addExample(new Value[]{trusie, trusie}, new Value[]{falsie});
      mlp.learnByExample(40000000);
      mlp.save("xor.xml");
    } else {
      mlp = MLP.getInstance("xor.xml");
    }
    System.out.println("LEARNED :");
    System.out.printf( "  - 0 xor 0 = %s\n", mlp.predict(new Value[]{falsie, falsie})[0].asBoolean());
    System.out.printf( "  - 0 xor 1 = %s\n", mlp.predict(new Value[]{falsie, trusie})[0].asBoolean());
    System.out.printf( "  - 1 xor 0 = %s\n", mlp.predict(new Value[]{trusie, falsie})[0].asBoolean());
    System.out.printf( "  - 1 xor 1 = %s\n", mlp.predict(new Value[]{trusie, trusie})[0].asBoolean());
  }

  public static void main(String[] args) {
    XOr xor = new XOr();
  }
}
