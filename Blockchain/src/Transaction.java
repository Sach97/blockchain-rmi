import java.security.*;
import java.util.ArrayList;

public class Transaction {

	public String transactionId;
	public static PublicKey sender;
	public static PublicKey recipient;
	public float value;
	public static byte[] signature;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	public static int sequence = 0;
	
	//Transaction Constructor
	public Transaction(PublicKey from, PublicKey to, float value,ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	
	/**
	This Calculates the transaction hash (which will be used as Transaction Id)
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	private String calculateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		String calculatedHash = StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)+Float.toString(value)+sequence);
		return calculatedHash;
	}
	
	/**
	Signs all the data we dont wish to be tampered with.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public static void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient);
		signature = StringUtil.applyECDSASig(privateKey, data);
	}
	
	/**
	Verifies the data we signed hasnt been tampered with
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	
	/**
	Returns true if new transaction could be created.	
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public boolean processTransaction() {
		
		//Check signature
		if(verifySignature() == false) {
			System.out.println("#Transaction signature failed to verify");
			return false;
		}
		
		//gather transaction inputs (Make sure they are unspent):
		for(TransactionInput i : inputs) {
			i.UTXO = SimpleChain.UTXOs.get(i.transactionOutputId);
		}
		
		
		//check if transaction is valid:
		if(getInputsValue() < SimpleChain.minimumTransaction) {
			System.out.println("#TransactionInputs to small: "+ getInputsValue());
			return false;
		}
		
		//generate transaction outputs
		float leftOver = getInputsValue() - value;
		transactionId = calculateHash();
		outputs.add(new TransactionOutput(this.recipient,value,transactionId));
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));
		
		//add outputs to Unspent list
		for(TransactionOutput o : outputs) {
			SimpleChain.UTXOs.put(o.id,o);
		}
		
		//remove transaction inputs from UTXO lists as spent
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue;  //if Transaction can't be found skip it 
			SimpleChain.UTXOs.remove(i.UTXO.id);
			
		}
		
		return true;
	}

	/**
	Returns sum of inputs(UTXOs) values
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue;  //if Transaction can't be found skip it 
			total += i.UTXO.value;
		}
		return total;
	}
	
	/**
	Returns sum of outputs
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	
	
}
