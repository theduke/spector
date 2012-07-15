package at.theduke.spector.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import at.theduke.spector.Session;

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
	
	private Application application;
	
	public static void showGUI(Application app) {
		
	}
	
	public static ConfigData readConfiguration() {
		String content = "";
		
		try {
			String line = null;
			
			BufferedReader reader = new BufferedReader(new  FileReader(getConfigFilePath()));
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
		String path = getConfigFilePath();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			
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
	
	private static String getConfigFilePath() {
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".ispy";
		return path;
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
