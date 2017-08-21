package gui.loopguess;

import java.util.Random;

import memory.cache.Associativity;
import memory.cache.CacheInfo;

/**
 * Abstract class to generate a pseudo-random CacheQuestion
 */
public abstract class CacheQuestionGenerator {
	
	// A random generator
	private static Random rnd;
	
	private CacheQuestionGenerator() {
	}	
	
	
	/**
	 * Use this method for deterministic testing, by setting a specific seed
	 * to the RNG used in the question generation.
	 */
	public static void setSeed(long seed) {
		rnd = new Random(seed);
	}	
	
	// Default access times
	private static final int L1_HIT_TIME = 10;
	private static final int L2_HIT_TIME = 20;
	private static final int L3_HIT_TIME = 50;
	
	// Probability distribution of getting any of these configurations
	private static final float L1_chance = 0.10F;	// 10%
	private static final float L2_chance = 0.80F;	// 70%
	private static final float L3_chance = 1.00F;	// 20%
	
	/**
	 * Gets a random number between 1 and 3 which represent the depth
	 * of the memory hierarchy. The probability distribution is given by
	 * the static constants of the class.
	 * 
	 * @return A random depth for the new hierarchy
	 */
	private static int chooseDepth() {
		float rnd_float = rnd.nextFloat();
		if (rnd_float < L1_chance) {
			return 1;
		} else if (rnd_float < L2_chance) {
			return 2;
		} else if (rnd_float < L3_chance) {
			return 3;
		}
		return 1;
	}
	
	
	// Associativity picking
	private static final int MIN_ENTRIES_FOR_ASSOC[] = {2, 2, 4, 8, 16, 32};
	private static final Associativity ASSOC[] = {Associativity.DIRECT_MAPPED, Associativity.FULLY_ASSOCIATIVE, Associativity.SET_ASSOCIATIVE_2WAY,
												Associativity.SET_ASSOCIATIVE_4WAY, Associativity.SET_ASSOCIATIVE_8WAY, Associativity.SET_ASSOCIATIVE_16WAY};
	private static final float DIRECT_MAPED_CHANCE = 0.05f;
	private static final float FULLY_ASSOCIATIVE_CHANCE = 0.05f;
	
	/**
	 * Choose an associativity at random according the total entries of the cache.
	 * 
	 * @param entries The amount of entries of the cache.
	 * @return A random associativity.
	 */
	private static Associativity chooseAssociativity(int entries) {
		// Select max associativity allowed
		int max_index = 1;
		for (int i = 0; i < 6; i++) {
			if (entries >= MIN_ENTRIES_FOR_ASSOC[i])
				max_index = i;
		}

		// There is always a 5% of choosing DirectMaped and 5% FullyAssociative
		Random rnd = new Random();
		float rnd_float = rnd.nextFloat();
		if (rnd_float < DIRECT_MAPED_CHANCE)
			return Associativity.DIRECT_MAPPED;
		if ((rnd_float - DIRECT_MAPED_CHANCE) < FULLY_ASSOCIATIVE_CHANCE || max_index == 1)
			return Associativity.FULLY_ASSOCIATIVE;
		
		// If it's not direct mapped, there is an equal chance of picking any of the set associativities
		int picked = rnd.nextInt(max_index - 2) + 2;
			
		return ASSOC[picked];
	}
	
		
	// Block size and cache size (number of entries) with its probabilities for a L1 cache.
	
	private static final int L1_BLOCK_SIZES_OPT = 5;
	private static final int L1_BLOCK_SIZES[] = {8, 16, 32, 64, 128};
	private static final float L1_BLOCK_SIZES_PROB[] = {0.10f, 0.40f, 0.70f, 0.95f, 1.0f}; // 10%, 30%, 30%, 25%, 5%
	
	private static final int L1_ENTRIES_OPT = 6;
	private static final int L1_ENTRIES[] = {32, 64, 128, 256, 512, 1024};
	private static final float L1_ENTRIES_PROB[] = {0.05f, 0.20f, 0.45f, 0.75f, 0.90f, 1.0f}; // 5%, 15%, 25%, 30%, 15%, 10%
	
	/**
	 * Randomizes a new L1 CacheInfo, selecting at random the cache and block
	 * and picking then a proper associativity.
	 * 
	 * @return a pseudo-randomized CacheInfo for a L1 cache
	 */
	private static CacheInfo generateL1() {
		Random rnd = new Random();
		
		// Select a random block size
		float rnd_float = rnd.nextFloat();
		int bsize = 0;
		for (int i = 0; i < L1_BLOCK_SIZES_OPT; i++) {
			if (rnd_float <= L1_BLOCK_SIZES_PROB[i] && bsize == 0)
				bsize = L1_BLOCK_SIZES[i];
		}
		
		// Select amount of entries
		rnd_float = rnd.nextFloat();
		int entries = 0;
		for (int i = 0; i < L1_ENTRIES_OPT; i++) {
			if (rnd_float <= L1_ENTRIES_PROB[i] && entries == 0)
				entries = L1_ENTRIES[i];
		}
		
		// Choose associativity
		Associativity assoc = chooseAssociativity(entries);
		
		return new CacheInfo(bsize, bsize*entries, L1_HIT_TIME, assoc);
	}
		
	
	
	// Block size and cache size (number of entries) with its probabilities for a L2 cache.
	
	private static final int L2_BLOCK_SIZES_OPT = 6;
	private static final int L2_BLOCK_SIZES[] = {64, 128, 256, 512, 1024, 2048};
	private static final float L2_BLOCK_SIZES_PROB[] = {0.05f, 0.15f, 0.30f, 0.60f, 0.85f, 1.0f}; // 5% 10% 15% 30% 25% 15%
	
	private static final int L2_ENTRIES_OPT = 5;
	private static final int L2_ENTRIES[] = {32, 64, 128, 256, 512};
	private static final float L2_ENTRIES_PROB[] = {0.05f, 0.25f, 0.50f, 0.75f, 1.0f}; // 5%, 20%, 25%, 25%, 25%,
	
	/**
	 * Randomizes a new L2 CacheInfo, selecting at random the cache and block
	 * and picking then a proper associativity. Takes the L1 size measures in order
	 * to assure that the L2 cache will be bigger and have at least the same block size
	 * as the L1 cache.
	 * 
	 * @param bsizeL1 L1 cache's block size
	 * @param csizeL1 L1 cache's total size
	 * 
	 * @return a pseudo-randomized CacheInfo for a L2 cache
	 */
	private static CacheInfo generateL2(int bsizeL1, int csizeL1) {
		Random rnd = new Random();
		float rnd_float;
		
		// Select a random block size
		int bsize = 0;
		while (bsize < bsizeL1) {
			rnd_float = rnd.nextFloat();
			bsize = 0;
			for (int i = 0; i < L2_BLOCK_SIZES_OPT; i++) {
				if (rnd_float <= L2_BLOCK_SIZES_PROB[i] && bsize == 0)
					bsize = L2_BLOCK_SIZES[i];
			}
		}
		
		// Select amount of entries
		int entries = 0;
		if (bsize == bsizeL1 && csizeL1 >= (bsize*L2_ENTRIES[L2_ENTRIES_OPT - 1])) {
			entries = L2_ENTRIES[L2_ENTRIES_OPT - 1] * 4;
		}
		while (bsize*entries < csizeL1) {
			rnd_float = rnd.nextFloat();
			entries = 0;
			for (int i = 0; i < L2_ENTRIES_OPT; i++) {
				if (rnd_float <= L2_ENTRIES_PROB[i] && entries == 0)
					entries = L2_ENTRIES[i];
			}
		}
		
		// Choose associativity
		Associativity assoc = chooseAssociativity(entries);
		
		return new CacheInfo(bsize, bsize*entries, L2_HIT_TIME, assoc);
	}
		
	// Block size and cache size (number of entries) with its probabilities for a L3 cache.
	
	private static final int L3_BLOCK_SIZES_OPT = 4;
	private static final int L3_BLOCK_SIZES[] = {2048, 4096, 0x2000, 0x4000};
	private static final float L3_BLOCK_SIZES_PROB[] = {0.25f, 0.50f, 0.75f, 1.0f}; // 25% 25% 25% 25%
	
	private static final int L3_ENTRIES_OPT = 6;
	private static final int L3_ENTRIES[] = {16, 32, 64, 128, 256, 512};
	private static final float L3_ENTRIES_PROB[] = {0.20f, 0.40f, 0.65f, 0.85f, 0.95f, 1.0f}; // 20%, 20%, 25%, 20% 10% 5%
	
	/**
	 * Randomizes a new L3 CacheInfo, selecting at random the cache and block
	 * and picking then a proper associativity. Takes the L3 size measures in order
	 * to assure that the L3 cache will be bigger and have at least the same block size
	 * as the L1 cache.
	 * 
	 * @param bsizeL1 L3 cache's block size
	 * @param csizeL1 L3 cache's total size
	 * 
	 * @return a pseudo-randomized CacheInfo for a L2 cache
	 */
	private static CacheInfo generateL3(int bsizeL2, int csizeL2) {
		Random rnd = new Random();
		float rnd_float;
		
		// Select a random block size
		int bsize = 0;
		while (bsize < bsizeL2) {
			rnd_float = rnd.nextFloat();
			bsize = 0;
			for (int i = 0; i < L3_BLOCK_SIZES_OPT; i++) {
				if (rnd_float <= L3_BLOCK_SIZES_PROB[i] && bsize == 0)
					bsize = L3_BLOCK_SIZES[i];
			}
		}
		
		// Select amount of entries
		int entries = 0;
		if (bsize == bsizeL2 && csizeL2 >= (bsize*L3_ENTRIES[L3_ENTRIES_OPT - 1])) {
			entries = L3_ENTRIES[L2_ENTRIES_OPT - 1] * 4;
		}
		while (bsize*entries < csizeL2) {
			rnd_float = rnd.nextFloat();
			entries = 0;
			for (int i = 0; i < L3_ENTRIES_OPT; i++) {
				if (rnd_float <= L3_ENTRIES_PROB[i] && entries == 0)
					entries = L3_ENTRIES[i];
			}
		}
		
		// Choose associativity
		Associativity assoc = chooseAssociativity(entries);
		
		return new CacheInfo(bsize, bsize*entries, L3_HIT_TIME, assoc);
	}
	
	
	/**
	 * Method that returns the previous possible value for the associativity of a cache.
	 * If it is the minimum (DirectMapped), the same value is returned.
	 */
	private static Associativity prevAssociativity(CacheInfo L1) {
		Associativity act_assoc = L1.getAssociativity();
		int entries = L1.getCacheSize()/L1.getBlockSize();
		
		switch(act_assoc) {
		case FULLY_ASSOCIATIVE:
			if (entries > 16)
				return Associativity.SET_ASSOCIATIVE_16WAY;
			else if (entries > 8)
				return Associativity.SET_ASSOCIATIVE_8WAY;
			else if (entries > 4)
				return Associativity.SET_ASSOCIATIVE_4WAY;
			else if (entries > 2)
				return Associativity.SET_ASSOCIATIVE_2WAY;
			else
				return Associativity.DIRECT_MAPPED;
			
		case SET_ASSOCIATIVE_4WAY:
			return Associativity.SET_ASSOCIATIVE_2WAY;
			
		case SET_ASSOCIATIVE_8WAY:
			return Associativity.SET_ASSOCIATIVE_4WAY;
			
		case SET_ASSOCIATIVE_16WAY:
			return Associativity.SET_ASSOCIATIVE_8WAY;

		case SET_ASSOCIATIVE_2WAY:
		case DIRECT_MAPPED:
		default:
			return act_assoc;
		}
		
	}
	
	
	
	/**
	 * Method that returns the next possible value for an associativity.
	 * If it is the maximum (FullyAssociative), the same value is returned.
	 */
	private static Associativity nextAssociativity(CacheInfo L1) {
		Associativity act_assoc = L1.getAssociativity();
		int entries = L1.getCacheSize()/L1.getBlockSize();
		switch(act_assoc) {
		case DIRECT_MAPPED:
			if (entries > 2)
				return Associativity.SET_ASSOCIATIVE_2WAY;
			else
				return Associativity.FULLY_ASSOCIATIVE;
			
		case SET_ASSOCIATIVE_2WAY:
			if (entries > 4)
				return Associativity.SET_ASSOCIATIVE_4WAY;
			else
				return Associativity.FULLY_ASSOCIATIVE;
			
		case SET_ASSOCIATIVE_4WAY:
			if (entries > 4)
				return Associativity.SET_ASSOCIATIVE_4WAY;
			else
				return Associativity.FULLY_ASSOCIATIVE;
			
		case SET_ASSOCIATIVE_8WAY:
			if (entries > 8)
				return Associativity.SET_ASSOCIATIVE_8WAY;
			else
				return Associativity.FULLY_ASSOCIATIVE;

		case SET_ASSOCIATIVE_16WAY:
			return Associativity.FULLY_ASSOCIATIVE;
			
		case FULLY_ASSOCIATIVE:
		default:
			return act_assoc;
		}
	}
	
	
	/**
	 * Checks if the associativities of two consecutive levels of cache are valid
	 */
	private static boolean validateAssociativities(CacheInfo L1, CacheInfo L2) {
		int L1_ways = L1.getAssociativity().getNumberWays();
		if (L1_ways == 0)
			L1_ways = L1.getCacheSize()/L1.getBlockSize();
		
		int L2_ways = L2.getAssociativity().getNumberWays();
		if (L2_ways == 0)
			L2_ways = L2.getCacheSize()/L2.getBlockSize();
				
		return ( (L1_ways < L2_ways) || (L1_ways == 1 && L2_ways == 1) );
	}
	
	
	private static void fixAssociativities(CacheInfo L1, CacheInfo L2) {
		while (!validateAssociativities(L1, L2)) {
			Associativity buffer;
			if (rnd.nextFloat() <= 0.5f) {
				buffer = prevAssociativity(L1);
				if (buffer != L1.getAssociativity())
					L1.associativity = buffer;
			} else {
				buffer = nextAssociativity(L2);
				if (buffer != L2.getAssociativity())
					L2.associativity = buffer;
			}
		}
	}
	
	
	private static void fixAssociativities(CacheInfo L1, CacheInfo L2, CacheInfo L3) {
		fixAssociativities(L1, L2);
		fixAssociativities(L2, L3);
	}
	
	
	/**
	 * Generates a new CacheQuestion at random
	 */
	public static CacheQuestion generate() {
		// If there is no externally set seed, a new randomizer is created with system time as seed		
		
		if (rnd == null) {
			rnd = new Random(System.nanoTime());
		}
		
		int depth = chooseDepth();
		CacheInfo L1 = CacheQuestionGenerator.generateL1();
		if (depth == 1) {
			return new CacheQuestion(L1);
		} else {
			CacheInfo L2 = CacheQuestionGenerator.generateL2(L1.block_size, L1.cache_size);
			if (depth == 2) {
				
				fixAssociativities(L1, L2);
				return new CacheQuestion(L1, L2);
			} else {
				CacheInfo L3 = CacheQuestionGenerator.generateL3(L2.block_size, L2.cache_size);
				fixAssociativities(L1, L2, L3);
				return new CacheQuestion(L1, L2, L3);
			}
		}
	}
}
