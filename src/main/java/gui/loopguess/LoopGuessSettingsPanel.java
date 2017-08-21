package gui.loopguess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class LoopGuessSettingsPanel extends JPanel {

	private static final long serialVersionUID = 3198133884748747309L;

	// Constants and strings for commands
	private static final String TITLE_DEFAULT = "Controls";
	
	public static final String SELECTION_CHANGED_CMD = "changeLevel";
	public static final String SHOW_GRAPH_CMD = "showGraph";
	public static final String NEW_GRAPH_CMD = "newGraph";
	public static final String SHOW_ANSWER_CMD = "showAnswer";
	public static final String MAKE_GUESS_CMD = "makeGuess";
	
	private static final int DEFAULT_MARGIN = 5;
	private static final int SEPARATION_SPACE = 5;
	
	// Radial buttons and group (and panel)
	final JPanel level_panel;
	final JRadioButton oneLvl_rbtn;
	final JRadioButton twoLvl_rbtn;
	final JRadioButton threeLvl_rbtn;
	final ButtonGroup level_group;
	
	// Buttons
	final JButton makeGuess_btn;
	final JButton showGraph_btn;
	final JButton newQuestion_btn;
	final JButton showAnswer_btn;
	
	public LoopGuessSettingsPanel() {
		super();
		
		// Initial layout and border configuration
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(TITLE_DEFAULT));

		// GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridheight = 1;	gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 0.5; gbc.weighty = 0.5;
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, SEPARATION_SPACE);
		
		// Panel with RadioButtons to chose hierarchy depth
		this.level_panel = new JPanel();
		this.level_panel.setBorder(BorderFactory.createTitledBorder("Hierarchy depth"));
		this.level_panel.setLayout(new GridBagLayout());
		
		// The actual radio buttons, grouped together
		this.oneLvl_rbtn = new JRadioButton("One level");
		this.oneLvl_rbtn.setActionCommand(SELECTION_CHANGED_CMD);
		this.twoLvl_rbtn = new JRadioButton("Two levels", true);
		this.twoLvl_rbtn.setActionCommand(SELECTION_CHANGED_CMD);
		this.threeLvl_rbtn = new JRadioButton("Three levels");
		this.threeLvl_rbtn.setActionCommand(SELECTION_CHANGED_CMD);
		this.level_group = new ButtonGroup();
		this.level_group.add(oneLvl_rbtn);
		this.level_group.add(twoLvl_rbtn);
		this.level_group.add(threeLvl_rbtn);

		gbc.gridy = 0;
		this.level_panel.add(this.oneLvl_rbtn, gbc);
		gbc.gridy = 1;
		this.level_panel.add(this.twoLvl_rbtn, gbc);
		gbc.gridy = 2;
		this.level_panel.add(this.threeLvl_rbtn, gbc);

		Dimension dim = new Dimension(110, 100);
		this.level_panel.setPreferredSize(dim);
		this.level_panel.setMaximumSize(dim);
		this.level_panel.setMinimumSize(dim);
		
		// The four 'action' buttons
		gbc.gridy = 0;
		gbc.gridheight = 4;
		this.add(this.level_panel, gbc);
		
		// ShowGraph button
		this.showGraph_btn = new JButton("Show plot");
		this.showGraph_btn.setActionCommand(SHOW_GRAPH_CMD);
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.showGraph_btn, gbc);
		
		// NewQuestion button
		this.newQuestion_btn = new JButton("New plot");	
		this.newQuestion_btn.setActionCommand(NEW_GRAPH_CMD);	
		gbc.gridy = 1;
		this.add(this.newQuestion_btn, gbc);
		
		// ShowAnswer button
		this.showAnswer_btn = new JButton("Show answer");	
		this.showAnswer_btn.setActionCommand(SHOW_ANSWER_CMD);	
		gbc.gridy = 2;
		this.add(this.showAnswer_btn, gbc);
		
		// MakeGuess button
		this.makeGuess_btn = new JButton("Make guess");
		this.makeGuess_btn.setActionCommand(MAKE_GUESS_CMD);
		gbc.gridy = 3;
		this.add(this.makeGuess_btn, gbc);
		
		// Setting preferred dimensions of the whole panel
		dim = new Dimension(250, 150);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
		
	}
	
	/**
	 * This method makes possible to link the settings panel to a proper controller,
	 * to intercept and resolve events. The controller will listen for any events in the
	 * components of the panel.
	 * 
	 * @param control The controller to link
	 */
	public void addController(LoopGuessController control) {
		this.oneLvl_rbtn.addActionListener(control);
		this.twoLvl_rbtn.addActionListener(control);
		this.threeLvl_rbtn.addActionListener(control);
		this.showGraph_btn.addActionListener(control);
		this.newQuestion_btn.addActionListener(control);
		this.makeGuess_btn.addActionListener(control);
		this.showAnswer_btn.addActionListener(control);
	}
	

	/**
	 * Method to get the selected depth in the panel.
	 */
	public int getSelectedDepth() {
		if (this.oneLvl_rbtn.isSelected()) {
			return 1;
		} else if (this.twoLvl_rbtn.isSelected()) {
			return 2;
		} else if (this.threeLvl_rbtn.isSelected()) {
			return 3;
		} else {
			return 0;
		}
	}
	
}
