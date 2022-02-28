package server.utils;

import java.util.Random;
import utils.NodeData;
import utils.ReadIn;



public class ServerNodeManager {
    
    private final String CONFIG_EXT = "/machine.txt";
    private final String CONFIG_VAR = "PROJ_PATH";
    private Random random;
    private NodeData[] nodes;

    public ServerNodeManager() {
        random = new Random();
        ReadIn r = new ReadIn();
        String config = System.getenv(CONFIG_VAR) + CONFIG_EXT;

        nodes = r.getNodes(config);
    }

    public NodeData getRandomNodeData() {
        int randomIndex = random.nextInt(nodes.length);
        return nodes[randomIndex];
    }

}