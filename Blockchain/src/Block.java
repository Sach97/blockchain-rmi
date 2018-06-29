import java.util.ArrayList;
import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private String data;
	private long timeStamp;
	private int nonce;
	
	
	//Block constructor
	public Block(String previousHash) {
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
		String calculatedHash = StringUtil.applySha256(data + previousHash +Long.toString(timeStamp)+Integer.toString(nonce)+ merkleRoot);
				return calculatedHash;
	}
	
	
	/**
	Takes in argument an int called difficulty, this is the number of 0’s they must solve.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
	
	/**
	Add transactions to this block
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public boolean addTransaction(Transaction transaction) {
		
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;
		if((previousHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction successfully added to block");
		return true;
	}
	
}