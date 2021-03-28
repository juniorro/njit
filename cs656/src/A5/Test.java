package A5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws IOException {
        String hostname = "stackoverflow.com";
        int port = 80;
        ServerSocket serverSocket  = new ServerSocket(3000);

        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            socket = serverSocket.accept();
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("GET / HTTP/1.1");
            writer.println("Host: " + hostname);
            writer.println("Accept: */*");
            writer.println("User-Agent: Java"); // Be honest.
            writer.println(""); // Important, else the server will expect that there's more into the request.
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.isEmpty()) break; // Stop when headers are completed. We're not interested in all the HTML.
                System.out.println(line);
            }
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException logOrIgnore) {
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) try {
                socket.close();
            } catch (IOException logOrIgnore) {
            }
        }
    }
}
