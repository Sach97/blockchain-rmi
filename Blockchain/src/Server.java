import java.util.ArrayList;

import com.google.gson.GsonBuilder;

public class Server {

	public static void main(String[] args) {

		Node node = new Node();
		ArrayList<String> blockData = new ArrayList<String>();
		blockData.add("Hi Im' the first block");
		blockData.add("Hey Im' the second block");
		blockData.add("Yo Im' the third block");
		for(String data: blockData) {
			node.addTransactionToPool(data);
		}
		node.processBlocks();
		String blockchainJson = node.getBlockchainJson();		
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
	
}
	
}
