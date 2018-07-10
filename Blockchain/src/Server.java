import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server {
	
	public Server(int port) {
        try {
            Node n = new Node();
            NodeInterface node = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Naming.rebind("rmi://localhost:" + port + "/Node", node);
            System.out.println("Node is starting...");
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

        // create new server for auction system
        int p = 1099; // default port number

        if (args.length == 1) {
            p = Integer.parseInt(args[0]); // custom port number
        }
        new Server(p);
    }
	
}
