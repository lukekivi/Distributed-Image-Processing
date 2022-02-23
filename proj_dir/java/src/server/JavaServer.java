import pa1.*;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class JavaServer {

    public static ImageProcessingNodeHandler handler;
    public static ImageProcessingServer.Processor processor;

    public static void main(String[] args) {
        try {

            handler = new ImageProcessingServerHandler();
            processor = new ImageProcessingServer.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(handler);
                }
            };

            new Thread(simple).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void simple(ImageProcessingServer.Processor processor) {
        
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            System.out.println("Starting simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}