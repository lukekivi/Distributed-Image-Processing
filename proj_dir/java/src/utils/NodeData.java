/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

 package utils;
 import java.io.File;
 import java.util.*;

public class NodeData {
    String[][] nodes = new String[4][3];
    public NodeData(String filePath) {
        File file = new File(filePath);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in node lines
            for (int i = 0; i < 4; i++) { // Looping to get addresses of the nodes
                line = scanConfig.nextLine().split(" ");
                if (!line[0].equals("node_" + i)) {
                    System.out.println("Improper Configuration file.\n");
                    System.exit(1);
                }
                nodes[i][0] = line[1]; // address
                nodes[i][1] = line[2]; // probability
                nodes[i][2] = line[3]; // port number
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
    }

    public int getPort(int num) {
        return Integer.parseInt(nodes[num][2]);
    }

    public String getAddress(int num) {
        return nodes[num][0];
    }

    public double getProbability(int num) {
        return Double.parseDouble(nodes[num][1]);
    }

    // public static void main(String args[]) {
    //     NodeData nodes = new NodeData("/project/droeg022/Distributed-Image-Processing/proj_dir/machine.txt");
    //     System.out.println("Node0 add: " + nodes.getAddress(0));
    //     System.out.println("Node0 prob: " + nodes.getProbability(0));
    //     System.out.println("Node0 port: " + nodes.getPort(0));
    //     System.out.println("Node1 add: " + nodes.getAddress(1));
    //     System.out.println("Node1 prob: " + nodes.getProbability(1));
    //     System.out.println("Node1 port: " + nodes.getPort(1));
    //     System.out.println("Node2 add: " + nodes.getAddress(2));
    //     System.out.println("Node2 prob: " + nodes.getProbability(2));
    //     System.out.println("Node2 port: " + nodes.getPort(2));
    //     System.out.println("Node3 add: " + nodes.getAddress(3));
    //     System.out.println("Node3 prob: " + nodes.getProbability(3));
    //     System.out.println("Node3 port: " + nodes.getPort(3));
    // }
}