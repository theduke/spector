package at.theduke.spector.client;

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
	
	 /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyPressed(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void nativeKeyPressed(NativeKeyEvent e) {
    	int keyCode = e.getKeyCode();
    	session.recordKeyPress(keyCode, e.getWhen());
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mousePressed(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMousePressed(NativeMouseEvent e) {
    	session.recordMouseClick(e.getButton(), e.getX(), e.getY(), e.getWhen());
    }
    
    /**
     * @see org.jnativehook.mouse.NativeMouseMotionListener#mouseMoved(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMouseMoved(NativeMouseEvent e) {
    	session.recordMouseMove(e);
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
