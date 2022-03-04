/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package node;

import utils.NodeData;
import utils.ReadIn;
import pa1.ImageProcessingNode;
import pa1.InvalidLocation;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import pa1.SchedulingPolicy;
import org.apache.thrift.TException;


public class ImageProcessingNodeHandler implements ImageProcessingNode.Iface {

    private static final String PROJ_FILE_PATH = System.getenv("PROJ_PATH");
    private static final String MACHINE_FILE_PATH = PROJ_FILE_PATH + "/machine.txt";
    private static final String CONFIG_FILE_PATH = PROJ_FILE_PATH + "/config.txt";
    private static final NodeManager nodeManager = new NodeManager();
    public static int nodeNum; // Number node that is to be used

    /**
     * Grabs the node number so it can locate its information in the array of NodeData objects
     * @param num node num which is used in nodes array
     */
    public ImageProcessingNodeHandler(int num) {
        nodeNum = num;
    }

    /**
     * Called by server, node accepts the task and deals with it
     * @param task the task that has been assigned to this node
     * @return TaskReceipt after processing the task received
     */
    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {
        
        System.out.println("Task received!");

        TaskReceipt receipt; // Initializing values
        TaskRequest myTask = task;
        String dataPath = myTask.task; // Path to the image
        ReadIn reader = new ReadIn();
        SchedulingPolicy policy = reader.getPolicy(CONFIG_FILE_PATH); // Getting either MANDATORY or OPTIONAL for scheduling policy

        if (dataPath == "" || myTask == null) { // Invalid path
            return new TaskReceipt(
                dataPath,
                TaskStatus.FAILURE,
                "Improper Task Request."
            );
        }
        
        NodeData[] nodes = reader.getNodes(MACHINE_FILE_PATH, CONFIG_FILE_PATH); // Getting array of objects containing node information

        if (nodes == null) { // Error when comparing the config and machine
            return new TaskReceipt(
                dataPath,
                TaskStatus.FAILURE,
                "Error with config.txt and machine.txt file matching." +
                    "Make sure they contain the same amount of nodes."
            );
        }

        double prob = nodes[nodeNum].getProbability(); // Getting the load-injecting/rejection probability

        try { // If optional, need to see if this node rejects or not
            boolean decision = true;
            if (policy == SchedulingPolicy.OPTIONAL) {
                System.out.println("Checking for rejection: ");
                decision = nodeManager.decide(prob);
            }

            if (decision) { // Accepted the task
                // Conduct image operation here
                TransformationData transformationData = nodeManager.transformImage(dataPath, prob);

                if (transformationData.getStatus() == TransformationStatus.SUCCESS) { // Image transformation succeeded
                    receipt = new TaskReceipt(
                        dataPath,
                        TaskStatus.SUCCESS,
                        "Successful task completion."
                    );
                } else { // Image transformation failed
                    receipt = new TaskReceipt(
                        dataPath,
                        TaskStatus.FAILURE,
                        transformationData.getMsg()
                    );
                }

            } else { // Rejected the task
                System.out.println("Rejected");
                receipt = new TaskReceipt(
                    dataPath,
                    TaskStatus.REJECTED,
                    "Node Rejected Task."
                );
            }
        } catch (Exception e) {
            System.out.println("Error encountered, returning.");
            return new TaskReceipt(
                dataPath,
                TaskStatus.FAILURE,
                "Task Failed."
            );
        }
        return receipt; // Returning the TasReceipt to the server
    }
}