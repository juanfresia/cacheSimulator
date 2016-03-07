package gui.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 * This singleton class will provide static methods to show any chart in a separated window.
 * Only one window of this type can exists.
 */
public class GraphicFrame extends JDialog {
	private static final long serialVersionUID = -3512445108280356017L;
	
	private final JPanel graphPanel;
	private static GraphicFrame singleton = null;
	
	// Private constructor, that will be called by any of the static methods
	// if there is no singleton instance.
	private GraphicFrame() {
		this.setTitle("Loop test plot");
		this.graphPanel = new JPanel();
		this.graphPanel.setLayout(new BorderLayout());
		
		this.add(this.graphPanel);
	
		this.setBounds(30, 30, 1300, 800);
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
		
	/**
	 * Returns a line plot made from the CategoryDataset data passed as parameter.
	 * The plot is return in the form of a ChartPanel, and it can then be placed into any container.
	 * 
	 * @param dataset The data to be plotted.
	 * @return ChartPanel The plot made from the dataset inside a ChartPanel.
	 */
	public static ChartPanel chartFromDataset(CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createLineChart("Loop test results", "Loop step", "Average access time", dataset, PlotOrientation.VERTICAL, true, true, false);
		
		// Customization
		chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = chart.getCategoryPlot();
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		plot.setRenderer(renderer);
		
		for (int i = 0; i < dataset.getRowCount(); i++) {
			renderer.setSeriesStroke(i, new BasicStroke(2.0f));
		}
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
			
		// Font customization
		Font customFont = new Font("Consola", plot.getDomainAxis().getLabelFont().getStyle(), plot.getDomainAxis().getLabelFont().getSize());
		plot.getDomainAxis().setLabelFont(customFont);
		
		customFont = new Font("Consola", plot.getRangeAxis().getLabelFont().getStyle(), plot.getRangeAxis().getLabelFont().getSize());
		plot.getRangeAxis().setLabelFont(customFont);
		
		customFont = new Font("Consola", chart.getTitle().getFont().getStyle(), chart.getTitle().getFont().getSize());
		chart.getTitle().setFont(customFont);
		
		ChartPanel cp = new ChartPanel(chart);
		return cp;
	}
	
	/**
	 * Creates and shows a line plot made from the CategoryDataset parameter. It is a static method, and
	 * the plot will be shown in a separate, exclusive window. This window is unique, and any subsequent call
	 * will only update the plot it shows, and it will not open any new dialog.
	 * 
	 * @param dataset The data to be plotted.
	 */
	public static void showGraphic(CategoryDataset dataset) {
		if (singleton == null)
			singleton = new GraphicFrame();
		
		singleton.graphPanel.removeAll();
		singleton.graphPanel.add(chartFromDataset(dataset));
		singleton.graphPanel.revalidate();
		singleton.graphPanel.repaint();
		
		singleton.setVisible(true);
	}
	
	
	/**
	 * Shows a previously made graph in a new window.This window is unique, and any subsequent call
	 * will only update the plot it shows, and it will not open any new dialog.
	 * 
	 * @param chart A ChartPanel containing the plot to be shown.
	 */
	public static void showGraphic(ChartPanel chart) {
		if (singleton == null)
			singleton = new GraphicFrame();
		
		singleton.graphPanel.removeAll();
		singleton.graphPanel.add(chart);
		singleton.graphPanel.revalidate();
		singleton.graphPanel.repaint();
		
		singleton.setVisible(true);
	}
	
	
	/**
	 * Hides graphic window.
	 */
	public static void hideGraphic() {
		if (singleton != null)
			singleton.setVisible(false);
	}
	
}
