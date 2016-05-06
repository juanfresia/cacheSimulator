package gui.components.inspector;

import gui.components.inspector.renderers.EntryRenderer;

import javax.swing.JTable;

import memory.cache.types.CacheSA;

public class CacheSATable extends JTable {
	
	private static final long serialVersionUID = -890404698874056758L;

	public CacheSATable(CacheSA cache) {
		super(new CacheSATableModel(cache));
		
		for (int i = 0; i < this.getColumnModel().getColumnCount(); i++){
			if (i%2 == 1)
				this.getColumnModel().getColumn(i).setCellRenderer(new EntryRenderer(cache.getBlockSize()));
		}
		this.setFillsViewportHeight(true);
	}	
}
