package at.theduke.spector.client.config;

public class PathSpec {
	
	public PathSpec(String path, int maxDepth, boolean ignoreHidden) {
		this.path = path;
		this.maxDepth = maxDepth;
		this.ignoreHidden = ignoreHidden;
	}
	
	/**
	 * Filesystem path.
	 */
	public String path;
	
	/**
	 * Maximum depth of monitoring.
	 * 0 for non-recursive, -1 for unlimited.
	 */
	public int maxDepth = -1;
	
	/**
	 * Ignore hidden files?
	 */
	public boolean ignoreHidden = true;
}
