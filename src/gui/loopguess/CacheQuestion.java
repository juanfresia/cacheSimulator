package gui.loopguess;

import memory.MemoryHierarchy;
import memory.cache.CacheDescriptor;
import memory.cache.CacheInfo;

/**
 * Holds the specification of a memory hierarchy of up to three levels of caches.
 * It can be used to create an actual 
 */
public class CacheQuestion {
	
	// Default main memory access time for MemoryHierarchy creator method
	private static final int DEFAULT_MMTIME = 200;
	
	// Up to three levels of cache, and a depth variable
	// if depth == 1; then L2 = null and L3 = null
	// if depth == 2; then L3 = null
	private CacheInfo L1;
	private CacheInfo L2;
	private CacheInfo L3;
	private int depth;	
	
	// Three constructors for the three possible values of depth
	public CacheQuestion(CacheDescriptor L1) {
		this.depth = 1;
		this.L1 = new CacheInfo(L1);
		this.L2 = null;
		this.L3 = null;		
	}				
	public CacheQuestion(CacheDescriptor L1, CacheDescriptor L2) {
		this(L1);
		this.depth = 2;
		this.L2 = new CacheInfo(L2);
	}
	public CacheQuestion(CacheDescriptor L1, CacheDescriptor L2, CacheDescriptor L3) {
		this(L1, L2);
		this.depth = 3;
		this.L3 = new CacheInfo(L3);
	}
	
	
	/**
	 * Compares two descriptors and return true if they describe the same cache.
	 * The hit time is not taken into account for the comparison, only sizes and associativity
	 * are checked.
	 *  
	 * @param one Any CacheDescriptor
	 * @param other Another CacheDescriptor
	 * @return Returns true if the descriptors are the same, false otherwise
	 */
	private static boolean compareLevels(CacheDescriptor one, CacheDescriptor other) {
		if (one == null && other == null)
			return true;
		if (one == null || other == null)
			return false;
		
		if (one.getAssociativity() != other.getAssociativity())
			return false;
		
		if (one.getBlockSize() != other.getBlockSize())
			return false;
		
		if (one.getCacheSize() != other.getCacheSize())
			return false;
		
		return true;		
	}
	
		
	/**
	 * Compares two CacheQuestions and returns true if they are the same.
	 * The CacheDescriptors of each level are compared using the compareLevels() method,
	 * so any differences between access times will be ignored.
	 * 
	 * @param other Another CacheQuestion to compare to
	 * @return Return true if the questions refer to the same hierarchy structure, false otherwise
	 */
	public boolean compare(CacheQuestion other) {
		if (other == null)
			return false;
		
		if (this.depth != other.depth)
			return false;
		
		if (!compareLevels(this.L1, other.L1))
			return false;
		
		if (this.depth == 2 && !compareLevels(this.L2, other.L2))
			return false;
		
		if (this.depth == 3 && !compareLevels(this.L3, other.L3))
			return false;		
		
		return true;
	}
	
	
	/**
	 * Converts the question in an actual MemoryHierarchy.
	 * @return A MemoryHierarchy specified by the CacheQuiestion data
	 */
	public MemoryHierarchy toMemoryHierarchy() {
		MemoryHierarchy mem = null;
		if (this.depth == 1) {
			mem = new MemoryHierarchy(this.L1, DEFAULT_MMTIME);
		} else if (this.depth == 2) {
			mem = new MemoryHierarchy(this.L1, this.L2, DEFAULT_MMTIME);
		} else if (this.depth == 3) {
			mem = new MemoryHierarchy(this.L1, this.L2, this.L3, DEFAULT_MMTIME);
		}		
		return mem;
	}
	
	// Converts the question data into a printable string
	@Override
	public String toString() {
		String s = "<i>" + this.depth + " level cache</i><br><br>";
		s = s + "<b>L1: </b>" + this.L1.toString() + "<br><br>";
		
		if (this.depth > 1)
			s = s + "<b>L2: </b>" + this.L2.toString() + "<br><br>";
		
		if (this.depth > 2)
			s = s + "<b>L3: </b>" + this.L3.toString() + "<br><br>";
		
		return s;
	}
	
}
