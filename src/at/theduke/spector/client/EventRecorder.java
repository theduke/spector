package at.theduke.spector.client;

import java.awt.Toolkit;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import at.theduke.spector.Session;

public class EventRecorder implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener
{
	GlobalScreen screen;
	
	Session session;
	
	public static final int IDLE_TIME_LIMIT = 120 * 1000;
	public static final double MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT = 0.05;
	
	public EventRecorder()
	{
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
		
		screen = GlobalScreen.getInstance();
		
		screen.addNativeKeyListener(this);
		screen.addNativeMouseListener(this);
		screen.addNativeMouseMotionListener(this);
	}
	
	public Session startSession(ConfigData config)
	{
		session = new Session();
		session.start(config);
		
		return session;
	}
	
	protected void onEvent(NativeInputEvent e) {
		/* @todo  use actual event time instead of current time, upstream fix required*/
		long time = System.currentTimeMillis();
		
		long timeSpan = time - session.endTime; 
		if (timeSpan >= IDLE_TIME_LIMIT)
		{
			session.idleTime += timeSpan;
		}
		
		session.endTime = time;
	}
	
	protected void onEvent(NativeInputEvent e, String eventType, String eventData)
	{
		/* @todo  use actual event time instead of current time, upstream fix required*/
		long time = System.currentTimeMillis();
		
		String logEntry = eventType + ":" + eventData + "|" + Long.toString(time - session.startTime) + "\n";
		session.log += logEntry;
		
		onEvent(e);
	}
	
	 /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyPressed(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void nativeKeyPressed(NativeKeyEvent e) {
    	int keyCode = e.getKeyCode();
    	
    	onEvent(e, "kp", Integer.toString(keyCode));
    	
    	boolean isRegular = (keyCode >= 48 && keyCode <= 90) || (keyCode >= 186 && keyCode <= 222); 
    	
    	session.keyCodes.add(keyCode);
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mousePressed(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMousePressed(NativeMouseEvent e) {
    	String data = e.getButton() + "," + e.getX() + "," + e.getY();
    
    	onEvent(e, "mp", data);
    	session.mouseClicks.add(e);
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseMotionListener#mouseMoved(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMouseMoved(NativeMouseEvent e) {
    	// ensure that idle time is reset, but do not log any data yet
    	onEvent(e);
    	
    	boolean shouldLog = false;
    	
    	// determine whether to log the move
    	int size = session.mouseMoves.size();
    	if (size > 0)
    	{
    		NativeMouseEvent lastEvent = session.mouseMoves.get(size - 1);
    		
    		int deltaX = Math.abs(e.getX() - lastEvent.getX());
    		if ((float) deltaX / session.screenResolution.width > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT)
    		{
    			shouldLog = true;
    		}
    		else
    		{
    			int deltaY = Math.abs(e.getY() - lastEvent.getY());
    			if ((float) deltaY / session.screenResolution.height > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT) shouldLog = true;
    		}
    	}
    	else
    	{
    		shouldLog = true;
    	}
    	
    	if (shouldLog) {
    		String data = e.getX() + "," + e.getY();
        	onEvent(e, "mm", data);
    		session.mouseMoves.add(e);
    	}
    }

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
