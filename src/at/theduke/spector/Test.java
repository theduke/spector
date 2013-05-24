package at.theduke.spector;

import at.theduke.spector.eventwriter.FileWriter;
import at.theduke.spector.eventwriter.HttpWriter;
import at.theduke.spector.eventwriter.SocketWriter;
import at.theduke.spector.eventwriter.StdOutWriter;

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
		
		StdOutWriter p = new StdOutWriter();
		p.setDoGzip(true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testHttpPusher() {
		Session s = new Session();
		
		HttpWriter p = new HttpWriter("https://localhost/dump.php", 443, true, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testSocketPusher() {
		Session s = new Session();
		
		SocketWriter p = new SocketWriter("localhost", 3333, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testFilePusher() {
		Session s = new Session();
		
		FileWriter p = new FileWriter("/home/theduke/tmp", true, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 200; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
}
