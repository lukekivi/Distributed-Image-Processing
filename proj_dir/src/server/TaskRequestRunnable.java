/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import pa1.ImageProcessingNode;
import pa1.InvalidLocation;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import server.utils.ServerNodeManager;
import utils.NodeData;

public class TaskRequestRunnable implements Runnable {
    private static final String LOG_TAG = "TaskRequestRunnable: ";
    private static final int MAX_REJECTIONS = 40;

    private volatile TaskReceipt taskReceipt = null;
    private TaskRequest taskRequest = null;
    private ServerNodeManager serverNodeManager;

    public TaskRequestRunnable(
        ServerNodeManager serverNodeManager,
        TaskRequest taskRequest
    ) {
        this.serverNodeManager = serverNodeManager;
        this.taskRequest = taskRequest;
    }

    @Override
    public void run() {
        int rejections = 0;

        while (taskReceipt == null) {

            // Do image processing
            try {
                NodeData nodeData = serverNodeManager.getRandomNodeData();
                TaskReceipt receipt = perform(nodeData.getAddress(), nodeData.getPort());

                if (receipt.status == TaskStatus.REJECTED) {
                    // Try another node
                    System.out.println("Node " + nodeData.getPort() + " rejected and will try again");
                    rejections++;
                    if (rejections == MAX_REJECTIONS) {
                        taskReceipt = new TaskReceipt(
                            taskRequest.task,
                            TaskStatus.FAILURE,
                            "Task rejected too many times."
                        );
                    }
                    continue;
                } else {
                    taskReceipt = receipt;
                }

            } catch (TException exception) {
                System.out.println(LOG_TAG + exception);
            }
        }
    }

    private TaskReceipt perform(String address, int portNum) throws TException {
        TaskReceipt receipt;

        try {
            TTransport transport = new TSocket(address, portNum); 
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
    
            ImageProcessingNode.Client client = new ImageProcessingNode.Client(protocol);
    
            receipt = client.sendTask(taskRequest);
            System.out.println("TaskRequestRunnable: task Completed.");

            transport.close();

        } catch (TTransportException exception) {
            String localErrorMsg = "Node not running as expected.";
            System.out.println(localErrorMsg);
            receipt = new TaskReceipt(
                taskRequest.task,
                TaskStatus.FAILURE,
                localErrorMsg
            );
        } catch (InvalidLocation exception) {
            System.out.println(exception.msg);
            receipt = new TaskReceipt(
                taskRequest.task,
                TaskStatus.FAILURE,
                exception.msg
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            receipt = new TaskReceipt(
                taskRequest.task,
                TaskStatus.FAILURE,
                exception.getMessage()
            );
        }
    
        return receipt;
    }

    public TaskReceipt getTaskReceipt() {
        return taskReceipt;
    }
}