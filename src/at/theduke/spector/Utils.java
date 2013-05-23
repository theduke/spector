package at.theduke.spector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String getCommandOutput(String cmd) throws InterruptedException, IOException {
		Runtime run = Runtime.getRuntime() ;
		
		Process pr;
		pr = run.exec(cmd) ;
		pr.waitFor();
		
		BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
		String line;
		
		StringBuilder builder = new StringBuilder();
		
		while ((line = buf.readLine()) != null) {
			builder.append(line);
			builder.append("\n");
		}
		
		return builder.toString();
	}

}
