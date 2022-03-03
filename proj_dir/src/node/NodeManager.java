/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

 // Used https://java2blog.com/delay-java-program-few-secs/ for help with sleeping

package node;

import java.util.Random;
import pa1.SchedulingPolicy;
import java.lang.Thread;


public class NodeManager {

    private static final String INPUT_DIR_NAME = "input_dir";
    private static final String OUTPUT_DIR_NAME = "output_dir";
    private final OpenCVTransformer transformer = new OpenCVTransformer();

    /**
     * @brief Conducts the heavy lifting of the image transformation call
     * @param imagePath path leading to the image
     * @param prob probability of load-injection
     * @return data about the transformation
     */
    public TransformationData transformImage(String imagePath, double prob) {

        boolean delay = decide(prob); // Calculating whether or not a 3 second delay will be implemented
        if (!delay) {
            try {
                Thread.sleep(3000); // 3 second delay
                System.out.println("After sleep.");
            } catch (Exception e) {
                return new TransformationData(
                    TransformationStatus.FAILURE,
                    "NodeManager: transformImage() - " + e
                );
            }
        }

        String outputPath = imagePath.replace(INPUT_DIR_NAME, OUTPUT_DIR_NAME); // File that the transformation will write to

        return transformer.perform(imagePath, outputPath); // Transform
    }

    /**
     * @brief Calculates whether or not to do something depending on probability
     * @param probability: probability of either rejection or load-injection
     * @return boolean, true if accepted/no-delay and false if denied or time delay
     */
    public boolean decide(double probability) {
        
        Random rand = new Random();
        double val = rand.nextDouble(); // Calculating random double value 0.0<=val<=1.0
        System.out.println("Value calculated: " + val + "\nProbability: " + probability); // Logging
        if (val <= probability) { // Hits if val is below probability
            return false;
        }
       return true;
    }
}