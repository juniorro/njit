package A3;

import java.io.*;
import java.net.*;

public class Sender {
    public static void main(String[] args) throws IOException {
        try (Socket echoSocket = new Socket("127.0.0.1", 3000);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            /*BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))*/
        ) {
            String userInput = "GET https://www.berkeley.edu/";
            //while ((userInput  != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            //}
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to ");
            System.exit(1);
        } 
    }
}