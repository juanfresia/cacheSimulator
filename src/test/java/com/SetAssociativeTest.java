package tests;

import static org.junit.Assert.*;
import memory.MemoryResults;
import memory.cache.CacheEntry;
import memory.cache.types.CacheSA;

import org.junit.Test;

public class SetAssociativeTest {

	@Test
	public void testSimpleMemoryAccess() {
		CacheSA cache = new CacheSA(4, 64, 10, 2);
		
		int cant_accesos = 16;
		int memseq[] = {4, 16, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean memhit[] = {false, false, false, false, false, false, false, false, false, false, true, false, true, false, true, true};
		
		for (int i = 0; i < cant_accesos; i++) {
			MemoryResults ce = cache.access(memseq[i]);
			assertTrue("Expected cache " + (memhit[i]?"hit but it missed":"miss but it hit") + " at dir " + memseq[i] + " (" + (i+1) + "th access)", ce.wasHit() == memhit[i]);
		}	
	}
	
	
	@Test
	public void testReplacedBlock() {
		CacheSA cache = new CacheSA(4, 64, 10, 2);

		int cant_accesos = 16;
		int memseq[] = {4, 12, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean memOverwrite[] = {false, false, false, false, false, false, false, false, true, true, false, true, false, false, false, false};
		int addressOverwritten[] = {-1, -1, -1, -1, -1, -1, -1, -1, 4, 12, -1, 76, -1, -1, -1, -1};
		int index[] = {2, 6, 0, 10, 8, 3, 7, 1, 2, 6, 9, 7, 10, 12, 2, 3};
		
		MemoryResults mr;
		for (int i = 0; i < cant_accesos; i++) {
			mr = cache.access(memseq[i]);
			assertTrue("Expected block " + (memOverwrite[i]?"to be overwritten":"to not be overwritten") + " at dir " + memseq[i] + " (" + (i+1) + "th access)", mr.blockWasRemoved() == memOverwrite[i]);
			if (mr.blockWasRemoved()) {
				assertTrue("Expected block address " + addressOverwritten[i] + " to be overwritten.", mr.getRemovedBlockAddress() == addressOverwritten[i]);
			}
			assertTrue("Wrong block placement" + " at " + (i+1) + "th access.", mr.getBlockPosition() == index[i]);
		}		
	}
	
	
	@Test
	public void testFinalState() {
		CacheSA cache = new CacheSA(4, 64, 10, 2);

		int cant_accesos = 16;
		int memseq[] = {4, 12, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean expectedValid[] = {true, true, true, true, false, false, true, true, true, true, true, false, true, false, false, false};
		int expectedBlockAddress[] = {32, 224, 36, 68, -1, -1, 44, 172, 80, 16, 20, -1, 24, -1, -1, -1};
		
		for (int i = 0; i < cant_accesos; i++) {
			cache.access(memseq[i]);
		}
		
		int entries = cache.numberOfEntries();
		assertTrue("Expected 16 total entries.", entries == 16);
		
		CacheEntry array[] = cache.cacheToArray();
				
		for (int i = 0; i < entries; i++) {
			assertTrue("Expected " + (expectedValid[i]?"valid":"invalid") + " entry.", expectedValid[i] == array[i].valid);
			if (expectedValid[i])
				assertTrue("Expected another block in memory.", expectedBlockAddress[i] == array[i].block_address);
		}		
	}

	
}
