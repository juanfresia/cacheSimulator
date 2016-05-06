package gui.components.inspector;

import javax.swing.table.AbstractTableModel;

import memory.cache.CacheEntry;
import memory.cache.types.CacheFA;

// TODO document
public class CacheFATableModel extends AbstractTableModel {
	private static final long serialVersionUID = 7601203668447396041L;
	
	private CacheFA cache;
	private int rowNumber;
	private String[] columnNames = {"#", "Block", "LRU"};
	
	public CacheFATableModel(CacheFA cache) {
		this.cache = cache;
		this.rowNumber = this.cache.getCacheSize()/this.cache.getBlockSize();
	}	
	
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}
	@Override
	public int getRowCount() {
		return rowNumber;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		CacheEntry act = this.cache.getEntry(rowIndex);
		switch(colIndex) {
		case 2:
			if (act.valid) {
				int LRUOrder = this.cache.getEntryLRUOrder(rowIndex);
				return LRUOrder == 0 ? "LRU" : LRUOrder;
			} else {
				return "EMPTY";
			}
		case 1:
			return act;
		case 0:
		default:
			return rowIndex;
		}
	}

}
