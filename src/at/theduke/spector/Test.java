package at.theduke.spector;

import at.theduke.spector.client.Session;
import at.theduke.spector.eventwriter.ElasticSearchWriter;
import at.theduke.spector.eventwriter.FileWriter;
import at.theduke.spector.eventwriter.HttpWriter;
import at.theduke.spector.eventwriter.PipeWriter;
import at.theduke.spector.eventwriter.SocketWriter;
import at.theduke.spector.eventwriter.StdOutWriter;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testHttpPusher();
		//testFilePusher();
		//testStdOutPusher();
		//testSocketPusher();
		testElasticSearchPusher();
	}
	
	public static void testStdOutPusher() {
		Session s = new Session(new PipeWriter());
		
		StdOutWriter p = new StdOutWriter();
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testHttpPusher() {
		Session s = new Session(new PipeWriter());
		
		HttpWriter p = new HttpWriter("http://localhost/dev/test/dump.php", 80, false, false);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testSocketPusher() {
		Session s = new Session(new PipeWriter());
		
		SocketWriter p = new SocketWriter("localhost", 5555, false);
		p.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
	
	public static void testElasticSearchPusher() {
		Session s = new Session(new PipeWriter());
		
		ElasticSearchWriter writer = new ElasticSearchWriter();
		writer.onSessionStart("11");
		
		for (int i = 0; i < 300; i++) {
			writer.pushEvent(new Event("test", "testdata", s));
		}
		
		writer.onSessionStop();
	}
	
	public static void testFilePusher() {
		Session s = new Session(new PipeWriter());
		
		FileWriter p = new FileWriter("/home/theduke/tmp", true, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 200; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
		
		p.onSessionStop();
	}
}
