package gui;

import gui.components.inspector.InspectorDialog;
import memory.cache.types.CacheSA;

public class MainTest {
	public static void main(String[] args) {
		CacheSA cache = new CacheSA(16, 1024, 10, 8);
		int memseq[] = {4, 12, 32, 20, 80, 68, 76, 224, 36, 44, 16, 172, 20, 24, 36, 68, 172};
		
		for (int i = 0; i < memseq.length; i++) {
			cache.access(memseq[i]);
		}
		
		InspectorDialog diag = new InspectorDialog(cache);
		
		diag.setVisible(true);
		
		
		cache.access(48);
	}	
	
}
