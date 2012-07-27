/**
 * 
 */
package at.theduke.spector.client;

import java.net.UnknownHostException;

/**
 * @author theduke
 *
 */
public class ConfigData
{
	public boolean pushToServer = true;
	
	public String serverHost = "localhost";
	public int serverPort = 27017;
	
	public String hostname = "";
	public String username = "";
	
	public String authKey = "";
	
	public String lastNotificationId;
	
	public boolean persistLocally = true;
	
	public boolean guiEnabled = true;
	
	public ConfigData() {
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
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
