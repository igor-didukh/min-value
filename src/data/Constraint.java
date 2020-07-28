package data;

import main.Common;

public class Constraint {
	private double a, b, c;
	private String direction, name, lineName;
	
	public Constraint() {}
	
	public Constraint(double a, double b, String direction, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.direction = direction;
		name = Common.makeConstraintName(a, b, direction, c);
		lineName = Common.makeConstraintName(a, b, "=", c);
	}
	
	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public String getLineName() {
		return lineName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
