package at.theduke.ispy.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
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
public class Configuration extends org.eclipse.swt.widgets.Composite {
	
	public Shell shell;
	
	private Application application;
	
	private Label labelHost;
	private Text host;
	
	private Label labelPort;
	private Text port;
	
	private Text password;
	private Label labelPassword;
	
	private Text user;
	private Label labelUser;
	
	private Text database;
	private Label labelCollection;
	
	private Text username;
	private Label labelUsername;

	private Button saveButton;
	
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
	
	private static String getConfigFilePath() {
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".ispy";
		return path;
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI(Application app) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		
		Configuration inst = new Configuration(shell, SWT.NULL, app);
		inst.shell = shell;
		
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public Configuration(org.eclipse.swt.widgets.Composite parent, int style, Application app) {
		super(parent, style);
		this.application = app;
		initGUI();
	}

	private void initGUI() {
		ConfigData config = application.getConfig();
		
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(300, 500);
			{
				saveButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData saveButtonLData = new FormData();
				saveButtonLData.left =  new FormAttachment(0, 1000, 210);
				saveButtonLData.top =  new FormAttachment(0, 1000, 468);
				saveButtonLData.width = 69;
				saveButtonLData.height = 28;
				saveButton.setLayoutData(saveButtonLData);
				saveButton.setText("Save");
				
				saveButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						if (readAndSaveSettings()) {
							shell.dispose();
						} else {
							MessageBox msg = new MessageBox(shell);
							msg.setMessage("Invalid settings");
							msg.open();
						}
						
					}
				});
			}
			{
				FormData passwordLData = new FormData();
				passwordLData.left =  new FormAttachment(0, 1000, 133);
				passwordLData.top =  new FormAttachment(0, 1000, 92);
				passwordLData.width = 112;
				passwordLData.height = 14;
				password = new Text(this, SWT.NONE);
				password.setLayoutData(passwordLData);
				password.setText("hallo");//config.mongoPassword);
			}
			{
				labelPassword = new Label(this, SWT.NONE);
				FormData labelPasswordLData = new FormData();
				labelPasswordLData.left =  new FormAttachment(0, 1000, 12);
				labelPasswordLData.top =  new FormAttachment(0, 1000, 92);
				labelPasswordLData.width = 109;
				labelPasswordLData.height = 14;
				labelPassword.setLayoutData(labelPasswordLData);
				labelPassword.setText("Mongo Password");
			}
			{
				FormData userLData = new FormData();
				userLData.left =  new FormAttachment(0, 1000, 133);
				userLData.top =  new FormAttachment(0, 1000, 72);
				userLData.width = 112;
				userLData.height = 14;
				user = new Text(this, SWT.NONE);
				user.setLayoutData(userLData);
				user.setText(config.mongoUser);
			}
			{
				labelUser = new Label(this, SWT.NONE);
				FormData labelUserLData = new FormData();
				labelUserLData.left =  new FormAttachment(0, 1000, 12);
				labelUserLData.top =  new FormAttachment(0, 1000, 72);
				labelUserLData.width = 95;
				labelUserLData.height = 14;
				labelUser.setLayoutData(labelUserLData);
				labelUser.setText("Mongo User");
			}
			{
				database = new Text(this, SWT.NONE);
				FormData databaseLData = new FormData();
				databaseLData.left =  new FormAttachment(0, 1000, 133);
				databaseLData.top =  new FormAttachment(0, 1000, 52);
				databaseLData.width = 112;
				databaseLData.height = 14;
				database.setLayoutData(databaseLData);
				database.setText(config.mongoDatabase);
			}
			{
				labelCollection = new Label(this, SWT.NONE);
				FormData labelCollectionLData = new FormData();
				labelCollectionLData.left =  new FormAttachment(0, 1000, 12);
				labelCollectionLData.top =  new FormAttachment(0, 1000, 52);
				labelCollectionLData.width = 109;
				labelCollectionLData.height = 14;
				labelCollection.setLayoutData(labelCollectionLData);
				labelCollection.setText("Mongo Collection");
			}
			{
				FormData portLData = new FormData();
				portLData.left =  new FormAttachment(0, 1000, 133);
				portLData.top =  new FormAttachment(0, 1000, 32);
				portLData.width = 112;
				portLData.height = 14;
				port = new Text(this, SWT.NONE);
				port.setLayoutData(portLData);
				port.setText(Integer.toString(config.mongoPort));
			}
			{
				labelPort = new Label(this, SWT.NONE);
				FormData labelPortLData = new FormData();
				labelPortLData.left =  new FormAttachment(0, 1000, 12);
				labelPortLData.top =  new FormAttachment(0, 1000, 32);
				labelPortLData.width = 98;
				labelPortLData.height = 14;
				labelPort.setLayoutData(labelPortLData);
				labelPort.setText("Mongo Port");
			}
			{
				FormData hostLData = new FormData();
				hostLData.left =  new FormAttachment(0, 1000, 133);
				hostLData.top =  new FormAttachment(0, 1000, 12);
				hostLData.width = 112;
				hostLData.height = 14;
				host = new Text(this, SWT.NONE);
				host.setLayoutData(hostLData);
				host.setText(config.mongoHost);
			}
			{
				labelHost = new Label(this, SWT.NONE);
				FormData labelHostLData = new FormData();
				labelHostLData.left =  new FormAttachment(0, 1000, 12);
				labelHostLData.top =  new FormAttachment(0, 1000, 12);
				labelHostLData.width = 103;
				labelHostLData.height = 14;
				labelHost.setLayoutData(labelHostLData);
				labelHost.setText("Mongo Host");
			}
			{
				FormData hostData = new FormData();
				hostData.left =  new FormAttachment(0, 1000, 133);
				hostData.top =  new FormAttachment(0, 1000, 155);
				hostData.width = 103;
				hostData.height = 14;
				
				username = new Text(this, SWT.NONE);
				username.setLayoutData(hostData);
				username.setText(config.username);
			}
			{
				labelUsername = new Label(this, SWT.NONE);
				FormData labelUsernameData = new FormData();
				labelUsernameData.left =  new FormAttachment(0, 1000, 7);
				labelUsernameData.top =  new FormAttachment(0, 1000, 159);
				labelUsernameData.width = 114;
				labelUsernameData.height = 17;
				
				labelUsername.setLayoutData(labelUsernameData);
				labelUsername.setText("Username");
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean readAndSaveSettings() {
		ConfigData data = buildConfigData();
		if (data.isValid()) {
			writeConfiguration(data);
			application.initializeMongo(data);
			return true;
		} else {
			return false;
		}
	}
	
	private ConfigData buildConfigData() {
		ConfigData data = new ConfigData();
		
		data.mongoHost = host.getText();
		data.mongoPort = Integer.parseInt(port.getText());
		data.mongoDatabase = database.getText();
		data.mongoUser = user.getText();
		data.mongoPassword = password.getText();
		
		data.username = username.getText();
		
		return data;
	}

}
