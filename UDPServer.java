import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

public class UDPServer {

	public static void main(String[] args) throws Exception {
		int port = 0;
        
        /** Parse port number from command line **/
		
        try {
        
        	port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need one argument: port number.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

        
        
        
        /** Create a new datagram socket at the port **/
        @SuppressWarnings("resource")
		DatagramSocket serverSocket = new DatagramSocket(port);
//        . . . . 

        /** Let the user know the server is running **/
        System.out.println("The UDP server is listening on port " + port);

        byte buf[]= new byte[512];
        int counter = 0;
        
        while (true) {
            
            /** Create a new datagram packet and let the socket receive it **/

        	DatagramPacket packet = new DatagramPacket(buf, buf.length);
        	
        	serverSocket.receive(packet);
        	
             /** Print the message received **/
        	
//        	String str = packet.getData();
        	
        	System.out.print("PING "+ counter + " "+ new String(packet.getData()));
        	
        	
        	System.out.println();
        	
        	
             /** Get the IP Address of the Sender **/
        	InetAddress senderIP = packet.getAddress();
        	
        	/** Get the port of the Sender **/
             int senderPort = packet.getPort();             
             
             /** Prepare the data to send back **/
             
             packet.setAddress(senderIP);
             packet.setPort(senderPort);
             
             
             /** Send the packet **/
             serverSocket.send(packet);
             counter++;
             
         }

        
        
        
	}

}












