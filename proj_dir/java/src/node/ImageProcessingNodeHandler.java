/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */
package node;

import utils.ReadIn;
import pa1.ImageProcessingNode;
import pa1.InvalidLocation;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import pa1.SchedulingPolicy;
import utils.NodeData;
import org.apache.thrift.TException;


public class ImageProcessingNodeHandler implements ImageProcessingNode.Iface {
    static private final NodeManager nodeManager = new NodeManager();
    static public int nodeNum;

    public ImageProcessingNodeHandler(int num) {
        nodeNum = num;
    }

    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {
        System.out.println("Task received!");
        TaskRequest myTask = task; // Grabbing values from Task Request
        String inputPath = myTask.task;
        String config = System.getenv("PROJ_PATH") + "/machine.txt";
        ReadIn reader = new ReadIn();
        SchedulingPolicy policy = reader.getPolicy(config);
        TaskReceipt receipt = new TaskReceipt( // Initializing TaskReceipt object
            inputPath,
            TaskStatus.FAILURE,
            "Initializing TaskReceipt object."
        );

        if (inputPath == "" || myTask == null) { // Return if the path is empty (Will check validity later)
            return new TaskReceipt(
                inputPath,
                TaskStatus.FAILURE,
                "Improper Task Request."
            );
        }
        
        NodeData[] nodes = reader.getNodes(config);
        double prob = nodes[nodeNum].getProbability();

        // Creating a runnable for the task
        NodeTaskRunnable runnable = new NodeTaskRunnable( // Creating a runnable
            nodeManager, // Manager class instance
            myTask, // Task request was sent
            policy, // Policy to be used
            prob // Probability for specific node
        );

        // Creating a thread for the task
        Thread thread = new Thread(runnable);
        thread.start();

        try {
            thread.join();
            receipt = runnable.getTaskReceipt();
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
            System.exit(1);
        }
        return receipt;
    }
}