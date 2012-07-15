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
	public String serverHost = "localhost";
	public int serverPort = 27017;
	
	public String hostname = "";
	public String username = "";
	
	public String authKey = "";
	
	public String lastNotificationId;
	
	public ConfigData() {
		hostname = java.net.InetAddress.getLocalHost().getHostName();
		username = System.getProperty("user.name");
	}
	
	public boolean isValid() {
		if ((hostname.length() > 0)
			&&(username.length() > 0) 
			&& (serverHost.length() > 0) 
			&& (serverPort > 0)) { 
			return true;
		} else {
			return false;
		}
	}
	
	public boolean authRequired() {
		return authKey != null && authKey.length() > 0; 
	}
}
