/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package node;

import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import pa1.ImageProcessingNode;
import utils.NodeData;
import utils.ReadIn;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.opencv.core.Core;

public class JavaNode {

    private static final String PROJ_FILE_PATH = System.getenv("PROJ_PATH");
    private static final String MACHINE_FILE_PATH = PROJ_FILE_PATH + "/machine.txt";
    private static final String CONFIG_FILE_PATH = PROJ_FILE_PATH + "/config.txt";
    public static ImageProcessingNodeHandler handler;
    public static ImageProcessingNode.Processor processor;
    public static int num = 0;

    public static void main(String [] args) {

        Logger.getRootLogger().setLevel(Level.ERROR);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try {
            if (args.length != 1) {
                System.out.println("Need 1 argument, a node number");
                System.exit(1);
            }
            num = Integer.parseInt(args[0]); // Grabbing node number passed in

            handler = new ImageProcessingNodeHandler(num); // Creating handler 
            processor = new ImageProcessingNode.Processor<ImageProcessingNodeHandler>(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };      

            new Thread(simple).start(); // Starting thread
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
    
    public static void simple(ImageProcessingNode.Processor processor) {

        try {
            ReadIn r = new ReadIn(); // Creating a ReadIn instance to get config and machine info
            NodeData nodeData = r.getNodes(MACHINE_FILE_PATH, CONFIG_FILE_PATH)[num]; // Getting its own nodeData object

            if (nodeData == null) { // Config and machine aren't aligning with node info
                System.out.println(
                    "Error with config.txt and machine.txt file matching." +
                    "Make sure they contain the same amount of nodes."
                    );
                System.exit(1);
            }
            
            TServerTransport nodeTransport = new TServerSocket(nodeData.getPort());
            TServer node = new TThreadPoolServer( // Auto creates new threads for each task
                new TThreadPoolServer.Args(nodeTransport).processor(processor)
            );
            System.out.println("Starting the multi threaded server node...");
            node.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrintStream outputFile(String name) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(name)), true);
    }
}
