package gui.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MemoryPanel extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 2518470686370275118L;

	private final static int PREFERRED_WIDTH = 250;
	private final static int PREFERRED_HEIGHT = 100;
	
	final JLabel accessTime_label;
	final JSlider  accessTime_slider;
	private static final String ACCESSTIME_STRING = "Access time: ";
	
	private static final int DEFAULT_MARGIN = 5;
	
	public MemoryPanel(String title) {
		super();

		// General configuration of the panel, and the constraints
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(title));	
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;	gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 0.5; gbc.weighty = 0.5;
		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_MARGIN);
		
		// Add label and slider for access time
		this.accessTime_label = new JLabel(ACCESSTIME_STRING);
		this.add(this.accessTime_label, gbc);

		gbc.insets = new Insets(0, DEFAULT_MARGIN, DEFAULT_MARGIN, 0);
		gbc.gridx = 0; gbc.gridy = 1;
		
		this.accessTime_slider = new JSlider(0, 500, 200);
		this.accessTime_slider.setMajorTickSpacing(100);
		this.accessTime_slider.setMinorTickSpacing(10);
		this.accessTime_slider.setPaintTicks(true);
		this.accessTime_slider.setSnapToTicks(true);
		this.accessTime_slider.setPaintLabels(true);
		this.accessTime_slider.addChangeListener(this);
		this.add(this.accessTime_slider, gbc);
		this.accessTime_label.setText(ACCESSTIME_STRING + this.accessTime_slider.getValue());
		
		// Setting preferred dimension
		Dimension dim = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		this.setPreferredSize(dim);		
		this.setMaximumSize(dim);		
		this.setMinimumSize(dim);
		
	}
	
	// This method will update the slider's label any time the slider itself changes
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.accessTime_slider) {
			this.accessTime_label.setText(ACCESSTIME_STRING + this.accessTime_slider.getValue());
		}	
	}	

	/**
	 * Method that returns the value selected by the panel for the memory access time
	 */
	public int getAccessTime() {
		return this.accessTime_slider.getValue();
	}

}
