import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable{
	
	private String hash;
	//private int index;
	private String previousHash;
	private String data;
	private long timeStamp;
	//private int nonce;
	
	
	//Block constructor
	
	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	
	//getters 
	 public String getPreviousHash() {
		 return previousHash;
	 }
	 
	 public String getData() {
		 return data;
	 }
	 
	 public String getHash() {
		 return hash;
	 }
	 
//	 public int getIndex() {
//		 return index;
//	 }
	 
	 
	 
	/**
	Calculate the hash from all parts of the block we don’t want to be tampered with
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public String calculateHash() {
		String calculatedHash = StringUtil.applySha256(data + previousHash +Long.toString(timeStamp));
				return calculatedHash;
	}
	
	
	/**
	Takes in argument an int called difficulty, this is the number of 0’s they must solve.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
//	public void mineBlock(int difficulty) {
//		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
//		while(!hash.substring( 0, difficulty).equals(target)) {
//			nonce ++;
//			hash = calculateHash();
//		}
//		System.out.println("Block Mined!!! : " + hash);
//	}
	
	
}