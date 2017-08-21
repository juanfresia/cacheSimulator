package memory;

/**
 *	This class is a compact structure that can hold all the information regarding
 *	a memory access. It has a reference to an instance of itself to concatenate 
 *	access to consecutive levels in the memory (e.g. a L1 cache may have a reference
 *	to the results of the L2 cache if it was a miss).
 */
public class MemoryResults {
	
	// Private members to hold the info
	private boolean wasHit;
	private Integer address;
	private int accessTime;
	private boolean blockWasRemoved;
	private int removedBlockAddress;
	private int blockPosition;
	
	// Reference to next level access. It is null if 'wasHit' is true.
	private MemoryResults nextLevelResult;
	
	/**
	 * This constructor creates a basic MemoryResults object, with the ability to
	 * specify if the access was a miss or not, the total access time and the address
	 * of the reference.<br><br>
	 * 
	 * In case of a miss, the 'setNextLevelResult(MemoryResults)' should be called to
	 * link the outcome of the next level.<br>
	 * 
	 * In case of a block replacement (in a miss, when a block gets overwritten), the method
	 * 'setRemovedBlock(int)' may be called to specify the first address of the removed block.<br>
	 * 
	 * If the position in the cache of the block is known, the method 'setBlockPosition(int)'
	 * can be used to also inform that.
	 *  
	 * @param hit boolean to inform if the access was a hit or miss
	 * @param accessTime total access time
	 * @param address the actual address of the memory reference
	 * 
	 * @see Cache.cacheToArray()
	 */
	public MemoryResults (boolean hit, int accessTime, Integer address) {
		this.wasHit = hit;
		this.address = address;
		this.accessTime = accessTime;
		this.blockWasRemoved = false;
		this.removedBlockAddress = 0;
		this.blockPosition = 0;
		this.nextLevelResult = null;
	}
		
	
	/**
	 * Use this method in case of a miss in any memory system, to concatenate the results
	 * of the access level.<br>
	 * Main memories should never call this method, as they always make a hit.
	 * 
	 * @param nextLevel MemoryResults of the access to the next level
	 */
	public void setNextLevelResult(MemoryResults nextLevel) {
		this.nextLevelResult = nextLevel;
	}
	
	
	/**
	 * Use this method when the access to a block generates a miss, and the new block replaces
	 * an existing valid entry in the memory. Automatically sets 'blockWasRemoved' boolean to true
	 * and assigns the block address passed as parameter to be the removed block.
	 * 
	 * @param blockAddress the memory address of the first word of the removed block
	 */
	public void setRemovedBlock(int blockAddress) {
		this.blockWasRemoved = true;
		this.removedBlockAddress = blockAddress;
	}
	
	
	/**
	 * Use this method to set the block position inside the memory. The integer represents the position
	 * inside the cache as if it were an array. The convention for this is:<br>
	 * <li> For DirectMapped and FullyAssociative, the block position is the number of the entry (both can be
	 * seen as an array, and for the DirectMapped this value is the same as the index of the entry that has the block)<br>
	 * <li> For any SetAssociative, the position is the number such that it verify the next equation:
	 * 				<p style='text-align: center'>	blockPosition = blockIndex * cacheTotalWays + blockWay<p>
	 * So that for a block place at a set with index 3, in the second way (way1) of a 4-way set associative,
	 * its position would be 3*4+1 = 13.<br><br>
	 * 
	 * Note: if the access is a hit, the block position should reference the place of the block that was
	 * already on the cache.
	 * 
	 * @param blockPosition the position of the block in memory AFTER the access
	 * 
	 * @see Cache.cacheToArray()
	 */
	public void setBlockPosition(int blockPosition) {
		this.blockPosition = blockPosition;
	}
	
	
	
	// Method to convert the results in a printable string.
	@Override
	public String toString() {
		String s = "Access to: 0x" + Integer.toString(this.address, 16) + " was " + (this.wasHit?"hit. ":"miss. ");
		s = s + (this.blockWasRemoved?("A block was removed (0x"+Integer.toString(this.removedBlockAddress,16)+"). "):"No block was removed. ");
		s = s + "The block was placed in position " + this.blockPosition + "\n";
		return s;
	}
	
	
	// Getters!!
	public boolean wasHit() {
		return this.wasHit;
	}	
	public int getAccessTime() {
		return this.accessTime;
	}	
	public MemoryResults getNextLevelResult() {
		return this.nextLevelResult;
	}	
	public boolean blockWasRemoved() {
		return this.blockWasRemoved;
	}	
	public int getRemovedBlockAddress() {
		return this.removedBlockAddress;
	}	
	public int getBlockPosition() {
		return this.blockPosition;
	}	
}
