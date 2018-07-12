import java.security.MessageDigest;

import com.google.gson.GsonBuilder;

public class StringUtil {
	
	/**
	Takes a string and applies SHA256 algorithm to it, and returns the generated signature as a string.
	@param the parameters used by the method
	@return the value returned by the method
	@throws what kind of exception does this method throw
	*/
	
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for(int i=0; i<hash.length;i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Short hand helper to turn Object into a json string
		public static String getJson(Object o) {
			return new GsonBuilder().setPrettyPrinting().create().toJson(o);
		}

}