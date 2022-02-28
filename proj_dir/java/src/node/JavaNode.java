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
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class JavaNode {

    public static ImageProcessingNodeHandler handler;
    public static ImageProcessingNode.Processor processor;
    public static int num = 0;

    public static void main(String [] args) {
        Logger.getRootLogger().setLevel(Level.ERROR);

        try {
            if (args.length != 1) {
                System.out.println("Need 1 argument, a node number");
                System.exit(1);
            }
            num = Integer.parseInt(args[0]);

            handler = new ImageProcessingNodeHandler(num);
            processor = new ImageProcessingNode.Processor<ImageProcessingNodeHandler>(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };      

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
    public static void simple(ImageProcessingNode.Processor processor) {
        try {
            String path = System.getenv("PROJ_PATH") + "/machine.txt";
            ReadIn r = new ReadIn();
            NodeData nodeData = r.getNodes(path)[num];
            
            TServerTransport nodeTransport = new TServerSocket(nodeData.getPort());
            TServer node = new TThreadPoolServer(
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
