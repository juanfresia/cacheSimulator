package gui.loopguess;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import gui.components.CachePanel;
import gui.components.TextPanel;

import javax.swing.JPanel;

public class LoopGuessFrame extends JPanel {
	private static final long serialVersionUID = 4131127721703268521L;
	
	// Preferred size for the window that will show the panel
	public static final int prefered_sizeX = 550;
	public static final int prefered_sizeY = 640;
	
	// All needed components..
	final CachePanel panelL1;
	final CachePanel panelL2;
	final CachePanel panelL3;
	final LoopGuessSettingsPanel panelSettings;
	final TextPanel resultText;
	
	// ..and a controller
	final LoopGuessController controlGuess;
	
	public LoopGuessFrame() {
		// Layout configuration and constraints
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.1; c.weighty = 0.1;
		
		// Three cache panels
		panelL1 = new CachePanel("L1 Cache", false);
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 2;
		this.add(panelL1, c);
		
		panelL2 = new CachePanel("L2 Cache", false);
		c.gridx = 2; c.gridy = 0;
		this.add(panelL2, c);

		panelL3 = new CachePanel("L3 Cache", false);
		c.gridx = 0; c.gridy = 2;
		this.add(panelL3, c);
				
		// Text panel to show results
		resultText = new TextPanel("Results: ");
		c.gridx = 2; c.gridy = 2;
		c.gridheight = 3; c.gridwidth = 2;
		this.add(resultText, c);

		// Settings panel
		panelSettings = new LoopGuessSettingsPanel();
		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 2; c.gridheight = 1;
		this.add(panelSettings, c);
		
		// Controller creation, and value initialization (the link between the controller and the settings panel
		// is made by the controller itself when it's created)
		this.controlGuess = new LoopGuessController(panelL1, panelL2, panelL3, resultText, panelSettings);
		this.initValues();
	}	
	
	private void initValues(){
		this.panelL3.setEnabled(false);
		this.panelL3.enableComponents();
	}
	
}
