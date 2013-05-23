package at.theduke.spector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
	
	/**
	 * Retrieve the current time as an ISO8601 string.
	 */
	public static String getIso8601Time(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	    df.setTimeZone(tz);
	    String nowAsISO = df.format(date);
	    
	    return nowAsISO;
	}

}
