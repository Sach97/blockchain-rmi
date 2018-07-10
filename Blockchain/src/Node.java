import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.google.gson.GsonBuilder;


public class Node implements NodeInterface {
	private ArrayList<Block> blockchain = new ArrayList<Block>(); 
	private int difficulty = 5;
	private PriorityQueue<String> transactionPool;
	private int blockCount=0;
	private boolean ready = false;
	
	protected Node() {
		transactionPool = new PriorityQueue<String>();
		
	}
	
	
	public void processBlocks() throws RemoteException {
	while(!transactionPool.isEmpty()) {
		String transaction = getTransactionFromPool();
		
		blockchain.add(new Block(transaction,getHash()));
		System.out.println("Trying to mine Block " +blockCount);
		blockchain.get(blockCount).mineBlock(difficulty);
		if(transactionPool.isEmpty()) setReady();
		blockCount+=1;
	}
}

	public String getHash() throws RemoteException  {
	String hash = "0";
	if(!(blockchain.size()== 0)) {
		hash = blockchain.get(blockchain.size()-1).getHash();
	}
	return hash;
}
	
	public String getBlock(int blockId) throws RemoteException  {
		return blockchain.get(blockId).getData();
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
	
	public boolean isReady() throws RemoteException {
		return this.ready;
	}
	
	public int getCount() throws RemoteException  {
		return blockCount;
	}
	
	//setters
	public void addTransactionToPool(String transaction) throws RemoteException {
		transactionPool.add(transaction);
	}
	
	public void setReady() throws RemoteException {
		this.ready = true;
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


//	public void cli() throws RemoteException {
////		if(isReady() == true) {
////			String blockchainJson = getBlockchainJson(); //TODO: error here, doesnt wait the end of process blocks after bat execution	 	
////    		System.out.println("\nThe block chain: ");
////    		System.out.println(blockchainJson);
////		}
//		
//		while (true) {
//            int choice = -1;
//            System.out.println("//-------------------------//");
//            System.out.println("[1] Get Blockchain");
//            System.out.println("[2] Add transaction to pool");
//            System.out.println("//-------------------------//");
//            System.out.println("Choose a number: ");
//
//            Scanner scn = new Scanner(System.in);
//            choice = scn.nextInt();
//            switch (choice) {
//                case 1:
//                    createAccount(s, a);
//                    break;
//                case 2:
//                    login(s, a);
//                    break;
//            }
//        }
//		
//	}
	
	

}
