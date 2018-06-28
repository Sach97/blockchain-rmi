
public class SimpleChain {
	
	public static void main(String[] args) {
		Block genesisBlock = new Block("Hi im the first block","0");
		System.out.println("Hash for block 1 : "+genesisBlock.hash);
		
		Block secondBlock = new Block("Hey Im the secod block",genesisBlock.hash);
		System.out.println("Hash for block 2 : "+secondBlock.hash);
		
		Block thirdBlock = new Block("Hey Im the secod block",secondBlock.hash);
		System.out.println("Hash for block 2 : "+thirdBlock.hash);
		
		
	}

}
