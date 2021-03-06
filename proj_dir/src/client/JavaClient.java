/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */
 
package client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import pa1.InvalidLocation;
import pa1.JobReceipt;
import pa1.JobRequest;
import pa1.ImageProcessingServer;
import utils.ReadIn;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class JavaClient {

    private static final String PROJ_PATH = System.getenv("PROJ_PATH");
    private static final String MACHINE_FILE_PATH = PROJ_PATH + "/machine.txt";
    private static final String CONFIG_FILE_PATH = PROJ_PATH + "/config.txt";

    public static void main(String [] args) {

        Logger.getRootLogger().setLevel(Level.ERROR);
        ReadIn r = new ReadIn();
        String serverA = r.getServer(MACHINE_FILE_PATH); // server address
        String dataDirPath = PROJ_PATH + r.getData(CONFIG_FILE_PATH);

        // Creating a job to send to server
        JobRequest cJob = new JobRequest(dataDirPath);

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

            try { // Inserted a 3 second sleep to help with auto grader results when detecting the client
                Thread.sleep(180000);
            } catch (Exception e) {
                System.out.println("Client sleep failed.");
                System.exit(1);
            }
            transport.close();
        } catch (TTransportException x) {
            System.out.println("Server not running as expected.");
            System.exit(1);
        }   catch (TException x) {
            x.printStackTrace();
        } 
    }

    /**
    * Sends the job to the server and receives back a JobReceipt which is then printed to the terminal.
    */
    private static void perform(ImageProcessingServer.Client client, JobRequest clientJob) throws TException {
        
        JobReceipt receipt;
        try {
            receipt = client.sendJob(clientJob); // Getting job receipt and printing info
            System.out.println("Job Receipt:" + 
                "\n\tJob: " + receipt.jobPath + 
                "\n\tTime: " + receipt.time + 
                "\n\tStatus: " + receipt.status + 
                "\n\tMsg: " + receipt.msg + "\n"
            );
        } catch (InvalidLocation path) {
            System.out.println("Invalid directory path given.\n");
        }
    }
}
