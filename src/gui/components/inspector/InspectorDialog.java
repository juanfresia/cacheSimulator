package gui.components.inspector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import memory.cache.Cache;
import memory.cache.types.CacheSA;

public class InspectorDialog extends JDialog {
	private static final long serialVersionUID = 5819342377294016179L;

	private static final int START_X = 30;
	private static final int START_Y = 30;
	
	private JScrollPane tableContainer;
	private JTable table;
	
	public InspectorDialog(Cache c) {
		this.setTitle("Cache Inspector");
		
		this.table = new CacheSATable((CacheSA)c);
		this.tableContainer = new JScrollPane(this.table);

		this.setBounds(START_X, START_Y, 1200, 600);
		this.setResizable(false);

		this.add(this.tableContainer);
	}	
}
