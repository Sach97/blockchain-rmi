
public class TransactionInput {

	public String transactionOutputId;
	public TransactionOutput UTXO; //Unspent transaction output
	
	//TransactionInput Constructor
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	
}
