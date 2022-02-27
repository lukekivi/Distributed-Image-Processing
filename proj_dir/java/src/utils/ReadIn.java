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
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return nodes;
    }

    public double getProbability(String name, int num) {
        File file = new File(name);
        double ans = 0;
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in node line
            for (int i = 0; i < num; i++) { // Looping to get to proper node
                line = scanConfig.nextLine().split(" ");
            }

            line = scanConfig.nextLine().split(" ");
            if (!line[0].equals("node_" + num)) {
                System.out.println("Improper Configuration file.\n");
                System.exit(1);
            }
            ans = Double.parseDouble(line[2]);
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return ans;
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
            String[] line; // Read in client line
    
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
}