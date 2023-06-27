package tests;

import org.joedog.ann.*;
import org.joedog.ann.function.*;

/**
 * @author Jeffrey Fulmer
 */
public class XOrTrained {
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
  public XOrTrained() {
    MLP mlp = MLP.getInstance("xor.xml");
    Value trusie = new Value.ValueBuilder().asBoolean(true);
    Value falsie = new Value.ValueBuilder().asBoolean(false);
    System.out.println("LEARNED :");
    System.out.printf( "  - 0 xor 0 = %s\n", mlp.predict(new Value[]{falsie, falsie})[0].asBoolean());
    System.out.printf( "  - 0 xor 1 = %s\n", mlp.predict(new Value[]{falsie, trusie})[0].asBoolean());
    System.out.printf( "  - 1 xor 0 = %s\n", mlp.predict(new Value[]{trusie, falsie})[0].asBoolean());
    System.out.printf( "  - 1 xor 1 = %s\n", mlp.predict(new Value[]{trusie, trusie})[0].asBoolean());
    mlp.save("tmp.xml");
  }

  public static void main(String[] args) {
    XOrTrained xor = new XOrTrained();
  }
}
