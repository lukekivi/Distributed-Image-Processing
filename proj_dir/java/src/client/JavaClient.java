// Charles Droege
// droeg022
// Lucas Kivi
// kivix019
package client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pa1.InvalidLocation;
import pa1.JobReceipt;
import pa1.JobRequest;
import pa1.ImageProcessingServer;
import utils.ReadIn;

public class JavaClient {
    private static final String PROJ_PATH = System.getenv("PROJ_PATH");
    private static final String CONFIG_FILE_PATH = PROJ_PATH + "config/machine.txt";

    public static void main(String [] args) {
        
        ReadIn r = new ReadIn();
        String serverA = r.getServer(CONFIG_FILE_PATH); // server address
        String dirPath = PROJ_PATH + "/" + r.getData(CONFIG_FILE_PATH); // getting path to data dir

        // Creating a job to send to server
        JobRequest cJob = new JobRequest(dirPath);

        try {
            TTransport transport;

            int serverPortNum = r.getServerPort(CONFIG_FILE_PATH);

            if (serverPortNum == -1) {
                System.out.println("Unable to get port num from machine.txt");
                System.exit(-1);
            }

            transport = new TSocket(serverA, r.getServerPort(CONFIG_FILE_PATH)); 
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
