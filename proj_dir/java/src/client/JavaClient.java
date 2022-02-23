// Charles Droege
// droeg022
// Lucas Kivi
// kivix019

import java.util.*;
import pa1.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class JavaClient {

    public static void main(String [] args) {
        // Checking to see that proper files are provided
        if (args.length != 2) {
            System.out.println("Need config file first and Env file second.\n");
            System.exit(0);
        }
        String config = args[0];
        String env = args[1];

        ReadIn r = new ReadIn();
        String[] nodesA = r.getNodes(config);; // Array of node addresses
        String clientA = r.getClient(config); // client address
        String serverA = r.getServer(config); // server address
        int policy = r.getPolicy(config); // getting the policy
        String dirPath = r.getProjPath(env); // Setting proj path env var

        // Creating a job to send to server
        JobRequest cJob = new JobRequest();
        cJob.job = dirPath;

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
            System.out.println("Job Receipt:\nJob: " + receipt.jobPath + "\nTime: " + receipt.time + "\nStatus: " + receipt.status + "\n");
        } catch (InvalidLocation path) {
            System.out.println("Invalid directory path given.\n");
        }
    }
}
