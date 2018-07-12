import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class MasterServer {
	
	//public static ArrayList<String> blockData = new ArrayList<String>();
	
	public MasterServer(int port) {
        try {
            Node n = new Node();
            NodeInterface node = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Naming.rebind("rmi://localhost:" + port + "/MasterNode", node);
            System.out.println("MasterNode is starting...");
            System.out.println("MasterNode started");
            System.out.println("Waiting for blocks from slave node ...");
            while(true) {
            	 n.processBlocks();
            	 System.out.println(n.getBlockCount());
            	
            }
           
    		
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
        MasterServer s = new MasterServer(p);
    }
	
}
