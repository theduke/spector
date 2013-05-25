package at.theduke.spector.session;

import java.util.HashMap;

import com.google.gson.Gson;

public class Session {
	
	String id;
	
	String user;
	
	String host;
	
	String start;
	
	String end;
	
	long idletime = 0;
	
	String resolution;
	
	HashMap<Integer, Long> keypresses = new HashMap<Integer, Long>();
	
	HashMap<Integer, Long> mousepresses = new HashMap<Integer, Long>();
	
	long mousedistance = 0;
	
	String textlog = "";
	
	long filesCreated = 0;
	
	/**
	 * Not unique! A single file that s modified twice
	 * will be counted double.
	 */
	long filesModified = 0;
	
	long filesDeleted = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public long getIdletime() {
		return idletime;
	}

	public void setIdletime(long idletime) {
		this.idletime = idletime;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public HashMap<Integer, Long> getKeypresses() {
		return keypresses;
	}

	public void setKeypresses(HashMap<Integer, Long> keypresses) {
		this.keypresses = keypresses;
	}
	
	public void addKeypress(int keycode) {
		Integer code = new Integer(keycode);
		if (!keypresses.containsKey(code)) {
			keypresses.put(code, 1l);
		}
		else {
			keypresses.put(code, keypresses.get(code) + 1);
		}
	}
	
	public void clearKeypresses() {
		keypresses = new HashMap<Integer, Long>();
	}

	public HashMap<Integer, Long> getMousepresses() {
		return mousepresses;
	}

	public void setMousepresses(HashMap<Integer, Long> mousepresses) {
		this.mousepresses = mousepresses;
	}
	
	public void addMousepress(int keycode) {
		Integer code = new Integer(keycode);
		if (!mousepresses.containsKey(code)) {
			mousepresses.put(code, 1l);
		}
		else {
			mousepresses.put(code, mousepresses.get(code) + 1);
		}
	}
	
	public void clearMousepresses() {
		mousepresses = new HashMap<Integer, Long>();
	}

	public long getMousedistance() {
		return mousedistance;
	}

	public void setMousedistance(long mousedistance) {
		this.mousedistance = mousedistance;
	}
	
	public void incrementMouseDistance(int distance) {
		mousedistance += distance;
	}

	public String getTextlog() {
		return textlog;
	}

	public void setTextlog(String textlog) {
		this.textlog = textlog;
	}
	
	public void appendTextLog(String str) {
		textlog += str;
	}

	public long getFilesCreated() {
		return filesCreated;
	}

	public void setFilesCreated(long filesCreated) {
		this.filesCreated = filesCreated;
	}
	
	public void incrementFilesCreated(long inc) {
		filesCreated += inc;
	}

	public long getFilesModified() {
		return filesModified;
	}

	public void setFilesModified(long filesModified) {
		this.filesModified = filesModified;
	}
	
	public void incrementFilesModified(long inc) {
		filesModified += inc;
	}

	public long getFilesDeleted() {
		return filesDeleted;
	}

	public void setFilesDeleted(long filesDeleted) {
		this.filesDeleted = filesDeleted;
	}
	
	public void incrementFilesDeleted(long inc) {
		filesDeleted += inc;
	}
	
	public void clearAggregateData() {
		idletime = 0;
		clearKeypresses();
		clearMousepresses();
		mousedistance = 0;
		textlog = "";
		filesCreated = 0;
		filesModified = 0;
		filesDeleted = 0;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
}
