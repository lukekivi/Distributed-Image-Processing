/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
 */

package node;
import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import pa1.ImageProcessingNode;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class JavaNode {

    public static ImageProcessingNodeHandler handler;
    public static ImageProcessingNode.Processor processor;
    public static int num = 0;
    public static void main(String [] args) {
        if (args.length != 1) {
            System.out.println("Need 1 argument, node number.");
            System.exit(1);
        }
        num = args[0];
        try {
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
    public static void simple(Request.Processor processor) {
        try {
            TServerTransport nodeTransport = new TServerSocket(9090);
            TServer node = new TThreadPoolServer(new TThreadPoolServer.Args(nodeTransport).processor(processor));

            System.out.println("Starting the simple node...");
            node.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
