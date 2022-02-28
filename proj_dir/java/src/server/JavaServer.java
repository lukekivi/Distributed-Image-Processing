package server;

import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import utils.ReadIn;
import pa1.ImageProcessingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.log4j.BasicConfigurator;

public class JavaServer {

    private static final String CONFIG_EXT = "/machine.txt";
    private static final String CONFIG_VAR = "PROJ_PATH";
    public static ImageProcessingServerHandler handler;
    public static ImageProcessingServer.Processor processor;

    public static void main(String[] args) {

        try {

            if (args.length == 1) {
                System.setOut(outputFile(args[0]));
            }

            BasicConfigurator.configure();

            handler = new ImageProcessingServerHandler();
            processor = new ImageProcessingServer.Processor<ImageProcessingServerHandler>(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };

            new Thread(simple).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void simple(ImageProcessingServer.Processor processor) {
        try {
            ReadIn readIn = new ReadIn();
            String config = System.getenv(CONFIG_VAR) + CONFIG_EXT;
            int portNum = readIn.getServerPort(config);

            if (portNum == -1) {
                System.out.println("Unable to get port num from machine.txt");
                System.exit(-1);
            }

            TServerTransport serverTransport = new TServerSocket(portNum);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrintStream outputFile(String name) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(name)), true);
    }
}