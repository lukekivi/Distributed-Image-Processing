/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package node;

import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import pa1.SchedulingPolicy;
import pa1.ImageProcessingNode;

public class NodeTaskRunnable implements Runnable {
    private TaskRequest task;
    SchedulingPolicy policy;
    double prob;
    TaskReceipt receipt;
    NodeManager nodeManager;    

    public NodeTaskRunnable(
        NodeManager nodeManager,
        TaskRequest task,
        SchedulingPolicy policy,
        double prob
    ) {
        this.nodeManager = nodeManager;
        this.task = task;
        this.policy = policy;
        this.prob = prob;
    }


    @Override
    public void run() {
        String inputPath = task.task;
        String outputPath = inputPath + "/../output_dir";
        try {
            boolean decision = true;
            if (policy == SchedulingPolicy.OPTIONAL) {
                decision = nodeManager.decide(prob);
            }

            if (decision) {
                // Conduct image operation
                TaskStatus status = nodeManager.transformImage(inputPath, prob);
                
                receipt =  new TaskReceipt(
                    inputPath,
                    status,
                    "Successful task completion."
                );
            } else {
                receipt = new TaskReceipt(
                    inputPath,
                    TaskStatus.REJECTED,
                    "Node Rejected Task."
                );
            }
        } catch (Exception e) {
            System.out.println("Error encountered, returning.");
            receipt = new TaskReceipt(
                inputPath,
                TaskStatus.FAILURE,
                "Task Failed."
            );
        }
    }


    public TaskReceipt getTaskReceipt() {
        return receipt;
    }
}