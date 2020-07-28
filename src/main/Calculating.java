package main;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import data.Constraint;
import data.Function;
import data.Point;
import output.DrawSolution;
import output.InfoWindow;

public class Calculating {
	private static String output = "";
	
	public static Point[] sortPointsAnticlockwise(ArrayList<Point> aPoints) {
		int size = aPoints.size();
		Point[] srcPoints = new Point[size];
		Point[] workPoints = new Point[size];
		
		for (int i = 0; i < size; i++) {
			Point p = aPoints.get(i);
			double x = p.getX();
			double y = p.getY();
			srcPoints[i] = new Point(x,y);
			workPoints[i] = new Point(x,y);
		}
		
		// find center of gravity of polygon
		double sx = 0;
		double sy = 0;
		
		for (Point point : workPoints) {
			sx += point.getX();
			sy += point.getY();
		}
		
		double xCenter = sx / workPoints.length;
		double yCenter = sy / workPoints.length;
		
		// move coordinate system to new position
		for (int i = 0; i < workPoints.length; i++) {
			workPoints[i].setX(workPoints[i].getX() - xCenter);
			workPoints[i].setY(workPoints[i].getY() - yCenter);
		}
		
		// calc polar angle for every point 
		for (Point point : workPoints) {
			double x = point.getX();
			double y = point.getY();
			double len = Math.sqrt(x*x + y*y);
			
			double angle = Math.acos(x / len);
			angle = (y >=0) ? angle: -angle;
			point.setAngle(angle);
		}
		
		// sort points by angle (ascending)
		for (int i = 0; i < workPoints.length - 1; i++) {
			double minAngle = workPoints[i].getAngle();
			int minInd = i;
			
			for (int j = i + 1; j < workPoints.length; j++) {
				double angle = workPoints[j].getAngle();
				if (angle < minAngle) {
					minAngle = angle;
					minInd = j;
				}
			}
			
			Point bWorkPoint = workPoints[i];
			workPoints[i] = workPoints[minInd];
			workPoints[minInd] = bWorkPoint;
			
			Point bSrcPoint = srcPoints[i];
			srcPoints[i] = srcPoints[minInd];
			srcPoints[minInd] = bSrcPoint;
		}
		
		return srcPoints;
	}
	
	private static double Determinant(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}
	
	private static Point findOnePoint(Constraint cons1, Constraint cons2) {
		Point p = null;
		double a1 = cons1.getA(); double b1 = cons1.getB(); double c1 = cons1.getC();
		double a2 = cons2.getA(); double b2 = cons2.getB(); double c2 = cons2.getC();
		
		double D = Determinant(a1, b1, a2, b2); 
		if ( D != 0 ) {
			double x = Determinant(c1, b1, c2, b2) / D;
			double y = Determinant(a1, c1, a2, c2) / D;
			p = new Point(x,y);
		}
		
		return p;
	}
	
	protected static boolean oneCondition(Point p, Constraint cons) {
		boolean res = false;
		
		double left = cons.getA() * p.getX() + cons.getB() * p.getY();
		double c = cons.getC();
		
		switch (cons.getDirection()) {
		case Common.C_LOE:
			if (left <= c)
				res = true;
			break;
		case Common.C_GOE:
			if (left >= c)
				res = true;
			break;
		case Common.C_EQU:
			if (left == c)
				res = true;
			break;
		default:
			break;
		}
		
		return res;
	}
	
	private static boolean isPointInArea(Point p, Constraint[] constraints) {
		int res = 0;
		
		for (int i = 0; i < constraints.length; i++)
			if ( oneCondition(p, constraints[i]) ) 
				res++;
			
		return (res == constraints.length);
	}
	
	public static ArrayList<Point> findIntersectionPoints(Constraint[] cons) {
		int size = cons.length;
		Constraint[] constraints = new Constraint[ size + 4 ];
		
		for (int i = 0; i < size; i++) 
			constraints[i] = cons[i];
		
		constraints[size] = new Constraint(0, 1, Common.C_GOE, -Common.INF);
		constraints[size + 1] = new Constraint(0, 1, Common.C_LOE, Common.INF);
		constraints[size + 2] = new Constraint(1, 0, Common.C_LOE, Common.INF);
		constraints[size + 3] = new Constraint(1, 0, Common.C_GOE, -Common.INF);
		
		output = "Intersection points:\n";
		ArrayList<Point> ps = new ArrayList<Point>();
		
		for (int i = 0; i < constraints.length - 1; i++)
			for (int j = i + 1; j < constraints.length; j++) {
				Point p = findOnePoint( constraints[i], constraints[j] );
				
				if (p != null)
					if ( isPointInArea(p, cons) )
						if ( (Math.abs(p.getX()) <= Common.INF) && (Math.abs(p.getY()) <= Common.INF)) {
							output += constraints[i].getLineName() + " and " + constraints[j].getLineName() + " ---> ";
							output += p + "\n";
							ps.add(p);
						}
			}
		
		return ps;
	}
	
	private static Point[] turnCoordinates(ArrayList<Point> points) {
		double a = Solution.objectiveFunction.getA();
		double b = Solution.objectiveFunction.getB();
		double len = (double) Math.sqrt(a * a + b * b);
		
		double cos = a / len;
		double sin = b / len;
		
		Point[] newPoints = new Point[points.size()]; 
		
		output += "\nAfter rotation of the coordinate system:\n";
		
		for (int i = 0; i < points.size(); i++) {
			double x = points.get(i).getX();
			double y = points.get(i).getY();
			
			double x1 = cos * x + sin * y;
			double y1 = -sin * x + cos * y;
			
			newPoints[i] = new Point(x1, y1);
			output += newPoints[i] + "\n";
		}
		
		return newPoints;
	}
	
	private static int findMinMaxIndex(Point[] ps) {
		double minX = ps[0].getX();
		int minInd = 0;
		
		double maxX = ps[0].getX();
		int maxInd = 0;
		
		
		for (int i = 1; i < ps.length; i++) {
			double x = ps[i].getX();
			
			if (x < minX) {
				minX = x;
				minInd = i;
			}
			
			if (x > maxX) {
				maxX = x;
				maxInd = i;
			}
		}
		
		if (Solution.objectiveFunction.getDirection() == Common.F_MIN)
			return minInd;
		else
			return maxInd;
		
	}
	
	public static void solve(DefaultListModel<Constraint> listModel) {
		output = "";
		
		int size = listModel.size();
		Constraint[] consFromTask = new Constraint[size];
		for (int i = 0; i < size; i++)
			consFromTask[i] = listModel.getElementAt(i);
		
		ArrayList<Point> points = findIntersectionPoints(consFromTask); 
		int index = findMinMaxIndex( turnCoordinates(points) );
		
		output += "\n";
		Point p = points.get(index);
		
		if ((p.getX() == Common.INF) || (p.getY() == Common.INF)) {
			output += "The problem has no solution!";
			Common.showFrame( new InfoWindow(output) );
		} else {
			Function f = Solution.objectiveFunction;
			double sol = f.getA() * p.getX() + f.getB() * p.getY(); 
			output += f + ": " + p + "\n";
			output += "F" + p + " = " + sol;
			
			Common.showFrame( new InfoWindow(output) );
			Point[] areaPoints = sortPointsAnticlockwise(points);
			new DrawSolution(areaPoints, p);
		}
	}
}
