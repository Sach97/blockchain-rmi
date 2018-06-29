import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
	public PrivateKey privateKey;
	public PublicKey publicKey;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.
	
	public Wallet() {
		generateKeyPair();
	}

	
	/**
	Generate an Elliptic Curve KeyPair. This methods makes and sets our Public and Private keys
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public void generateKeyPair() {
		try {
			
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			
			//Initialize the key generator  and generate a key pair
			keyGen.initialize(ecSpec,random);
			KeyPair keyPair = keyGen.generateKeyPair();
			
			//Set the private and public keys from key pair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
			
		} catch (Exception e){
			throw new RuntimeException(e);
			}
		
	}

	
	/**
	Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public float getBalance() {
		float total = 0;
		for(Map.Entry<String, TransactionOutput> item: SimpleChain.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
		}
		
		return total;
	}
	
	/**
	Generates and returns a new transaction from this wallet
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public Transaction sendFunds(PublicKey _recipient, float value) {
		
		float total = 0;
		
		if(getBalance() < value) { //gather balance and check funds
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
			
		}
		
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		for(Map.Entry<String, TransactionOutput> item: SimpleChain.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction(publicKey,_recipient,value,inputs);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input : inputs) {
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
	
	
}
