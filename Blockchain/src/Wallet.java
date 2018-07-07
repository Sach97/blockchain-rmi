
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

public class Wallet {
	
	public PrivateKey privateKey;
	public PublicKey publicKey;
	//public String address;
	
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	
	//Wallet Constructor
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
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // Set the public and private keys from the keyPair
	        privateKey = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	       //address = setAddress(publicKey);
	        
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
//	public String getAddress() {
//		return this.address;
//		
//	}
	
//	//inspired by https://en.bitcoin.it/wiki/Address
//	public String setAddress(PublicKey publicKey) {
//		byte[] address = new byte[20];
//		byte[] sha256PubKeyBytes = StringUtil.applySha256(publicKey.toString()).getBytes(); 
//		try {
//		        byte[] sha256 = MessageDigest.getInstance("SHA-256").digest(sha256PubKeyBytes);
//		        RIPEMD160Digest digest = new RIPEMD160Digest();
//		        digest.update(sha256, 0, sha256.length);
//		        digest.doFinal(address, 0);
//		    } catch (NoSuchAlgorithmException e) {
//		        throw new RuntimeException(e);
//		    }
//		
//		String mainnetworkAddress = "00".concat(address.toString());
//		String sha256hash = StringUtil.applySha256(mainnetworkAddress);
//		String addressChecksum = StringUtil.applySha256(sha256hash).substring(0, 8);
//		//Base58 Base58 = new Base58();
//		String strAddress = Base58.encode(mainnetworkAddress.concat(addressChecksum).getBytes());
//		
//		return strAddress;
//		
//	}
	
	

	/**
	Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, TransactionOutput> item: SimpleChain.UTXOs.entrySet()){
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
	public Transaction sendFunds(PublicKey _recipient,float value ) {
		if(getBalance() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		
		return newTransaction;
	}
	
}


