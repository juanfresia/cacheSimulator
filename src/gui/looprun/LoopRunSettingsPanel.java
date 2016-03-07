package gui.looprun;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LoopRunSettingsPanel extends JPanel implements ChangeListener, ActionListener {
	private static final long serialVersionUID = -2117695934537866006L;

	// Constants and strings for commands
	public static final String ENABLEL2_CMD = "enableL2";
	public static final String ENABLEL3_CMD = "enableL3";
	public static final String RUN_CMD = "runLoop";

	private final static int PREFERRED_WIDTH = 250;
	private final static int PREFERRED_HEIGHT = 150;
	
	private static final int DEFAULT_MARGIN = 5;
	private static final int SEPARATION_SPACE = 5;
	
	private static final String MAXLOOPSIZE_STRING = "Max loop size: ";
	private static final String TITLE_DEFAULT = "Controls";
	
	private static final String SIZE_STRINGS[] = {"1b", "2b", "4b", "8b", "16b", "32b", "64b", "128b", "256b", "512b",
									"1Kb", "2Kb", "4Kb", "8Kb", "16Kb", "32Kb", "64Kb", "128Kb", "256Kb", "512Kb",
									"1Mb", "2Mb", "4Mb", "8Mb", "16Mb", "32Mb", "64Mb", "128Mb", "256Mb", "512Mb", "1GB"};
	
	// CheckBoxes to enable L2 and L3 panels
	final JCheckBox enableL2_chkbox;
	final JCheckBox enableL3_chkbox;
	
	// Max loop size slider, with label
	final JLabel maxLoop_lbl;
	final JSlider maxLoop_sld;
	
	// Speculation and run buttons
	final JCheckBox speculate_chkbox;
	final JButton run_btn;
	
	public LoopRunSettingsPanel() {
		super();

		// Initial layout and border configuration	
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(TITLE_DEFAULT));

		// GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;	gbc.gridwidth = 2;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 0.5; gbc.weighty = 0.5;
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		
		// Label table for max loop size slider
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 18; i <= 26; i += 2) {
			labelTable.put(i, new JLabel(SIZE_STRINGS[i]));
		}
				
		// Max loop size slider and label
		this.maxLoop_lbl = new JLabel(MAXLOOPSIZE_STRING + SIZE_STRINGS[22]);
		this.maxLoop_lbl.setHorizontalTextPosition(JLabel.LEFT);
		this.add(this.maxLoop_lbl, gbc);

		gbc.insets = new Insets(0, DEFAULT_MARGIN, 0, DEFAULT_MARGIN);
		gbc.gridy = 1;
		this.maxLoop_sld = new JSlider(18, 26, 22);
		this.maxLoop_sld.setSnapToTicks(true);
		this.maxLoop_sld.setMajorTickSpacing(2);
		this.maxLoop_sld.setMinorTickSpacing(1);
		this.maxLoop_sld.setLabelTable(labelTable);
		this.maxLoop_sld.setPaintTicks(true);
		this.maxLoop_sld.setPaintLabels(true);
		this.maxLoop_sld.addChangeListener(this);
		this.add(this.maxLoop_sld, gbc);
		
		// Enable L2 checkbox
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		this.enableL2_chkbox = new JCheckBox("Enable L2 cache");
		this.enableL2_chkbox.setActionCommand(ENABLEL2_CMD);
		this.enableL2_chkbox.addActionListener(this);
		this.enableL2_chkbox.setSelected(true);
		this.add(this.enableL2_chkbox, gbc);
		
		// Enable L3 checkbox
		gbc.gridx = 1;
		this.enableL3_chkbox = new JCheckBox("Enable L3 cache");
		this.enableL3_chkbox.setActionCommand(ENABLEL3_CMD);
		this.enableL3_chkbox.setSelected(false);
		this.add(this.enableL3_chkbox, gbc);
		
		// Speculate checkbox
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.speculate_chkbox = new JCheckBox("Use speculation");
		this.speculate_chkbox.addActionListener(this);
		this.speculate_chkbox.setSelected(true);
		this.speculate_chkbox.setToolTipText("<html>Al habilitar la especulación el lazo sólo se ejecuta dos veces,<br>"
				+ " y se utiliza la tasa de misses de la segunda corrida para estimar los tiempos medios <br>"
				+ "	de acceso. Sin especulación, cada loop se ejecuta 20 veces seguidas,<br>"
				+ " realizando explícitamente cada acceso. (Marcar para resultados más rápidos)</html>");
		this.add(this.speculate_chkbox, gbc);
		
		// Run button
		gbc.gridx = 1;
		this.run_btn = new JButton("Run Loop");
		this.run_btn.setActionCommand(RUN_CMD);
		this.add(this.run_btn, gbc);
		
		// Setting preferred dimension
		Dimension dim = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		this.setPreferredSize(dim);		
		this.setMaximumSize(dim);		
		this.setMinimumSize(dim);
	}
	
	
	/**
	 * Link the setting panel to a controller to intercept and resolve events from the
	 * components.
	 *  
	 * @param lrc The controller to be linked
	 */
	public void addController(LoopRunController lrc) {
		this.run_btn.addActionListener(lrc);
		this.enableL2_chkbox.addActionListener(lrc);
		this.enableL3_chkbox.addActionListener(lrc);
	}
		
	
	// Self-updating responses to control interaction (e.g. update max loop size slider label)
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.enableL2_chkbox) {
			this.enableL3_chkbox.setEnabled(this.enableL2_chkbox.isSelected());
		}
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.maxLoop_sld) {
			this.maxLoop_lbl.setText(MAXLOOPSIZE_STRING + SIZE_STRINGS[this.maxLoop_sld.getValue()]);
		}		
	}

	
	// Methods to get information of the status of the panel	
	public boolean panelL2Enabled() {
		return this.enableL2_chkbox.isSelected();
	}	
	public boolean panelL3Enabled() {
		return this.enableL3_chkbox.isSelected();
	}	
	public boolean speculateEnabled() {
		return this.speculate_chkbox.isSelected();
	}	
	public int getMaxLoopSize() {
		return (1 << this.maxLoop_sld.getValue());
	}
	
}
