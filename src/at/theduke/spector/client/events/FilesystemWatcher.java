package at.theduke.spector.client.events;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

import at.theduke.spector.Event;
import at.theduke.spector.Session;
import at.theduke.spector.client.Application;
import at.theduke.spector.client.config.PathSpec;

public class FilesystemWatcher extends Thread implements EventWatcher {
	
	protected Logger logger;
	protected Session session = null;
	
	/**
	 * The paths that should be watched.
	 */
	protected ArrayList<PathSpec> paths;
	
	/**
	 * WatchService.
	 */
	protected WatchService watchService;
	
	private Map<WatchKey,Path> keys = new HashMap<WatchKey,Path>();;
	
	public void setPaths(ArrayList<PathSpec> paths) {
		this.paths = paths;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	@Override
    public void run() {
		connect(session);
	}
	
	@Override
	public void connect(Session session) {
		System.out.println("connect");
		logger = Application.getLogger();
		logger.debug("Connectin filesystem watcher.");
		
		this.session = session;
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
			
			for (PathSpec pathSpec : paths) {
				Path path = Paths.get(pathSpec.path);
				registerAll(path, pathSpec.maxDepth, pathSpec.ignoreHidden);
			}
			logger.debug("Finished connecting watches.");
        } catch (IOException e) {
            logger.error("IOException: "+ e.getMessage());
        }
		
		doWatch();
	}
	
	@SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
	
	/**
     * Register the given directory with the WatchService
     */
    protected void register(Path dir) throws IOException {
    	logger.debug("Registering path " + dir.toAbsolutePath());
        WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    protected void registerAll(final Path start, int maxDepth, final boolean ignoreHidden) throws IOException {
    	
    	// walkFileTree does not understand -1.
    	if (maxDepth == -1) {
    		maxDepth = Integer.MAX_VALUE;
    	}
    	
        // register directory and sub-directories
    	Set<FileVisitOption> options = Collections.emptySet();
    	
        Files.walkFileTree(start, options, maxDepth, new SimpleFileVisitor<Path>() {
        	
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            {
            	if (ignoreHidden) {
            		// Ignore files/directories starting with a dot.
            		if (dir.getFileName().toString().startsWith(".")) {
            			return FileVisitResult.SKIP_SUBTREE;
            		}
            	}
            	
                try {
                	register(dir);
                }
                catch (IOException e) {
                	logger.warn("Could not register path " + dir.toAbsolutePath() + ": " + e.getMessage());
                	return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
	
	protected void doWatch() {
		WatchKey key = null;
		
		logger.debug("Starting to watch FS");
		
        while(true) {
            try {
                key = watchService.take();
                
                Path dir = keys.get(key);
                if (dir == null) {
                    logger.warn("WatchKey not recognized!!");
                    continue;
                }
                
                for (WatchEvent<?> event: key.pollEvents()) {
                    Kind kind = event.kind();
                    WatchEvent<Path> ev = cast(event);
                    Path name = ev.context();
                    Path fullPath = dir.resolve(name);
                    
                    String fullFilePath = fullPath.toAbsolutePath().toString();
                    
                    // if directory is created, and watching recursively, then
                    // register it and its sub-directories
                    if (kind == ENTRY_CREATE) {
                        try {
                            if (Files.isDirectory(fullPath, NOFOLLOW_LINKS)) {
                                registerAll(fullPath, 1, true);
                            }
                        } catch (IOException x) {
                            // Ignore to keep running.
                        }
                    }
                    
                    // Determine Event type.
                    
                    String eventType = null;
                    
                    if (Files.isDirectory(fullPath)) {
                    	if (kind == ENTRY_CREATE) {
                    		eventType = Event.EVENT_DIR_CREATE;
                    	}
                    	else if (kind == ENTRY_DELETE) {
                    		eventType = Event.EVENT_DIR_DELETE;
                    	}
                    }
                    else {
                    	if (kind == ENTRY_CREATE) {
                    		eventType = Event.EVENT_FILE_CREATE;
                    	}
                    	else if (kind == ENTRY_DELETE) {
                    		eventType = Event.EVENT_FILE_DELETE;
                    	}
                    	else if (kind == ENTRY_MODIFY) {
                    		eventType = Event.EVENT_FILE_MODIFY;
                    	}
                    }
                    
                    if (eventType != null) {
                    	session.logEvent(eventType, fullFilePath);
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("InterruptedException: " + e.getMessage());
            }
            
            // Reset key and remove from set if directory no longer accessible.
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // All directories are inaccessible, so exit the loop.
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
	}
	
    public static void main(String[] args) throws IOException {
    	FilesystemWatcher watcher = new FilesystemWatcher();
    	
    	ArrayList<PathSpec> paths = new ArrayList<PathSpec>();
    	paths.add(new PathSpec("/home/theduke", 5, true));
    	
    	watcher.setPaths(paths);
    	watcher.setSession(new Session());
    	//watcher.connect(new Session());
    	
    	System.out.println("Start");
    	watcher.start();
    	while (true) {
    		try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    }
}
