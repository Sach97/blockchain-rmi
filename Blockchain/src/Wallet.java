import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
	public Wallet() {
		generateKeyPair();
	}

	
	/**
	Generate an Elliptic Curve KeyPair. This methods makes and sets our Public and Private keys
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	public void generateKeyPair() {
		try {
			
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			
			//Initialize the key generator  and generate a key pair
			keyGen.initialize(ecSpec,random);
			KeyPair keyPair = keyGen.generateKeyPair();
			
			//Set the private and public keys from key pair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
			
		} catch (Exception e){
			throw new RuntimeException(e);
			}
		
	}
}
