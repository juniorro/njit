package A5;/*
* CS 656 / Spring 21 / A5.Apache Mediator / Stub V3.00
* Group: C1 / Leader: Junior (rt28)
* Group Members: Nancy (), Susmita (), Prasuna ()
*
*   ADC - add your code here
*   NOC - do not change this
*   ??  - you may change these vars/parms etc
*   Your own methods must be after run() ONLY!
*/

import java.net.InetAddress;
// other imports go here
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;
//import java.util.Date;
import java.net.Inet4Address;
import java.io.DataOutputStream;
import java.io.InputStream;
/*--------------- end imports --------------*/

class Apache {
  // NOC these 3 fields
  private byte [] HOST ;      /* should be initialized to 1024 bytes in the constructor */
  private int PORT;      /* port this A5.Apache should listen on, from cmdline        */
  private InetAddress PREFERRED; /* must set this in dns() */
  // ADC additional fields here
  private String FILE;      /* name of the file in URL, if you like */
  private int startpos=0,endpos=0;
  byte[] buffer;
  String hostDestination;
  String preferredIP;
public static void main(String [] a) { // NOC - do not change main()
  Apache apache = new Apache(Integer.parseInt(a[0]));
  apache.run(2);
}

Apache(int port) {
  PORT = port;
  HOST = new byte[1024];
  // other init stuff ADC here
}

// Note: parse() must set HOST correctly
int parse(byte [] buffer) { // throws Exception {
        startpos=0; endpos=0;
        for(int x=0;x<buffer.length&& x<65527;x++)
        {
                if(buffer[x+3]==32 && buffer[x+4]==104 && buffer[x+5]==116 && buffer[x+6]==116 && buffer[x+7]==112)
                {
                        startpos=(buffer[x+8]==115)?12:11                       ;
                        x=startpos;
                        //System.out.println("entered"+x);
                }
                else if(startpos==0&&buffer[x+4]==32 && buffer[x+5]==104 && buffer[x+6]==116 && buffer[x+7]==116 && buffer[x+8]==112)
                {
                        startpos=(buffer[x+9]==115)?13:12;
                        x=startpos;
                }
                else if(startpos>0 && buffer[x]==47)
                {

                endpos=x-1;
                break;
                }
                else if((buffer[x]==10 || buffer[x]== 58) && endpos==0)
                {
                        if (startpos>0)
                        {
                        endpos=x-1;
                        break;
                        }
                        if (startpos==0 && buffer[x+1]==72 && buffer[x+2]==111 && buffer[x+3]==115&& buffer[x+4]==116 && buffer[x+5]==58)
                        {
                                if(buffer[x+6]==32)
                                        startpos=x+7;
                                else
                                startpos=x+6;

                          x=x+6;
                        }

                        }

        }
        return 0;
}
int dns(int X)  // NOC - do not change this signature; X is whatever you want
{
          InetAddress[] inet;
                int count=0; //to count the loop
                long currentLatency = 0; //to set the latency for the connectivity
    //(IP whose latency is minimum will be stored in this variable for reference)

                try {
      //Getting only IPV4
     // System.setProperty("java.net.preferIPv4Stack" , "true");
      // TO DO host name should be updated
      //get all ipaddresses of host
                        inet = InetAddress.getAllByName(byte2str(buffer,startpos,endpos+1));//InetAddress.getAllByName(hostName);
                        for(InetAddress inetAddr : inet) {
                        if(inetAddr !=null && inetAddr instanceof Inet4Address){

                                count++;

                                //String ipAddr= inetAddr.getHostAddress();
        /*Establishing a connection to the IP and check if it
        the socket is able to connect within the defined timeout*/
                                try (Socket socket = new Socket()) {
                                        long start = System.currentTimeMillis(); //noting down the start time of socket connection
                                        //System.out.println("IP :"+inetAddr);
                                        socket.connect(new InetSocketAddress(inetAddr,80), 1000);
                                        long timeDiff = System.currentTimeMillis()-start;               //diff between start and end as response time for comparison
         //for first iteration set latency and preferred IP
                                        if(count==1) {
                                                currentLatency=timeDiff;
            PREFERRED=inetAddr;
                                        }
         //considered for second iteration if there is more than 1
         //if the latency calculated is less than current one then override the preferred ip and latency
                                        if(timeDiff <= currentLatency) {
                                                currentLatency=timeDiff;
            PREFERRED=inetAddr;
                                        }
                                        socket.close();

                                } catch(IOException ex) {
          PREFERRED=null;               //set null when the request times out or host is not reachable
 System.out.println(ex.getMessage());
                                }
                                }
      }

                } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
      PREFERRED=null;           //set null when the request times out or host is not reachable
System.out.println(e.getMessage());
                }

  return 0;

}

int http_fetch(Socket client) // NOC - don't change signature
{
  Socket p; // peer, connection to HOST

  // get file from peer (HOST) and send back to c
  //
  // return bytes transferred
  return 0;
}

int  ftp_fetch(Socket client) // NOC - don't change signature
{
  Socket p; // peer, connection to HOST

  // do FTP transaction with peer, get file, send back to c
  // Note: do not 'store' the file locally; it must be sent
  // back as it arrives
  //
  // return bytes transferred
  return 0;
}

int  echo_req(Socket client) // NOC - don't change signature
{
   // used in Part 1 only; echo the HTTP req with added info
   // from PREFERRED
	 try {
         //client.getOutputStream().write(buffer);
 	String statusLine = "HTTP/1.1 200 OK" + "\r\n\r\n";
 	  
         DataOutputStream output = new DataOutputStream(client.getOutputStream());
        output.writeBytes(statusLine);
                        
         output.writeBytes("<html><body>");
                          for(int i=0;i<buffer.length;i++)
                         {
                         	output.write(buffer[i]);
                         	if(buffer[i]==10)
                         			{
                         		output.writeBytes("<BR>");
                         			}
                         	
                         }
                         
                         output.writeBytes("DNS LOOKUP: "+hostDestination+"<BR>");
                         output.writeBytes("<font color=\"red\" >Preferred IP:"+preferredIP+"</font>");
                         
                         output.writeBytes("</body></html>");
                         output.close();
 } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
 }
 return 0;
}

int run(int X) { // NOC - do not change the signature for run()
  ServerSocket serverSocket = null; // NOC - this is the listening socket
  Socket socket = null; // NOC - this is the accept-ed socket i.e. client
  buffer = new byte[65535];  // ADC - general purpose buffer
  int counter = 0;
  int res;
  long startTime = 0L;//to be removed after testing
  InputStream input;
  try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(PORT), 20);
      //System.out.println("Process Start "+ new Date());
      System.out.println("A5.Apache Listening on socket " + PORT);
     // ADC here
      while(true) { // NOC - main loop, do not change this!
      // ADC from here to LOOPEND : add or change code

      socket = serverSocket.accept();
      startTime = System.currentTimeMillis();
      counter++;
      //PrintStream printStream = new PrintStream(socket.getOutputStream());
      //socket.InputStream??.read(b0 ) ?? ; // example: req is "GET http://site.com/dir1/dir2/file2.html"
      //parse(b0 ?? or some other buffer ?? ); // set HOST as 's' 'i' 't' 'e' '.' 'c' 'o' 'm'
      //dns(0);  // sets PREFERRED
      //echo_req( s1 );  // used in Part 1 only
      /* Part 2 - hints
      is it http_fetch or ftp_fetch ??
      nbytes = http_fetch(s1) or ftp_fetch(s1);
      LOG "REQ http://site.com/dir1/dir2/file2.html transferred nbytes"
      */
      input =socket.getInputStream();
      if ((input.read(buffer, 0, buffer.length)) != -1) {
              // LOGIC HERE - todo
            res =parse(buffer);
              hostDestination = byte2str(buffer,startpos,endpos+1);
              dns(0);
              preferredIP = PREFERRED != null ? PREFERRED.getHostAddress() : "ERROR";
             echo_req( socket );
              System.out.println("(" + counter + ")" + " Incoming connection from [" +
                                               socket.getInetAddress().getHostAddress() + ":"+socket.getPort()+"] to me [" + serverSocket.getInetAddress().getLocalHost().getHostAddress() + ":"+PORT+"]");
              System.out.println("REQ: "+hostDestination+" / "+ " RESP:"+preferredIP);
              System.out.println("Time taking per client request processing = "+(System.currentTimeMillis() - startTime)+" msec");
              
             // System.out.println("Process End "+ new Date());
              /*printStream.print("GET /index.html HTTP/1.1 \n");
              printStream.print("Host:   todo \r\n");
              printStream.print("User-Agent: user-agent \n");
              printStream.print("Accept: todo \r\n");
              printStream.print("Accept-Language: todo \n");*/
             // printStream.print(byte2str(buffer,0,buffer.length));
              //printStream.close();
         }
      //break;
      // LOOPEND
      } // NOC - main loop
   } catch(IOException exception) {
         System.out.println("An error occurred: " + exception.getMessage());
         }

  return 0; //TODO
}

/* ------------- your own methods below this line ONLY ----- */
String byte2str(byte []b, int i, int j) {
        String output="";
        char[] req=new char[j-i];

        for(int k=i,l=0;k<j&&l<req.length;k++,l++)
        {
                req[l]=(char)b[k];
                //HOST[l]=b[k];
        }
                output= new String(req);


        return output;

}

} // class A5.Apache

