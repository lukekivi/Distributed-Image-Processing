package server.utils;

import java.util.Random;

import utils.NodeData;
import utils.ReadIn;


public class ServerNodeManager {
    
    private Random random;
    private NodeData[] nodes;

    public ServerNodeManager() {
        random = new Random();
        ReadIn r = new ReadIn();
        String config = System.getenv("PROJ_PATH") + "/machine.txt";

        nodes = r.getNodes(config);
    }

    public String getRandomNodeAddress() {
        int randomIndex = random.nextInt() % 4;
        return nodes[randomIndex].getAddress();
    }

}