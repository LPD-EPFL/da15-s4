package ch.epfl.lpd.net;


import java.io.*;
import java.net.*;

import ch.epfl.lpd.NodeInfo;


public class PointToPointLink
{
    /** the receive socket is a singleton (unique) */
    private static DatagramSocket receiveSocket = null;
    private DatagramSocket sendSocket;
    private InetAddress receiverIP;
    private int receiverPort;

    public PointToPointLink(String ip, int port, int myReceivePort) throws Exception {
        System.out.println("Establishing point-to-point link to " + ip + ":" + port);
        sendSocket = new DatagramSocket();
        setReceiveSocketInstance(myReceivePort);
        receiverIP = InetAddress.getByName(ip);
        receiverPort = port;
    }

    public PointToPointLink(NodeInfo nInfo, int myReceivePort) throws Exception {
        System.out.println("Establishing point-to-point link to " + nInfo.toString());
        sendSocket = new DatagramSocket();
        setReceiveSocketInstance(myReceivePort);
        receiverIP = InetAddress.getByName(nInfo.getIP());
        receiverPort = nInfo.getPort();
    }

    private static void setReceiveSocketInstance(int port) throws Exception {
        if (null == receiveSocket ) {
            receiveSocket = new DatagramSocket(port);
            System.out.println("Listening on port: " + port);
        }
    }

    public void sendOnce(String message) throws Exception {
        byte[] b = message.getBytes();
        DatagramPacket packet = new DatagramPacket(b, b.length, receiverIP, receiverPort);
        sendSocket.send(packet);
    }

    public String receiveOnce() throws Exception {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        receiveSocket.receive(receivePacket);
        String message = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        return message;
    }

    public void shutdownLink() {
        System.out.println("Closing link");
        receiveSocket.close();
        sendSocket.close();
    }

    public String toString() {
        return receiverIP.toString() + ":" + Integer.toString(receiverPort);
    }
}
