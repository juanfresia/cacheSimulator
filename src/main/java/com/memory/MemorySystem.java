package memory;

/**
 * This interface is meant to be implemented by anything that can be taken as a source
 * of memory addresses by a processor.
 */
public interface MemorySystem {
	
	/**
	 * Simulates an access (e.g. load or store) into memory. Thes method MODIFIES the state
	 * of the memory (e.g. may bring blocks to the cache in a miss, or update LRU information for the 
	 * accesses address). 
	 * 
	 * @param address Memory address to be accessed (up-to-byte)
	 * 
	 * @return a MemoryResults instance which contains all the information regarding the access: hit or miss,
	 * total access time, references to other instances of MemoryResults of further structures in the cache, 
	 * if any block was overwritten, etc.
	 * 
	 * @see MemoryResults
	 */
	public MemoryResults access(Integer address);
	
	
	/**
	 * Cleans the memory system. It will leave the memory system as if it was new.
	 * For example, after a clean() call on a cache, all its entries should be marked as
	 * invalid so that any access returns a compulsive miss.<br>
	 * Ignore for main memories.
	 */
	public void clean();
	
	
	/**
	 * Method that checks if the specified address is currently in the memory system.
	 * This method DOES NOT represent an access and it will NOT change the state of the memory.<br>
	 * Use this to 'spy' the memory system.<br>
	 * 
	 * A physical main memory will always return true.
	 * 
	 * @param address Memory address to be checked (up-to-byte)
	 * @return true if the address is in the memory system, false otherwise
	 */
	public boolean inMemory(Integer address);
	
	
	// Methods to obtain average information of all the access since last clean.
	// Counters:
	public int getTotalReferences();
	public int getTotalMisses();
	public int getTotalHits();
	// Average and rates:
	public float getAverageAccessTime();
	public float getMissRate();
}
