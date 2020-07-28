package output;

import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPolygonAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import data.Constraint;
import data.Point;
import main.Calculating;
import main.Common;

public class DrawConstraint {
	
	private ArrayList<Point> polyPoints = new ArrayList<Point>();
	private String title;
	
	public DrawConstraint(Constraint constraint) {
		title = constraint.toString();
		ArrayList<Point> points = Calculating.findIntersectionPoints(new Constraint[] {constraint});
		
		XYDataset dataset = createDataset(points, constraint);
		JFreeChart chart = createChart(dataset);
		Common.showFrame( new DrawChart(chart, title) );
	}
	
	public XYDataset createDataset(ArrayList<Point> points, Constraint constraint) {
		XYSeries seriesLine = new XYSeries("Line");
		XYSeries seriesPoly = new XYSeries("Poly");
		
		for (Point point : points) {
			double x = point.getX();
			double y = point.getY();
			
			seriesPoly.add(x,y);
			polyPoints.add(point);
			
			if (Math.abs(x) + Math.abs(y) != 2 * Common.INF) 
				seriesLine.add(x,y);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(seriesLine);
		dataset.addSeries(seriesPoly);
		dataset.addSeries( DrawChart.createCorners() );

		return dataset;
	}
	
	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				title, // chart title
    			"X1", // x axis label
    			"X2", // y axis label
    			dataset, // data
    			PlotOrientation.VERTICAL,
    			false, // include legend
    			false, // tooltips
    			false // urls
    		);
		
		chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        
        // Draw OX and OY
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesLinesVisible(2, false);
        
        // Draw a polygon
        Point[] pts = Calculating.sortPointsAnticlockwise(polyPoints);
        double[] coords = new double[pts.length * 2];
        
        for (int j = 0; j < pts.length; j++) {
        	double x = pts[j].getX();
        	double y = pts[j].getY();
        	coords[2*j] = x;
        	coords[2*j+1] = y;
		}
        
        XYPolygonAnnotation a = new XYPolygonAnnotation(coords, null, null, new Color(200, 200, 255, 150));
        a.setToolTipText(title);
        renderer.addAnnotation(a, Layer.BACKGROUND);
		
		return chart;
	}
	
}