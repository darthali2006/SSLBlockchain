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
import sun.misc.BASE64Encoder;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.misc.BASE64Decoder;
import java.nio.charset.Charset;
//import static javaapplication1.certificate;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Ali Akbar
 */
public class PutCertificate {


    String filename;
    String concat_text;
    String hash;
    String details_file;
    blocks b[]=new blocks[1000];
    static int noBlocks=0;

    public void concatenate(String fn) throws FileNotFoundException {
        filename = fn;
        FileReader f = new FileReader(filename);
        concat_text = "";
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            concat_text += line;
        }
//        System.out.println(concat_text);
    }

    public void hashIt() throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(concat_text.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        hash = encoder.encode(result);
        System.out.println(hash);
    }

    public void addHash() throws FileNotFoundException, IOException {
        FileReader f = new FileReader(filename);
        Scanner fileScanner = new Scanner(f);
        File newTestFile = new File("test.certificate");
        FileWriter fw = new FileWriter(newTestFile, true);
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            fw.write(line + hash + "\n");
        }
        fw.close();
        f.close();
    }

    public void readMaster()throws FileNotFoundException,InvalidKeyException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, InvalidKeySpecException{
        filename = "master";
        FileReader f = new FileReader(filename);
        String x = "";
        Scanner fileScanner = new Scanner(f);
        b[noBlocks]=new blocks();
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            int p=line.indexOf(" ");
            filename=line.substring(0,p);
            details_file="details"+line.substring(0,p);
            String ha=line.substring(p+1);
            //concatenate(filename);
            String details = getDetails(details_file,filename);
            b[noBlocks].fileANDhash.put(filename,ha);
            //concatanate(details_file);
        }
        noBlocks++;
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, InvalidKeySpecException {
//[11:14 AM, 4/20/2018] Ali Akbar: .certifcate file has values , take those values concatanate them , use the contactenated string to geneate hash
//[11:14 AM, 4/20/2018] Ali Akbar: then add hash to all the values and verify signature
//[11:14 AM, 4/20/2018] Ali Akbar: if signature verifies put all of the things from master to the block
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        PutCertificate p = new PutCertificate();
        FileWriter fw1 =new FileWriter("Blockchain",true);
        BufferedWriter writer1 = new BufferedWriter(fw1);
        char ch='y';
        System.out.println("Do you want to verify certifcates in the master file? [y/n]");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        ch=(br.readLine()).charAt(0);
        while(ch=='y'){
            p.readMaster();
            writer1.write(p.b[noBlocks-1].printBlock().toString());
            System.out.println("Block"+(noBlocks-1)+"created.");
            System.out.println("Do you want to verify certifcates in the master file? [y/n]");
            ch=(br.readLine()).charAt(0);
        }
        writer1.flush();
        writer1.close();
        //p.concatenate(fn);
        //p.hashIt();
       // p.addHash();
//        String pubFile = "Alicesuepk";
//        
//        System.out.println("starting the verify section");
//        
        

        //verify(pubFile, base64Signature, details);
        //certificate c = new certificate();
        
    }

    private String getDetails(String x,String y) throws FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, IOException {
        String text = "", sign = "";
        int count = 0;
        concatenate(y);
        FileReader f = new FileReader(x);
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNext()) {
            if (count++ == 8) break;
                text += fileScanner.nextLine();
            }
        int pos1 = concat_text.indexOf("Signature:");
        int pos2= concat_text.indexOf("Hash:");
        String base64Signature = concat_text.substring(pos1+11, pos2);

        //System.out.println("\n\n\n\n"+text);
        VerifySignature(text,base64Signature);
        return text;
    }







    public boolean VerifySignature(String trans,String sign) {
        boolean verifies=false;
        /* Verify a RSA signature */

        //if (args.length != 3) {
          //  System.out.println("Usage: VerSig publickeyfile signaturefile datafile");
       try{

            /* import encoded public key */

            FileInputStream keyfis = new FileInputStream(filename.substring(0,filename.indexOf('.'))+"suepk");
            byte[] encKey = new byte[keyfis.available()];  
            keyfis.read(encKey);
            keyfis.close();

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            InputStream targetStream = new ByteArrayInputStream(sign.getBytes());
            //FileInputStream input = new FileInputStream( name+"signature.base64");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] sigBytes = decoder.decodeBuffer(targetStream);
            //input.close();


            /* input the signature bytes 
            FileInputStream sigfis = new FileInputStream(args[1]);
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );

            sigfis.close();*/

            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(pubKey);
            sig.update(trans.getBytes());

            /* Update and verify the data */

            verifies = sig.verify(sigBytes);

            System.out.println("signature verifies: " + verifies);


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
            
        };
        return verifies;
    }
}