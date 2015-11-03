package ch.epfl.lpd;


import java.util.*;
import java.util.concurrent.CountDownLatch;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import ch.epfl.lpd.NodeInfo;
import ch.epfl.lpd.net.PointToPointLink;
import ch.epfl.lpd.store.StoreMap;


public class App {

    /**
     * TODO: Add any other needed variables.
     */

    public static void main(String args[]) throws Exception {

        /**
         * Parse command line arguments.
         */
        List<NodeInfo> nodes = parseCmdNodesInfo(args);
        for (int i = 0; i < 3; i++)
            System.out.println("Node [" + i + "] info " + nodes.get(i).toString());

        int thisNodeIndex = Integer.parseInt(args[6]);
        if (thisNodeIndex > 2 || thisNodeIndex < 0)
            throw new Exception("Invalid node index (" + thisNodeIndex + "); must be >= 0 and <= 2!");
        System.out.println("This node has index: " + thisNodeIndex);

        String inputFilePath = args[7];
        System.out.println("Using input file: " + inputFilePath);

        String outputFilePath = thisNodeIndex + ".out";
        System.out.println("Using output file: " + outputFilePath);

        /**
         * Instantiate the backend, i.e., the local in-memory store.
         */
        StoreMap store = new StoreMap();

        /**
         * Instantiate point-to-point links to the other nodes.
         */
        List<PointToPointLink> ptpLinks = establishPTPLinks(nodes, thisNodeIndex);

        /**
         * TODO: Implement and instantiate the broadcast implementation on top
         * of PointToPointLinks.
         */

        /**
         * Now wait for the starting signal...
         *
         * This flag tells the App when it can start executing the trace, i.e.,
         * when SIGINT was received.
         */
        CountDownLatch startSignal = new CountDownLatch(1);
        registerSignalHandler(startSignal);

        System.out.println("Awaiting for SIGINT... please send the signal using: kill -INT PID");
        startSignal.await();
        System.out.println("Got SIGINT. Starting...");

        /**
         * TODO: Parse the input file.
         */

        /**
         * TODO: Coordinate with the other nodes; read/write to/from the local
         * store by executine the trace in the input file.
         */


        /**
         * TODO: Print to the output file.
         */
    }


    private static List<PointToPointLink> establishPTPLinks(List<NodeInfo> nodes, int index) throws Exception {
        List<PointToPointLink> links = new ArrayList<PointToPointLink>();

        int myPort = nodes.get(index).getPort();

        for (int i = 0; i < 3; i++) {
            if (i == index)
                continue;
            links.add(new PointToPointLink(nodes.get(i), myPort));
        }
        return links;
    }


    private static List<NodeInfo> parseCmdNodesInfo(String args[]) throws Exception {
        if (args.length < 8)
            throw new Exception("Insuffient command line arguments!");

        List<NodeInfo> nInfo = new ArrayList<NodeInfo>();
        for (int i = 0; i < 3; i++)
        {
            int pos = i*2;

            String ip = args[pos];
            int port = Integer.parseInt(args[pos + 1]);
            NodeInfo n = new NodeInfo(ip, port);

            nInfo.add(n);
        }
        return nInfo;
    }

    private static void registerSignalHandler(final CountDownLatch signalLatch)
    {
        Signal.handle(new Signal("INT"),
            new SignalHandler() {
                public void handle(Signal sig) {
                    // decrement the counter to signal that the App can start
                    signalLatch.countDown();
                }
            }
        );
    }
}
