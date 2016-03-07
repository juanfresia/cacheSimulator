package gui.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TextPanel extends JPanel {
	private static final long serialVersionUID = -831979629481875334L;
	
	private static final int PREFERRED_WIDTH = 250;
	private static final int PREFERRED_HEIGHT = 350;
	
	final JLabel label;
	
	public TextPanel(String title) {
		super();

		// GridBagLayout and constraints
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(title));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridheight = 1;	gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 0.5; gbc.weighty = 0.5;
		
		this.label = new JLabel();
		this.label.setHorizontalAlignment(SwingConstants.LEFT);
		this.label.setVerticalAlignment(SwingConstants.TOP);
		this.add(this.label, gbc);
		
		Dimension dim = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
	}
	
	public void setText(String text) {
		this.label.setText(text);
	}
	
}
