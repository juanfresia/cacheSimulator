package memory.cache;

import memory.cache.types.CacheDM;
import memory.cache.types.CacheFA;
import memory.cache.types.CacheSA;

/**
 *	Class that provide static methods to create caches.
 */
public abstract class FactoryCache {

	// Private constructor, abstract class
	private FactoryCache(){
	}	
	
	
	/**
	 * Creates a new cache from the four parameters specified.<br>
	 * Note: any kind of replacement policy in associative caches is LRU.
	 *  
	 * @param block_size Block size in bytes, must be power of 2 and smaller than cache_size
	 * @param cache_size Cache size in bytes, must be power of 2 and greater than block_size
	 * @param hit_time Non-negative integer that represents the access time on hit of the cache (in ns)
	 * @param associativity The amount of associativity
	 * 
	 * @return Returns a new cache made according to the parameters. In case of error, returns a default cache.
	 */
	
	// TODO Implement exception to throw on invalid parameters
	public static Cache createCache(int block_size, int cache_size, int hit_time, Associativity associativity) {
		if (!validateSizes(block_size, cache_size) || hit_time < 0)
			return new CacheFA();
				
		Cache newCache = null;
		switch(associativity) {
		case DIRECT_MAPPED:
			newCache = new CacheDM(block_size, cache_size, hit_time);
			break;			
		case FULLY_ASSOCIATIVE:
			newCache = new CacheFA(block_size, cache_size, hit_time);
			break;			
		case SET_ASSOCIATIVE_16WAY:
			newCache = new CacheSA(block_size, cache_size, hit_time, 16);
			break;			
		case SET_ASSOCIATIVE_4WAY:
			newCache = new CacheSA(block_size, cache_size, hit_time, 4);
			break;			
		case SET_ASSOCIATIVE_8WAY:
			newCache = new CacheSA(block_size, cache_size, hit_time, 8);
			break;			
		case SET_ASSOCIATIVE_2WAY:
			newCache = new CacheSA(block_size, cache_size, hit_time, 2);
			break;			
		default:
			newCache = new CacheFA();
		}
		return newCache;
	}
	
	
	/**
	 * Static method that allows the user to create a new cache memory from a descriptor.
	 *  
	 * @param descriptor Any CacheDescriptor
	 * @return Returns a new Cache made according to the information brought by the descriptor.
	 */
	public static Cache createCache(CacheDescriptor descriptor) {
		return FactoryCache.createCache(descriptor.getBlockSize(), descriptor.getCacheSize(), descriptor.getHitTime(), descriptor.getAssociativity());		
	}	
	
	
	// Private methods for validating data
	
	private static boolean validateSizes(int block_size, int cache_size) {
		boolean valid = true;
		valid &= block_size != 0;
		valid &= cache_size != 0;
		valid &= cache_size >= block_size;
		valid &= isPowerOf2(block_size);
		valid &= isPowerOf2(cache_size);		
		
		return valid;
	}
	
	private static boolean isPowerOf2(int number) {
		if (Integer.bitCount(number) > 1)
			return false;
		return true;
	}
	
	
}
