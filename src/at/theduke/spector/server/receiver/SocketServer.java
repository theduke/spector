package at.theduke.spector.server.receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SocketServer {
	
	ServerSocket serverSocket;
	
	
	public void start() throws IOException {
		int port = 3333;
		final ExecutorService pool;
		
		pool = Executors.newCachedThreadPool();
		
		serverSocket = new ServerSocket(port);
		
		Thread serverThread = new Thread(new NetworkService(pool,serverSocket));
		
		serverThread.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				pool.shutdown();
				try {
					pool.awaitTermination(4L, TimeUnit.SECONDS);
					if (!serverSocket.isClosed()) {
						System.out.println("ServerSocket close");
						serverSocket.close();
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
}

class NetworkService implements Runnable {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public NetworkService(ExecutorService pool, ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.pool = pool;
	}

	public void run() {
		try {
			while (true) {
				Socket cs = serverSocket.accept();
				pool.execute(new SocketHandler(serverSocket, cs));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("--- Ende NetworkService(pool.shutdown)");
			pool.shutdown();
			try {
				pool.awaitTermination(4L, TimeUnit.SECONDS);
				if (!serverSocket.isClosed()) {
					System.out
							.println("--- Ende NetworkService:ServerSocket close");
					serverSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
