package gui.looprun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import memory.MemoryHierarchy;
import memory.MemorySystem;

import org.jfree.data.category.CategoryDataset;

import processor.Processor;
import gui.components.CachePanel;
import gui.components.GraphicFrame;
import gui.components.MemoryPanel;

public class LoopRunController implements ActionListener {
	private static final int DEFAULT_ITERATIONS = 20;
	
	// References to all components in the frame
	private CachePanel panelL1;
	private CachePanel panelL2;
	private CachePanel panelL3;
	private MemoryPanel panelMM;
	
	private LoopRunSettingsPanel panelSettings;
	
	// The constructor sets the new controller to be the listener of the events in the settings panel
	public LoopRunController(CachePanel L1, CachePanel L2, CachePanel L3, MemoryPanel MM, LoopRunSettingsPanel SP) {
		this.panelL1 = L1;
		this.panelL2 = L2;
		this.panelL3 = L3;
		this.panelMM = MM;
		this.panelSettings = SP;
		this.panelSettings.addController(this);
	}
	
	// Event listener method
	@Override
	public void actionPerformed(ActionEvent e) {
		this.panelSettings.actionPerformed(e);
		
		// If the EnableL2 CheckBox has changed its state
		if (e.getActionCommand() == LoopRunSettingsPanel.ENABLEL2_CMD) {
			boolean enabled = this.panelSettings.panelL2Enabled();
			this.panelL2.setEnabled(enabled);
			this.panelL3.setEnabled(enabled);
			if (enabled) {
				this.panelL3.setEnabled(this.panelSettings.panelL3Enabled());
			}
			this.panelL2.enableComponents();
			this.panelL3.enableComponents();

		// If the EnableL3 CheckBox has changed its state
		} else if (e.getActionCommand() == LoopRunSettingsPanel.ENABLEL3_CMD) {
			boolean enabled = this.panelSettings.panelL2Enabled();
			if (enabled) {
				this.panelL3.setEnabled(this.panelSettings.panelL3Enabled());
			}
			this.panelL3.enableComponents();

		// If the Run button has been pressed the resolution is done by another method
		} else if (e.getActionCommand() == LoopRunSettingsPanel.RUN_CMD) {
			this.runLoop();
		}
	}		
	
	
	/**
	 * This method is called when the RunLoop button is pressed.
	 * It takes the information from the cache and memory panels, creates a memory hierarchy
	 * and runs loop on it. The results are plotted and shown in a new window.
	 */
	private void runLoop() {
		MemorySystem mem;
		
		// Creation of the hierarchy
		if (this.panelL2.isEnabled()) {
			if (this.panelL3.isEnabled()) {
				mem = new MemoryHierarchy(this.panelL1, this.panelL2, this.panelL3, this.panelMM.getAccessTime());
			} else {
				mem = new MemoryHierarchy(this.panelL1, this.panelL2, this.panelMM.getAccessTime());
			}
		} else {
			mem = new MemoryHierarchy(this.panelL1, this.panelMM.getAccessTime());
		}
		Processor p = new Processor(mem);
		
		// Loop run and data collection
		CategoryDataset dataset;
		if (this.panelSettings.speculateEnabled()) {
			dataset = p.runEspeculatedLoopTest(this.panelSettings.getMaxLoopSize());
		} else {
			dataset = p.runSimulatedLoopTest(this.panelSettings.getMaxLoopSize(), DEFAULT_ITERATIONS);
		}
		
		// Create and show a new line plot from the dataset
		GraphicFrame.showGraphic(dataset);
	}
	
}
