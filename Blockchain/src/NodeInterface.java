import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NodeInterface extends Remote {
	
	public String getBlockchainJson() throws RemoteException;
	public void addTransactionToPool(String transaction) throws RemoteException;
	public void processBlocks() throws RemoteException;
	public Boolean isChainValid() throws RemoteException;
	public int getCount() throws RemoteException;
	public String getTransactionFromPool() throws RemoteException;
	public int getDifficulty() throws RemoteException;
	public ArrayList<Block> getBlockchain() throws RemoteException;
	public String getHash() throws RemoteException;
	public void setReady() throws RemoteException;
	public boolean isReady() throws RemoteException;

}
