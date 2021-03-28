package A3;
/*
 * CS 656 Recv V2.0
 * Indicate your Group below
 * Group ZZ: Andy (aa123), Bella (bb234), Chung (cy123), Devika (dv12)
 *
 * NOTES:
 1. Do not change any method signatures
 1a. You may add your own private fields or methods
 2. "NOC" means do not change this line
    "ADC" - suggestion: add your code here
 3. Wrap long lines - 70 characters max!
    Unwrapped or uncommented code may be penalized.
 4. Must use Packet.write() to write out the packet
    to the file
*/
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/* include any additional imports below this line */

/* end imports */

public class Recv {
  private Socket socket = null;
  private InputStream inputStream = null;
  private List<Packet> packetList = null;  // not used; NOC
  private ServerSocket serverSocket = null;

    public Recv(String host, int port) throws IOException {
      // ADC
        serverSocket = new ServerSocket(port);
        System.out.println("Listening for socket connection on port: " + port);
        socket = serverSocket.accept();
        
      System.out.println("Connected to " +
         socket.getInetAddress().getHostAddress() + " on port " + port);
    }

    public void close() throws IOException {
      // ADC
    }

    // get_pkts: ADC - you may modify this method, but not signature
    public int get_pkts(String fname) throws IOException {
      byte[] buff = new byte[1506];  // buffer to get pkts off wire
      int    npkt = 0;               // no of pkts recd
      int bytesRead = 0;
        int bytesRecorded = 0;
      File file = new File("/home/juniorro/Documents/github/cs656/src/data.txt");
      FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        // loop, get pkts, write to file fname
        while ((bytesRead = socket.getInputStream().read(buff, 0, buff.length)) != -1) {
            System.out.println(bytesRead);
            Packet packet = new Packet(buff);
            packet.write(fileOutputStream);
            npkt++;
            bytesRecorded += bytesRead;
        }
      // out of loop; print results
      System.out.println(
        "Total" + npkt + " packets / " + bytesRecorded + " bytes recd. " +
        "Total delay = 17920 ms, average = 1280 ms");

      return npkt;
    }

    public static void main(String[] args) throws IOException {
        String hostname = args[0], fileName = args[2];
        if(args.length != 3) {
            System.out.println("Usage: host  port filename");
            return;
        }
        int port = Integer.parseInt(args[1]);
        Recv recv = new Recv(hostname, port);

        // ADC: your code here, 4 lines max
        recv.get_pkts(fileName);
    }
}

class Packet {
    private byte[] buff;  // NOC
    private int seqNo;    // NOC
    private short len;    // NOC

  // Ctor1: make Packet out of raw data "buff" which came off wire
  // ADC to the method but don't change the signature
    public Packet(byte[] buff) {
        this.buff = buff;
        this.seqNo = 0;
        this.len = ToShort(buff);

    }

    private short ToShort(byte[] b) {
      // ADC: can't use bit-shift operators; math only
        return 1;
    }

    private int ToInt(byte[] b) {
      // ADC: can't use bit-shift operators; math only
        return 1;
    }

    private byte[] ShortToByte(short value) {
      // your code here: must use this to convert short to byte[]
        return new byte[12];
    }

    private byte[] IntToByte(int value) {
      // your code here: must use this to convert int to byte[]
        return new byte[12];
    }

    public int getSeqNo() { return seqNo; } // NOC

    public short getLen() { return len; }   // NOC

  // write packet payload to the end of file "fp"
    public void write(FileOutputStream fp) throws IOException {
        System.out.println("Writing to file...");
        fp.write(this.buff);
        fp.flush();
      // ADC

    }

    // your methods etc here
}
