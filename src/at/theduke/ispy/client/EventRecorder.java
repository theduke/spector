package at.theduke.ispy.client;

import java.awt.Toolkit;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import at.theduke.ispy.Session;

public class EventRecorder implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener
{
	GlobalScreen screen;
	
	Session session;
	
	public static final int IDLE_TIME_LIMIT = 120 * 1000;
	public static final int MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT = 5;
	
	public EventRecorder() 
	{
		screen.addNativeKeyListener(this);
		screen.addNativeMouseListener(this);
		screen.addNativeMouseMotionListener(this);
	}
	
	public Session startSession()
	{
		session = new Session();
		session.startTime = session.endTime = System.currentTimeMillis();
		session.screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		
		return session;
	}
	
	protected void onEvent(NativeInputEvent e)
	{
		long timeSpan = e.getWhen() - session.endTime; 
		if (timeSpan >= IDLE_TIME_LIMIT)
		{
			session.idleTime += timeSpan;
		}
		
		session.endTime = e.getWhen();
	}
	
	 /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyPressed(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void keyPressed(NativeKeyEvent e) {
    	onEvent(e);
    	
    	session.keyLog += NativeKeyEvent.getKeyText(e.getKeyCode());
    }
    
    /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyReleased(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void keyReleased(NativeKeyEvent e) {
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mousePressed(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void mousePressed(NativeMouseEvent e) {
    	onEvent(e);
    	session.mouseClicks.add(e);
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mouseReleased(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void mouseReleased(NativeMouseEvent e) {
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseMotionListener#mouseMoved(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void mouseMoved(NativeMouseEvent e) {
    	onEvent(e);
    	
    	boolean shouldLog = false;
    	
    	// determine whether to log the move
    	int size = session.mouseMoves.size();
    	if (size > 0)
    	{
    		NativeMouseEvent lastEvent = session.mouseMoves.get(size - 1);
    		
    		int deltaX = Math.abs(e.getX() - lastEvent.getX());
    		if (deltaX / session.screenResolution.width > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT)
    		{
    			shouldLog = true;
    		}
    		else
    		{
    			int deltaY = Math.abs(e.getY() - lastEvent.getY());
        		if (deltaY / session.screenResolution.height > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT) shouldLog = true;
    		}
    	}
    	else
    	{
    		shouldLog = true;
    	}
    	
    	if (shouldLog) session.mouseMoves.add(e);
    }
}
