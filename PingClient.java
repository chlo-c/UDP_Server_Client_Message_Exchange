import java.net.DatagramSocket;
//import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.DatagramPacket;

public class PingClient {
	
	DatagramSocket socket;
    
	byte buf[]= new byte[512];

    /** Create a datagram socket with random port for sending UDP messages */
    public void createSocket() {
    	try {
    		socket = new DatagramSocket() ;
    	} catch (SocketException e) {
    		System.out.println("Error creating socket: " + e);
            System.exit(-1);

    	}
    }

    /** Create a datagram socket for receiving UDP messages. 
     * This socket must be bound to the given port. */
    public void createSocket(int port) {
    	try {
    		socket = new DatagramSocket(port);
    	} catch (SocketException e) {
    		System.out.println("Error creating socket: " + e);
            System.exit(-1);

    	}
    }
    
    /** Send a UDP ping message which is given as the argument. */
    public void sendPing(Message ping) {
        
		try {
		    /* Create a datagram packet addressed to the recipient */
	    	
			DatagramPacket packet = new DatagramPacket(
					ping.getContents().getBytes(), 
					ping.getContents().length(),
					ping.getIP(), ping.getPort());
			
			
		    /* Send the packet */
	
			socket.send(packet);
			

		    System.out.println("Sent message to " + packet.getAddress() + 
		    		":" + packet.getPort());
		} catch (IOException e) {
			System.out.println("Error sending packet: " + e);
		}
    }
       
    /** Receive a UDP ping message and return the received message. 
     * We throw an exception to indicate that the socket timed out. 
     * This can happen when a message is lost in the network. */
    
	public Message receivePing() throws SocketTimeoutException {
		
		/* Create packet for receiving the reply */
    	DatagramPacket packet = new DatagramPacket(buf, buf.length);
    	
		/* Read message from socket. */
		try {
			socket.receive(packet);
			
		} catch (SocketTimeoutException e) {
		    throw e;
		} catch (IOException e) {
		    System.out.println("Error reading from socket: " + e);
		}
		
		Message reply = new Message(
				packet.getAddress(), 
				packet.getPort(), 
				new String(packet.getData()));
				
		return reply;   
	}
	

	
    
}
