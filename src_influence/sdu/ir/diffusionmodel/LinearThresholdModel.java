package sdu.ir.diffusionmodel;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.input.ReadGraph;
import sdu.ir.interfaces.DiffusionModel;
import sdu.ir.interfaces.Graph;
import test.Print;
@SuppressWarnings({ "unused", "rawtypes" })
public class LinearThresholdModel implements DiffusionModel{
	private double[] thresholds;;
	private double threshold;;
	private boolean[] activated ;
	public LinearThresholdModel(double threshold) {
		this.threshold = threshold;
	}
	
	public LinearThresholdModel(Graph graph) {
		activated = new boolean[graph.size()+1];
		
	}
	
	public LinearThresholdModel(double threshold2, Graph gh) {
		this.threshold = threshold2;
		activated = new boolean[gh.size()+1];
		thresholds = new double[gh.size()+1];
//		System.out.println(threshold);
		if(threshold <= 0 ){
			for (int i = 0; i < thresholds.length; i++) {
				double a = Math.random();
//				System.out.println(a);
				thresholds[i] = a;
			}
			Print.print(thresholds,1,9);
		}else{
			for (int i = 0; i < thresholds.length; i++) {
				thresholds[i] = threshold;
			}
		}
	}

	public double diffusion(Graph graph,Set initialSet) {
		
		if(activated==null){
			activated = new boolean[graph.size()+1];
		}else{
			for (int i = 0; i < activated.length; i++) {
				activated[i] = false;
			}
		}
		for (Object obj: initialSet) {  
		      if(obj instanceof Integer){  
		    	  int aa= (Integer)obj;  
		    	  activated[aa] = true;
		      }   
		}
		
		return spread(graph,initialSet);
		
	}
	//递归的用LTM传播模型传播
	private int spread(Graph graph, Set initialSet) {
		Set thisMayInfection = new HashSet();
		for (Object obj: initialSet) {  
			if(obj instanceof Integer){  
				int i= (Integer)obj;  
				Set temp = graph.getNeighbor(i);
				if(temp != null)
					add2MayInfection(temp,thisMayInfection);
			}   
		}
//		System.out.println("initialSet--------->"+initialSet);
		boolean haveNewActived = true;
		Set addSet = new HashSet();
		Set removeSet = new HashSet();
		int cou = 1;
		while(haveNewActived){
//			System.out.println("------------round"+cou+"------------");
			haveNewActived = false;
			add2MayInfection(addSet,thisMayInfection);
			addSet.removeAll(addSet);
			removeSet.removeAll(removeSet);
			for (Object obj: thisMayInfection) {  
			      if(obj instanceof Integer){  
			    	  int aa= (Integer)obj;  
			    	  Set neiborOfaa = graph.getNeighbor(aa);
			    	  
			    	  int indedgree = neiborOfaa.size();
			    	  int neiborOfaaInfected = 0;
			    	  for (Object obj1: neiborOfaa) {  
					      if(obj1 instanceof Integer){  
					    	  int bb= (Integer)obj1;  
					    	  if(activated[bb]){
					    		  neiborOfaaInfected++;
					    	  }
					      }   
			    	  }
//			    	  System.out.println(aa+" "+neiborOfaa+" "+neiborOfaaInfected);
			    	  if((double)neiborOfaaInfected/(double)indedgree >= thresholds[aa]){
//			    		  System.out.println(aa);
			    		  haveNewActived = true;
			    		  removeSet.add(aa);
			    		  addSet.addAll(neiborOfaa);
			    	  }
			      }
			}  
			thisMayInfection.removeAll(removeSet);
			for (Object obj: removeSet) {  
			      if(obj instanceof Integer){  
			    	  int aa= (Integer)obj;  
			    	  activated[aa] = true;
			      }   
			}
			cou++;
		}
		int out = 0;
		for (int i = 0; i < activated.length; i++) {
			if(activated[i])out++;
		}
//		Print.print(activated,1);
		return out;
	}

	//本次可能被感染的加到thisMayInfection中
	private void add2MayInfection(Set temp, Set thisMayInfection) {
		for (Object obj: temp) {  
		      if(obj instanceof Integer){  
		    	  int aa= (Integer)obj;  
		    	  if(!activated[aa]){
		    		  thisMayInfection.add(aa);
		    	  }
		      }   
		}
		
	}
	public static void main(String[] args) {
//		 String filePath = "E:\\数据集\\gh1.txt";
		 String filePath = "E:\\数据集\\Email\\email.txt";
		ReadGraph rd = new ReadGraph();
		Graph graph1 = rd.readTxtFile2Graph(filePath, "AdjacentMatrix",2);
//		Print.print(graph1);
		Graph graph = rd.readTxtFile2Graph(filePath, "AdjacentListwithweight",2);
//		Print.print(graph);System.out.println(graph.size());
		LinearThresholdModel ltm = new LinearThresholdModel(-1,graph);
		Set set = new HashSet();
		set.add(105);
//		set.add(2);
//		set.add(3);
//		set.add(4);
//		set.add(5);
//		set.add(6);
//		set.add(105);
//		set.add(333);
//		set.add(8);
//		set.add(9);
		System.out.println(ltm.diffusion(graph, set));
	}

}
