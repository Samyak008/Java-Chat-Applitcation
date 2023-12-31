import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient2 {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner userInput = new Scanner(System.in)) {

            System.out.print("Enter your username: ");
            String username = userInput.nextLine();
            out.println(username);

            // Start a thread to handle messages from the server
            Thread serverListener = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        // Display messages from other clients (assuming they don't start with "ChatServer:")
                        if (!message.startsWith("ChatServer:")) {
                            System.out.println(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverListener.start();

            // Main thread to send messages to the server
            String message;
            while (true) {
                message = userInput.nextLine();
                out.println(username + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
