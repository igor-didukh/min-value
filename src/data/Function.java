package data;

import main.Common;

public class Function {
	double a, b;
	String direction;
	String name;
	
	public Function() {}
	
	public Function(double a, double b, String direction) {
		this.a = a;
		this.b = b;
		this.direction = direction;
		name = Common.makeBinomialName(a, b, direction);
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getShortName() {
		return name.substring(0, name.length() - direction.length());
	}

	@Override
	public String toString() {
		return name;
	}

}
