package server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pa1.InvalidLocation;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import server.utils.ServerNodeManager;
import utils.NodeData;

public class TaskRequestRunnable implements Runnable {
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
        while (taskReceipt == null) {
            // Do image processing
            try {

                NodeData nodeData = serverNodeManager.getRandomNodeData();
                TaskReceipt receipt = perform(nodeData.getAddress(), nodeData.getPort());

                if (receipt.status == TaskStatus.REJECTED) {
                    // Try another node
                    continue;
                } else {
                    taskReceipt = receipt;
                }

            } catch (TException exception) {
                System.out.println(exception);
                System.exit(1);
            }
        }
    }

    private TaskReceipt perform(String address, int portNum) throws TException {
        TaskReceipt receipt;
        TTransport transport = new TSocket(address, portNum); 
        transport.open();

        TProtocol protocol = new  TBinaryProtocol(transport);
    
        ImageProcessingNode.Client client = new ImageProcessingNode.Client(protocol);
    
        try {
            receipt = client.sendTask(taskRequest);
            System.out.println("Job Receipt:\nJob: " + receipt.taskPath + 
                    "\nStatus: " + receipt.status + "\nMsg " + receipt.msg + "\n");
        } catch (InvalidLocation exception) {
            receipt = new TaskReceipt(
                taskRequest.task,
                TaskStatus.FAILURE,
                exception.msg
            );
        }
    
        transport.close();

        return receipt;
    }

    public TaskReceipt getTaskReceipt() {
        return taskReceipt;
    }
}