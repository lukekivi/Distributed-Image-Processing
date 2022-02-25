/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */
package node;

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

    public ImageProcessingNodeHandler(int num) [
        nodeNum = num;
    ]

    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {

        System.out.println("Task received!");
        TaskRequest myTask = task;
        String inputPath = myTask.task;
        String config = System.getenv("PROJ_PATH") + "/machine.txt";
        ReadIn reader = new ReadIn();
        SchedulingPolicy policy = reader.getPolicy(config);

        if (inputPath == "" || myTask == null) {
            return new TaskReceipt(
                TaskStatus.FAILURE,
                policy,
                "Improper Task Request."
            );
        }
        
        double prob = reader.getProbability(config, nodeNum);

        boolean decision = true;
        if (policy == SchedulingPolicy.OPTIONAL) {
            decision = nodeManager.decide(prob);
        }

        if (decision) {
            nodeManager.transformImage(inputPath, prob);
            // Conduct image operation
            return new TaskReceipt(
                TaskStatus.SUCCESS,
                policy,
                "Successful task completion."
            );
        } else {
            return new TaskReceipt(
                TaskStatus.REJECTED,
                policy,
                "Node Rejected Task."
            );
        }
    }
}