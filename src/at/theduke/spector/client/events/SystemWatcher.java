package at.theduke.spector.client.events;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.client.Application;
import at.theduke.spector.client.Session;

public class SystemWatcher implements EventWatcher, PolledEventWatcher {
	static final Logger logger = Application.getLogger();
	protected Session session;
	
	protected String lastResolution = "";
	
	@Override
	public void connect(Session session) {
		this.session = session;
	}
	
	public void pollEvents() {
		checkScreenResolution();
	}
	
	protected void checkScreenResolution() {
		Dimension screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		String resolution = screenResolution.width + "," + screenResolution.height;
		
		if (resolution != lastResolution) {
			session.logEvent(Event.EVENT_SCREEN_RESOLUTION_SET, resolution);
			screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		}
	}
	
	protected void checkKeyboardLayout() {
		if (System.getProperty("os.name") == "Linux") {
			String command = " setxkbmap -print";
			try {
				String output = Utils.getCommandOutput(command);
			} catch (InterruptedException | IOException e) {
				logger.error("Could not determine keyboard layout", e);
				return;	
			}
		}
	}
}
