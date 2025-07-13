package task3;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            Thread readThread = new Thread(() -> {
                String msg;
                try {
                    while ((msg = input.readLine()) != null) {
                        System.out.println("Message: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            readThread.start();

            String msg;
            while ((msg = keyboard.readLine()) != null) {
                output.println(msg);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
