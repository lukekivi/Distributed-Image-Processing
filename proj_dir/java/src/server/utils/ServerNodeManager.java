package server.utils;

import java.util.Random;
import utils.NodeData;
import utils.ReadIn;



public class ServerNodeManager {
    
    private static final String CONFIG_FILE_PATH = System.getenv("PROJ_PATH") + "/config/machine.txt";
    private Random random;
    private NodeData[] nodes;

    public ServerNodeManager() {
        random = new Random();
        ReadIn r = new ReadIn();
        nodes = r.getNodes(CONFIG_FILE_PATH);
    }

    public NodeData getRandomNodeData() {
        int randomIndex = random.nextInt(nodes.length);
        return nodes[randomIndex];
    }

}