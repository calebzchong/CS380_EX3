import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Ex3Client {
	public static void main(String[] args ){
		try (Socket socket = new Socket("18.221.102.182", 38103)) {
			System.out.println("Connected to server.");
			InputStream is = socket.getInputStream();
			int numBytes = is.read();
			System.out.println("Reading " + numBytes + " bytes.");
			byte[] bytes = new byte[numBytes];
			System.out.print("Received bytes:");
			for( int i = 0; i < bytes.length ; i++){
				bytes[i] = (byte)is.read();
			}
			printBytes(bytes);
			short checksum = checksum(bytes);
			System.out.printf("Checksum calculated: 0x%04X\n", checksum);
			byte[] checksumBytes = new byte[2];
			checksumBytes[0] = (byte)(checksum >>> 8);
			checksumBytes[1] = (byte)(checksum & 0xFF);
			OutputStream os = socket.getOutputStream();
			os.write(checksumBytes);
			int result = is.read();
			if ( result ==  1 ){
				System.out.println("Response good.");
			} else {
				System.out.println("Response bad.");
			}	
				
        } catch ( Exception e ){
        	e.printStackTrace();
        } finally {
        	System.out.println("Disconnected from server.");
        }
		
	}
	
	private static void printBytes(byte[] bytes) {
		for( int i = 0; i < bytes.length ; i++){
			if ( i % 10 == 0){
				System.out.print("\n   ");
			}
			System.out.print(String.format("%02X", bytes[i]));
		}
		System.out.println();
	}

	public static short checksum(byte[] bytes){
		long sum = 0;
		for ( int i = 0; i < bytes.length; i=i+2){
			int a = byteToUnsignedInt(bytes[i]);
			int b = 0;
			if ( i+1 < bytes.length ){
				b = byteToUnsignedInt(bytes[i+1]);
			}
			sum += a * 0x100 + b;
			if ( (sum & 0xFFFF0000) != 0 ){
				sum = sum & 0xFFFF;
				sum++;
			}
		}
		return (short) ~(sum & 0xFFFF);
	}
	
	private static int byteToUnsignedInt(byte b){
		return b & 0xFF;
	}
}
