/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import pa1.SchedulingPolicy;
import utils.NodeData;

public class ReadIn {

    public NodeData[] getNodes(String name) {
        String address; 
        double prob;
        int port;
        NodeData[] nodes = new NodeData[4];
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
                address = line[1]; // address
                prob = Double.parseDouble(line[2]); // probability
                port = Integer.parseInt(line[3]); // port number
                nodes[i] = new NodeData(address, prob, port);
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return nodes;
    }

    public String getServer(String name) {
        String ans = "";
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
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return ans;
    }

    public String getClient(String name) {
        String ans = "";
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
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return ans;
    }

    public SchedulingPolicy getPolicy(String name) {
        SchedulingPolicy policy = null;
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
            if (!line[0].equals("policy")) {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);
            }

            if (line[1].equals("random")) {
                policy = SchedulingPolicy.MANDATORY;
            }
            else if (line[1].equals("balancing")){
                policy = SchedulingPolicy.OPTIONAL;
            }
            else {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);               
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return policy;
    }
    
    public String getData(String name) {
        String ans = "";
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
            line = scanConfig.nextLine().split(" ");

            line = scanConfig.nextLine().split(" "); // Scheduling Policy
            if (!line[0].equals("data")) {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);
            }
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return ans;
    }

    public String getThriftPath(String name) {
        String ans = "";
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in thrift path line

            line = scanConfig.nextLine().split(" "); // thrift path
            if (!line[0].equals("THRIFT_LIB_PATH")) {
                System.out.println("Improper Environment file.\n");
                System.exit(1);
            }
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        return ans;
    }

    public String getOpenCVPath(String name) {
        String ans = "";
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
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        return ans;
    }

    public String getProjPath(String name) {
        String ans = "";
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
            ans = line[1];
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        return ans;
    }


    /**
     * Returns the port number or -1 for an error.
     */
    public int getServerPort(String name)  {
        int ans = -1;
        
        try {
            FileInputStream file = new FileInputStream(name);

            Scanner scanConfig = new Scanner(file);
            String[] line;
    
            // search for server line
            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");

                if (line[0].equals("server")) {
                    ans = Integer.parseInt(line[2]);
                    break;
                }
            }
            
        } catch (Exception exception) {
            System.out.println("ReadIn: getServerPort() - " + exception);
        }
      
        return ans;
    }

    // public static void main(String[] args) {
    //     ReadIn r = new ReadIn();
    //     NodeData[] nodes;
    //     nodes = r.getNodes("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     System.out.println("Node 0 address: " + nodes[0].getAddress());
    //     System.out.println("Node 1 address: " + nodes[1].getAddress());
    //     System.out.println("Node 2 address: " + nodes[2].getAddress());
    //     System.out.println("Node 3 address: " + nodes[3].getAddress());

    //     System.out.println("Node 0 prob: " + nodes[0].getProbability());
    //     System.out.println("Node 1 prob: " + nodes[1].getProbability());
    //     System.out.println("Node 2 prob: " + nodes[2].getProbability());
    //     System.out.println("Node 3 prob: " + nodes[3].getProbability());

    //     System.out.println("Node 0 port: " + nodes[0].getPort());
    //     System.out.println("Node 1 port: " + nodes[1].getPort());
    //     System.out.println("Node 2 port: " + nodes[2].getPort());
    //     System.out.println("Node 3 port: " + nodes[3].getPort());
    // }
}