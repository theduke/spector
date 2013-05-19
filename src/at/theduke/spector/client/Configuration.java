package at.theduke.spector.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import at.theduke.spector.client.config.PathSpec;

import com.google.gson.Gson;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Configuration {
	
	protected ConfigData data = null;
	protected String configPath = null;
	
	private static ConfigData readConfiguration(String configPath) {
		String content = "";
		
		try {
			String line = null;
			
			File configFile = new File(configPath);
			
			if (!configFile.exists()) {
				throw new FileNotFoundException();
			}
			
			BufferedReader reader = new BufferedReader(new  FileReader(configFile));
			while ((line = reader.readLine()) != null) {
				content += line;
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			return new ConfigData();
		} catch (IOException e) {
			return new ConfigData();
		}
		finally {
			
		}
		
		
		Gson gson = new Gson();
		ConfigData data = gson.fromJson(content, ConfigData.class);
		
		// Fill in default values.
		
		
		return data;
	}
	
	private static boolean writeConfiguration(String configPath, ConfigData data) {
		File file = new File(configPath);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			Gson gson = new Gson();
			writer.write(gson.toJson(data));
			
			writer.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public Configuration() {
		configPath = getDefaultConfigPath();
	}
	
	public boolean isValid() {
		if ((data.hostname.length() > 0)
			&&(data.username.length() > 0) 
			&& (data.serverHost.length() > 0) 
			&& (data.serverPort > 0)) { 
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean load() {
		return load(configPath);
	}

	/**
	 * 
	 * @param configPath
	 * @return
	 */
	public boolean load(String path) {
		data = readConfiguration(path);
		
		// Set default values.
		
		if (data.pushToFile) {
			if (data.dataPath.length() < 1) {
				data.dataPath = getDefaultDataPath();
			}
		}
		
		// Set default watched directories.
		if (data.monitoredPaths.size() < 1) {
			String homePath = System.getProperty("user.home");
			PathSpec spec = new PathSpec(homePath, 8, true);
			data.monitoredPaths.add(spec);
		}
		
		// Determine hostname.
		try {
			data.hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// Get current user's name.
		if (data.username.length() < 1) {
			data.username = System.getProperty("user.name");
		}
		
		return true;
	}
	
	public boolean save() {
		return writeConfiguration(configPath, data);
	}
	
	protected String getDefaultConfigPath() {
		String confPath = ".spector_client_config";
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + confPath;
		
		return path;
	}
	
	public String getDefaultDataPath() {
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".spector-client";
		return path;
	}
	
	public String getSessionDataPath(String id) {
		String path = data.dataPath + System.getProperty("file.separator") + id + ".session";	
		return path;
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
		
	}
	
	/**
	 * Getters and setters.
	 */
	
	public ConfigData getData() {
		return data;
	}
	
	public void setData(ConfigData data) {
		this.data = data;
	}
	
	public String getDataPath() {
		return data.dataPath;
	}

	public void setDataPath(String dataPath) {
		this.data.dataPath = dataPath;
	}

	public boolean isPushToFile() {
		return data.pushToFile;
	}

	public void setPushToFile(boolean pushToFile) {
		this.data.pushToFile = pushToFile;
	}

	public String getUsername() {
		return data.username;
	}

	public void setUsername(String username) {
		this.data.username = username;
	}
	
	public String getHostname() {
		return data.hostname;
	}

	public void setHostname(String hostname) {
		this.data.hostname = hostname;
	}	

	public String getLastNotificationId() {
		return data.lastNotificationId;
	}

	public void setLastNotificationId(String lastNotificationId) {
		this.data.lastNotificationId = lastNotificationId;
	}

	public boolean isGuiEnabled() {
		return data.guiEnabled;
	}

	public void setGuiEnabled(boolean guiEnabled) {
		this.data.guiEnabled = guiEnabled;
	}

	public ArrayList<PathSpec> getMonitoredPaths() {
		return data.monitoredPaths;
	}

	public void setMonitoredPaths(ArrayList<PathSpec> monitoredPaths) {
		this.data.monitoredPaths = monitoredPaths;
	}
}
