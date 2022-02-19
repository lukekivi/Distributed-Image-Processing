// Charles Droege
// droeg022
// Lucas Kivi
// kivix019

import pa1.*;
import java.util.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class JavaClient {
    // Env vars from env file
    String THRIFT_LIB_PATH;
    String OPENCV_LIB_PATH;
    String PROJ_PATH;

    public static void main(String [] args) {
        // Checking to see that proper files are provided
        if (args.length != 2) {
            System.out.println("Need config file first and Env file second.\n");
            System.exit(0);
        }
        String config = args[0];
        String env = args[1];

        // Grab information from config file here
        Scanner scanConfig = new Scanner(config);
        String[] line; // Read in lines of each file
        String[] nodes = new String[3]; // Array of nodes
        String client; // client address
        String server; // server address

        // Reading in machine file
        try {
            for (int i = 0, i < 3; i++) { // Looping to get addresses of the nodes
                line = scanConfig.nextLine().split(" ");
                nodes[i] = line[1];
            }
            line = scanConfig.nextLine().split(" "); // Server address
            server = line[1];

            line = scanConfig.nextLine().split(" "); // Client address
            client = line[1];
        } catch (NoSuchElementException err) {
            System.out.println("Improper Configuration file.\n");
        }

        // Reading in environment file
        try {
            Scanner scanEnv = new Scanner(env);
            line = scanEnv.nextLine().split(" "); // Path to thrift 
            THRIFT_LIB_PATH = line[1];

            line = scanEnv.nextLine().split(" "); // Path to opencv
            OPENCV_LIB_PATH = line[1];

            line = scanEnv.nextLine().split(" "); // Path to directory of files
            PROJ_PATH = line[1];
        } catch (NoSuchElementException err) {
            System.out.println("Improper Environment file.\n");
        }
        
        // Creating a job to send to server
        JobRequest cJob = new JobRequest();
        cJob.job = PROJ_PATH;
        cJob.policy = RANDOM; // Can be changed

        try {
            TTransport transport;
            transport = new TSocket(server, 9090);
            transport.open();


            TProtocol protocol = new  TBinaryProtocol(transport);
            ImageProcessServer.Client client = new ImageProcessServer.Client(protocol);

            perform(client, cJob); // Passing job as arg for client

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        } 
    }

    private static void perform(ImageProcessServer.Client client, JobRequest clientJob) throws TException {
        JobReceipt receipt;
        try {
            receipt = client.sendJob(clientJob); // Getting job receipt and printing info
            System.out.println("Job Receipt:\nJob: " + receipt.job + "\nTime: " + receipt.time + "receipt.time\n" + "Status: " + receipt.status + "\n");
        } catch (InvalidLocation path) {
            System.out.println("Invalid directory path given.\n");
        }
    }
}
