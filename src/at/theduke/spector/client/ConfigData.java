/**
 * 
 */
package at.theduke.spector.client;

import java.util.ArrayList;

import at.theduke.spector.client.config.PathSpec;

/**
 * @author theduke
 *
 */
public class ConfigData
{
	public String dataPath = "";
	
	public boolean pushToServer = false;
	public boolean pushToFile = true;
	
	public String serverHost = "localhost";
	public int serverPort = 3333;
	
	public String hostname = "";
	public String username = "";
	
	public String authKey = "";
	
	public String lastNotificationId;
	
	public boolean guiEnabled = true;
	
	public ArrayList<PathSpec> monitoredPaths = new ArrayList<PathSpec>();
}
