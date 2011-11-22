/**
 * 
 */
package at.theduke.ispy.client;

/**
 * @author theduke
 *
 */
public class ConfigData
{
	public String mongoHost = "localhost";
	public int mongoPort = 27017;
	public String mongoDatabase = "spector";
	
	public String mongoUser = "";
	public String mongoPassword = "";
	
	public String username = "";
	
	public String lastNotificationId;
	
	public ConfigData() {
		username = System.getProperty("user.name");
	}
	
	public boolean isValid() {
		if ((username.length() > 0) && (mongoHost.length() > 0) && (mongoPort != 0) && (mongoDatabase.length() > 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean authRequired() {
		return ((mongoUser.length() > 0) && (mongoPassword.length() > 0));
	}
}
