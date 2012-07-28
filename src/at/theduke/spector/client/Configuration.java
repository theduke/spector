package at.theduke.spector.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	
	public static void showGUI(Application app) {
		
	}
	
	public static ConfigData readConfiguration() {
		String content = "";
		
		try {
			String line = null;
			
			File configFile = getConfigFilePath();
			
			if (!configFile.exists()) {
				throw new FileNotFoundException();
			}
			
			BufferedReader reader = new BufferedReader(new  FileReader(configFile));
			while ((line = reader.readLine()) != null) {
				content += line;
			}
		} catch (FileNotFoundException e) {
			return new ConfigData();
		} catch (IOException e) {
			return new ConfigData();
		}
		
		Gson gson = new Gson();
		ConfigData data = gson.fromJson(content, ConfigData.class);
		
		return data;
	}
	
	public static boolean writeConfiguration(ConfigData data) {
		File file = getConfigFilePath();
		
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
	
	public static String getDataPath() {
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".spector-client";
		
		File file = new File(path);
		if (!file.exists()) file.mkdir();
		
		return path;
	}
	
	public static String getSessionDataPath(String id) {
		String path = getDataPath() + System.getProperty("file.separator") + id + ".session";
		
		return path;
	}
	
	private static File getConfigFilePath() {
		String confPath = ".spector_client_config";
		
		// first, try a user specific config in home dir
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + confPath;
		File file = new File(path);
		if (file.exists()) return file;
		
		// then check to see if a config file was bundled with the executable
		path = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path += confPath;
		file = new File(path);
		return file;
	}

	private boolean readAndSaveSettings() {
		ConfigData data = buildConfigData();
		
		if (data.isValid()) {
			writeConfiguration(data);
			return true;
		} else {
			return false;
		}
	}
	
	private ConfigData buildConfigData() {
		ConfigData data = new ConfigData();
		
		/*
		data.mongoHost = host.getText();
		data.mongoPort = Integer.parseInt(port.getText());
		data.mongoDatabase = database.getText();
		data.mongoUser = user.getText();
		data.mongoPassword = password.getText();
		
		data.username = username.getText();
		*/
		
		return data;
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
		
	}
}
