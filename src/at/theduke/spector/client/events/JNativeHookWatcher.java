package at.theduke.spector.client.events;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import at.theduke.spector.Session;

public class JNativeHookWatcher extends BaseEventWatcher implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener
{
	GlobalScreen screen;
	
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
	}
	
	 /**
     * @see org.jnativehook.keyboard.NativeKeyListener#keyPressed(org.jnativehook.keyboard.NativeKeyEvent)
     */
    public void nativeKeyPressed(NativeKeyEvent e) {
    	int keyCode = e.getKeyCode();
    	session.recordKeyDown(keyCode, e.getWhen());
    }
    
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		int keyCode = e.getKeyCode();
    	session.recordKeyUp(keyCode, e.getWhen());
	}
    
    /**
     * @see org.jnativehook.mouse.NativeMouseListener#mousePressed(org.jnativehook.mouse.NativeMouseEvent)
     */
    public void nativeMousePressed(NativeMouseEvent e) {
    	session.recordMouseDown(e.getButton(), e.getX(), e.getY(), e.getWhen());
    }
    
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		session.recordMouseUp(e.getButton(), e.getX(), e.getY(), e.getWhen());
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
	public void nativeMouseClicked(NativeMouseEvent e) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}
