import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash;
	private String data;
	private long timeStamp;
	
	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
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
	

}
