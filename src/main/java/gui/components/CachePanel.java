package gui.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import memory.cache.Associativity;
import memory.cache.CacheDescriptor;

/**
 * A panel which provides all the controls necessary to specify a cache memory.
 * It extends CacheDescriptor, so it can be used to easily generate a new Cache
 */
public class CachePanel extends JPanel implements ChangeListener, ActionListener, CacheDescriptor {
	private static final long serialVersionUID = 8384259585612057338L;
	
	// Size and margin constants
	private final static int PREFERRED_WIDTH = 250;
	private final static int PREFERRED_HEIGHT = 250;
	private final static int PEFERRED_SHORT_HEIGHT = 200;
	private static final int DEFAULT_MARGIN = 5;
	private static final int SEPARATION_SPACE = 5;
	
	// Components of the panel and its labels
	final JLabel size_label;
	final JSlider size_slider;
	
	final JLabel block_label;
	final JSlider block_slider;
	
	final JLabel associativity_label;
	final JComboBox<Associativity> associativity_box;

	final JLabel hitTime_label;
	final JSlider hitTime_slider;
	
	// String constants to show in the controls
	private static final String SIZE_STRINGS[] = {"1b", "2b", "4b", "8b", "16b", "32b", "64b", "128b", "256b", "512b",
									"1Kb", "2Kb", "4Kb", "8Kb", "16Kb", "32Kb", "64Kb", "128Kb", "256Kb", "512Kb",
									"1Mb", "2Mb", "4Mb", "8Mb", "16Mb", "32Mb", "64Mb", "128Mb", "256Mb", "512Mb"};
	private static final String CACHE_SIZE_STRING = "Cache size: ";
	private static final String BLOCK_SIZE_STRING = "Block size: ";
	private static final String ASSOCIATIVITY_STRING = "Associativity: ";
	private static final String HITTIME_STRING = "Hit time: ";
		
		
	/**
	 * Creates a new panel with all the controls needed to specify any cache. The panel will
	 * have a line border, and its title can be set by the first parameter of the constructor.<br>
	 * All panels have cache_size and block_size sliders, as well as a combo box to select the associativity.
	 * However, if the second parameter is set to false, the panel will not show any hit_time slider.
	 * 
	 * @param title The title of the panel.
	 * @param showTimeSlider If it is true, the hit_time slider will be shown; it will be missing if set to false.
	 */
	public CachePanel(String title, boolean showTimeSlider) {
		super();
		
		// Initial configuration and layout
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(title));		
		
		// Label table for size sliders (to make evenly spaced ticks)
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 2; i <= 26; i += 4) {
			labelTable.put(i, new JLabel(SIZE_STRINGS[i]));
		}
		
		// GridBagConstraints initialization
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;	gbc.gridwidth = 2;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 0.5; gbc.weighty = 0.5;
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		
		// Size label and slider
		this.size_label = new JLabel(CACHE_SIZE_STRING + SIZE_STRINGS[14]);
		this.size_label.setHorizontalTextPosition(JLabel.LEFT);
		this.add(this.size_label, gbc);

		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 0; gbc.gridy = 2;
		this.size_slider = new JSlider(2, 26, 13);
		this.size_slider.setSnapToTicks(true);
		this.size_slider.setMajorTickSpacing(4);
		this.size_slider.setMinorTickSpacing(1);
		this.size_slider.setLabelTable(labelTable);
		this.size_slider.setPaintTicks(true);
		this.size_slider.setPaintLabels(true);
		this.size_slider.addChangeListener(this);
		this.add(this.size_slider, gbc);

		
		// BlockSize label and slider
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 0; gbc.gridy = 3;
		this.block_label = new JLabel(BLOCK_SIZE_STRING + SIZE_STRINGS[14]);
		this.add(this.block_label, gbc);

		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 0; gbc.gridy = 4;
		this.block_slider = new JSlider(2, 26, 13);
		this.block_slider.setSnapToTicks(true);
		this.block_slider.setMajorTickSpacing(4);
		this.block_slider.setMinorTickSpacing(1);
		this.block_slider.setLabelTable(labelTable);
		this.block_slider.setPaintTicks(true);
		this.block_slider.setPaintLabels(true);
		this.block_slider.addChangeListener(this);
		this.add(this.block_slider, gbc);
		
		// ComboBox for associativity selection and proper label
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.gridheight = 1;	gbc.gridwidth = 1;
		this.associativity_label = new JLabel(ASSOCIATIVITY_STRING);
		this.add(this.associativity_label, gbc);		

		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.gridheight = 1;	gbc.gridwidth = 1;
		this.associativity_box = new JComboBox<Associativity>(Associativity.values());
		this.associativity_box.addActionListener(this);
		this.add(this.associativity_box, gbc);
		
		// Last slider is always created, but it is only shown when second parameter is true
		
		// Hit time slider and label
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		gbc.gridx = 0; gbc.gridy = 6;
		gbc.gridheight = 1;	gbc.gridwidth = 2;
		this.hitTime_label = new JLabel(HITTIME_STRING);
		if (showTimeSlider)
			this.add(this.hitTime_label, gbc);

		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, 0);
		gbc.gridx = 0; gbc.gridy = 7;
		this.hitTime_slider = new JSlider(0, 500, 10);
		this.hitTime_slider.setSnapToTicks(true);
		this.hitTime_slider.setMajorTickSpacing(100);
		this.hitTime_slider.setMinorTickSpacing(10);
		this.hitTime_slider.setPaintTicks(true);
		this.hitTime_slider.setPaintLabels(true);
		this.hitTime_slider.addChangeListener(this);
		if (showTimeSlider)
			this.add(this.hitTime_slider, gbc);
		this.hitTime_label.setText(HITTIME_STRING + this.hitTime_slider.getValue());
		
		
		// Setting dimensions
		Dimension dim;
		if (showTimeSlider) {
			dim = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		} else {
			dim = new Dimension(PREFERRED_WIDTH, PEFERRED_SHORT_HEIGHT);
		}
		this.setPreferredSize(dim);		
		this.setMaximumSize(dim);		
		this.setMinimumSize(dim);
	}
	
	/**
	 * This constructor allows to create a panel without having to explicitly specify if the hit time slider
	 * will be visible. Assumed true.
	 * To create a panel without a time slider, call the other constructor.
	 * 
	 * @param title Title of the panel
	 * @see CachePanel(String, boolean)
	 */
	public CachePanel(String title) {
		this(title, true);
	}	
	
	
	// Listener method that updates the labels of the sliders when they change.
	// It also avoids invalid caches to be created (checking that block size is always less
	// than total size, for example)
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.size_slider || e.getSource() == this.block_slider) {
			// Check for invalid relation between cache and block sizes
			if (this.block_slider.getValue() > this.size_slider.getValue())
				this.block_slider.setValue(this.size_slider.getValue());
			
			// Update labels
			this.block_label.setText(BLOCK_SIZE_STRING + SIZE_STRINGS[this.block_slider.getValue()]);
			this.size_label.setText(CACHE_SIZE_STRING + SIZE_STRINGS[this.size_slider.getValue()]);
			
			// Check for invalid associativity (a 16-way associativity should not be allowed in a 8 entry cache)
			Associativity selected = (Associativity)this.associativity_box.getSelectedItem();
			if (selected != Associativity.DIRECT_MAPPED && selected != Associativity.FULLY_ASSOCIATIVE) {
				int size = 1 << (this.size_slider.getValue() - 1);
				int block = 1 << (this.block_slider.getValue() - 1);
				if (selected.getNumberWays() > size/block)
					this.associativity_box.setSelectedItem(Associativity.FULLY_ASSOCIATIVE);
			}
			
		} else if (e.getSource() == this.hitTime_slider) {
			this.hitTime_label.setText(HITTIME_STRING + this.hitTime_slider.getValue());
		}
	}
	

	// This methods checks for any changes in the associativity combo box and validate that no
	// invalid state is reached.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.associativity_box) {
			// Check for invalid associativity (a 16-way associativity should not be allowed in a 8 entry cache)
			Associativity selected = (Associativity)this.associativity_box.getSelectedItem();
			if (selected != Associativity.DIRECT_MAPPED && selected != Associativity.FULLY_ASSOCIATIVE) {
				int size = 1 << (this.size_slider.getValue() - 1);
				int block = 1 << (this.block_slider.getValue() - 1);
				if (selected.getNumberWays() > size/block)
					this.associativity_box.setSelectedItem(Associativity.FULLY_ASSOCIATIVE);
			}
		}
	}
	
	
	/**
	 * This method allows to set the cache in any initial state.
	 * WARNING: the parameters are NOT checked for validity.
	 */
	public void setPanel(int cache_size, int block_size, Associativity assoc, int hit_time) {
		// TODO Check for invalid parameters
		this.size_slider.setValue(cache_size);
		this.block_slider.setValue(block_size);
		this.hitTime_slider.setValue(hit_time);
		this.associativity_box.setSelectedItem(assoc);
	}
		
	
	
	// Getters from CacheDescriptor
	@Override
	public int getCacheSize() {
		return 1 << this.size_slider.getValue();
	}
	@Override
	public int getBlockSize() {
		return 1 << this.block_slider.getValue();
	}
	@Override
	public int getHitTime() {
		return this.hitTime_slider.getValue();
	}
	@Override
	public Associativity getAssociativity() {
		return (Associativity)this.associativity_box.getSelectedItem();
	}
	
	
	
	/**
	 * This method enables or disables all the panel's components according to the isEnabled() method
	 * of the panel itself. It should be called any time the setEnable() method is used on the panel, so that
	 * all the components can follow the panel's behavior.
	 */
	public void enableComponents() {
		boolean enabled = this.isEnabled();
		this.associativity_box.setEnabled(enabled);
		this.block_slider.setEnabled(enabled);
		this.size_slider.setEnabled(enabled);
		this.hitTime_slider.setEnabled(enabled);
		this.block_label.setEnabled(enabled);
		this.size_label.setEnabled(enabled);
		this.associativity_label.setEnabled(enabled);
		this.hitTime_label.setEnabled(enabled);
	}

	
}
