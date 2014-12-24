package test;

public class A1 {
	public static final int i=1;
	int[] test = new int[1024];
    static{
    	System.out.println("A");
    	}
    {
    	System.out.println("B");
    }
	public A1(){
	    System.out.print(2);
	}
	public static void main(String[] args) {
		 int i=A1.i;
		 new A1();
		 new A1();
	}
}
