package memory.cache.types;

import memory.MainMemory;
import memory.MemoryResults;
import memory.MemorySystem;
import memory.cache.Associativity;
import memory.cache.Cache;
import memory.cache.CacheEntry;
import memory.cache.CacheLRU;

/**
 * Set associative cache implementation (with LRU replacement policy)
 */
public class CacheSA extends Cache implements MemorySystem, CacheLRU {
	
	// This type of cache will be represented as array of sets. Each set will have
	// as many entries as the number of ways, and will keep track of its own LRU information
	
	/**
	 * Nested class that represents a set of cache entries
	 */
	private class CacheSet {
		/* Each set will be an array of blocks, all together another array of integers, which
		 * will represent the LRU information. The index of both arrays is the number of way
		 * the entry is in. The LRU array holds numbers from 0 to (total_ways-1), and the way
		 * whose correspondent LRU info integer is higher will be the LRU (following that, the
		 * one with a 0 is the one that has been accessed last time).
		 */
		public CacheEntry[] blocks_in_set;
		public int[] lru_info;
		
		// Total number of ways of the set. Also, the size of both arrays.
		private int capacity;
		
		// Initialization of the structure
		public CacheSet(int number_of_ways) {
			this.capacity = number_of_ways;
			this.blocks_in_set = new CacheEntry[number_of_ways];
			this.lru_info = new int[number_of_ways];
			for (int i = 0; i < number_of_ways; i++) {
				this.blocks_in_set[i] = new CacheEntry();
				this.blocks_in_set[i].valid = false;
				this.lru_info[i] = 0;
			}
		}
		
		/**
		 * Method that selects and return the index of the LRU entry in the set. If there is any invalid (empty)
		 * entry in the set, the method will return its index instead (if there were multiple empty entries, the
		 * first one found is returned). <br>
		 * This method will always find the most suitable place for any block to be put in the set (an empty entry or
		 * over the LRU entry).
		 * 
		 * @return The most suitable way to insert a new block. If the entry in the returned way is valid, that means
		 * the set is full, and that the returned entry is the LRU.
		 */
		private int findLRU() {
			int min_LRU = -1;
			int buffer = 0;
			for (int i = 0; i < this.capacity; i++) {
				if (!this.blocks_in_set[i].valid) {
					return i;
				} else {
					if (this.lru_info[i] > min_LRU) {
						min_LRU = this.lru_info[i];
						buffer = i;
					}
				}
			}
			return buffer;
		}
		
		/**
		 * Check if the block address exists in the set, and if it does it return the number of the way where it
		 * is placed. If there is no match, -1 is returned.
		 * 
		 * @param blockAddress The address of the first word of the block in search
		 * @return Returns the number of the way where the block is placed, if it does not exists in the set it returns -1.
		 */
		public int findWay(int blockAddress) {
			for (int i = 0; i < this.capacity; i++) {
				if (this.blocks_in_set[i].valid && this.blocks_in_set[i].block_address == blockAddress)
					return i;
			}
			return -1;
		}
		
		/**
		 * Cleans the set (set valid bit of each entry to false) and restart LRU information.
		 */
		public void clean() {
			for (int i = 0; i < capacity; i++) {
				this.blocks_in_set[i].valid = false;
				this.lru_info[i] = 0;
			}
		}
		
		
		/**
		 * Simulates an access to the specified way in the set. Use this method to update LRU status
		 * for a particular block (may be useful to find the way the block is in by calling 'findWay()' before).
		 * 
		 * @param wayNumber The number of the way to update.
		 * @see findWay(blockAddress)
		 */
		public void updateLRUInfo(int wayNumber) {
			int last_LRU_value = this.lru_info[wayNumber];
			for (int i = 0; i < this.capacity; i++) {
				if (this.lru_info[i] <= last_LRU_value && this.blocks_in_set[i].valid)
					this.lru_info[i]++;
			}
			this.lru_info[wayNumber] = 0;
		}
				
		
		/**
		 * Searches for a particular block in the set, and returns true if it is found.
		 * This method DOES NOT update any LRU information, nor it changes the state of the set.
		 * 
		 * @param blockAddress The block address to search for.
		 * @return Returns true if the block exists in the set (and its entry is valid); otherwise it returns false.
		 */
		public boolean inMemory(int blockAddress) {
			for (int i = 0; i < this.capacity; i++) {
				if (this.blocks_in_set[i].valid && this.blocks_in_set[i].block_address == blockAddress) {
					return true;
				}
			}
			return false;
		}	
		
	}
	
	// CacheSA class starts here
	
	// The set associative cache is implemented as an array of sets
	private CacheSet[] cache_table;
	private int sets;
	private char sets_mag;
	private int ways;
	
	// Default values
	private final static int NUM_WAYS_DEF = 2;
	private final static int BLOCK_SIZE_DEF = 64;		// 16 words
	private final static int CACHE_SIZE_DEF = 0x800;	// 2kb
	private final static int HIT_TIME_DEF = 10;
	

	// Default and specific constructors
	public CacheSA() {
		super(BLOCK_SIZE_DEF, CACHE_SIZE_DEF, HIT_TIME_DEF);
		
		this.ways = NUM_WAYS_DEF;
		this.info.associativity = Associativity.SET_ASSOCIATIVE_2WAY;
		this.next_level = new MainMemory();
		
		this.sets = this.info.cache_size/(this.info.block_size*ways);
		this.sets_mag = compute_mag(sets);
		
		this.cache_table = new CacheSet[sets];
		for (int i = 0; i < this.sets; i++) {
			this.cache_table[i] = new CacheSet(ways);
		}
	}
	public CacheSA(int block_size, int cache_size, int hit_time, int ways) {
		super(block_size, cache_size, hit_time);

		this.ways = NUM_WAYS_DEF;
		if (ways==2)
			this.info.associativity = Associativity.SET_ASSOCIATIVE_2WAY;
		else if(ways == 4)
			this.info.associativity = Associativity.SET_ASSOCIATIVE_4WAY;
		else if(ways == 8)
			this.info.associativity = Associativity.SET_ASSOCIATIVE_8WAY;
		else if(ways == 16)
			this.info.associativity = Associativity.SET_ASSOCIATIVE_16WAY;
		else {
			ways = 2;
			this.info.associativity = Associativity.SET_ASSOCIATIVE_2WAY;
		}
		this.next_level = new MainMemory();
		
		// TODO Arreglar esto
		this.ways = this.info.associativity.getNumberWays();
		
		this.sets = cache_size/(block_size*ways);
		this.sets_mag = compute_mag(sets);
		
		this.cache_table = new CacheSet[sets];
		for (int i = 0; i < this.sets; i++) {
			this.cache_table[i] = new CacheSet(ways);
		}
	}		
	
	/**
	 * Computes the index of any address, which will be used to find the set the block should be placed in
	 * @param address The address
	 * @return The index of the set the address belongs.
	 */
	private int computeIndex(int address) {
		address = address >> block_mag;
		int mask = 0;
		int i = sets_mag;
		while (i > 0) {
			mask = mask << 1;
			mask |= 0x1;
			i--;
		}
		return address & mask;
	}
	
	
	/**
	 * Returns the number of ways the cache has.
	 */
	public int getNumberOfWays() {
		return this.ways;
	}
	
	@Override
	public void clean() {
		for (int i = 0; i < this.sets; i++) {
			this.cache_table[i].clean();
		}
		super.cleanCounters();
	}

	// Access method
	@Override
	public MemoryResults access(Integer address) {
		this.references++;
		// Obtain index and set from address
		int blockAddress = this.findBlockAddress(address);
		int index = this.computeIndex(blockAddress);
		CacheSet actSet = this.cache_table[index];
		
		int blockWay = actSet.findWay(blockAddress);		
		MemoryResults result = null;
		
		// findWay method is used to check if the block is actually in the cache.
		if (blockWay >= 0) {
			// If the block already exists in cache
									
			result = new MemoryResults(true, this.info.hit_time, address);
			result.setBlockPosition(index * this.ways + blockWay);
			actSet.updateLRUInfo(blockWay);
			
		} else {
			// Otherwise, we find if there is any free way. If the set is full, the 
			// way of the LRU entry in the set is obtained.
			// As it is a miss, a call to the next level is needed.
			MemoryResults nextLvl = this.next_level.access(address);
			result = new MemoryResults(false, this.info.hit_time + nextLvl.getAccessTime(), address);
			result.setNextLevelResult(nextLvl);
			this.misses++;
						
			blockWay = actSet.findLRU();
			if (!actSet.blocks_in_set[blockWay].valid) {
				// This is executed if there is any free entry in the set
				actSet.blocks_in_set[blockWay].valid = true;
				actSet.blocks_in_set[blockWay].block_address = blockAddress;
			} else {
				// This is called if the new block will overwrite some other entry
				result.setRemovedBlock(actSet.blocks_in_set[blockWay].block_address);
				actSet.blocks_in_set[blockWay].block_address = blockAddress;
			}
			actSet.updateLRUInfo(blockWay);
			result.setBlockPosition(index * this.ways + blockWay);
		}
		return result;
	}
	@Override
	public boolean inMemory(Integer address) {
		int blockAddress = this.findBlockAddress(address);
		int index = this.computeIndex(blockAddress);
		return this.cache_table[index].inMemory(blockAddress);
	}	
	
	
	// cacheToArray() and numberOfEntries() methods
	@Override
	public CacheEntry[] cacheToArray() {

		int entries = this.info.cache_size/(this.info.block_size);
		CacheEntry[] array = new CacheEntry[entries];
				
		for (int i = 0; i < this.sets; i++) {
			CacheSet actSet = this.cache_table[i];
			for (int j = 0; j < this.ways; j++) {
				array[i * this.ways + j] = new CacheEntry();
				array[i * this.ways + j].valid = actSet.blocks_in_set[j].valid;
				array[i * this.ways + j].block_address = actSet.blocks_in_set[j].block_address;
			}
		}
		return array;
	}
	
	
	@Override
	public CacheEntry getEntry(int entryNumber) {
		// If the entry number is not in range
		if (entryNumber < 0 || entryNumber >= (this.info.cache_size/(this.info.block_size)))
			return null;
				
		CacheSet actSet = this.cache_table[entryNumber / this.ways];
		return actSet.blocks_in_set[entryNumber % this.ways];
	}
	
	@Override
	public int numberOfEntries() {
		return this.info.cache_size/(this.info.block_size);
	}
	
	@Override
	public int getEntryLRUOrder(int entryNumber) {
		// If the entry number is not in range
		if (entryNumber < 0 || entryNumber >= (this.info.cache_size/(this.info.block_size)))
			return -1;
		
		CacheSet actSet = this.cache_table[entryNumber / this.ways];
		return actSet.lru_info[entryNumber % this.ways];
	}

	
}
