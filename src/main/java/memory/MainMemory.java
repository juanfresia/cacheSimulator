package memory;

/**
 * Simple model of a main memory.
 */
public class MainMemory implements MemorySystem {
	// We define a default access time
	private final static int DEFAULT_ACCESS_TIME = 500;
	private int accessTime;
	
	// Counter to satisfy statistic methods from MemorySystem
	private int accessCount;
	
	/**
	 * Default constructor, with default access time (500 ns)	
	 */
	public MainMemory() {
		this.accessTime = DEFAULT_ACCESS_TIME;
		this.accessCount = 0;
	}
	
	/**
	 * Constructor which allows to specify any access time.
	 * @param accessTime any access time greater than zero
	 */
	public MainMemory(int accessTime) {
		if (accessTime < 0)
			accessTime = DEFAULT_ACCESS_TIME;
		this.accessTime = accessTime;
		this.accessCount = 0;
	}
		
	
	// Overridden methods from MemorySystem
	
	@Override
	public MemoryResults access(Integer address) {
		MemoryResults results = new MemoryResults(true, accessTime, address);
		results.setBlockPosition(address);
		this.accessCount++;
		return results;
	}	
	@Override
	public void clean() {
		this.accessCount = 0;
	}	
	@Override
	public boolean inMemory(Integer address) {
		return true;
	}
	@Override
	public int getTotalReferences() {
		return this.accessCount;
	}
	@Override
	public int getTotalMisses() {
		return 0;
	}
	@Override
	public int getTotalHits() {
		return this.accessCount;
	}
	@Override
	public float getAverageAccessTime() {
		return this.accessTime;
	}
	@Override
	public float getMissRate() {
		return 0;
	}
	
}
