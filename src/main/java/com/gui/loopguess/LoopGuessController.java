package gui.loopguess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import memory.MemoryHierarchy;

import org.jfree.chart.ChartPanel;

import processor.Processor;
import gui.components.CachePanel;
import gui.components.GraphicFrame;
import gui.components.TextPanel;

public class LoopGuessController implements ActionListener {

	// References to components
	private CachePanel panelL1;
	private CachePanel panelL2;
	private CachePanel panelL3;	
	private TextPanel panelResult;
	
	private LoopGuessSettingsPanel panelSettings;
	
	// Plot and answer buffer
	private CacheQuestion question;
	private ChartPanel plot;
	
	// The constructor will link the controller to the components inside the settings panel
	public LoopGuessController(CachePanel L1, CachePanel L2, CachePanel L3, TextPanel result, LoopGuessSettingsPanel settings) {
		this.panelL1 = L1;
		this.panelL2 = L2;
		this.panelL3 = L3;
		this.panelResult = result;
		this.panelSettings = settings;
		
		this.plot = null;
		this.question = null;
		
		this.panelSettings.addController(this);
	}
	
	// Command interpretation and execution
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == LoopGuessSettingsPanel.SELECTION_CHANGED_CMD) {
			// If the command comes from the depth hierarchy selector, the panels of the L2 and
			// L3 levels must be updated accordingly
			int depth = this.panelSettings.getSelectedDepth();
			this.panelL2.setEnabled(depth > 1);
			this.panelL3.setEnabled(depth > 2);
			this.panelL2.enableComponents();
			this.panelL3.enableComponents();
			
			// Each button has its own method to resolve the command
		} else if (e.getActionCommand() == LoopGuessSettingsPanel.SHOW_GRAPH_CMD) {
			this.showGraph();
		} else if (e.getActionCommand() == LoopGuessSettingsPanel.NEW_GRAPH_CMD) {
			this.newQuestion();
		} else if (e.getActionCommand() == LoopGuessSettingsPanel.MAKE_GUESS_CMD) {
			this.makeGuess();
		} else if (e.getActionCommand() == LoopGuessSettingsPanel.SHOW_ANSWER_CMD) {
			this.showAnswer();
		}
	}
	
	/**
	 * Method to be called when the 'ShowGraph' button is pressed.
	 * If there is an existing plot it shows it, otherwise a message is printed to inform
	 * the user that a plot needs to be created beforehand.
	 */
	private void showGraph() {
		if (this.plot != null)
			GraphicFrame.showGraphic(this.plot);
		else
			this.panelResult.setText("<html><br>There is no graph to show!<br> Press 'New plot'</html>");
	}
	
	/**
	 * Method to be called when the 'NewQuestion' button is pressed.
	 * Generates a new question (random cache hierarchy) and shows the results of a loop run
	 * in a new graph. The cache configuration that produced the plot is stored under the form of
	 * a CacheQuestion instance.
	 */
	private void newQuestion() {
		this.question = CacheQuestionGenerator.generate();
		MemoryHierarchy mem = this.question.toMemoryHierarchy();
		Processor p = new Processor(mem);
		int max_loop_size = mem.getDepth()==3?0x800000:0x400000;
		this.plot = GraphicFrame.chartFromDataset(p.runEspeculatedLoopTest(max_loop_size));
		
		this.panelResult.setText("");
		
		this.showGraph();		
	}
	
	/**
	 * Method to be called when the 'ShowAnswer' button is pressed.
	 * If there is an existing CacheQuestion, it prints its description into the text panel.
	 * Otherwise, the user is requested to generate a new question to continue.
	 */
	private void showAnswer() {
		if (this.question == null) {
			this.panelResult.setText("<html><br>There is nothing to answer!<br> Press 'New plot'</html>");
			return;
		}
		this.panelResult.setText("<html>ANSWER: <br><br>" + this.question.toString() + "</html>");
	}
	
	/**
	 * Method to be called when the 'MakeGuess' button is pressed.
	 * Compares the values of the panels in the frame (the answer given) with the cache stored as the question. If there is a match,
	 * the answer is considered right, and a message is shown accordingly. If the answer is wrong, the user is also notified by a message
	 * in the text panel.
	 */
	private void makeGuess() {
		if (this.question == null) {
			this.panelResult.setText("<html><br>There is nothing to make a guess about!<br> Press 'New plot'</html>");
			return;
		}
		CacheQuestion answer = null;
		int depth = this.panelSettings.getSelectedDepth();
		if (depth == 1) {
			answer = new CacheQuestion(this.panelL1);
		} else if (depth == 2) {
			answer = new CacheQuestion(this.panelL1, this.panelL2);
		} else if (depth == 3) {
			answer = new CacheQuestion(this.panelL1, this.panelL2, this.panelL3);
		}		
		
		if (this.question.compare(answer)) {
			this.panelResult.setText("<html>CORRECT!!<br><br>" + this.question.toString() + "</html>");
		} else {
			this.panelResult.setText("<html>WRONG! <br> Try again!</html>");
		}
	}
	
}
