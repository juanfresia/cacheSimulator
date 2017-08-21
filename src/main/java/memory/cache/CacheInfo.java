package memory.cache;

/**
 * Simplest version of CacheDescriptor, it's simply an struct with the four
 * sufficient values to unequivocally describe a cache unit.
 */
public class CacheInfo implements CacheDescriptor {
	public int block_size;
	public int cache_size;
	public int hit_time;
	public Associativity associativity;
	
	/** A constructor provided to create an instance from any CacheDescriptor
	 * 
	 * @param cd Any cache descriptor whose information will be cloned in the new instance.
	 */
	public CacheInfo(CacheDescriptor cd) {
		this.block_size = cd.getBlockSize();
		this.cache_size = cd.getCacheSize();
		this.associativity = cd.getAssociativity();
		this.hit_time = cd.getHitTime();
	}
	
	/**
	 * A constructor to create an instance by giving the four parameter separately.
	 * 
	 * @param block_size Block size in bytes, must be power of 2 and smaller than cache_size
	 * @param cache_size Cache size in bytes, must be power of 2 and greater than block_size
	 * @param hit_time Non-negative integer that represents the access time on hit of the cache (in ns)
	 * @param associativity The amount of associativity
	 */
	public CacheInfo(int block_size, int cache_size, int hit_time, Associativity assoc) {
		this.associativity = assoc;
		this.block_size = block_size;
		this.cache_size = cache_size;
		this.hit_time = hit_time;
	}	
	
	// Getters (must override from CacheDescriptor even though they are public variables)
	
	@Override
	public int getBlockSize() {
		return this.block_size;
	}
	@Override
	public int getCacheSize() {
		return this.cache_size;
	}
	@Override
	public Associativity getAssociativity() {
		return this.associativity;
	}
	@Override
	public int getHitTime() {
		return this.hit_time;
	}		
	
		
	// Methods to print the cache specifications on screen
	
	// String constants
	private static final String SIZE_STRINGS[] = {"1", "2", "4", "8", "16", "32", "64", "128", "256", "512",
		"1K", "2K", "4K", "8K", "16K", "32K", "64K", "128K", "256K", "512K",
		"1M", "2M", "4M", "8M", "16M", "32M", "64M", "128M", "256M", "512M", "1G", "2G", "4G", "8G"};
	private static final String ERROR_STRING = "_ERROR_NOT_POW2_";
	
	
	/**
	 * Converts a power-of-two size into its shortened version by showing it
	 * in multiples of 2^n10. For example, 2048 turns into "2K", 65536 turns int "64K",
	 * and so on.
	 * 
	 * @param size Value to convert, it MUST be a power of two
	 * @return Return the value as a string expressed as multiples of 2^n10 (K, M, G). If the
	 * value is not a power of two, it returns "ERROR".
	 */
	private String sizeToString(int size) {
		// Checks if its a power of 2 (must have at least one '1')
		if (Integer.bitCount(size) > 1)
			return ERROR_STRING;
		
		// Checks if its zero
		if (size == 0)
			return "0";
		
		// It will bit-shift to the right until it's equal to one.
		// We count the number of times the '1' is shifted
		int i = 0;
		while (size != 1) {
			i++;
			size = size >> 1;
		}
		
		// We can use the shift counter to access the SIZE_STRING array, and
		// return that value		
		return SIZE_STRINGS[i];
	}
	
	
	/**
	 * Converts CacheInfo into printable specifications.
	 */
	@Override
	public String toString() {
		String s = this.associativity.toString();
		s = s + " cache of ";
		s = s + sizeToString(this.cache_size) + "b total size and ";
		s = s + sizeToString(this.block_size) + "b block size. (";
		s = s + sizeToString(this.cache_size/this.block_size) + " entries).";		
		return s;
		
	}
	
}
