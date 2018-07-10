import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	public static ArrayList<String> blockData = new ArrayList<String>();
	
	public Server(int port) {
        try {
            Node n = new Node();
            NodeInterface node = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Naming.rebind("rmi://localhost:" + port + "/Node", node);
            System.out.println("Node is starting...");
            System.out.println("Node started");
            System.out.println("Waiting for transactions to process ...");
            while(true) {
            	 n.processBlocks();
            	 System.out.println(n.getCount());
//            	 System.out.println(n.isChainValid().toString());
            	
            }
           
//    		blockData.add("Hi Im' the first block");
//    		blockData.add("Hey Im' the second block");
//    		blockData.add("Yo Im' the third block");
//    		for(String data: blockData) {
//    			node.addTransactionToPool(data);
//    		}
    		
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
    }

    public static void main(String args[]) {

    	
        int p = 1099; // default port number

        if (args.length == 1) {
            p = Integer.parseInt(args[0]); // custom port number
        }
        Server s = new Server(p);
    }
    
//	public static void cli(NodeInterface n) {
//		
//		while (true) {
//            int choice = -1;
//            System.out.println("//-------------------------//");
//            System.out.println("[1] Get Blockchain");
//            System.out.println("[2] Add data to blockchain");
//            System.out.println("//-------------------------//");
//            System.out.println("Choose a number: ");
//
//            Scanner scn = new Scanner(System.in);
//            choice = scn.nextInt();
//            switch (choice) {
//                case 1:
//				String blockchainJson;
//				try {
//					blockchainJson = n.getBlockchainJson();
//					System.out.println("\nThe block chain: ");
//                	System.out.println(blockchainJson);
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} //TODO: error here, doesnt wait the end of process blocks after bat execution	 	
//                    break;
//                case 2:
//                	Scanner scn2 = new Scanner(System.in);
//                    String data = scn2.next();
//                    blockData.add(data);
//                    break;
//            }
//        }
//		
//	}
	
}
