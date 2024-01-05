import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPClient extends PingClient{

	/** Host to ping */
    String remoteHost;
    
    /** Port number of remote host */
    int remotePort;

    /** How many pings to send */
    static final int NUM_PINGS = 10;
    
    /** How many reply pings have we received */
    int numReplies = 0;
    
    
    /** Create an array for holding replies and RTTs */
    Long RTTs[][] = new Long[2][512];
    
    
    /* Send our own pings at least once per second. If no replies received
     within 5 seconds, assume ping was lost. */
    /** 1 second timeout for waiting replies */
    static final int TIMEOUT = 1000;
    
    /** 5 second timeout for collecting pings at the end */
    static final int REPLY_TIMEOUT = 5000;


    /** constructor **/
    public UDPClient(String host, int port) {
    	remoteHost = host;
    	remotePort = port;
    }

	
	
    /**
     * Main function. Read command line arguments and start the client.
     */

	public static void main(String[] args) {
		String host = null;
        int port = 0;

        
        /* Parse host and port number from command line */
        try {
        	host = args[0];
        	port = Integer.parseInt(args[1]);
        	

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need two arguments: remoteHost remotePort");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

        System.out.println("Contacting host " + host + " at port " + port);

    	UDPClient udpClient = new UDPClient(host, port);
    	udpClient.run();
    }

		
	 public void run() {
		    
	     /* Create socket. We do not care which local port we use. */
		 createSocket(); 
		 
	        try {
	            socket.setSoTimeout(TIMEOUT);
	        } catch (SocketException e) {
	            System.out.println("Error setting timeout TIMEOUT: " + e);
	        }

	        for (int i = 0; i < NUM_PINGS; i++) {
	        	
	            /* Message we want to send to server is just the current time. */
	        	String time = String.valueOf(System.currentTimeMillis());	        	

	        	
	            /* Send ping to recipient */
	            try {
		        	InetAddress rHost = InetAddress.getByName(remoteHost);
	            	
	            	Message ping = new Message(rHost, remotePort, time);
	                sendPing(ping);
//	                System.out.println("Sent message to "+ remoteHost+ ": "+remotePort);
	                
	            } catch (UnknownHostException e) {
	                System.out.println("Cannot find host: " + e);
	            }
	            	            
	            
	            /* Read the reply by getting the received ping message */
	            try {
	                Message reply = receivePing();
	                System.out.println("Received message from "+remoteHost+": "+ remotePort);
	                handleReply(reply.getContents());
	            } catch (SocketTimeoutException e) {
	                /* Reply did not arrive. Do nothing for now. Figure
	                 * out lost pings later. */
	            }
	        }
	        

	        try {
	            socket.setSoTimeout(REPLY_TIMEOUT);
	        } catch (SocketException e) {
	            System.out.println("Error setting timeout REPLY_TIMEOUT: " + e);
	        }
	        
	        
	        while (numReplies < NUM_PINGS) {
	            try {
	                Message reply = receivePing();
	                System.out.println("Received message from "+remoteHost+": "+ remotePort);
	                handleReply(reply.getContents());
	            } catch (SocketTimeoutException e) {
	                /* Nothing coming our way apparently. Exit loop. */
	                 break;
	            }
	        }
	        /* Print statistics */
	     	System.out.println("STATISTICS\n"+"     " + "Num "+ "    "+ "Reply "+ "      RTT  ");
	        
	        
	        for (int i = 0; i < NUM_PINGS; i++) {
	   		 	System.out.println("PING  " + i+ "  "+RTTs[1][i] +"   "+  RTTs[0][i]);
	        }

	    }

	 public void handleReply(String reply) {
		 reply = reply.replaceAll("[^0-9]", "");
		 long oldTime = Long.parseUnsignedLong(reply);
		 long RTT = System.currentTimeMillis() - oldTime;
		 
		 RTTs[0][numReplies]= RTT;
		 RTTs[1][numReplies]= oldTime;
		 
		 System.out.println("PING " + numReplies+ " "+ oldTime);
		 
		 /* Calculate RTT and store it in the rtt-array. */
		 numReplies++;

	 }
	        
	        

		
 }
	



