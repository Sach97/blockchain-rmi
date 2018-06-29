import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.security.Security;
import org.bouncycastle.*;

public class SimpleChain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //list of unspent transactions
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	public static float minimumTransaction = 0.1f;
	public static Transaction genesisTransaction;
	
	public static void main(String[] args) {
		
		//Setup bounceycastle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//Create the new wallets
		walletA = new Wallet();
		walletB = new Wallet();
		Wallet coinbase = new Wallet();
		
		
		//create genesis transaction, which sends 100 NoobCoin to walletA: 
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		Transaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and mining genesis block ... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is trying to send funds (40) to WalletB ... ");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA is trying to send funds (1000) to WalletB ... ");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is trying to send funds (20) to WalletA ... ");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
		addBlock(block3);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletB's balance is: " + walletB.getBalance());
		
		if (!isChainValid()) System.out.println("Blockchain is not valid");
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
	Add new block on the blockchain
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	private static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
		
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
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
		for(int i=1;i<blockchain.size();i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("#Current Hash not equal");
				return false;
			}
			
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(previousBlock.calculateHash())) {
				System.out.println("#Previous hash not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			
			TransactionOutput tempOutput;
			for(int t=0;t<currentBlock.transactions.size();t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				//check current transaction signature
				if(!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction("+currentBlock.transactions.get(t).transactionId+") is Invalid");
					return false;
				}
				
				//Check if output equals inpu on current transaction
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are not equals to outputs on Transaction(" + currentBlock.transactions.get(t).transactionId+")");
					return false;
				}
				
				//loop thru inputs
				for(TransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput ==null) {
						System.out.println("#Referenced input on Transaction ("+currentBlock.transactions.get(t).transactionId+") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input on Transaction ("+currentBlock.transactions.get(t).transactionId+") is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				//Loop thru outputs
				for(TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id,output);
				}
				
				//Check the recipient is who expected to be
				if(currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					System.out.println("#Transaction(" + currentBlock.transactions.get(t).transactionId + ") output reciepient is not who it should be");
					return false;
				}
				
				//Check if the sender is who expected to be
				if(currentTransaction.outputs.get(0).recipient != currentTransaction.sender ) {
					System.out.println("#Transaction(" + currentBlock.transactions.get(t).transactionId + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	

}
