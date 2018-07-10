import java.util.ArrayList;

import com.google.gson.GsonBuilder;

public class Server {

	public static void main(String[] args) {
		ArrayList<Block> blockchain = new ArrayList<Block>(); 
		int difficulty = 5;
		Node node = new Node(blockchain, difficulty);
		node.processBlocks();
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(node.getBlockchain());		
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
	
}
	
}
