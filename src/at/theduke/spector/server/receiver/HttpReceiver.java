package at.theduke.spector.server.receiver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * 
 * @author theduke
 * See http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty#Details
 *
 */
public class HttpReceiver {
	
	public static void main(String[] args) {
		HttpReceiver rec = new HttpReceiver("", 3334);
		rec.listen();
	}
	
	public class HelloHandler extends AbstractHandler
	{
	    public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) 
	        throws IOException, ServletException
	    {
	        response.setContentType("text/html;charset=utf-8");
	        response.setStatus(HttpServletResponse.SC_OK);
	        baseRequest.setHandled(true);
	        response.getWriter().println("<h1>Hello World</h1>");
	    }
	}
	
	protected String host;
	protected int port;
	
	public HttpReceiver(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void listen() {
		
	    
	}
}
