import java.io.*;
import java.net.*;
import java.util.*;

public class ServerUDP extends Thread {
    public final static int PORT = 7331;
    private final static int BUFFER = 1024;

    private final DatagramSocket socket;
    private final ArrayList<InetAddress> clientAddresses;
    private final ArrayList<Integer> clientPorts;
    private final HashSet<String> existingClients;
    public ServerUDP() throws IOException {
        socket = new DatagramSocket(PORT);
        clientAddresses = new ArrayList<>();
        clientPorts = new ArrayList<>();
        existingClients = new HashSet<>();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte)0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String content = new String(buf, buf.length);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                if (!existingClients.contains(id)) {
                    existingClients.add( id );
                    clientPorts.add( clientPort );
                    clientAddresses.add(clientAddress);
                }

                System.out.println(id + " : " + content);
                byte[] data = (id + " : " +  content).getBytes();
                for (int i=0; i < clientAddresses.size(); i++) {
                    InetAddress cl = clientAddresses.get(i);
                    int cp = clientPorts.get(i);
                    packet = new DatagramPacket(data, data.length, cl, cp);
                    socket.send(packet);
                }
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ServerUDP s = new ServerUDP();
        s.start();
    }
}