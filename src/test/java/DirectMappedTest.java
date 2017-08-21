package tests;

import static org.junit.Assert.*;
import memory.MemoryResults;
import memory.cache.CacheEntry;
import memory.cache.types.CacheDM;

import org.junit.Test;

public class DirectMappedTest {
	@Test
	public void testSimpleMemoryAccess() {
		CacheDM cache = new CacheDM(16, 64, 10);
		
		int cant_accesos = 16;
		int memseq[] = {4, 16, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean memhit[] = {false, false, false, true, false, false, true, false, false, true, false, false, true, true, false, true};
		
		for (int i = 0; i < cant_accesos; i++) {
			MemoryResults ce = cache.access(memseq[i]);
			assertTrue("Expected cache " + (memhit[i]?"hit but it missed":"miss but it hit") + " at dir " + memseq[i] + " (" + (i+1) + "th access)", ce.wasHit() == memhit[i]);
		}		
	}


	@Test
	public void testReplacedBlock() {
		CacheDM cache = new CacheDM(16, 64, 10);

		int cant_accesos = 16;
		int memseq[] = {4, 12, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean memOverwrite[] = {false, false, false, false, true, true, false, true, true, false, true, true, false, false, true, false};
		int addressOverwritten[] = {-1, -1, -1, -1, 16, 0, -1, 32, 224, -1, 80, 32, -1, -1, 160, -1};
		int index[] = {0, 0, 2, 1, 1, 0, 0, 2, 2, 2, 1, 2, 1, 1, 2, 0};
		
		MemoryResults mr;
		for (int i = 0; i < cant_accesos; i++) {
			mr = cache.access(memseq[i]);
			assertTrue("Expected block " + (memOverwrite[i]?"to be overwritten":"to not be overwritten") + " at dir " + memseq[i] + " (" + (i+1) + "th access)", mr.blockWasRemoved() == memOverwrite[i]);
			if (mr.blockWasRemoved()) {
				assertTrue("Expected block address " + addressOverwritten[i] + " to be overwritten.", mr.getRemovedBlockAddress() == addressOverwritten[i]);
			}
			assertTrue("Wrong block placement", mr.getBlockPosition() == index[i]);
		}		
	}
	
	
	@Test
	public void testFinalState() {
		CacheDM cache = new CacheDM(16, 64, 10);
		int cant_accesos = 16;
		int memseq[] = {4, 12, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68};
		boolean expectedValid[] = {true, true, true, false};
		int expectedBlockAddress[] = {64, 16, 32, -1};
		
		for (int i = 0; i < cant_accesos; i++) {
			cache.access(memseq[i]);
		}
		
		int entries = cache.numberOfEntries();
		assertTrue("Expected 4 total entries.", entries == 4);
		
		CacheEntry array[] = cache.cacheToArray();
				
		for (int i = 0; i < entries; i++) {
			assertTrue("Expected " + (expectedValid[i]?"valid":"invalid") + " entry.", expectedValid[i] == array[i].valid);
			if (expectedValid[i])
				assertTrue("Expected another block in memory.", expectedBlockAddress[i] == array[i].block_address);
		}		
	}

}
