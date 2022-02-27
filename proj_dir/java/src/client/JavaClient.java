// Charles Droege
// droeg022
// Lucas Kivi
// kivix019

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pa1.*;
import utils.ReadIn;

public class JavaClient {
    public static void main(String [] args) {
        // Checking to see that proper files are provided
        if (args.length != 2) {
            System.out.println("No arguments needed.\n");
            System.exit(0);
        }
        String config = System.getenv("PROJ_PATH") + "/machine.txt";

        ReadIn r = new ReadIn();

        String serverA = r.getServer(config); // server address
        
        String dirPath = r.getData(config); // getting path to data dir

        // Creating a job to send to server
        JobRequest cJob = new JobRequest(dirPath);

        try {
            TTransport transport;
            transport = new TSocket(serverA, 9090); 
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            ImageProcessingServer.Client client = new ImageProcessingServer.Client(protocol);

            perform(client, cJob); // Passing job as arg for client

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        } 
    }

    private static void perform(ImageProcessingServer.Client client, JobRequest clientJob) throws TException {
        JobReceipt receipt;
        try {
            receipt = client.sendJob(clientJob); // Getting job receipt and printing info
            System.out.println("Job Receipt:\nJob: " + receipt.jobPath + "\nTime: " +
             receipt.time + "\nStatus: " + receipt.status + "\nMsg " + receipt.msg + "\n");
        } catch (InvalidLocation path) {
            System.out.println("Invalid directory path given.\n");
        }
    }
}
