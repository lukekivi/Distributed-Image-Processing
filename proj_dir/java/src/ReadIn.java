// Charles Droege
// droeg022
// Lucas Kivi
// kivix019

import java.util.*;
import java.io.File;

public class ReadIn {

    public String[] getNodes(String name) {
        String[] nodes = new String[4]; // Array of nodes
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in node lines
            for (int i = 0; i < 4; i++) { // Looping to get addresses of the nodes
                line = scanConfig.nextLine().split(" ");
                if (!line[0].equals("node_" + i)) {
                    System.out.println("Improper Configuration file.\n");
                    System.exit(1);
                }
                nodes[i] = line[1];
            }
        } catch (Exception fileName) {
            System.out.println("Caught exception.\n");
            System.exit(1);
        }
        return nodes;
    }

    public String getServer(String name) {
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in client line
            line = scanConfig.nextLine().split(" "); // Iterating through node lines
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");

            line = scanConfig.nextLine().split(" "); // Client address
            if (!line[0].equals("server")) {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);
            }
            return line[1];
        } catch (Exception fileName) {
            System.out.println("Improper file name.\n");
            System.exit(1);
        }
        return null;
    }

    public String getClient(String name) {
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in client line
            line = scanConfig.nextLine().split(" "); // Iterating through node and server lines
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");

            line = scanConfig.nextLine().split(" "); // Client address
            if (!line[0].equals("client")) {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);
            }
            return line[1];
        } catch (Exception fileName) {
            System.out.println("Improper file name.\n");
            System.exit(1);
        }
        return null;
    }

    public int getPolicy(String name) {
        int policy = 0;
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in client line
            line = scanConfig.nextLine().split(" "); // Iterating through node, server, and client lines
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");
            line = scanConfig.nextLine().split(" ");

            line = scanConfig.nextLine().split(" "); // Scheduling Policy
            if (line[1].equals("random")) {
                policy = 1;
            }
            else if (line[1].equals("balancing")){
                policy = 0;
            }
            else {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);               
            }
            return policy;
        } catch (Exception fileName) {
            System.out.println("Improper file name.\n");
            System.exit(1);
        }
        return 10;
    }

    public static void main(String [] args) {
        ReadIn myRead = new ReadIn();
        String[] test = myRead.getNodes("machine.txt");
        for (int i = 0; i < 4; i++) {
            System.out.println("Node " + i + ": " + test[i]);
        }
        System.out.println("Server: " + myRead.getServer("machine.txt"));
        System.out.println("Client: " + myRead.getClient("machine.txt"));
        System.out.println("Policy: " + myRead.getPolicy("machine.txt"));
    }
}