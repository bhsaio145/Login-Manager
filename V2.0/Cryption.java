import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.table.DefaultTableModel;

public class Cryption {
	
	private static byte[] ivSave;
	private static byte[] saltSave;
	
	protected static IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
	    new SecureRandom().nextBytes(iv);
	    ivSave = iv;
	    return new IvParameterSpec(iv);
	}
	
	protected static String generateSalt() {
		byte[] slt = new byte[16];
	    new SecureRandom().nextBytes(slt);
	    saltSave = slt;
	    return new String(slt);
	}
	//function that uses password and salt string to generate a secret key
	private static SecretKey getKeyFromPassword(String password, String salt)
		throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;
	}
	
	public static void encrpytData(String password, String inFile, String outFile) 
		throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		//generate a new instance of salt and IV value per encryption to improve security
		String salt = generateSalt();
		IvParameterSpec iv = generateIv();
		SecretKey key = getKeyFromPassword(password, salt);
		//setting encryption method
		String algorithm = "AES/CBC/PKCS5Padding";
	    Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	    //writing information needed for decryption
	    File outputFile = new File(outFile);
	    FileOutputStream outputStream = new FileOutputStream(outputFile);
	    File inputFile = new File(inFile);
	    FileInputStream inputStream = new FileInputStream(inputFile);
	    outputStream.write(ivSave);
	    outputStream.write(saltSave);
	    byte[] buffer = new byte[64];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        byte[] output = cipher.update(buffer, 0, bytesRead);
	        if (output != null) {
	            outputStream.write(output);
	        }
	    }
	    byte[] outputBytes = cipher.doFinal();
	    if (outputBytes != null) {
	        outputStream.write(outputBytes);
	    }
	    inputStream.close();
	    outputStream.close();
	}
	public static void decrpytFile(String password, String inFile, String outFile) 
		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		//setting Files for reading and writing
		File outputFile = new File(outFile);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		File inputFile = new File(inFile);
		FileInputStream inputStream = new FileInputStream(inputFile);
		//reading header bytes for IV and salt values
		ivSave = inputStream.readNBytes(16);
		saltSave = inputStream.readNBytes(16);
		//converting read bytes to correct format for decryption
		IvParameterSpec iv = new IvParameterSpec(ivSave);
		String salt = new String(saltSave);
		SecretKey key = getKeyFromPassword(password, salt);
		//setting decryption method
		String algorithm = "AES/CBC/PKCS5Padding";
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		//decoding inputStream content by byte to prevent a large data decryption
		byte[] buffer = new byte[64];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        byte[] output = cipher.update(buffer, 0, bytesRead);
	        if (output != null) {
	            outputStream.write(output);
	        }
	    }
	    byte[] outputBytes = cipher.doFinal();
	    if (outputBytes != null) {
	        outputStream.write(outputBytes);
	    }
	    inputStream.close();
	    outputStream.close();
	}
}
