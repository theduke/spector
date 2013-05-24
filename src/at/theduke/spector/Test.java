package at.theduke.spector;

import java.io.IOException;

import at.theduke.spector.client.Pusher.FilePusher;
import at.theduke.spector.client.Pusher.HttpPusher;
import at.theduke.spector.client.Pusher.StdOutPusher;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testHttpPusher();
		//testFilePusher();
		testStdOutPusher();
	}
	
	public static void testStdOutPusher() {
		Session s = new Session();
		
		StdOutPusher p = new StdOutPusher();
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testHttpPusher() {
		Session s = new Session();
		
		HttpPusher p = new HttpPusher("https://owncloud.theduke.at/dump.php", 443, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testFilePusher() {
		Session s = new Session();
		
		FilePusher p = new FilePusher("/home/theduke/tmp/test.data");
		p.onSessionStart("11");
		
		for (int i = 0; i < 200; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
}
