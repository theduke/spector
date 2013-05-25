package at.theduke.spector.client.events;

import java.awt.Dimension;
import java.util.Date;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import at.theduke.spector.client.Session;

public class JNativeHookWatcher extends BaseEventWatcher implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener
{
	public static final double MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT = 0.05;
	
	GlobalScreen screen;
	
	private NativeMouseEvent lastMouseMove = null;
	
	@Override
	public void connect(Session session)
	{
		this.session = session;
		
		// Establish mouse and keyboard event listeners
		// with the jNativeHook library.
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			logger.error("There was a problem registering the native hook.");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		
		screen = GlobalScreen.getInstance();
		
		screen.addNativeKeyListener(this);
		screen.addNativeMouseListener(this);
		screen.addNativeMouseMotionListener(this);
		
		logger.info("JNativeHookWatcher has connected.");
	}
	
	 /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyPressed(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void nativeKeyPressed(NativeKeyEvent e) {
    	//int keyCode = e.getKeyCode();
    	//session.recordKeyDown(keyCode, new Date(e.getWhen()));    	
    }

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		int keyCode = e.getKeyCode();
		String descr = NativeKeyEvent.getKeyText(e.getKeyCode());
    	session.recordKeyUp(keyCode, descr, new Date(e.getWhen()));
	}
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mousePressed(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMousePressed(NativeMouseEvent e) {
    	session.recordMouseDown(e.getButton(), e.getX(), e.getY(), new Date(e.getWhen()));
    }
    
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		session.recordMouseUp(e.getButton(), e.getX(), e.getY(), new Date(e.getWhen()));
	}
    
    /**
     * @see org.jnativehook.mouse.NativeMouseMotionListener#mouseMoved(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMouseMoved(NativeMouseEvent e) {
      	// determine whether to log the move.
    	// Moves are only logged if either the x or y position
    	// is at least MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT 
    	// percent away from the last recorded position to avoid
    	// flooding.
    	
    	boolean shouldLog = false;
    	
    	if (lastMouseMove == null) {
    		shouldLog = true;
    	}
    	else {
    		Dimension resolution = session.getScreenResolution();
    		
          	if (lastMouseMove != null) {
	      		int deltaX = Math.abs(e.getX() - lastMouseMove.getX());
	      		if ((float) deltaX / resolution.width > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT) {
	      			shouldLog = true;
	      		}
	      		else {
	      			int deltaY = Math.abs(e.getY() - lastMouseMove.getY());
	      			if ((float) deltaY / resolution.height > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT) {
	      				 shouldLog = true;
	      			}
	      		}
          	}
      	}
      	
      	if (shouldLog) {
      		session.recordMouseMove(e.getX(), e.getY(), new Date(e.getWhen()));
      		lastMouseMove = e;
      	}
    }

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		/**
		 * @TODO implement drag event!
		 */
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
    	/*
    	System.out.println(e.getRawCode());
    	System.out.println(e.getKeyCode());
    	
    	aber bitte mit sahne#'!@@²ł@~~#@'@äÄöläöülöä
    	System.out.println(e.getKeyLocation());
    	System.out.println(e.getModifiers());
    	*/
    	//System.out.println(e.getKeyChar());
    	//System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
    	//System.out.println(NativeKeyEvent.getModifiersText(e.getModifiers()));
    	//System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
		
		String typed = new String(Character.toChars(e.getKeyChar()));
		session.recordKeyPress(typed, new Date(e.getWhen()));
	}
}
