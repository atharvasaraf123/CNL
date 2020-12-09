import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiUserChatTCP {

    private static final Set<String> names = new HashSet<>();

    private static final Set<PrintWriter> writers = new HashSet<>();

    private static final ArrayList<String> chatHistory = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("CHAT SERVER IS ONLINE");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(2720)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    private static class Handler implements Runnable {
        private String name;
        private final Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("Enter your name: ");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!name.isBlank() && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                        else {
                            out.println(name + " is already taken. Please try something else!");
                        }
                    }
                }
                writers.add(out);
                chatHistory.add(name + " joined");
                for (PrintWriter writer : writers) {
                    chatLoop(writer);
                }

                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    chatHistory.add(name + ": " + input);
                    for (PrintWriter writer : writers) {
                        chatLoop(writer);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    chatHistory.add(name + " has left");
                    for (PrintWriter writer : writers) {
                        chatLoop(writer);
                    }
                }
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }

        private void chatLoop(PrintWriter writer) {
            writer.println("\033[H\033[2J");
            writer.flush();

            writer.println("Enter /quit to exit the lobby");
            writer.println("Lobby Members: ");
            names.forEach(writer::println);
            writer.println("------------------------------");
            writer.println("CHAT");
            chatHistory.forEach(writer::println);
        }
    }
}