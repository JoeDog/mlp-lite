package tests;

import java.util.Random;
import org.joedog.ann.*;
import org.joedog.ann.function.*;

public class CountTrained {
  /**
   * Given input n, produce the next five consecutive numbers.
   */
  public static void main(String [] args) {
    MLP mlp = MLP.getInstance("count.xml");
    Value [] val = new Value[100];
    for (int i = 1; i < val.length; i++) {
      val[i-1] = new Value.ValueBuilder().inRange(1, 100, i);
    }
    for (int i = 0; i < 5; i++) {
      Random   rdm = new Random();
      int      r   = rdm.nextInt(94-1) + 1; 
      Value [] in  = new Value[]{val[r]};
      Value [] out = mlp.predict(in); 
      System.out.printf( "  input: %d output: %d, %d, %d, %d, %d\n", 
        in[0].asInt(),  out[0].asInt(0,100), out[1].asInt(0,100), 
        out[2].asInt(1,100), out[3].asInt(1,100), out[4].asInt(1,100)
      );
    }
  }
}
