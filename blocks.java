import java.io.*;
import java.util.*;

/**
 *
 * @author Ali Akbar
 */

public class blocks{
	public String blockId;
	public HashMap<String,String> fileANDhash=new HashMap<>();
	public static void main(String args[]){

	}
	public String getBlockID(){
		return this.blockId;
	}
	public String printBlock(){
		String test="";
		for (Map.Entry<String, String> entry : fileANDhash.entrySet()) {
    		test+=(entry.getKey()+"\n"+entry.getValue());
		}
		return test;
	}
}