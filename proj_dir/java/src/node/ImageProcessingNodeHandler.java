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

    private static final String CONFIG_FILE_PATH = System.getenv("PROJ_PATH") + "/config/machine.txt";
    static private final NodeManager nodeManager = new NodeManager();
    static public int nodeNum;

    public ImageProcessingNodeHandler(int num) {
        nodeNum = num;
    }

    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {
        System.out.println("Task received!");

        TaskReceipt receipt;
        TaskRequest myTask = task;
        String dataPath = myTask.task;
        ReadIn reader = new ReadIn();
        SchedulingPolicy policy = reader.getPolicy(CONFIG_FILE_PATH);

        if (dataPath == "" || myTask == null) {
            return new TaskReceipt(
                dataPath,
                TaskStatus.FAILURE,
                "Improper Task Request."
            );
        }
        
        NodeData[] nodes = reader.getNodes(CONFIG_FILE_PATH);
        double prob = nodes[nodeNum].getProbability();

        try {
            boolean decision = true;
            if (policy == SchedulingPolicy.OPTIONAL) {
                System.out.println("Checking for rejection: ");
                decision = nodeManager.decide(prob);
            }

            if (decision) {
                // Conduct image operation here
                TransformationData transformationData = nodeManager.transformImage(dataPath, prob);

                if (transformationData.getStatus() == TransformationStatus.SUCCESS) {
                    receipt = new TaskReceipt(
                        dataPath,
                        TaskStatus.SUCCESS,
                        "Successful task completion."
                    );
                } else {
                    receipt = new TaskReceipt(
                        dataPath,
                        TaskStatus.FAILURE,
                        transformationData.getMsg()
                    );
                }

            } else {
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
        return receipt;
    }
}