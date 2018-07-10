import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import com.google.gson.GsonBuilder;


public class Node implements NodeInterface {
	private ArrayList<Block> blockchain = new ArrayList<Block>(); 
	private int difficulty = 5;
	private PriorityQueue<String> transactionPool;
	private int count=0;
	
	protected Node() {
		transactionPool = new PriorityQueue<String>();
		
	}
	
	
	public void processBlocks() throws RemoteException {
	while(!transactionPool.isEmpty()) {
		String transaction = getTransactionFromPool();
		
		blockchain.add(new Block(transaction,getHash()));
		System.out.println("Trying to mine Block " +count);
		blockchain.get(count).mineBlock(difficulty);

		count+=1;
	}
}

	public String getHash() throws RemoteException  {
	String hash = "0";
	if(!(blockchain.size()== 0)) {
		hash = blockchain.get(blockchain.size()-1).getHash();
	}
	return hash;
}

	//getters
	public ArrayList<Block> getBlockchain() throws RemoteException {
		return blockchain;
	}
	
	public String getBlockchainJson() throws RemoteException {
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(getBlockchain());
		return blockchainJson;
	}
	
	public int getDifficulty() throws RemoteException  {
		return difficulty;
	}
	
	public String getTransactionFromPool() throws RemoteException {
		String transaction = transactionPool.remove();
		return transaction;
		
	}
	
	public int getCount() throws RemoteException  {
		return count;
	}
	
	//setters
	public void addTransactionToPool(String transaction) throws RemoteException {
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
	public Boolean isChainValid() throws RemoteException {
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
