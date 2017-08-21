package gui;

import gui.components.AboutPanel;
import gui.loopguess.LoopGuessFrame;
import gui.looprun.LoopRunFrame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

public class MainFrame extends JFrame implements ChangeListener {

	private static final long serialVersionUID = -8026416994513756565L;
	
	private static final int STARTING_X = 20;
	private static final int STARTING_Y = 20;
	
	private int lastTab;	
	private final JTabbedPane tabPanel;
	
	public MainFrame(String title) {		
		super(title);
		this.setLayout(new BorderLayout());
		this.tabPanel = new JTabbedPane(JTabbedPane.TOP);
		this.tabPanel.addChangeListener(this);
		
		this.tabPanel.addTab("Loop run", null, new LoopRunFrame(), "Run loop test in a custom memory hierarchy configuration");
		this.tabPanel.addTab("Loop guess", null, new LoopGuessFrame(), "Try and guess the memory hierarchy that generated the plot!");
		this.tabPanel.addTab("About", null);
		this.add(this.tabPanel);
		this.initValues();
	}
	
	
	private void initValues() {
		this.setResizable(false);
		this.setBounds(STARTING_X, STARTING_Y, LoopGuessFrame.prefered_sizeX, LoopGuessFrame.prefered_sizeY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.lastTab = 0;
	}
	
	// Listener for the change of tab events
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.tabPanel) {
			if (this.tabPanel.getSelectedIndex() == 2) {
				JDialog diag = new AboutPanel(this, true);
				diag.setTitle("About...");
				diag.setVisible(true);
				this.tabPanel.setSelectedIndex(this.lastTab);
			} else if (this.tabPanel.getSelectedIndex() == 1) {
				this.setBounds(this.getLocation().x, this.getLocation().y, LoopGuessFrame.prefered_sizeX, LoopGuessFrame.prefered_sizeY);
				this.lastTab = 1;
			} else if (this.tabPanel.getSelectedIndex() == 0) {
				this.setBounds(this.getLocation().x, this.getLocation().y, LoopRunFrame.prefered_sizeX, LoopRunFrame.prefered_sizeY);
				this.lastTab = 0;
			}
		}
				
	}	
	
	// Main method
	public static void main(String[] args) {
		setUIFont(new FontUIResource(new Font("Helvetica",Font.PLAIN, 11)));
		setPanelTitleFont(new FontUIResource(new Font("Helvetica",Font.BOLD, 12)));
				
		MainFrame frame = new MainFrame("Cache Loop Simulator");
		frame.setVisible(true);
	}	
	
	
	
	// Methods that search for the right keys in the UIManager defaults and replace
	// the default fonts with the custom ones.	
	private static void setUIFont(FontUIResource f) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource) {
	            UIManager.put(key, f);
	        }
	    }
	}	
	private static void setPanelTitleFont(FontUIResource f) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource && (String)key == "TitledBorder.font") {
	            UIManager.put(key, f);
	        }
	    }
	}

}
