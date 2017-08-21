package memory.cache;

/**
 * Simple model for a cache entry, holds first address of a block and a valid tag.
 */
public class CacheEntry {
	public int block_address;
	public boolean valid;
	
	public CacheEntry() {
		this.block_address = 0;
		this.valid = false;
	}
}
