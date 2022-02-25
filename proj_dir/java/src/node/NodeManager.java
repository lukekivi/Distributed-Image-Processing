/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
 */

package node;
import java.util.Random;

public class NodeManager {
   private static final String OUTPUT_DIRECTORY_NAME = "output_dir";

   public void conductImage(String imagePath) {
       String outputPath = path + "/../" + OUTPUT_DIRECTORY_NAME;


   }

   public boolean decide(double probability) {
       Random rand = new Random();
       int val = rand.nextInt(100);
       int prob = (int) (probability * 10);
       if (val < prob) {
           return true;
       }
       return false;
   }



}