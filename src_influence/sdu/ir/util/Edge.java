package sdu.ir.util;

public class Edge {
	
	int i;
	int j;
	double betweenness;
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public double getBetweenness() {
		return betweenness;
	}
	public void setBetweenness(double betweenness) {
		this.betweenness = betweenness;
	}
	
	public Edge(int i2, int j2, double max) {
		i = i2;
		j = j2;
		betweenness = max;
	}
}
