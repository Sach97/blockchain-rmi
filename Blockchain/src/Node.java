import java.util.ArrayList;
import java.util.PriorityQueue;


public class Node {
	private ArrayList<Block> blockchain = new ArrayList<Block>(); 
	private int difficulty = 5;
	private PriorityQueue<String> transactionPool;
	private int count=0;
	
	public Node() {
		transactionPool = new PriorityQueue<String>();
		
	}
	
	
	public void processBlocks() {
	while(!transactionPool.isEmpty()) {
		String transaction = this.getTransactionFromPool();
		
		blockchain.add(new Block(transaction,getHash()));
		System.out.println("Trying to mine Block" +count);
		blockchain.get(count).mineBlock(difficulty);

		count+=1;
	}
}

	private String getHash() {
	String hash = "0";
	if(!(blockchain.size()== 0)) {
		hash = blockchain.get(blockchain.size()-1).getHash();
	}
	return hash;
}

	//getters
	public ArrayList<Block> getBlockchain(){
		return this.blockchain;
	}
	
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public String getTransactionFromPool(){
		String transaction = transactionPool.remove();
		return transaction;
		
	}
	
	public int getCount() {
		return count;
	}
	
	//setters
		public void addTransactionToPool(String transaction) {
			transactionPool.add(transaction);
		}
		
	
	
	
	/**
	Check the integrity of our blockchain. Loop through all blocks in the chain and compare the hashes. 
	This method will need to check the hash variable is actually equal to the calculated hash, 
	and the previous block’s hash is equal to the previousHash variable.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		for(int i=1;i<blockchain.size();i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			//compare registered hash and calculated hash:
			if(!currentBlock.getHash().equals(currentBlock.calculateHash())) {
				System.out.println("Current Hash not equal");
				return false;
			}
			
			//compare previous hash and registered previous hash
			if(!previousBlock.getHash().equals(previousBlock.calculateHash())) {
				System.out.println("Previous hash not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
	}
	
	

}
