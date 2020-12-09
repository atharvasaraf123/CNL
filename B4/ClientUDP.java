import java.io.*;
import java.net.*;


class MessageSender implements Runnable {
    public final static int PORT = 7331;
    private final DatagramSocket sock;
    private final String hostname;
    MessageSender(DatagramSocket s, String h) {
        sock = s;
        hostname = h;
    }
    private void sendMessage(String s) throws Exception {
        byte[] buf = s.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
    }
    public void run() {
        boolean connected = false;
        do {
            try {
                sendMessage("GREETINGS");
                connected = true;
            } catch (Exception ignored) {

            }
        } while (!connected);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (!in.ready()) {
                    Thread.sleep(100);
                }
                sendMessage(in.readLine());
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
class MessageReceiver implements Runnable {
    DatagramSocket sock;
    byte[] buf;
    MessageReceiver(DatagramSocket s) {
        sock = s;
        buf = new byte[1024];
    }
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
public class ClientUDP {

    public static void main(String[] args) throws Exception {
        String host = null;
        if (args.length < 1) {
            System.out.println("Usage: java ClientUDP <server_hostname>");
            System.exit(0);
        } else {
            host = args[0];
        }
        DatagramSocket socket = new DatagramSocket();
        MessageReceiver r = new MessageReceiver(socket);
        MessageSender s = new MessageSender(socket, host);
        Thread rt = new Thread(r);
        Thread st = new Thread(s);
        rt.start(); st.start();
    }
}