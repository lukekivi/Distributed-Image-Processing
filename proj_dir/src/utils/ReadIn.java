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
    /**
     * Returns an array of NodeData objects which contain node info
     * @param machineFileName path to machine.txt
     * @param configFileName path to config.txt
     * @return array of NodeData objects
     */
    public NodeData[] getNodes(String machineFileName, String configFileName) {
        
        String address;
        double prob;
        int port;
        NodeData[] nodes = null;
        ArrayList<String> nodeAddresses = new ArrayList<String>(); // Dynamic list of node addresses
        File machineFile = new File(machineFileName); // Getting files from names
        File configFile = new File(configFileName);

        try {
            Scanner scanConfig = new Scanner(machineFile); // Scanner for machine file
            String[] line; // Read in node lines

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].contains("node_")) { // Detects a node
                    nodeAddresses.add(line[1]); // Add to list
                }
            }

            scanConfig = new Scanner(configFile); // Scanner for config file
            nodes = new NodeData[nodeAddresses.size()]; // NodeData array according to num of nodes in machine file

            int nodesInConfig = 0; // Nodes counter for config file
            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].contains("node_")) { // Detects a node
                    if (nodesInConfig >= nodeAddresses.size()) { // Check num of nodes in config < num in machine
                        return null;
                    } else {
                        prob = Double.parseDouble(line[1]); // Set probability
                        port = Integer.parseInt(line[2]);   // Set port number
                        nodes[nodesInConfig] = new NodeData(nodeAddresses.get(nodesInConfig), prob, port); // Create NodeData object
                        nodesInConfig++; // Increase num of nodes in config
                    }
                }
            }

            if (nodesInConfig != nodeAddresses.size()) { // Unequal num of nodes in machine and config files
                return null;
            }

        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        return nodes;
    }

    /**
     * Grabs the server address
     * @param name file to be checked for server, machine.txt
     * @return the address of the server
     */
    public String getServer(String name) {

        String ans = "";
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in server line

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("server")) { // Detect server line
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans.equals("")) { // Server line never found
            System.out.println("Improper Configuration file - No Server given.\n");
            System.exit(1);
        }
        return ans;
    }

    /**
     * Grabs the client address
     * @param name file to be checked for client, machine.txt
     * @return the address of the client
     */
    public String getClient(String name) {

        String ans = "";
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line; // Read in client line

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("client")) { // Detects client line
                    ans = line[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans.equals("")) { // Client line never found
            System.out.println("Improper Configuration file - No Client given.\n");
            System.exit(1);
        }
        return ans;
    }

    /**
     * Grabs the scheduling policy
     * @param name file to be checked for policy, config.txt
     * @return the scheduling policy, either 'random' or 'balancing'
     */
    public SchedulingPolicy getPolicy(String name) {

        SchedulingPolicy policy = null;
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line;

            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("policy")) { // Detected policy line
                    if (line[1].equals("random")) { // Random so forced to accept
                        policy = SchedulingPolicy.MANDATORY;
                    }
                    else if (line[1].equals("balancing")){ // Balancing so capable of rejecting
                        policy = SchedulingPolicy.OPTIONAL;
                    }
                    else { // Not a valid policy
                        System.out.println("Improper Configuration file - Invalid Policy.\n");
                        System.exit(1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Improper Configuration file\n");
            System.exit(1);
        }
        if (policy == null) { // Policy line never found
            System.out.println("Improper Configuration file - No Policy given.\n");
            System.exit(1);
        }
        return policy;
    }

    /**
     * Grabs the path to directory containing images
     * @param name file to be checked for data path, config.txt
     * @return the path to the image directories. Either empty or leads to a test directory
     */
    public String getData(String name) {

        String ans = null;
        File file = new File(name);
        try {
            Scanner scanConfig = new Scanner(file);
            String[] line;
            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("data")) { // Detected data line
                    if (line.length > 1 ) { // Leads to test directory or another
                        ans = "/"  + line[1];
                    } else { // Image directories are in proj_dir
                        ans = "";
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Improper Configuration file.\n");
            System.exit(1);
        }
        if (ans == null) { // Data line never found
            System.out.println("Improper Configuration file - No Data path given.\n");
            System.exit(1);
        }
        return ans;
    }

    /**
     * Grabs the port number
     * @param name file to be checked for server port, config.txt
     * @return server's port number or -1 if it can't be found
     */
    public int getServerPort(String name)  {

        int ans = -1;
        try {
            FileInputStream file = new FileInputStream(name);
            Scanner scanConfig = new Scanner(file);
            String[] line;
    
            while (scanConfig.hasNextLine()) {
                line = scanConfig.nextLine().split(" ");
                if (line[0].equals("server")) { // Found server line
                    ans = Integer.parseInt(line[1]);
                    break;
                }
            }
            
        } catch (Exception exception) {
            System.out.println("ReadIn: getServerPort() - " + exception);
        }
        if (ans == -1) { // Server line never found
            System.out.println("Improper Configuration file - Invalid Server port given.\n");
            System.exit(1);
        }
        return ans;
    }
}