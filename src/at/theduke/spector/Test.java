package at.theduke.spector;

import java.io.IOException;
import java.util.Map;

import at.theduke.spector.client.events.EventWatcher;
import at.theduke.spector.client.events.JNativeHookWatcher;
import at.theduke.spector.client.events.PolledEventWatcher;
import at.theduke.spector.client.events.SystemWatcher;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(Utils.getCommandOutput("ls /"));
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
