import java.io.*;
import java.security.*;
import sun.misc.BASE64Encoder;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.misc.BASE64Decoder;
import java.util.Base64;
import java.nio.charset.Charset;
import java.nio.*;
import java.util.*;

/**
 * This file is used to generate certificates.
 *
 * @author Ali Akbar
 */

public class certificate{

	public static String VersionNumber;
	public static String SerialNumber;
	public static String SignatureAlgoUsed;
	public static Date before;
	public static Date after;
	public static String SubjectName;
	public static String pubprivKeyAlgo;
	public static String publickKeyFile;
	public static String privateKeyFile;
	public static String signatureGen;
	public static String cert;//checkName sets this
	public static String error;
	public static String t;

	public certificate(	String vN, String subN, String sA, String pkiAlgo, String pubF, String privF){
		//subn,signalgo,pkialgo,pub,priv
		if(!getVn(vN))
			System.out.println("Wrong Certificate Version.");
		else
			this.VersionNumber=vN;
		this.SerialNumber=getSerial();
		if(!checkSA(sA))
			System.out.println("Wrong Public/Private Key Algorithm Used.");
		else
			this.SignatureAlgoUsed=sA;
		if(!pkiAlgols(pkiAlgo))
			System.out.println("Wrong Signature Algorithm Used.");
		else
			this.pubprivKeyAlgo=pkiAlgo;

		Calendar cal = Calendar.getInstance();
		Date bef = cal.getTime();
		this.before=bef;
		cal.add(Calendar.YEAR, 1); // to get previous year add -1
		Date aft = cal.getTime();
		this.after=aft;
		//subN
		if(!checkName(subN))
			System.out.println("Wrong Subject Name Format Used.");
		else
			this.SubjectName=subN;
		if(!fileExists(pubF))
			System.out.println("Public Key File could not be found.");
		else
			this.publickKeyFile=pubF;
		if(!fileExists(privF))
			System.out.println("Private Key File could not be found.");
		else
			this.privateKeyFile=privF;
	}
	public static boolean checkSA(String sAA){
		if(sAA.equalsIgnoreCase("SHA256withRSA"))
			return true;
		else
			return false;
	}
	public static boolean pkiAlgols(String sAA){
		if(sAA.equalsIgnoreCase("SHA1"))
			return true;
		else
			return false;
	}
	public static boolean fileExists(String fn){
		try{
			File tmpDir = new File(fn);
			return tmpDir.exists();
		}
		catch(Exception e){
			System.err.println("File Not Found.");
		}
		return false;
	}


	public static boolean getVn(String vN){
			if(vN.equals("BlockChainSSL1.0"))
				return true;
			else
				return false;
	}

	public static String getSerial(){
			Random rand = new Random(); 
			int value = rand.nextInt(5000);
			StringBuilder sb=new StringBuilder("APV"+value);
			return sb.toString();
	}

	public static boolean checkName(String test){
			String ww=test.substring(0,3);
			String x=test.substring(test.length()-4);
			if(ww.equalsIgnoreCase("www")){
				if(x.equalsIgnoreCase(".com")||x.equalsIgnoreCase(".org")||x.equalsIgnoreCase(".edu")||x.equalsIgnoreCase(".biz")||x.equalsIgnoreCase(".gov"))
					{cert=test.substring(4,test.length()-4);return true;}
			}
			return false;
	}

    public static String GenerateSignature(String data){

        try{

            /* Generate a key pair */

            FileInputStream keyfis = new FileInputStream(privateKeyFile);
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();

            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);

             /* Create a Signature object and initialize it with the private key */

            Signature dsa = Signature.getInstance("SHA256withRSA"); 

            dsa.initSign(privKey);
            dsa.update(data.getBytes());            

            byte[] realSig = dsa.sign();

        
            BASE64Encoder encoder = new BASE64Encoder();
            String signature = encoder.encode(realSig);
            signatureGen=signature;
            //System.out.println("\nSignature = [" + signature + "]\n\n");
            return signature;
            //FileOutputStream output = new FileOutputStream(name+"signature.base64");
            //output.write( signature.getBytes());
            //output.close();
            
        }

        catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return signatureGen;

    }
    public static String getDetails(){
    	return VersionNumber+SerialNumber+SignatureAlgoUsed+before.toString()+after.toString()+SubjectName+pubprivKeyAlgo+publickKeyFile;

    }
    public static String sha1(String input)throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        String hash = encoder.encode(result);
        return hash;
    }
    public static String writeTOcerti(String signa, String hashi)throws IOException{
	    FileWriter fw =new FileWriter(cert+".certificate");
	    BufferedWriter writer = new BufferedWriter(fw);
	    writer.write("Version Number: "+VersionNumber+"\n");
	    writer.write("Serial Number: "+SerialNumber+"\n");
	    writer.write("Hash Algorithm Used: "+SignatureAlgoUsed+"\n");
	    writer.write("Date before: "+before+"\n");
	    writer.write("Date after: "+after+"\n");
	    writer.write("Subject Name: "+SubjectName+"\n");
	    writer.write("Signature Algorithm Used: "+pubprivKeyAlgo+"\n");
	    writer.write("Public Key File: "+publickKeyFile+"\n");
	    writer.write("Signature: "+signa+"\n");
	    writer.write("Hash: "+hashi+"\n");
	    writer.close();

	    //

	    FileWriter fw1 =new FileWriter("details"+cert+".certificate");
        BufferedWriter writer1 = new BufferedWriter(fw1);
            writer1.write(VersionNumber+"\n");
        writer1.write(SerialNumber+"\n");
        writer1.write(SignatureAlgoUsed+"\n");
        writer1.write(before+"\n");
        writer1.write(after+"\n");
        writer1.write(SubjectName+"\n");
        writer1.write(pubprivKeyAlgo+"\n");
        writer1.write(publickKeyFile+"\n");
        writer1.write(signa+"\n");
        writer1.write(hashi+"\n");
        writer1.close();
           
        //return cert+".certificate";

	    return cert+".certificate";
    }

//    certificate() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public void writeTOmaster(String cf,String hash)throws IOException{
    	FileWriter fw =new FileWriter("master",true);
	    BufferedWriter writer = new BufferedWriter(fw);
	    writer.write(cf+" "+hash+"\n");
	    writer.close();
    }

	public static void main(String args[])throws IOException, NoSuchAlgorithmException{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		char ch='y';
		System.out.println("Do you want to generate a certificate? [y/n]");
		ch=(br.readLine()).charAt(0);
		while(ch=='y'){
			System.out.println("Enter Subject Name");
			String subn=br.readLine();
			System.out.println("Enter Signature Algorithm Used");
			String signalgo=br.readLine();
			System.out.println("Enter Public Key Algorithm Used");
			String pkialgol=br.readLine();
			System.out.println("Enter Public Key File Name");
			String pub=br.readLine();
			System.out.println("Enter Private Key File Name");
			String priv=br.readLine();
			certificate c=new certificate("BlockChainSSL1.0",subn,signalgo,pkialgol,pub,priv);
			String genSign=c.GenerateSignature(c.getDetails());
			String hash=c.sha1(c.getDetails()+genSign);
			String certiFileName=c.writeTOcerti(genSign,hash);
			c.writeTOmaster(certiFileName,hash);
			System.out.println("Do you want to generate another certificate? [y/n]");
			ch=(br.readLine()).charAt(0);
		}
		//checkName(subn);
		br.close();
	}
}