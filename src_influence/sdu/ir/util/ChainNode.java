package sdu.ir.util;

public class ChainNode {
	private int i;
	private int weight;
	private double betweenness;
	public ChainNode(int i, int k) {
		this.i = i;
		this.weight = k;
	}
	public int getNumber() {
		return i;
	}
	public void setWeight(int weight) {
		this.weight = weight;
		
	}
	public int getWeight() {
		return weight;
	}
	@Override
	public String toString() {
		
		return i+"";
	}
	public void setBetweenness(double betweenness) {
		this.betweenness = betweenness;
	}
	public double getBetweenness() {
		return betweenness;
	}
	
	
}
