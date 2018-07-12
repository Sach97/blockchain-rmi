import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NodeInterface extends Remote {
	
	public String getBlockchainJson(NodeInterface masterNode) throws RemoteException;
	public void addBlockToPool(Block block) throws RemoteException;
	public void broadcastBlock(String newData, NodeInterface masterNode) throws RemoteException;
	public void processBlocks() throws RemoteException;
	//public void processTransactions(NodeInterface masterNode) throws RemoteException;
	public void processTransactions() throws RemoteException, MalformedURLException, NotBoundException;
	public Boolean isChainValid() throws RemoteException;
	public int getBlockCount() throws RemoteException;
	public int getTxCount() throws RemoteException;
	public Block getBlockFromPool() throws RemoteException;
	public int getDifficulty() throws RemoteException;
	public ArrayList<Block> getBlockchain() throws RemoteException;
	public String getHash() throws RemoteException;
	//public void setReady() throws RemoteException;
	//public boolean isReady() throws RemoteException;
	public String getBlockHashById(int blockId) throws RemoteException;
	public String getBlockDataById(int blockId) throws RemoteException;
	public void sendTransaction(String data) throws RemoteException, MalformedURLException, NotBoundException;
	//public void sendTransaction(String data, NodeInterface masterNode) throws RemoteException;
	public String getStatus() throws RemoteException;
	public String getStatus(NodeInterface masterNode) throws RemoteException;
	public String getStatusFromLookup() throws RemoteException, MalformedURLException, NotBoundException;
	public int getBlockchainSize() throws RemoteException;


}
