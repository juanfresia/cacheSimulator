package memory.cache.types;

import memory.MainMemory;
import memory.MemoryResults;
import memory.MemorySystem;
import memory.cache.Associativity;
import memory.cache.Cache;
import memory.cache.CacheEntry;

/**
 * Direct mapped cache implementation
 */
public class CacheDM extends Cache implements MemorySystem {
	
	// It uses a simple array, as it is an indexed cache
	private CacheEntry[] cache_table;
	private int entries;
	private char entries_mag;
	
	// Default values for default constructor
	private final static int BLOCK_SIZE_DEF = 64;		// 16 words
	private final static int CACHE_SIZE_DEF = 0x800;	// 2kb
	private final static int HIT_TIME_DEF = 10;

	// Constructors
	
	public CacheDM() {
		super(BLOCK_SIZE_DEF, CACHE_SIZE_DEF, HIT_TIME_DEF);
		
		this.info.associativity = Associativity.DIRECT_MAPPED;
		this.next_level = new MainMemory();
		
		this.entries = this.info.cache_size/this.info.block_size;
		this.entries_mag = compute_mag(entries);
		this.cache_table = new CacheEntry[entries];
		for (int i = 0; i < this.entries; i++) {
			this.cache_table[i] = new CacheEntry();
			this.cache_table[i].valid = false;
		}
		
	}
	public CacheDM(int block_size, int cache_size, int hit_time) {
		super(block_size, cache_size, hit_time);
		
		this.info.associativity = Associativity.DIRECT_MAPPED;
		this.next_level = new MainMemory();
		
		this.entries = cache_size/block_size;
		this.entries_mag = compute_mag(entries);
		this.cache_table = new CacheEntry[entries];
		for (int i = 0; i < this.entries; i++) {
			this.cache_table[i] = new CacheEntry();
			this.cache_table[i].valid = false;
		}
	}			
	
	/**
	 * This method calculates de index of a given address in the cache.
	 * 
	 * @param address Any address
	 * @return The index of the address in the cache.
	 */
	private int computeIndex(int address) {
		address = address >> block_mag;
		int mask = 0;
		int i = entries_mag;
		while (i > 0) {
			mask = mask << 1;
			mask |= 0x1;
			i--;
		}
		return address & mask;
	}

	
	// Clean and inMemory methods	
	@Override
	public void clean() {
		for (int i = 0; i < this.entries; i++) {
			this.cache_table[i].valid = false;
		}
		super.cleanCounters();
	}
	@Override
	public boolean inMemory(Integer address) {
		int blockAddress = this.findBlockAddress(address);
		int index = this.computeIndex(address);
		// A block is in the cache if the entry in the respective index is valid
		// and the block addresses match
		CacheEntry block = this.cache_table[index];
		return (block.valid && block.block_address == blockAddress);
	}
	
	// Access method	
	@Override
	public MemoryResults access(Integer address) {
		this.references++;
		
		// Get index and block address
		int blockAddress = this.findBlockAddress(address);
		int index = this.computeIndex(address);
		MemoryResults result = null;
		
		// Get the entry which is at the same index
		CacheEntry block = this.cache_table[index];
		
		// If the entry is valid...
		if (block.valid && block.block_address == blockAddress) {
			// Report valid and generate results directly (the block position is the index here)
			result = new MemoryResults(true, this.info.hit_time, address);
			result.setBlockPosition(index);
		} else {
			// Otherwise, an access to the next level is required
			MemoryResults nextLvl = this.next_level.access(address);
			result = new MemoryResults(false, this.info.hit_time + nextLvl.getAccessTime(), address);
			this.misses++;
			
			// Before storing the missing block in the cache, we check if the previous entry was valid
			// if so, we record the removed block
			if (block.valid)
				result.setRemovedBlock(block.block_address);
			else
				block.valid = true;
			
			// Overwriting the missing block
			block.block_address = blockAddress;
		
			// and updating return results
			result.setNextLevelResult(nextLvl);
			result.setBlockPosition(index);
		}
		return result;
	}
	
	
	// cacheToArray() and numberOfEntries() methods
	@Override
	public CacheEntry[] cacheToArray() {
		CacheEntry[] array = new CacheEntry[this.entries];
		
		for (int i = 0; i < this.entries; i++) {
			array[i] = new CacheEntry();
			array[i].valid = this.cache_table[i].valid;
			array[i].block_address = this.cache_table[i].block_address;
		}
		
		return array;
	}
	
	
	@Override
	public CacheEntry getEntry(int entryNumber) {
		// If the entry number is not in range
		if (entryNumber < 0 || entryNumber >= (this.entries))
			return null;
		
		return this.cache_table[entryNumber];
	}
	
	
	@Override
	public int numberOfEntries() {		
		return this.entries;
	}

}
