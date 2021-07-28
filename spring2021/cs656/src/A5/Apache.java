/*
* CS 656 / Spring 21 / Apache Mediator / Stub V3.00
* Group: C1 / Leader: Junior (rt28)
* Group Members: Nancy (), Susmita (), Prasuna ()
*
*   ADC - add your code here
*   NOC - do not change this
*   ??  - you may change these vars/parms etc
*   Your own methods must be after run() ONLY!
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.util.Date;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
// other imports go here
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/*--------------- end imports --------------*/

class Apache {
  // NOC these 3 fields
  private byte [] HOST ;      /* should be initialized to 1024 bytes in the constructor */
  private int PORT;      /* port this Apache should listen on, from cmdline        */
  private InetAddress PREFERRED; /* must set this in dns() */
  // ADC additional fields here
  private String FILE;      /* name of the file in URL, if you like */
  private int startpos=0,endpos=0,reqtype=0;
  byte[] buffer;
  byte[] URIPATH;
  byte[] REQURL;
  int StatusCode=0 ;
  byte[] outputbuffer;// to send the output to client
  byte[] requestBuffer;//to extract the request from buffer array
  int bytesReceived ;  
  int bytesRead;
  int  pos1, end1;//=0;
  byte[] passvHost = new byte[40];
  byte[] ipaddress,port1,port2;
  int pasvPort=0;
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
        startpos=0; endpos=0;reqtype=0;
        pos1=0; end1=0;
        int httpabsuri=0;
         if ( buffer.length>4 &&buffer[0]==71 && buffer[1]==69 && buffer[2]==84 && buffer[3]==32) {
    	 
          for(int x=4;x<buffer.length;x++) {
        	// get host name if uri is absolute uri 
		        if(httpabsuri==0 && buffer.length>10&&((buffer[4]==104 && buffer[5]==116 && buffer[6]==116 && buffer[7]==112 && buffer[8]==58 && buffer[9]==47 
		        		&& buffer[10]==47)||(buffer[4]==72 && buffer[5]==84 && buffer[6]==84 && buffer[7]==80 && buffer[8]==58 && buffer[9]==47 
		        		&& buffer[10]==47) ))
			        {
			          startpos=11;
			          x=11;
			          httpabsuri=1;
			          
			        }
		        if(startpos>0&& endpos>0 &&pos1>0 && end1>0) {
			    break;	
			    }
		        if(buffer[4]==47 && pos1==0) {
		        	 pos1=4;
		        }
		        if (x>11 && httpabsuri==1 &&buffer[x]==58 && startpos>0 && endpos==0) { //uri is with port
		        	endpos=x-1;
		        }
		        if(x>10 && buffer[x]==47 && buffer[x+1]!=47 && buffer[x-1]!=47 && buffer[x-1]!=32 &&  pos1==0) {	
		        	if(startpos>0 && endpos==0){
		        		endpos=x-1;
		        	}		        	
		        		pos1=x;		        				        	
		        }
		         
        	//get uri path positions       	  
		      if ( pos1>0 && buffer[x]==32 ) {
		    	// to handle  HTTP/1.1
		    	  if (x+8<buffer.length && buffer[x+1]==72 && buffer[x+2]==84 && buffer[x+3]==84 && buffer[x+4]==80 && buffer[x+5]==47 && buffer[x+6]==49 && buffer[x+7]==46&&  buffer[x+8]==49) {	  			    	 
			    	  end1=x-1;
			    	  x=x+8;
		    	  }
		    	  if(end1==0 && buffer[x+1]==13 && buffer[x+2]==10 ) {
		    		  return 0;
		    	  }		    	  
		      }
		     
		   // get host value start pos from header
			    if (httpabsuri==0) {   	           
	        	 if(x+8<buffer.length && startpos==0 && buffer[x]==13&& buffer[x+1]==10&& buffer[x+2]==72 && buffer[x+3]==111 && buffer[x+4]==115&& buffer[x+5]==116 && buffer[x+6]==58 &&buffer[x+7]==32) {
	        		  startpos=x+8;
	        		  x=x+8;
	        	 }
	        	   // get host value end pos
	        	 else if((buffer[x]==13 || buffer[x]== 58) && endpos==0) {
	        		 if (startpos>0) {
	        			 endpos=x-1;
	        			 break;
	        		 }	                       
	               }
			    }			    			  
          }//end loop        
                  
    	}
     //get request url
      if (pos1!=4 && end1>pos1) {
    	  REQURL= new byte[end1-4+1];
    	  for(int q1=0,y=4;q1<REQURL.length && y<=end1;q1++,y++) {
    		REQURL[q1]=buffer[y];
    	  }
      }
      if(pos1==4 && end1>=pos1) {
    	  int z,k;
    	  
    	  REQURL= new byte[endpos-startpos+end1-pos1+1+7+1];
    	  REQURL[0]=104;
    	  REQURL[1]=116;
    	  REQURL[2]=116;
    	  REQURL[3]=112;
    	  REQURL[4]=58;
    	  REQURL[5]=47;
    	  REQURL[6]=47;
    	 for (z=7,k=startpos; z<REQURL.length&& k<=endpos; z++,k++) {
    		REQURL[z]=buffer[k];
    	 }
    	 
    	 for(k=pos1;z<REQURL.length&&k<buffer.length && k<=end1 ;k++,z++ ) {
    		 REQURL[z]=buffer[k];
    	 }
      }
    	 
    	  
    	  // if request is valid and uri has value
        if(pos1>0&&end1>0) {        		
        	URIPATH= new byte[end1-pos1+1];
        	for (int q=0,x=pos1;q<URIPATH.length && q<buffer.length && x<=end1;q++,x++) {
        		URIPATH[q]=buffer[x];
        	}        	        	
        }
        
        if(startpos>0&&endpos>0){
        	HOST= new byte[endpos-startpos+1];
        	for (int p=0;p<HOST.length && startpos<=endpos;p++,startpos++) {
        		HOST[p]=buffer[startpos];
        	}        	
        }
        
        if(startpos==0 || endpos==0 || pos1==0 || end1==0) {
               return 0;
		}
		if(startpos!=0 && endpos!=0 && pos1!=0 && end1!=0) {
			if((REQURL[0]==104 && REQURL[1]==116 && REQURL[2]==116 && REQURL[3]==112 && REQURL[4]==58 && REQURL[5]==47 
	        		&& REQURL[6]==47)||(REQURL[0]==72 && REQURL[1]==84 && REQURL[2]==84 && REQURL[3]==80 && REQURL[4]==58 && REQURL[5]==47 
	        		&& REQURL[6]==47)) {
						System.out.println("http");
						return 1;
					}
			if((REQURL[0]==102 && REQURL[1]==116 &&  REQURL[2]==112 && REQURL[3]==58 && REQURL[4]==47 
	        		&& REQURL[5]==47)||(REQURL[0]==70 && REQURL[1]==84  && REQURL[2]==80 && REQURL[3]==58 && REQURL[4]==47&& REQURL[5]==47)) {
				System.out.println("ftp");
				return 2;
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
          		// TO DO host name should be updated
          		//get all ipaddresses of host
                  inet = InetAddress.getAllByName(byte2str(HOST,0,HOST.length));//InetAddress.getAllByName(hostName);
                  for(InetAddress inetAddr : inet) {
                  if(inetAddr !=null && inetAddr instanceof Inet4Address){                        	
                          count++;
                          /*Establishing a connection to the IP and check if it
  						the socket is able to connect within the defined timeout*/
                          try (Socket socket = new Socket()) {
                                  long start = System.currentTimeMillis(); //noting down the start time of socket connection
                                 
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
                                  
                                  StatusCode = 200;
                                  socket.close();

                          } catch(IOException ex) {
                          	PREFERRED=null;               //set null when the request times out or host is not reachable
                          	StatusCode = 503; //service unavailable
                          }
                  	}
                  }

          } catch (UnknownHostException e) {
                  // TODO Auto-generated catch block
          	StatusCode = 400; //bad request
          	PREFERRED=null;  //set null when the request times out or host is not reachable
          }
          
          return StatusCode;

}

int http_fetch(Socket client) // NOC - don't change signature
{
  Socket p; // peer, connection to HOST 
  p = new Socket();
  bytesRead =0;
  outputbuffer = new byte[1500]; //initialize output buffer 
  try {
	PREFERRED = InetAddress.getAllByName(byte2str(HOST,0,HOST.length))[0];
	 //connect to the preferred IP of the requested server
	p.connect(new InetSocketAddress(PREFERRED,80),300);
	//get the input and output stream
	OutputStream os = p.getOutputStream();
	InputStream is = p.getInputStream();
	OutputStream os_client = client.getOutputStream();
	//write to request from the client to the socket	
	os.write(requestBuffer);
	//read the response from the server through socket
	while((bytesReceived=is.read(outputbuffer, 0, outputbuffer.length)) != -1){
		//call echo_req for the respective client
		bytesRead += bytesReceived;
		os_client.write(outputbuffer,0,bytesReceived);
	}
	os.close();
	is.close();
	p.close();
	
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	return -1;
} catch(Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	return -1;
}
  // return bytes transferred
 //System.out.println("Bytes transferred = "+bytesRead);
  return bytesRead;
}

int  ftp_fetch(Socket client) // NOC - don't change signature
{		
	Socket p = new Socket(); // peer, connection to HOST
	Socket pasv = new Socket();

	byte[] USER= {85,83,69,82,32,97,110,111,110,121,109,111,117,115,13,10};
	byte[] PASS= {80,65,83,83,32,116,101,115,116,64,103,109,97,105,108,46,99,111,109,13,10};
	byte[] PASV= {80,65,83,86,13,10};
	byte[] RETR= {82,69,84,82};  
	byte[] QUIT= {81,85,73,84,13,10};	
	byte[] BTYPE= {84,89,80,69,32,73,13,10};
	byte[] status200 = {50,48,48};
	byte[] status220 = {50,50,48};
	byte[] status331 = {51,51,49};
	byte[] status230 = {50,51,48};	
	byte[] status226 = {50,50,54};
	byte[] status227 = {50,50,55};	
	int servererr=0;
  // do FTP transaction with peer, get file, send back to c
  // Note: do not 'store' the file locally; it must be sent
  // back as it arrives
  //
    InputStream input = null,passiveInput = null;
		try {	
			 PREFERRED = InetAddress.getAllByName(byte2str(HOST,0,HOST.length))[0];
			 p.connect(new InetSocketAddress(PREFERRED,21),300); // connect on to ftp host port 21
			 input = p.getInputStream();
			 
			 byte[] ftpReply = ReadFtpOutput(p);
			 
			 if(ftpReply!=null) {
			 byte[] ftpStatusCode = GetStatusCode(ftpReply);
			 			 
			 if(IsValidReply(ftpStatusCode,status220)){ 
				 
				 System.out.println("Sending user id - anonymous");
				 ftpReply = WritetoFtp(p,USER); //Sending user id - anonymous
				 
				 System.out.println("Ftp reply "+ftpReply);
				 
				 if(ftpReply!=null && IsValidReply(GetStatusCode(ftpReply),status331)) {
					 
					 System.out.println("Sending password");
					 ftpReply = WritetoFtp(p,PASS); //Sending password
			
            
					 if(ftpReply!=null && IsValidReply(GetStatusCode(ftpReply),status230)) {
                                 
						 System.out.println("Ftp reply for 230 "+byte2str(ftpReply,0,ftpReply.length));
						 ftpReply = null;

						 System.out.println("Setting non-ascii mode transfer..");						 
						 ftpReply = WritetoFtp(p,BTYPE); //Set non ascii type
			 		   
                                                                    
						// if(ftpReply!=null && IsValidReply(GetStatusCode(ftpReply),status200)) {
							 System.out.println("Entering passive ftp mode"); //set passive ftp mode
							 ftpReply = null;
							 ftpReply = WritetoFtp(p,PASV);
							 System.out.println("Ftp reply "+ftpReply);							 
						     
							 if(ftpReply!=null && IsValidReply(GetStatusCode(ftpReply),status227)) {
								 
								 ParsePassiveModeInputs(ftpReply);
								 System.out.println("connecting to host "+passvHost+" on port "+pasvPort);
								 pasv.connect(new InetSocketAddress(byte2str(ipaddress,0,ipaddress.length), pasvPort), 5000); //connect to host sent by ftp server
								 								 
								 passiveInput = pasv.getInputStream();																 								 
								 
								 System.out.println("Retrieving file..");								 							
								 int bytesRead = RetrieveFile(pasv,RetriveFtpTransferInput(),passiveInput,client); 
								 ftpReply = WritetoFtp(pasv,RetriveFtpTransferInput());
								 System.out.println("Ftp reply "+ftpReply);								 
							 
								 input.close();
								 client.close();
								 passiveInput.close();
								 p.close();	 
								 
								 return bytesRead;
								 /*if(ftpReply!=null && GetStatusCode(ftpReply).equals("226")) {
									 //set http status code 200
									 System.out.println("File transfer completed");
									 ftpReply = WritetoFtp(p,QUIT,passiveInput);
									 
									 if(ftpReply!=null && GetStatusCode(ftpReply).equals("221")) {
										 //set http status code 200
										 System.out.println("File transfer completed");												 
									 }
								 }*/								 
							 }
						 }						 
					 //}
				 }				 
			 }			 
		  }
			 WriteHttpResponse(client, 500);
			 input.close();
			 client.close();
			 //passiveInput.close();
			 p.close();	 
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block				
			WriteHttpResponse(client, 500);
			e.printStackTrace();
			
			return 0;
      
		} catch(Exception e) {
	    // TODO Auto-generated catch block			
			WriteHttpResponse(client, 500);
			e.printStackTrace();
			return 0;
			
		} 		
  // return bytes transferred TO DO
  
  return 0;
}



int  echo_req(Socket client) // NOC - don't change signature
{
   // used in Part 1 only; echo the HTTP req with added info
   // from PREFERRED	
	 try {        		
		 byte[] status400="HTTP/1.1 400 Bad Request\n\n".getBytes();
		 byte[] status200="HTTP/1.1 200 OK\n\n".getBytes();
		 byte[] status301="HTTP/1.1 301 Moved Permanently\n\n".getBytes();
		 byte[] status404="HTTP/1.1 404 Not Found\n\n".getBytes();
		 byte[] status408="HTTP/1.1 408 Request timed out\n\n".getBytes();
		 byte[] status414="HTTP/1.1 414 Request-url Too Long\n\n".getBytes();
		 byte[] status500="HTTP/1.1 500 Internal Server Error\n\n".getBytes();
		 byte[] status503="HTTP/1.1 503 Service Unavailable\n\n".getBytes();
		 
		 OutputStream output = client.getOutputStream();
		 if(StatusCode==200)
			 output.write(status200);
		 else if(StatusCode==503)
		 	output.write(status503);
		 else if(StatusCode==400)
			 	output.write(status400);
		 else
			 output.write(status408);
		 		     
		 output.write("<html><body>".getBytes());
         	for(int i=0;i<buffer.length;i++)
         	{
            	output.write(buffer[i]);
             	if(buffer[i]==10)
             	{             		
             		output.write("<br>".getBytes());
             	}
		
             }
             output.write("DNS LOOKUP:".getBytes() );
             output.write(HOST);output.write("<br>".getBytes());
             output.write("<font color=\"red\">".getBytes());
             output.write("Preferred IP: ".getBytes() );
             output.write( (PREFERRED != null ? PREFERRED.getHostAddress() : "ERROR").getBytes());
             output.write("</font>".getBytes() );
             output.write("</body></html>".getBytes()); 
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
  int requestLength = 0;
  InputStream input;
  try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(PORT), 20);      
      System.out.println("Apache Listening on socket " + PORT);
     // ADC here
      while(true) { // NOC - main loop, do not change this!
      // ADC from here to LOOPEND : add or change code

      socket = serverSocket.accept();
      startTime = System.currentTimeMillis();
      counter++;
      /* Part 2 - hints
      is it http_fetch or ftp_fetch ??
      nbytes = http_fetch(s1) or ftp_fetch(s1);
      LOG "REQ http://site.com/dir1/dir2/file2.html transferred nbytes"
      */
      input =socket.getInputStream();
              // LOGIC HERE - todo
		      int i=0,data=0;
		      data=input.read();
		      while (data!= -1) {
		    	  if(i>=65535) {				
		    		  input.close();
				      socket.close();
				      System.exit(1);
		    	  }  
		    	  
		      buffer[i]=(byte)data;
			  			  
			  if(i>=3&&buffer[i-3]==13 &&buffer[i-2]==10&& buffer[i-1]==13 &&buffer[i]==10 ) {
				  //set the requested byte array length till CRLF from the buffer array
				  requestLength = i+1;
				  break;
			  }
			  
			  data=input.read();			  
			  i++;
				
		      }
              res =parse(buffer);
              if(res!=0) {
            	  System.out.println("Host ="+byte2str(HOST,0,HOST.length));
            	  System.out.println("URI PATH = "+byte2str(URIPATH,0,URIPATH.length));
            	  System.out.println("REQ URL PATH = "+byte2str(REQURL,0,REQURL.length));
              //check if the request is HTTP
            	  if(res == 1) {
            		  //initialize the requested buffer and fill the data from buffer array if length is less than 65535
            		  requestBuffer = new byte[requestLength];
            		  if(requestLength < buffer.length) {
            			  for(int j = 0; j < requestLength; j++) {
            				  requestBuffer[j]= buffer[j];
            			  }
            		  }else {
            			  requestBuffer = buffer;
            		  }
                  
                  http_fetch(socket);
              } else {
            	  ftp_fetch(socket);
              }
              
	     
              System.out.println("(" + counter + ")" + " Incoming connection from [" +
              socket.getInetAddress().getHostAddress() + ":"+socket.getPort()+"] to me [" + serverSocket.getInetAddress().getLocalHost().getHostAddress() + ":"+PORT+"]");
            //  System.out.println("REQ: "+byte2str(HOST,0,HOST.length)+" / "+ " RESP:"+(PREFERRED != null ? PREFERRED.getHostAddress() : "ERROR"));
              System.out.println("REQ: "+byte2str(REQURL,0,REQURL.length)+" ( "+ bytesRead+" bytes transferred)");
              }
              if(res==0)
              {            	  
            	  WriteHttpResponse(socket, 400);            
              }
        input.close();
        socket.close();
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
        }
                output= new String(req);
                
        return output;
  }

	byte[] WritetoFtp(Socket ftpSocket,byte[] cmd) throws IOException {
		 OutputStream ftpOutput = ftpSocket.getOutputStream();	

		 ftpOutput.write(cmd);
		 return ReadFtpOutput(ftpSocket);      
	}

	byte[] ReadFtpOutput(Socket ftpSocket) throws IOException { 
		byte[] fbuffer = new byte[3];
		
		int byteread=0;
		byte[] status230 = {50,51,48};
		InputStream fInput = ftpSocket.getInputStream();
		fInput.read(fbuffer, 0, fbuffer.length);
		if(fbuffer ==  status230) {
			return fbuffer;
		}
//		while((byteread=fInput.read(fbuffer, 0, fbuffer.length)) != -1)  {
//			  break;
//      System.out.println("FTP response "+ byte2str(fbuffer,0,fbuffer.length));
//			}
			 
			 return fbuffer;					 				
	}

	byte[] GetStatusCode(byte[] ftpReply) {
		// TODO Auto-generated method stub
		byte[] status = new byte[3];
		for(int i=0;i<status.length;i++) {
			status[i]=ftpReply[i];
		}
		return status;
	}

	byte[] RetriveFtpTransferInput() {
		byte[] RETR= new byte[end1-pos1+1+7];
		int r,v;
		RETR[0]=82;
		RETR[1]=69;
		RETR[2]=84;
		RETR[3]=82;
		RETR[4]=32;
		for(r=5,v=0;r<RETR.length-2 && v<URIPATH.length;r++,v++)
		{
			RETR[r]=URIPATH[v];
		
		}
		RETR[r]=13;
		RETR[r+1]=10;
		System.out.println(byte2str(RETR,0,RETR.length));
		//return "CWD pub/RFC/\r\n".getBytes();
		return RETR;
	}
	
	Boolean IsValidReply(byte[] ftpReply, byte[] predefCode) {			
		return ftpReply[0]==predefCode[0]&&ftpReply[1]==predefCode[1]&&ftpReply[2]==predefCode[2];				
	}
	
	int RetrieveFile(Socket pasv, byte[] cmd, InputStream passiveInput, Socket client) {
		OutputStream ftpOutput, clientOutput;
		byte[] status150 = {49,53,48};	

		try {		
			ftpOutput = pasv.getOutputStream();
			clientOutput = client.getOutputStream();
			passiveInput = pasv.getInputStream();
			byte[] ftpbuffer = new byte[1500];			
			int bytesRecorded = 0 , byteread =0;
			
			ftpOutput.write(cmd); //retreiving file
			
			byte[] ftpReply = ReadFtpOutput(pasv);
			
			if(ftpReply!=null && IsValidReply(GetStatusCode(ftpReply),status150)) {			 
				//passiveInput = pasv.getInputStream();
				while((byteread=passiveInput.read(ftpbuffer, 0, ftpbuffer.length)) != -1)  {	
					if(bytesRecorded==0)
						//clientOutput.write(resstatus200);
						WriteHttpResponse(client, 200);
					bytesRecorded += bytesRead;				
					clientOutput.write(ftpbuffer);

				}
			}
			else				
				//clientOutput.write(resstatus404);		
				WriteHttpResponse(client, 404);
			
			
			
			return bytesRecorded;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); //need to throw to the calling method
		} 
		 
		return 0;
	}

	void ParsePassiveModeInputs(byte[] ftpReply) {
		passvHost = new byte[40];
		byte[] passvport1 = new byte[4];
		byte[] passvport2 = new byte[4];
		pasvPort=0;
		int count=0;		
		int i, k=0,j=0,x=0,m=0;
		
		for( i=0;i<ftpReply.length;i++) {
				
			if(i>0 &&k==0&& i<ftpReply.length &&ftpReply[i-1]==40) {
				passvHost[k]=ftpReply[i];				
				k++;
				i++;
				
			}
			if(k>0 ) {				
				if((char)ftpReply[i]==',') {
						count++;
						if (count<4) {
							passvHost[k]=46;//replacing with .
						
						k++;
						}
							//break;
					   }
			else {
						if(count<4) {	
							passvHost[k]=ftpReply[i];						
							k++;
						}
						if(count==4) {
							passvport1[j]=ftpReply[i];
							j++;
						}
						if(count==5) {
							if((char)ftpReply[i]==')') 
								break;
							passvport2[x]=ftpReply[i];
							x++;							
						}					
				}				
			}				
		}	
		ipaddress =new byte[k];
		port1=new byte[j];
		port2=new byte[x];
		for(m=0;m<ipaddress.length;m++)
			ipaddress[m]=passvHost[m];
		for(m=0;m<port1.length;m++)
			port1[m]=passvport1[m];			
		for(m=0;m<port2.length;m++)
			port2[m]=passvport2[m];
			
		pasvPort=Integer.parseInt(byte2str(port1,0,port1.length))*256 + Integer.parseInt(byte2str(port2,0,port2.length));
				
		System.out.println(" ipaddress="+byte2str(ipaddress,0,ipaddress.length)+" port1="+byte2str(port1,0,port1.length)+" port2="+byte2str(port2,0,port2.length)+"Passport"+pasvPort);					
	}
	
	void WriteHttpResponse(Socket client, int statusCode){
					
		byte[] resstatus400={72,84,84,80,47,49,46,49,32,52,48,48,32,66,97,100,32,82,101,113,117,101,115,116,13,10,13,10};
		byte[] resstatus200={72,84,84,80,47,49,46,49,32,50,48,48,32,79,75,13,10,13,10};
		byte[] resstatus404={72,84,84,80,47,49,46,49,32,52,48,52,32,78,111,116,32,70,111,117,110,100,13,10,13,10};
		byte[] resstatus500={72,84,84,80,47,49,46,49,32,53,48,48,32,73,110,116,101,114,110,97,108,32,83,101,114,118,101,114,32,69,114,114,111,114,13,10,13,10};
		byte[] resstatus503={72,84,84,80,47,49,46,49,32,53,48,51,32,83,101,114,118,105,99,101,32,85,110,97,118,97,105,108,97,98,108,101,13,10,13,10};		
		
		try {
				OutputStream httpOutput = client.getOutputStream();
				
				switch(statusCode) {
				
					case 200:
						httpOutput.write(resstatus200);
						break;
						
					case 400:
						httpOutput.write(resstatus400);
						break;
						
					case 404:
						httpOutput.write(resstatus404);
						break;
					
					case 503:
						httpOutput.write(resstatus503);
						break;
						
					default:
						httpOutput.write(resstatus500);
						break;
						
				}
				
				httpOutput.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
} // class Apache

