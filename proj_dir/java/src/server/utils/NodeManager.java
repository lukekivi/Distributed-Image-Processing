package server.utils;

import java.util.Random;
import utils.ReadIn;


public class NodeManager {
    
    private Random random;
    private String[] nodes;

    public NodeManager() {
        random = new Random();
        ReadIn r = new ReadIn();
        String config = System.getenv("PROJ_PATH") + "/machine.txt";

        nodes = r.getNodes(config);
    }

    public String getRandomNodeAddress() {
        int randomIndex = random.nextInt() % nodes.length;
        return nodes[randomIndex];
    }

}