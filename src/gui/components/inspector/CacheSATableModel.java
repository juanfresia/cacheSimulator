package gui.components.inspector;

import javax.swing.table.AbstractTableModel;

import memory.cache.CacheEntry;
import memory.cache.types.CacheSA;

public class CacheSATableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2390748545605873446L;

	
	private CacheSA cache;
	
	// The rowNumber is the number of the set
	private int rowNumber;
	
	public CacheSATableModel(CacheSA cache) {
		this.cache = cache;
		this.rowNumber = this.cache.getCacheSize()/(this.cache.getBlockSize()*this.cache.getNumberOfWays());
	}	
	
	
	// 2 Columns for each way (stored block, and LRU info), 1 column for set enumeration 
	@Override
	public int getColumnCount() {
		return this.cache.getNumberOfWays()*2 + 1;
	}

	@Override
	public int getRowCount() {
		return rowNumber;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Set #";
		} else if (column % 2 == 0) {
			return ("LRU - W" + (column/2-1));
		} else {
			return ("Block - W" + ((column-1)/2));
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		if (colIndex == 0)
			return rowIndex;
		
		int wayIndex = colIndex%2 == 1 ? (colIndex-1)/2 : colIndex/2 - 1;
		int entryNumber = rowIndex * this.cache.getNumberOfWays() + wayIndex;
				
		CacheEntry act = this.cache.getEntry(entryNumber);
		int LRUInfo = this.cache.getEntryLRUOrder(entryNumber);
		if (colIndex % 2 == 0) {
			if (act.valid) {
				return LRUInfo == 0 ? "LRU" : LRUInfo;
			} else {
				return "EMPTY";
			}			
		}
						
		return act;
	}

}
