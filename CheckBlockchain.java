import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;
import java.io.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.nio.charset.Charset;

/**
 *
 * @author Ali Akbar
 */

public class CheckBlockchain{

	public boolean certPresent(String x){
		try{
			FileReader f = new FileReader("Blockchain");
	        Scanner fileScanner = new Scanner(f);
	        while (fileScanner.hasNext()) {
	        	String l=fileScanner.nextLine();
	        	if(l.contains(x))
	        		return true;
	        }
	        f.close();
	    }
	    catch(Exception e){
	    	System.err.println("File Not Found, Blockchain not created.");
	    }
	    
	    return false;
	}
	public static void main(String args[])throws IOException{
		char ch='y';
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		CheckBlockchain cb=new CheckBlockchain();
		while(ch=='y'){
			System.out.println("Enter Name to search:");
			String x=br.readLine();
			boolean verified=cb.certPresent(x);
			if(verified==true){
				System.out.println(x+" is a valid certificate present in the Blockchain.");
			}
			else{
				System.out.println(x+" is not a valid certificate not present in the Blockchain.");
			}
			System.out.println("Do you want to search again? [y/n]");
			ch=(br.readLine()).charAt(0);
		}
	}
}