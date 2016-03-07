package gui.looprun;

import gui.components.CachePanel;
import gui.components.MemoryPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import memory.cache.Associativity;

public class LoopRunFrame extends JPanel {
	private static final long serialVersionUID = -8026416994513756565L;

	// Preferred size for the window that will show the panel
	public static final int prefered_sizeX = 550;
	public static final int prefered_sizeY = 600;

	// All needed components..
	final CachePanel panelL1;
	final CachePanel panelL2;
	final CachePanel panelL3;
	final MemoryPanel panelMM;
	final LoopRunSettingsPanel panelSettings;

	// ..and a controller
	final LoopRunController controlLoop;
	
	public LoopRunFrame() {		

		// Layout configuration and constraints
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.1; c.weighty = 0.1;
		
		// Three cache panels
		panelL1 = new CachePanel("L1 Cache");
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 2;
		this.add(panelL1, c);
		
		panelL2 = new CachePanel("L2 Cache");
		c.gridx = 2; c.gridy = 0;
		this.add(panelL2, c);

		panelL3 = new CachePanel("L3 Cache");
		c.gridx = 0; c.gridy = 2;
		this.add(panelL3, c);
		
		// A main memory configuration panel
		panelMM = new MemoryPanel("Main Memory");
		c.gridx = 2; c.gridy = 2;
		c.gridwidth = 2; c.gridheight = 1;
		this.add(panelMM, c);

		// The settings panel
		panelSettings = new LoopRunSettingsPanel();
		c.gridx = 2; c.gridy = 3;
		c.gridwidth = 2; c.gridheight = 1;
		this.add(panelSettings, c);
	
		// Controller creation and initialization of the panel
		controlLoop = new LoopRunController(panelL1, panelL2, panelL3, panelMM, panelSettings);
		this.initValues();
	}
	
	private void initValues() {		
		this.panelL1.setPanel(10, 6, Associativity.SET_ASSOCIATIVE_2WAY, 10);
		this.panelL2.setPanel(18, 6, Associativity.SET_ASSOCIATIVE_16WAY, 100);
		this.panelL3.setPanel(20, 10, Associativity.FULLY_ASSOCIATIVE, 150);
		this.panelL3.setEnabled(false);
		this.panelL3.enableComponents();
	}
		
}
