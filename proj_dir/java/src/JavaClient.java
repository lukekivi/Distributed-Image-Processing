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
    public static void main(String [] args) {

        if (args.length != 0) {
            System.out.println("No arguments needed.\n");
            System.exit(0);
        }

        try {
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();


            TProtocol protocol = new  TBinaryProtocol(transport);
            ImageProcessServer.Client client = new ImageProcessServer.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        } 
    }

    private static void perform(ImageProcessServer.Client client) throws TException {
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("Enter directory path or 'quit' to stop.\n");
            String path = s.nextLine();
            if (s.equals("quit") || s.equals("Quit")) {
                break;
            }
            System.out.println("Select a Scheduling Policy: Enter 'RANDOM' for Random or 'BALANCING' for Load balancing.")
            int cPolicy = s.nextline();
            if (cPolicy != RANDOM || cPolicy != BALANCING) {
                System.out.println("Incorrect input.\n")
                continue;
            }

            JobRequest cJob = new JobRequest();
            cJob.job = path;
            cJob.policy = cPolicy;

            JobReceipt receipt;

            try {
                receipt = client.sendJob(cJob);
                System.out.println("Job Receipt:\nJob: " + receipt.job + "\nTime: " + receipt.time + "receipt.time\n" + "Status: " + receipt.status + "\n");
            } catch (InvalidLocation path) {
                System.out.println("Invalid directory path given.\n");
            }
        }
        System.out.println("Client is now closing.\n");
    }
}
