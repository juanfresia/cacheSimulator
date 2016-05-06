package gui.components.inspector;

import gui.components.inspector.renderers.EntryRenderer;

import javax.swing.JTable;

import memory.cache.types.CacheFA;

public class CacheFATable extends JTable {

	private static final long serialVersionUID = -5738873400343696059L;
	
	
	public CacheFATable(CacheFA cache) {
		super(new CacheFATableModel(cache));
		
		this.getColumnModel().getColumn(1).setCellRenderer(new EntryRenderer(cache.getBlockSize()));
		this.setFillsViewportHeight(true);
	}	
}
