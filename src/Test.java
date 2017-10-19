import java.io.OutputStream;

public class Test {

	public static void main(String[] args) {
		String byteString = "F08F7807236C";
		byte[] bytes = new byte[byteString.length()/2];
		for ( int i = 0, j=0; i < bytes.length; i++,j=j+2){
			bytes[i] = (byte)Integer.parseInt(byteString.substring(j, j+2),16);
		}
		printBytes(bytes);
		short checksum = checksum(bytes);
		System.out.printf("Checksum calculated: 0x%04X\n", checksum);
		byte[] checksumBytes = new byte[2];
//		checksumBytes[0] = (byte)(checksum >>> 8);
//		checksumBytes[1] = (byte)(checksum & 0xFF);
//		System.out.printf("1: 0x%02X\n", checksumBytes[0]);
//		System.out.printf("2: 0x%02X\n", checksumBytes[1]);

	}
	
	private static void printBytes(byte[] bytes) {
		for( int i = 0; i < bytes.length ; i++){
			if ( i % 10 == 0 && i != 0){
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
			int b = byteToUnsignedInt(bytes[i+1]);
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
