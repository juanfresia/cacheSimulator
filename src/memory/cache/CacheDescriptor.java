package memory.cache;

/**
 * Any class that implements this interface can be used to specify
 * a cache level by providing methods to get block and cache sizes,
 * associativity and access time on hit.
 */
public interface CacheDescriptor {	
	public int getBlockSize();
	public int getCacheSize();
	public Associativity getAssociativity();
	public int getHitTime();	
}
