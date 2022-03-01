/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

 // Used https://java2blog.com/delay-java-program-few-secs/ for help with sleeping

package node;
import java.util.Random;
import pa1.SchedulingPolicy;
import pa1.TaskStatus;
import java.lang.Thread;


public class NodeManager {
    private static final String OUTPUT_DIRECTORY_NAME = "output_dir";

    /**
     * @brief Conducts the heavy lifting of the image transformation call
     * @param imagePath: path leading to the image
     * @param prob: probability of load injecting
     * @return nothing
     */
    public TaskStatus transformImage(String imagePath, double prob) {
        String outputPath = imagePath + "/../" + OUTPUT_DIRECTORY_NAME;
        boolean delay = decide(prob);
        if (!delay) {
            try {
                Thread.sleep(3000);
                System.out.println("After sleep.");
            } catch (Exception e) {
                return TaskStatus.FAILURE;
            }
        }
        // Conduct image transformation with OpenCV here
        return TaskStatus.SUCCESS;
    }

    /**
     * @brief Calculates whether or not to do something depending on probability
     * @param probability: probability passed in
     * @return boolean, true if accepted/sleeping and false if denied or injected
     */
    public boolean decide(double probability) {
        Random rand = new Random();
        double val = rand.nextDouble();
        System.out.println("Value calculated: " + val + "\nProbability: " + probability);
        if (val <= probability) {
            return false;
        }
       return true;
    }



}