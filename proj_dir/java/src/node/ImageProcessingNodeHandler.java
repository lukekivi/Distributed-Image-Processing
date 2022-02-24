/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
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
    public TaskReceipt sendTask(TaskRequest task) throws InvalidLocation {
            return new TaskReceipt(
                task.getTask(),
                TaskStatus.SUCCESS,
                SchedulingPolicy.MANDATORY,
                "Successful task completion."
            );
    }
}