package A5;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class HttpServer extends Thread {

    static String docroot = ".";
    static int thePort = 5000;

    public static void main(String[] args) {
//        System.setProperty("java.net.useSystemProxies", "true");
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "5000");
//        System.setProperty("https.proxyPort", "5000");
        // Set up server socket
        ServerSocket serverSocket;
        try { serverSocket = new ServerSocket(thePort); }
        catch (IOException e) {
            System.err.println("Could not open socket on port " + thePort);
            return;
        }
        // Loop forever waiting for connections
        while (true) {
            try {
                // New connection, handle it with new thread
                System.out.println("Waiting for connection on port " + thePort);
                Socket socket = serverSocket.accept();
                System.out.println("Client connection from " + socket.getRemoteSocketAddress());
                HttpServer serv = new HttpServer(socket);
                serv.start();
            }
            catch (IOException e) {
                System.err.println("Cannot accept connection");
                return;
            }
        }
    }

    private Socket sock;
    public HttpServer(Socket s) {
        sock = s;
    }

    public void run() {
        try {
            PrintStream os = new PrintStream(sock.getOutputStream());
            BufferedReader is = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
            String get = is.readLine();
            StringTokenizer st = new StringTokenizer(get);
            String method = st.nextToken();
            if (!method.equals("GET")) {
                System.err.println("Only HTTP \"GET\" implemented");
                return;
            }
            // Loop through the rest of the input lines
            /* "Ranges" support: Look for header
               "Range: bytes=500-" (from byte 500 (incl) to end) or 
               "Range: bytes=0-123" for the first or "Range: bytes=-123" for the last 123 bytes
               Send a Content-Range in the reply. */
            while ((get = is.readLine()) != null) {
                if (get.trim().equals("")) break;
            }
            String file = st.nextToken();
            // Sanity checks, for security
            if (file.charAt(0) != '/') {
                System.err.println("Exiting: Request filename must start "
                                   + "with \"/\"");
                return;
            }
            if (file.indexOf("../") != -1) {
                System.err.println("Exiting: \"../\" in filename not allowed");
                return;
            }
            // Send file
            System.out.println(file);
            sendFile(os, file);
            sock.close();
        }
        catch (IOException e) {
            System.err.println("Cannot accept connection");
            return;
        }
    }
    
    /* Ranges support: 1) Always send header "Accept-Ranges: bytes",
       2) Always send header "Content-Length: 734", i.e. nr of bytes
       in response or partial response, 3) Always send header
       "Content-Range: bytes 500-1233/1234" => serving bytes from
       offsets 500 (incl) to 1233 (incl), file length is 1234. */
    void sendFile(PrintStream os, String file) {
        System.out.println("Request for file \"" + docroot + file + "\"");
        File theFile;
        try {
            theFile = new File(file.substring(1,file.length()));
            FileInputStream fis = new FileInputStream(theFile);
            // A better implementation would read+send e.g.4096 bytes at a time
            byte[] theData = new byte[(int) theFile.length()];
            fis.read(theData);
            fis.close();
            URLConnection connection = new URL("").openConnection();
            // Send response
            os.print("HTTP/1.1 200 OK\r\n");
            os.print("Date: " + new Date() + "\r\n");
            os.print("Server: WebtechHttpServer/1.0\r\n");
            os.print("Content-type: text/html\r\n");
            os.print("\r\n"); // End of headers
            // Send file data
            os.write(theData);
            os.close();
        } // end try
        catch (IOException e) { // Assuming that this is a "file not found"
            System.out.println(e);
            os.print("HTTP/1.1 404 Not Found\r\n");
            //os.print("Date: .....\r\n");
            os.print("Server: WebtechHttpServer/1.0\r\n");
            os.print("Content-type: text/html\r\n");
            os.print("\r\n"); // End of headers
            os.print("<h1>Not found</h1>\n");
            os.print("<p>File \"" + file+ "\" not found</p>\n");
            os.print("<p><em>WebtechHttpServer/1.0</em></p>\n");
        }
    }

}
