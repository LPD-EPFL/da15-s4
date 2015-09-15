package ch.epfl.lpd;


public class NodeInfo
{
    private String ip;
    private int port;


    public NodeInfo(String nodeIP, int nodePort) {
        this.ip = nodeIP;
        this.port = nodePort;
    }

    public int getPort() {
        return this.port;
    }

    public String getIP() {
        return this.ip;
    }

    public String toString() {
        return this.ip + ":" + this.port;
    }
}
