package ch.epfl.lpd.net;


import java.io.*;
import java.net.*;


public class PointToPointLink
{
    private DatagramSocket socket;
    private InetAddress receiverIP;
    private int receiverPort;


    public PointToPointLink(String ip, int port) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket(port);
        receiverIP = InetAddress.getByName(ip);
        receiverPort = port;
    }

    public void sendOnce(String message) throws Exception {
        byte[] b = message.getBytes();
        DatagramPacket packet = new DatagramPacket(b, b.length, receiverIP, receiverPort);
        socket.send(packet);
    }

    public String receiveOnce() throws Exception {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String message = new String(receivePacket.getData());
        return message;
    }

    public void shutdownLink() {
        System.out.println("Closing link");
        socket.close();
    }
}
