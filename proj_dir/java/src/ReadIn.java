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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10;
    }

    public String getThriftPath(String name) {
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in thrift path line

            line = scanConfig.nextLine().split(" "); // thrift path
            if (!line[0].equals("THRIFT_LIB_PATH")) {
                System.out.println("Improper Environment file.\n");
                System.exit(1);
            }
            return line[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOpenCVPath(String name) {
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in thrift path line
            line = scanConfig.nextLine().split(" "); // Skip to openCV file line

            line = scanConfig.nextLine().split(" "); // openCV path
            if (!line[0].equals("OPENCV_LIB_PATH")) {
                System.out.println("Improper Environment file.\n");
                System.exit(1);
            }
            return line[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getProjPath(String name) {
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in Proj path line
            line = scanConfig.nextLine().split(" "); // Iterating until proj line
            line = scanConfig.nextLine().split(" ");

            line = scanConfig.nextLine().split(" "); // proj path
            if (!line[0].equals("PROJ_PATH")) {
                System.out.println("Improper Environment file.\n");
                System.exit(1);
            }
            return line[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}