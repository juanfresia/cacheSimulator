package memory.cache.types;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import memory.MainMemory;
import memory.MemoryResults;
import memory.MemorySystem;
import memory.cache.Associativity;
import memory.cache.Cache;
import memory.cache.CacheEntry;
import memory.cache.CacheLRU;

/**
 * Fully associative cache implementation (with LRU replacement policy)
 */
public class CacheFA extends Cache implements MemorySystem, CacheLRU {
	
	/* As the implementation will use an extension of LinkedHashMap to easily keep
	 * track of LRU information; we need a way to keep track of the initial ordering
	 * of the entries. This is because the LinkedHashMap will reorder the entries according
	 * to the LRU status, and we wish to keep the entries enumerated.
	 */ 
	
	// Nested class with extended entry information (position or number of the entry)
	private class CacheEntryFA extends CacheEntry {
		public int position;
		
		public CacheEntryFA() {
			super();
			this.position = 0;
			this.valid = true;
		}
	}
		
	// Nested class, a custom extension of LinkedHashMap to make LRU replacement when
	// number of entries get above the capacity
	private class CacheLRUTable<K, V> extends LinkedHashMap<K, V> {
		private static final long serialVersionUID = 1L;
		
		// Maximum number of entries allowed in the cache
		private int max_size;
		
		// These variables will track and keep information of any replacements made
		private V blockToRemove;
		private boolean blockRemoved;
		
		/**
		 * Creates a new CacheLRUTable() and set the upper bound to the number of entries it will
		 * be able to hold.
		 *  
		 * @param max_size The maximum amount of entries.
		 */
		public CacheLRUTable(int max_size) {
			// The last parameter of the LinkedHashMap set to true means
			// the LRU information will be updated not only when we call put(), but also when we call get()
			super(max_size, 1.0f, true);
			this.max_size = max_size;
			this.blockRemoved = false;
		}
		
		/* This method will be called by LinkedHashMap every time we add a new entry (after the put has ended). If it returns true,
		 * the LRU entry (the parameter eldest) will be deleted. We use this method to check whether the new
		 * insertion has over-flown the cache size, and if so; we mark the eldest entry for deletion and return true.
		 * This keeps the size of the cache fixed and allows us to later get access to the removed block.
		 */
		 @Override
		 protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			 this.blockRemoved = false;
			 // If the last insertion has made the collection bigger than the maximum size, the about to be removed block
			 // gets saved in the auxiliary variables and true is returned.
			 boolean mustRemove = this.size() > max_size;
			 if (mustRemove) {
				 this.blockRemoved = true;
				 this.blockToRemove = eldest.getValue();
			 }
			 return mustRemove;
		 }
		 
		 // These methods allow to check if any block was removed, and in that case, to get access to its address.
		 public boolean wasRemoved() {
			 return this.blockRemoved;
		 }		 
		 public V getRemovedBlock() {
			 return this.blockToRemove;
		 }		 
	}
		
	// Default constants

	private final static int BLOCK_SIZE_DEF = 0x40;		// 16 words
	private final static int CACHE_SIZE_DEF = 0x800;	// 2kb
	private final static int HIT_TIME_DEF = 10;
	
	// The actual structure of the cache
	private CacheLRUTable<Integer, CacheEntryFA> cache_table;
	private int entries;
	
	// Default and specific constructors
	public CacheFA() {
		super(BLOCK_SIZE_DEF, CACHE_SIZE_DEF, HIT_TIME_DEF);
		
		this.info.associativity = Associativity.FULLY_ASSOCIATIVE;
		this.next_level = new MainMemory();
		
		this.entries = this.info.cache_size/this.info.block_size;
		this.cache_table = new CacheLRUTable<Integer, CacheEntryFA>(entries);
	}	
	public CacheFA(int block_size, int cache_size, int hit_time) {
		super(block_size, cache_size, hit_time);
		
		this.info.associativity = Associativity.FULLY_ASSOCIATIVE;
		this.next_level = new MainMemory();
		
		this.entries = this.info.cache_size/this.info.block_size;
		this.cache_table = new CacheLRUTable<Integer, CacheEntryFA>(entries);
	}		

	
	
	@Override
	public void clean() {
		this.cache_table.clear();		
		super.cleanCounters();
	}
	@Override
	public boolean inMemory(Integer address) {
		int blockAddress = this.findBlockAddress(address);
		// Note that 'containsKey()' does not count as an access, so it does not update LRU info
		return this.cache_table.containsKey(blockAddress);
	}
	
	// Access method!
	@Override
	public MemoryResults access(Integer address) {
		this.references++;
		int blockAddress = this.findBlockAddress(address);
		MemoryResults result = null;
		
		// Check if the address is in the CacheLRUTable
		if (this.cache_table.containsKey(blockAddress)) {
			
			// As the block is in cache, we use the 'get' method to obtain its information
			// this not only allows us to inform the position but also updates the LRU information
			CacheEntryFA act = this.cache_table.get(blockAddress);
			result = new MemoryResults(true, this.info.hit_time, address);
			result.setBlockPosition(act.position);
		} else {
			MemoryResults nextLvl = this.next_level.access(address);
			result = new MemoryResults(false, this.info.hit_time + nextLvl.getAccessTime(), address);
			result.setNextLevelResult(nextLvl);
			this.misses++;
						
			// The block is not in the cache... if the size of our CacheLRUTable has not yet reach
			// its maximum, we simply create a new entry and store it in the cache. No block replacement is
			// needed here. Otherwise, the block overwrite some previous entry, in which case the position
			// info of the new entry gets updated.

			CacheEntryFA newEntry = new CacheEntryFA();
			newEntry.block_address = blockAddress;
			newEntry.position = this.cache_table.size();
			this.cache_table.put(blockAddress, newEntry);
			
			if (this.cache_table.wasRemoved()) {
				newEntry.position = this.cache_table.getRemovedBlock().position;
				result.setRemovedBlock(this.cache_table.getRemovedBlock().block_address);
			}
			
			result.setBlockPosition(newEntry.position);
		}
		return result;
	}
	
	
	

	// cacheToArray() and numberOfEntries() methods
	@Override
	public CacheEntry[] cacheToArray() {
		CacheEntry[] array = new CacheEntry[this.entries];
		
		for (int i = 0; i < this.entries; i++) {
			array[i] = new CacheEntry();
			array[i].valid = false;
		}
		
		Iterator<CacheEntryFA> iter = this.cache_table.values().iterator();
		while (iter.hasNext()) {
			CacheEntryFA act = iter.next();
			array[act.position].valid = true;
			array[act.position].block_address = act.block_address;
		}		
		
		return array;
	}
	

	public int getEntryLRUOrder(int entryNumber) {
		// If the entry number is not in range
		if (entryNumber < 0 || entryNumber >= (this.entries))
			return -1;
		
		Iterator<CacheEntryFA> iter = this.cache_table.values().iterator();
		int i = 0;
		while (iter.hasNext()) {
			CacheEntryFA act = iter.next();
			if (act.position == entryNumber)
				return i;
			i++;
		}	
		return entryNumber;
		
	}
	
	@Override
	public CacheEntry getEntry(int entryNumber) {
		// If the entry number is not in range
		if (entryNumber < 0 || entryNumber >= (this.entries))
			return null;
		
		Iterator<CacheEntryFA> iter = this.cache_table.values().iterator();
		while (iter.hasNext()) {
			CacheEntryFA act = iter.next();
			if (act.position == entryNumber)
				return act;
		}		
				
		return new CacheEntry();
	}
	
	
	@Override
	public int numberOfEntries() {
		return this.info.cache_size/this.info.block_size;
	}

	
}
