package at.theduke.spector;

import java.io.IOException;

import at.theduke.spector.client.Pusher.HttpPusher;

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
		
		testHttpPusher();
		
	}
	
	public static void testHttpPusher() {
		Session s = new Session();
		
		HttpPusher p = new HttpPusher("https://localhost/dump.php", 443, true);
		p.onSessionStart("11");
		
		for (int i = 0; i < 2; i++) {
			p.pushEvent(new Event("test", "testdata", s));
		}
	}

}
