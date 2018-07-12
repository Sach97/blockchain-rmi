import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class SlaveServer {
	
    public static void main(String[] args) {

        String reg_host = "localhost";
        int slavenode_port = 1098; //default port for SlaveNode
		int masternode_port = 1099; //default port for MasterNode
        if (args.length == 1) {
        	slavenode_port = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            reg_host = args[0];
            slavenode_port = Integer.parseInt(args[1]);
        }


        NodeInterface masterNode;
        NodeInterface slaveNode;
        
		try {
			masterNode = (NodeInterface) Naming.lookup("rmi://" + reg_host + ":" + masternode_port + "/MasterNode");
			Node n = new Node(masterNode);
            slaveNode = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Naming.rebind("rmi://localhost:" + slavenode_port + "/SlaveNode", slaveNode);
            System.out.println("SlaveNode is starting...");
            System.out.println("SlaveNode started");
            System.out.println("Waiting for transactions to process ...");
            while(true) {
            	slaveNode.processTransactions(masterNode);
           	 	System.out.println(slaveNode.getTxCount());
           	
           }
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
}