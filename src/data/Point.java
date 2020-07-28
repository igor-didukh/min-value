package data;

import main.Common;

public class Point {
	private double x, y;
	private double angle;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	private String formCoord(double f) {
		f = Common.Round(f,2);
		String coord = "" + f;
		//String sign = (f < 0) ? "-" : ""; 
		//coord = (Math.abs(f) == Common.INF) ? sign + "inf" : coord;
		
		coord = (coord.equals("-0.0")) ? "0.0" : coord; 
		return coord;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	@Override
	public String toString() {
		return "(" + formCoord(x) + "; " + formCoord(y) + ")";
	}
}