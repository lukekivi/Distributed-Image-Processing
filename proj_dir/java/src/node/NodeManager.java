/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

 // Used https://java2blog.com/delay-java-program-few-secs/ for help with sleeping

package node;
import java.util.Random;
import pa1.SchedulingPolicy;
import Thread;

public class NodeManager {
    private static final String OUTPUT_DIRECTORY_NAME = "output_dir";

    /**
     * @brief Conducts the heavy lifting of the image transformation call
     * @param imagePath: path leading to the image
     * @param prob: probability of load injecting
     * @return nothing
     */
    public void transformImage(String imagePath, double prob) {
        String outputPath = imagePath + "/../" + OUTPUT_DIRECTORY_NAME;
        boolean delay = decide(prob);
        if (delay) {
            Thread.sleep(3000);
        }
        // Conduct image transformation with OpenCV here
    }

    /**
     * @brief Calculates whether or not to do something depending on probability
     * @param probability: probability passed in
     * @return boolean for whether or not the probability hit
     */
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