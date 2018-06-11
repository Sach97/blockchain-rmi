import java.rmi.*;
import java.rmi.server.*;   
 
public class Server {
	   public static void main (String[] argv) {
		   try {
			   System.setSecurityManager(new RMISecurityManager());

			   Addition Hello = new Addition();			   		   
			   Naming.rebind("rmi://localhost/ABC", Hello);
 
			   System.out.println("Addition Server is ready.");
			   }catch (Exception e) {
				   System.out.println("Addition Server failed: " + e);
				}
		   }
}
