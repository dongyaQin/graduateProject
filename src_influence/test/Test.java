package test;

import java.util.Stack;

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
//		Set set = new HashSet((int) 100000000L);
////		Util.block();
//		System.out.println(set.size());
//		//calculate object size
//		try {
//			System.out.println(SizeOf.deepSizeOf(set));
//			System.out.println("size-->"+SizeOf.humanReadable(SizeOf.deepSizeOf(set)));
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		String temp ;
//		int[] test = new int[1024*1024];
//		int i = 0;
//	while(true) {
//		int[] temp1 = new int[1];
//		System.out.println(i++);
//		String temp = new String("asdfasf");;
//	}
//		String a = "1";
//		String b = "2";
//		String c = "12";
//		String d = a+"2";
//		String e = "1"+"2";
//		System.out.println(c==d);
//		System.out.println(c==e);
//		ArrayList list = new ArrayList<String>();
//		list.add(new Object());
//		list.add(1);
//		list.add("sdfa");
//		for (Object object : list) {
//			System.out.println(object);
//		}
//		PropagationProbability p = PropagationProbability.InDegree;
//		System.out.println(p==PropagationProbability.InDegree);
//		for (int i = 0; i < 10; i++) {
//			System.out.println(Util.random(0, 2));
//		}
//		System.out.println(Integer.MAX_VALUE+"\t"+Integer.MIN_VALUE);
//		System.out.println(Math.pow(2, 31));
//		System.out.println(Integer.toBinaryString(-2147483648));
//		System.out.println(Integer.toBinaryString(2147483647));
//		System.out.println(Integer.toBinaryString(-2));
//		System.out.println(Integer.toBinaryString(2));
//		long a = 2147483647;
//		System.out.println(a+1);
//		PropagationProbability pp = PropagationProbability.Constant;
//		System.out.println(pp);
		Stack<Character> s = new Stack<Character>();
		char a = 'A'+1;
		s.push(a);
		System.out.println(a);
	}

}
