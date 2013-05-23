package at.theduke.spector.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "mm:784,688|29351\n";
		
		Pattern p = Pattern.compile("(.*)\\:(.*?)\\|([0-9]+)\\n");
		Matcher m = p.matcher(s);
		
		m.find();
		
		System.out.println(m.group(3));
	}

}
