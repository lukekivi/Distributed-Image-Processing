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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("node_0")) {
                    address = line[1]; // address
                    prob = Double.parseDouble(line[2]); // probability
                    port = Integer.parseInt(line[3]); // port number
                    nodes[0] = new NodeData(address, prob, port);
                } else if (line[0].equals("node_1")) {
                    address = line[1]; // address
                    prob = Double.parseDouble(line[2]); // probability
                    port = Integer.parseInt(line[3]); // port number
                    nodes[1] = new NodeData(address, prob, port);
                } else if (line[0].equals("node_2")) {
                    address = line[1]; // address
                    prob = Double.parseDouble(line[2]); // probability
                    port = Integer.parseInt(line[3]); // port number
                    nodes[2] = new NodeData(address, prob, port);
                } else if (line[0].equals("node_3")) {
                    address = line[1]; // address
                    prob = Double.parseDouble(line[2]); // probability
                    port = Integer.parseInt(line[3]); // port number
                    nodes[3] = new NodeData(address, prob, port);
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (nodes[0] == null || nodes[1] == null || nodes[2] == null || nodes[3] == null) {
            System.out.println("Improper Configuration file - Invalid Nodes given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("server")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Configuration file - No Server given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // Client address
                if (line[0].equals("client")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Configuration file - No Client given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // Scheduling Policy
                if (line[0].equals("policy")) {
                    if (line[1].equals("random")) {
                        policy = SchedulingPolicy.MANDATORY;
                    }
                    else if (line[1].equals("balancing")){
                        policy = SchedulingPolicy.OPTIONAL;
                    }
                    else {
                        System.out.println("Improper Configuration file - Invalid Policy.\n");
                        System.exit(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file\n");
            System.exit(1);
        }
        if (policy == null) {
            System.out.println("Improper Configuration file - No Policy given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // Scheduling Policy
                if (line[0].equals("data")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Configuration file - No Data path given.\n");
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
            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // thrift path
                if (line[0].equals("THRIFT_LIB_PATH")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Environment file - No Thrift path given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // openCV path
                if (line[0].equals("OPENCV_LIB_PATH")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Environment file - No OpenCV path given.\n");
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

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" "); // proj path
                if (line[0].equals("PROJ_PATH")) {
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Environment file.\n");
            System.exit(1);
        }
        if (ans.equals("")) {
            System.out.println("Improper Environment file - No Project path given.\n");
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
        if (ans == -1) {
            System.out.println("Improper Configuration file - Invalid Server port given.\n");
            System.exit(1);
        }
        return ans;
    }

    // public static void main(String[] args) {
    //     ReadIn r = new ReadIn();
    //     NodeData[] nodes;
    //     nodes = r.getNodes("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     String serverA = r.getServer("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     String clientA = r.getClient("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     String dataPath = r.getData("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     String thriftPath = r.getThriftPath("/project/droeg022/Distributed-Image-Processing/proj_dir/env.txt");
    //     String opencvPath = r.getThriftPath("/project/droeg022/Distributed-Image-Processing/proj_dir/env.txt");
    //     String projPath = r.getThriftPath("/project/droeg022/Distributed-Image-Processing/proj_dir/env.txt");
    //     int serverPort = r.getServerPort("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");

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

    //     System.out.println("Server Address: " + serverA);
    //     System.out.println("Client Address: " + clientA);
    //     System.out.println("Data Path: " + dataPath);
    //     System.out.println("Thrift path: " + thriftPath);
    //     System.out.println("opencv path: " + opencvPath);
    //     System.out.println("proj_path path: " + projPath);
    //     System.out.println("Server Port num: " + serverPort);
    // }
}