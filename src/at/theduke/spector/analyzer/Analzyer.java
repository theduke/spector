package at.theduke.spector.analyzer;

import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import at.theduke.spector.Event;

public class Analzyer {

	protected ArrayList<Event> events;

	protected HashMap<Integer, Integer> pressedKeys = new HashMap<Integer, Integer> ();
	protected String text = "";

	public static void main(String[] args) {
		InputContext context = InputContext.getInstance();
		System.out.println(context.getInputMethodControlObject());
	}

	protected void analyze(ArrayList<Event> events) {
		for (Event event : events) {
			switch (event.getType()) {
				case Event.EVENT_KEYPRESS:
					int keycode = Integer.parseInt(event.getData());
					if (!pressedKeys.containsKey(keycode)) {
						pressedKeys.put(keycode, 0);
					}

					pressedKeys.put(keycode, pressedKeys.get(keycode) + 1);
					break;
			}
		}
	}

	protected ArrayList<Event> readSessionFile(String path) {
		ArrayList<Event> events = new ArrayList<Event>();


		FileInputStream fstream;
		try {
			fstream = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			return events;
		}
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;

		try {
			while ((line = br.readLine()) != null)   {
				Event event = Event.parseEvent(line);
				if (event != null) events.add(event);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return events;

	}
}
