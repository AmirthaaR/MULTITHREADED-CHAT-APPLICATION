package task3;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(1234)) {
            System.out.println("Server started...");
            while (true) {
                Socket socket = server.accept();
                System.out.println("New client connected: " + socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        String msg;
        try {
            while ((msg = input.readLine()) != null) {
                System.out.println("Received: " + msg);
                for (ClientHandler client : Server.clients) {
                    if (client != this)
                        client.output.println(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close(); // Properly closing the socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
