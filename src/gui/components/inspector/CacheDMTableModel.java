package gui.components.inspector;

import javax.swing.table.AbstractTableModel;

import memory.cache.CacheEntry;
import memory.cache.types.CacheDM;


// TODO document

public class CacheDMTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -7589326135496062683L;
	
	private CacheDM cache;
	private int rowNumber;
	private String[] columnNames = {"#", "Block"};
	
	public CacheDMTableModel(CacheDM cache) {
		this.cache = cache;
		this.rowNumber = this.cache.getCacheSize()/this.cache.getBlockSize();
	}	
		
	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}	
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}
	@Override
	public int getRowCount() {
		return this.rowNumber;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		CacheEntry act = this.cache.getEntry(rowIndex);
		switch(colIndex) {
		case 1:
			return act;
		case 0:
		default:
			return rowIndex;
		}
	}				
}
