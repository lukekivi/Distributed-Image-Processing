/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package server.utils;

import java.util.Random;
import utils.NodeData;
import utils.ReadIn;



public class ServerNodeManager {
    
    private static final String PROJ_PATH = System.getenv("PROJ_PATH");
    private static final String MACHINE_FILE_PATH = PROJ_PATH + "/machine.txt";
    private static final String CONFIG_FILE_PATH = PROJ_PATH + "/config.txt";
    private Random random;
    private NodeData[] nodes;

    public ServerNodeManager() {
        random = new Random();
        ReadIn r = new ReadIn();
        nodes = r.getNodes(MACHINE_FILE_PATH, CONFIG_FILE_PATH);
    }

    public boolean isSuccessful() {
        return nodes != null;
    }

    public NodeData getRandomNodeData() {
        int randomIndex = random.nextInt(nodes.length);
        return nodes[randomIndex];
    }

}