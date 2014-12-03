package sdu.ir.util;

import java.util.ArrayList;
import java.util.Map;

public class AdjacencyList {
	ArrayList<Integer>[] list;
	int index;
	public AdjacencyList(int i) {
		this.index = i;
	}
	public void cal(NetProperty np, int k) {
		this.list = np.getAdList(index,k);
		
	}
	public double gain(Map<Integer, Double> map, double[] p) {
		double gain = 0;
		for (int i = 0; i < list.length; i++) {
			ArrayList<Integer> temp = list[i];
			for (int j = 0; j < temp.size(); j++) {
				int node = temp.get(j);
				gain += (1-(1-p[i])*(1-map.get(node)))-map.get(node);
//				System.out.print(gain+"->"+node+"  ");
			}
		}
		gain += 1 - map.get(index);
//		System.out.println();
		return gain;
	}
	public void refresh(Map<Integer, Double> map, double[] p) {
		for (int i = 0; i < list.length; i++) {
			ArrayList<Integer> temp = list[i];
			for (int j = 0; j < temp.size(); j++) {
				int node = temp.get(j);
				map.put(node, 1-(1-p[i])*(1-map.get(node)));
			}
		}
		map.put(index, (double) 1);
		
	}

}
