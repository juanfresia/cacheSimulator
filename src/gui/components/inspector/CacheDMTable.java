package gui.components.inspector;

import gui.components.inspector.renderers.EntryRenderer;

import javax.swing.JTable;

import memory.cache.types.CacheDM;

public class CacheDMTable extends JTable {
	private static final long serialVersionUID = -6748731398348746428L;
	
		
	public CacheDMTable(CacheDM cache) {
		super(new CacheDMTableModel(cache));
		
		this.getColumnModel().getColumn(1).setCellRenderer(new EntryRenderer(cache.getBlockSize()));
		this.setFillsViewportHeight(true);
	}	
}
