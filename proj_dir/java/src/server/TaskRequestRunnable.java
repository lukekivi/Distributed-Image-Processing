package server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pa1.ImageProcessingNode;
import pa1.InvalidLocation;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import pa1.TaskStatus;
import server.utils.ServerNodeManager;

public class TaskRequestRunnable implements Runnable {
    private volatile TaskReceipt taskReceipt = null;
    private TaskRequest taskRequest = null;
    private int port;
    private ServerNodeManager serverNodeManager;

    public TaskRequestRunnable(
        ServerNodeManager serverNodeManager,
        int port,
        TaskRequest taskRequest
    ) {
        this.serverNodeManager = serverNodeManager;
        this.port = port;
        this.taskRequest = taskRequest;
    }

    @Override
    public void run() {
        while (taskReceipt == null) {
            // Do image processing
            try {
                TaskReceipt receipt = perform(serverNodeManager.getRandomNodeAddress());

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

    private TaskReceipt perform(String address) throws TException {
        TaskReceipt receipt;
        TTransport transport = new TSocket(address, this.port); 
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