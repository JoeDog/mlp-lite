package tests;

import java.util.Random;
import org.joedog.ann.*;
import org.joedog.ann.function.*;

public class Count {
  /**
   * Given input n, produce the next five consecutive numbers.
   */
  public static void main(String [] args) {
    MLP mlp = MLP.getInstance(new int[]{1,5,5,5,5}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);
    Value [] val = new Value[100];
    for (int i = 1; i < val.length; i++) {
      val[i-1] = new Value.ValueBuilder().inRange(1, 100, i);
    }
    for (int i = 0; i < 94; i++) {
      System.out.printf(
        "Training sets: %d => %d, %d, %d, %d, %d\n", 
        val[i].asInt(),   val[i+1].asInt(), val[i+2].asInt(), 
        val[i+3].asInt(), val[i+4].asInt(), val[i+5].asInt()
      );
      mlp.addExample(new Value[]{val[i]}, new Value[]{val[i+1], val[i+2], val[i+3], val[i+4], val[i+5]});
    }
    mlp.learnByExample(100000000);
    System.out.println("LEARNED :");
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
    mlp.save("count.xml");
  }
}
