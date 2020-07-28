package output;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import data.Constraint;
import data.Function;
import data.Point;
import main.Calculating;
import main.Common;
import main.Solution;

public class DrawSolution {
	private String title = "constraint area";
	private Point resPoint;
	
	public DrawSolution(Point[] areaPoints, Point resPoint) {
		this.resPoint = resPoint;
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset, areaPoints);
		Common.showFrame( new DrawChart(chart, title) );
	}
	
	private double calcC () {
		return Solution.objectiveFunction.getA() * resPoint.getX() + Solution.objectiveFunction.getB() * resPoint.getY();
	}
	
	private XYSeries createSolutionLine() {
		Function f = Solution.objectiveFunction;
		double c = calcC();
		XYSeries res = new XYSeries( f.getShortName() + " = " + Common.Round(c,2) );
		Constraint func = new Constraint( f.getA(), f.getB(), Common.C_EQU, c ); 
		
		ArrayList<Point> points = Calculating.findIntersectionPoints(new Constraint[] {func});
		for (Point point : points) {
			double x = point.getX();
			double y = point.getY();
			
			if (Math.abs(x) + Math.abs(y) != 2 * Common.INF) 
				res.add(x,y);
		}
		
		return res;
	}
	
	private XYSeries createSolutionVector() {
		Function f = Solution.objectiveFunction;
		double c1 = f.getA();
		double c2 = f.getB();
		
		XYSeries res = new XYSeries("V(" + c1 + "; " + c2 + ")");
		
		double x = resPoint.getX();
		double y = resPoint.getY();
		
		res.add(x,y);
		res.add(x + c1, y + c2);
		
		return res;
	}
	
	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries( DrawChart.createCorners() );
		dataset.addSeries( createSolutionLine() );
		dataset.addSeries( createSolutionVector() );
	
		return dataset;
	}
	
	private Color getAreaColor(int opacity) {
		return new Color(200, 200, 255, opacity);
	}
	
	private JFreeChart createChart(XYDataset dataset, Point[] polyPoints) {
		JFreeChart chart = ChartFactory.createXYLineChart(
    			Solution.objectiveFunction.toString() + " on " + title, // chart title
    			"X1", // x axis label
    			"X2", // y axis label
    			dataset, // data
    			PlotOrientation.VERTICAL,
    			true, // include legend
    			false, // tooltips
    			false // urls
    		);
		
		chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        // Draw OX and OY
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        // Draw poly point labels
        for (int i = 0; i < polyPoints.length; i++) {
        	double x = polyPoints[i].getX();
        	double y = polyPoints[i].getY();
        	if ( (Math.abs(x) != Common.INF) && (Math.abs(y) != Common.INF)) {
        		try {
    				XYAnnotation annotation = new XYTextAnnotation(polyPoints[i].toString(), x, y+1);
    				plot.addAnnotation(annotation);	
    			} catch (Exception e) {}
        	}
		}
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesPaint(2, Color.RED);
        renderer.setSeriesStroke(2, new BasicStroke(2.0f) );
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesLinesVisible(3, false);
        
        // create polygon annotation coordinates
        double[] coords = new double[polyPoints.length * 2];
        for (int i = 0; i < polyPoints.length; i++) {
        	coords[2*i] = polyPoints[i].getX();
        	coords[2*i+1] = polyPoints[i].getY();
		}
        
        XYPolygonAnnotation a = new XYPolygonAnnotation(coords, new BasicStroke(1.5f), getAreaColor(255), getAreaColor(150));
        a.setToolTipText(title);
        renderer.addAnnotation(a, Layer.BACKGROUND);
		
		return chart;
	}
	
}