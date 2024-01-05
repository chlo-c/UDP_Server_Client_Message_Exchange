import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class testtest {

	public static void main(String[] args) throws UnknownHostException {

		String remoteHost = "192.168.1.2";
		
		byte[] bytes = remoteHost.getBytes();
    	
		String s = new String(bytes);
		int i = 0;
		for (byte byt : bytes) {
			System.out.println(byt + " "+ s.charAt(i));
			i++;
		}
		
		InetAddress rHost = InetAddress.getByName(remoteHost);
		
		System.out.println(rHost.toString());
    
		
		String gev = "1696733191298";
		
		long num = Long.parseLong(gev);
		
		System.out.print(num);
		
		
	}

}
