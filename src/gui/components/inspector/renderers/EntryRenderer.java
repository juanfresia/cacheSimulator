package gui.components.inspector.renderers;

import javax.swing.table.DefaultTableCellRenderer;

import memory.cache.CacheEntry;

public class EntryRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 7212039663215909627L;
	private int bit_shift;
	
	public EntryRenderer() {
		super();
		this.bit_shift = 0;
	}
	
	public EntryRenderer(int block_size){
		super();
		if (block_size >= 0)
			this.bit_shift = this.compute_mag(block_size);
	}
	
	@Override
	protected void setValue(Object value) {
		CacheEntry act = (CacheEntry)value;
		if (act.valid)
			this.setText("0x" +  Integer.toString(act.block_address >> bit_shift, 16));
		else
			this.setText("INVALID");
	}
		

	private char compute_mag(int size) {
		char tmp = 0;
		if (size == 0)
			return tmp;
		
		while (((size >> tmp) & 1) == 0) {
			tmp++;
		}
		return tmp;
	}
}
