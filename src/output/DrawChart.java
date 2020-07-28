package output;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;

import main.Common;

public class DrawChart extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	public DrawChart(JFreeChart chart, String title) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(title);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(700, 700));
		setContentPane(chartPanel);
		
		pack();
		setVisible(true);
	}
	
	public static XYSeries createCorners() {
		XYSeries corners = new XYSeries("Legend:");
		
		corners.add(Common.INF, Common.INF);
		corners.add(Common.INF, -Common.INF);
		corners.add(-Common.INF, -Common.INF);
		corners.add(-Common.INF, Common.INF);
		
		return corners;
	}
}