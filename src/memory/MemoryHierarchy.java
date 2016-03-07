package memory;

import memory.cache.Cache;
import memory.cache.CacheDescriptor;
import memory.cache.FactoryCache;

/**
 *	Multilevel cache structure (up to three levels of cache).
 */
public class MemoryHierarchy implements MemorySystem {

	// MainMemory reference and up to three levels of cache
	private MemorySystem mainMemory;
	private Cache L1;
	private Cache L2;
	private Cache L3;
	
	/* A variable to store the depth of the hierarchy, can only be 1, 2 or 3; depending on how many cache
 	 * levels there are in the hierarchy.
 	 * 1: L1 -> MM; L2 = null; L3 = null;
 	 * 2: L1 -> L2 -> MM; L3 = null;
 	 * 3: L1 -> L2 -> L3 -> MM;
 	 * 
 	 * TODO Maybe it is better to use an enumeration or something more descriptive than int
 	 */
	private int depth;
	
	
	
	// Multiple constructors, one for each depth.
	
	/**
	 * One level hierarchy constructor. Automatically assigns L2, and L3 to be null, and the
	 * depth to 1.
	 * 
	 * @param L1 any CacheDescriptor to create a Cache from
	 * @param accessTime_MM the access time of the main memory
	 */
	public MemoryHierarchy(CacheDescriptor L1, int accessTime_MM) {
		this.depth = 1;
		this.L1 = FactoryCache.createCache(L1);
		this.mainMemory = new MainMemory(accessTime_MM);
		this.L1.setNextLevel(this.mainMemory);
		this.L2 = null;
		this.L3 = null;		
	}
	
	/**
	 * Two level hierarchy constructor. Automatically assigns L3 to null, and the depth to 2.
	 * It also properly links the newly created L1, L2 and MainMemory.
	 * 
	 * @param L1 any CacheDescriptor to create the L1 cache from.
	 * @param L2 any CacheDescriptor to create the L2 cache from.
	 * @param accessTime_MM the access time of the main memory
	 */
	public MemoryHierarchy(CacheDescriptor L1, CacheDescriptor L2, int accessTime_MM) {
		this(L1, accessTime_MM);		
		this.depth = 2;
		this.L2 = FactoryCache.createCache(L2);
		this.L1.setNextLevel(this.L2);
		this.L2.setNextLevel(this.mainMemory);		
	}
	
	/**
	 * Three level hierarchy constructor. Sets depth to 3, and properly link the three levels
	 * and the main memory.
	 * 
	 * @param L1 any CacheDescriptor to create the L1 cache from
	 * @param L2 any CacheDescriptor to create the L2 cache from
	 * @param L3 any CacheDescriptor to create the L3 cache from
	 * @param accessTime_MM the access time of the main memory
	 */
	public MemoryHierarchy(CacheDescriptor L1, CacheDescriptor L2, CacheDescriptor L3, int accessTime_MM) {
		this(L1, L2, accessTime_MM);
		this.depth = 3;
		this.L3 = FactoryCache.createCache(L3);
		this.L2.setNextLevel(this.L3);
		this.L3.setNextLevel(this.mainMemory);
	}
	
	
	// Getters!! One for each level, and one to know the depth.

	/** Gets the L1 cache of the hierarchy.
	 */
	public Cache getL1Cache() {
		return this.L1;
	}
	/**Gets the L2 cache of the hierarchy. May return null if the depth
	 * of the hierarchy is 1.
	 */
	public Cache getL2Cache() {
		return this.L2;
	}
	/**Gets the L3 cache of the hierarchy. May return null if the depth
	 * of the hierarchy is 1 or 2.
	 */
	public Cache getL3Cache() {
		return this.L3;
	}

	/**Gets the depth of the hierarchy. Use this method to know whether L2 and L3 caches
	 * exists or not.
	 */
	public int getDepth() {
		return this.depth;
	}
		
	
	// Overridden methods inherited from MemorySystem
	@Override
	public MemoryResults access(Integer address) {
		// The access is always made to the first level
		return this.L1.access(address);
	}
	@Override
	public void clean() {
		this.L1.clean();
		if (this.depth > 1)
			this.L2.clean();
		if (this.depth > 2)
			this.L3.clean();
		this.mainMemory.clean();
	}
	@Override
	public boolean inMemory(Integer address) {
		return this.L1.inMemory(address);
	}
	@Override
	public int getTotalReferences() {
		return this.L1.getTotalReferences();
	}
	@Override
	public int getTotalMisses() {
		return this.L1.getTotalMisses();
	}
	@Override
	public int getTotalHits() {
		return this.L1.getTotalHits();
	}
	@Override
	public float getAverageAccessTime() {
		return this.L1.getAverageAccessTime();
	}
	@Override
	public float getMissRate() {
		return this.L1.getMissRate();
	}

}
