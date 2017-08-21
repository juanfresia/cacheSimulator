package memory.cache;

import memory.MemorySystem;

public abstract class Cache implements MemorySystem, CacheDescriptor {
	
	// Access count variables to satisfy MemorySystem methods
	protected int references;
	protected int misses;
	protected int total_time;
	
	// The description of the cache, as well as the reference to next level
	protected CacheInfo info;
	protected MemorySystem next_level;
	
	// Some auxiliary variables: size magnitudes (log2[size]) and erased block info.
	protected char block_mag;
	protected char cache_mag;
	protected boolean erasedBlock;
	protected int erasedBlockAddress;
	
	// Protected constructor to initialize some common variables
	protected Cache(int block_size, int cache_size, int hitTime) {
		this.info = new CacheInfo(block_size, cache_size, hitTime, Associativity.FULLY_ASSOCIATIVE);
		this.block_mag = compute_mag(this.info.block_size);
		this.cache_mag = compute_mag(this.info.cache_size);	
		
		this.erasedBlock = false;
		this.erasedBlockAddress = 0;
	}	
	
	// Auxiliary method to compute magnitudes, block address and block number
	
	/**
	 * Given a power-of-2 size integer, it calculate its magnitude (it takes log base 2
	 * essentially). Use this function to compute the amount of bits necessary to reference
	 * a group of 'size' consecutive addresses
	 * 
	 * @param size The size variable whose magnitude is going to be calculated. MUST BE A POWER OF 2.
	 * @return The base-2 logarithm of the parameter. If size is 0, it returns 0.
	 */
	protected char compute_mag(int size) {
		char tmp = 0;
		if (size == 0)
			return tmp;
		// Using bit shifting and bitwise and to compute the log_2
		while (((size >> tmp) & 1) == 0) {
			tmp++;
		}
		return tmp;
	}
	
	/**
	 * Given any address, this method will calculate another address, corresponding
	 * to the memory location of the first word of the block in which the parameter
	 * address belong. (Essentially it turns to 0 as many bits of the address as the
	 * magnitude of the block size of the cache).
	 * 
	 * @param address A memory address
	 * @return The memory location of the first word of the block of 'address'
	 */
	protected int findBlockAddress(int address) {
		int block_mask = 0xFFFFFFFF;
		for (int i = 0; i < this.block_mag; i++)
			block_mask = block_mask << 1;

		return address & block_mask;
	}
	
	/**
	 * Computes the block number of the address passed as parameter. Block number stands
	 * for the quotient between the blockAddress and the blockSize.
	 * 
	 * @param address A memory address
	 * @return The block number of the parameter address
	 */
	protected int findBlockNumber(int address) {
		int block_number = address;
		// Uses bit shifting and a mask to eliminate any sign extension
		for (int i = 0; i < this.block_mag; i++)
			block_number = block_number >> 1 & 0x7FFFFFFF;

		return block_number;
	}	
	
	
	
	// Methods from MemorySystem

	@Override
	public int getTotalMisses() {
		return this.misses;
	}
	@Override
	public int getTotalReferences() {
		return this.references;
	}
	@Override
	public int getTotalHits() {
		return this.references - this.misses;
	}
	@Override
	public float getAverageAccessTime() {
		if (this.references == 0)
			return 0;
		return this.total_time/this.references;
	}
	@Override
	public float getMissRate() {
		if (this.references == 0)
			return 0;
		return this.misses/this.references;
	}
	
	// Auxiliary method to clean counters, should be called any time the cache is flushed.
	protected void cleanCounters() {
		this.references = 0;
		this.misses = 0;
		this.total_time = 0;
	}
	
	
	/**
	 * Allows to specify the next level of memory in which the cache
	 * will ask for a missing address.
	 * 
	 * @param level MemorySystem representing the next level.
	 */
	public void setNextLevel(MemorySystem level) {
		this.next_level = level;
	}
	
	
	// Methods from CacheDescriptor
	
	@Override
	public Associativity getAssociativity() {
		return this.info.associativity;
	}
	@Override
	public int getBlockSize(){
		return this.info.block_size;
	}
	@Override
	public int getCacheSize() {
		return this.info.cache_size;
	}
	@Override
	public int getHitTime(){
		return this.info.hit_time;
	}
	
	
	/**
	 * This method returns the state of the whole cache as an array of CacheEntry. Each entry
	 * has its own valid bit and block address (this last one is useless if the valid bit is set
	 * to false).<br><br>
	 * 
	 * The ordering of the entries, and for extension of the blocks, in the array depends on the
	 * type of cache, and is set to be the following by convention:
	 * 
	 * The index in the array corresponds to the position of the block in the cache. <br>
	 * <li> For DirectMapped and FullyAssociative, the block position is the number of the entry (both can be
	 * seen as an array, and for the DirectMapped this value is the same as the index of the entry that has the block)<br>
	 * 
	 * <li> For any SetAssociative, the position is the number such that it verify the next equation:<br>
	 * 		<p style='text-align: center'>	blockPosition = blockIndex * cacheTotalWays + blockWay <p>
	 * So, the 9th element in the array of a 4-Way set associative cache will correspond to the block placed at third set
	 * (set with index 2) in the second way (way1), given that 9 = 2 * 4 + 1.<br><br>
	 * 
	 * The size of the array returned is the total number of entries of the cache, which can be obtained with the
	 * 'numberOfEntries()' method.<br><br>
	 * 
	 * @return Gives an array with all the entries of the cache, ordered according to the convention described above
	 * @see CacheEntry, numberOfEntries();
	 */
	public abstract CacheEntry[] cacheToArray();
	
	/**
	 * Gives the total number of entries of the cache.
	 * 
	 * @return The number of entries.
	 * @see cacheToArray()
	 */
	public abstract int numberOfEntries();
	
}

