//import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
 
public class SlaveServer {
  
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
 
        String reg_host = "localhost";
        int port = 1099; //default port for SlaveNode
        //int masternode_port = 1099; //default port for MasterNode
        if (args.length == 1) {
        	port = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            reg_host = args[0];
            port = Integer.parseInt(args[1]);
        }
 
        NodeInterface masterNode;
        NodeInterface slaveNode;

		// https://stackoverflow.com/questions/7735112/two-rmi-registries-on-the-same-pc-notboundexception
        
        NodeInterface m = (NodeInterface) Naming.lookup("rmi://" + reg_host + ":" + port + "/MasterNode");
        masterNode = (NodeInterface) UnicastRemoteObject.exportObject(m, 0);
        
        
        Node s = new Node("slave",port);
        slaveNode = (NodeInterface) UnicastRemoteObject.exportObject(s, 0);
		Naming.rebind("rmi://localhost:" + port + "/SlaveNode", slaveNode);
		System.out.println("SlaveNode is starting...");
        System.out.println(masterNode.getStatus()+" From MasterNode registry");
        System.out.println(slaveNode.getStatus()+" From SlaveNode registry");
        System.out.println(slaveNode.getStatus(masterNode)+" From SlaveNode Registry");
        System.out.println(slaveNode.getStatusFromLookup()+" From SlaveNode Registry without passsing the object instance");
        
	    // send transaction to masterNode
    	
      
    
        
		}
    
}

