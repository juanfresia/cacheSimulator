package gui.components;

import java.awt.Color;

import gui.MainFrame;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class AboutPanel extends JDialog {
	private static final long serialVersionUID = -3818187529252565813L;

	// Just an about panel with a background color and some text!
	
	private static final int PREFERRED_WIDTH = 300;
	private static final int PREFERRED_HEIGHT = 320;
	private static final Color BACKGROUND_COLOR = Color.getHSBColor(0.190f/0.360f, 1.00f, 0.90f);
	
	private static final String ABOUT_TEXT = "<html><br><br><b> <font size=+2><i>Cache Loop Simulator</i></font> <br>"
											+ " <font size=+1>by Juan Manuel Fresia</font></b> <br><br><br>"
											+ "Interface made with <b>Swing</b><br>"
											+ "Plots made with <b>JFreeChart 1.0.19</b><br><br><br>"
											+ "Source code at: <i>github.com/juanfresia/cacheSimulator</i><br>"
											+ "Contact: <i>juanmanuelfresia@gmail.com</i><br><br><br>"
											+ "<p style='text-align: right'>March 2016 - All rights reserved</p></html>";

	public AboutPanel(MainFrame owner, boolean modal) {
		super(owner, modal);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel aboutLbl = new JLabel(ABOUT_TEXT);
		aboutLbl.setHorizontalAlignment(SwingConstants.CENTER);
		aboutLbl.setVerticalAlignment(SwingConstants.CENTER);
		
		panel.add(aboutLbl);
		panel.setBackground(BACKGROUND_COLOR);
		this.add(panel);

		this.setBounds(0, 0, PREFERRED_WIDTH, PREFERRED_HEIGHT);
		this.setResizable(false);
		
		// This last method centers the dialog in the screen
		this.setLocationRelativeTo(null);
	
	}
}
