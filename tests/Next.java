package tests;

import java.util.Random;
import org.joedog.ann.*;
import org.joedog.ann.data.*;
import org.joedog.ann.function.*;
import org.joedog.util.io.FileUtils;

public class Next {
  /**
   * Given input n, produce the next consecutive number.
   */
  public static void main(String [] args) {
    MLP      mlp = null;
    Value [] val = new Value[20];
    for (int i = 1; i < val.length; i++) {
      val[i-1] = new Value.ValueBuilder().inRange(i, 1, 100);
    }
  
    if (FileUtils.exists("next.xml")) {
      mlp = MLP.getInstance("next.xml");
      System.out.println("TRAINED :");
    } else {
      mlp = MLP.getInstance(new int[]{1,20,20,1}, new FunctionFactoryImpl().getFunction(Function.SIGMOIDAL), true);
      for (int i = 0; i < val.length-2; i++) {
        System.out.printf("Training sets: %d => %d\n", val[i].asInt(), val[i+1].asInt());
        mlp.addExample(new Value[]{val[i]}, new Value[]{val[i+1]});
      }
      mlp.learnByExample(50000000);
      mlp.save("next.xml");
      System.out.println("LEARNED :");
    }

    for (int i = 0; i < 5; i++) {
      Random   rdm = new Random();
      int      r   = rdm.nextInt(19-1) + 1; 
      Value [] in  = new Value[]{val[r]};
      Value [] out = mlp.predict(in); 
      System.out.printf( "  input: %d output: %d\n", in[0].asInt(), out[0].fromAnalog());
    }
  }
}
