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
     * @param imagePath: path leading to the image
     * @param prob: probability of load injecting
     * @return data about the transformation
     */
    public TransformationData transformImage(String imagePath, double prob) {
        boolean delay = decide(prob);
        if (!delay) {
            try {
                Thread.sleep(3000);
                System.out.println("After sleep.");
            } catch (Exception e) {
                return new TransformationData(
                    TransformationStatus.FAILURE,
                    "NodeManager: transformImage() - " + e
                );
            }
        }

        String outputPath = imagePath.replace(INPUT_DIR_NAME, OUTPUT_DIR_NAME);

        return transformer.perform(imagePath, outputPath);
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