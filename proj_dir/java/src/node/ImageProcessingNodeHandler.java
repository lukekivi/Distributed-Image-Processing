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
    static private final NodeManager nodeManager = new NodeManager();
    static public int nodeNum;

    public ImageProcessingNodeHandler(int num) {
        nodeNum = num;
    }

    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {
        TaskReceipt receipt;
        System.out.println("Task received!");
        TaskRequest myTask = task;
        String dataPath = myTask.task;
        String config = System.getenv("PROJ_PATH") + "/machine.txt";
        ReadIn reader = new ReadIn();
        SchedulingPolicy policy = reader.getPolicy(config);

        if (dataPath == "" || myTask == null) {
            return new TaskReceipt(
                dataPath,
                TaskStatus.FAILURE,
                "Improper Task Request."
            );
        }
        
        NodeData[] nodes = reader.getNodes(config);
        double prob = nodes[nodeNum].getProbability();

        try {
            boolean decision = true;
            if (policy == SchedulingPolicy.OPTIONAL) {
                System.out.println("Checking for rejection: ");
                decision = nodeManager.decide(prob);
            }

            if (decision) {
                // Conduct image operation here
                TaskStatus status = nodeManager.transformImage(dataPath, prob);

                receipt = new TaskReceipt(
                    dataPath,
                    status,
                    "Successful task completion."
                );
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