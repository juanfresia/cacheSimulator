package memory.cache;

/**
 * Enumeration of all the possible associativities supported
 *
 */
public enum Associativity {
	FULLY_ASSOCIATIVE ("Fully associative", 0),
	DIRECT_MAPPED ("Direct mapped", 1),
	SET_ASSOCIATIVE_2WAY ("2-Way set associative", 2),
	SET_ASSOCIATIVE_4WAY ("4-Way set associative", 4),
	SET_ASSOCIATIVE_8WAY ("8-Way set associative", 8),
	SET_ASSOCIATIVE_16WAY ("16-Way set associative", 16);
	
	// The string contains the printable name, while another variable holds
	// the number of ways.
	private String str;
	private int ways;
	
	private Associativity(String str, int ways) {
		this.str = str;
		this.ways = ways;
	}
	
	/**
	 * Get de number of ways of the given associativity.
	 * 
	 * @return Return the number of ways. For DirectMapped the value will be 1,
	 * and for FullyAssociative it will be 0.
	 */
	public int getNumberWays() {
		return this.ways;
	}
	
	@Override
	public String toString() {
		return this.str;
	}
}
