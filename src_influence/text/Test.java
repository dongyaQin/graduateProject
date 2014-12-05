package text;

import java.util.HashSet;
import java.util.Set;

import sdu.ir.util.Util;

import net.sourceforge.sizeof.SizeOf;

public class Test {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		/*StringBuffer sb = new StringBuffer();
		File file = new File("submodular.edges");
		System.out.println(file.exists());
		if(file.exists())file.delete();
		for (int i = 1; i < 10; i++) {
			sb.append(i+"\t"+(i+9)+"\n");
		}
		for (int i = 10; i < 19; i++) {
//			int a = ((i-1)/8)+65;
			sb.append(i+"\t"+19+"\n");
			
		}
//		sb.append("65\t69\n");
//		sb.append("66\t69\n");
//		sb.append("67\t69\n");
//		sb.append("68\t69");
		AppendFile.append("submodular.edges", sb.toString());*/
//		int[] a = new int[100];
//		int i= 0;
//		while(true){
//			for (int j = 0; j < 100000000; j++) {
//				
//			}
//			Thread.sleep(10);
//		}
//		for (double j = 0.01; j < 0.3; j = j+0.01) {
//			System.out.print(String.format("%.2f",j)+",");
//		}
		//configuration steps
//		SizeOf.skipStaticField(true);
//		SizeOf.setMinSizeToLog(10);
		Set set = new HashSet((int) 100000000L);
//		Util.block();
		System.out.println(set.size());
		//calculate object size
		try {
			System.out.println(SizeOf.deepSizeOf(set));
			System.out.println("size-->"+SizeOf.humanReadable(SizeOf.deepSizeOf(set)));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}

}
