package at.theduke.spector;

import at.theduke.spector.client.Pusher.FilePusher;
import at.theduke.spector.client.Pusher.HttpPusher;
import at.theduke.spector.client.Pusher.SocketPusher;
import at.theduke.spector.client.Pusher.StdOutPusher;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testHttpPusher();
		//testFilePusher();
		testStdOutPusher();
		//testSocketPusher();
	}
	
	public static void testStdOutPusher() {
		Session s = new Session();
		
		StdOutPusher p = new StdOutPusher();
		p.setDoGzip(true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testHttpPusher() {
		Session s = new Session();
		
		HttpPusher p = new HttpPusher("https://localhost/dump.php", 443, true, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testSocketPusher() {
		Session s = new Session();
		
		SocketPusher p = new SocketPusher("localhost", 3333, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testFilePusher() {
		Session s = new Session();
		
		FilePusher p = new FilePusher("/home/theduke/tmp", true, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 200; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
}
