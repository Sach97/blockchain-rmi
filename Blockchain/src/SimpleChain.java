import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.security.Security;
import org.bouncycastle.*;

public class SimpleChain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	
	public static void main(String[] args) {
		
		//Setup bounceycastle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//Create the new wallets
		walletA = new Wallet();
		walletB = new Wallet();
		//Test public and private key methods
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
		System.out.println(StringUtil.getStringFromKey(walletB.privateKey));

		//Create a test Transaction between walletA and WalletB
		Transaction transaction = new Transaction(walletA.publicKey,walletB.publicKey,5,null);
		Transaction.generateSignature(walletA.privateKey);
		
		//Verfiy if the signature works and  verify it from the public key
		System.out.println("Is signature verified");
		System.out.println(transaction.verifySignature());
		
//		blockchain.add(new Block("Hi im the first block", "0"));
//		System.out.println("Trying to mine Block 1");
//		blockchain.get(0).mineBlock(difficulty);
//		
//		blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash)); 
//		System.out.println("Trying to mine Block 2");
//		blockchain.get(1).mineBlock(difficulty);
//		
//		blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
//		System.out.println("Trying to mine Block 3");
//		blockchain.get(2).mineBlock(difficulty);
//		
//		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
//		System.out.println("\nThe block chain: ");
//		System.out.println(blockchainJson);
		
	}
	
	
	/**
	Check the integrity of our blockchain. Loop through all blocks in the chain and compare the hashes. 
	This method will need to check the hash variable is actually equal to the calculated hash, 
	and the previous block’s hash is equal to the previousHash variable.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		for(int i=1;i<blockchain.size();i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("Current Hash not equal");
				return false;
			}
			
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(previousBlock.calculateHash())) {
				System.out.println("Previous hash not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
	}
	
	

}
