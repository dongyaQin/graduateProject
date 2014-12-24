package test;

public class MultiThread implements Runnable{

	ThreadSynchronizeTest tst;
	
	public MultiThread(ThreadSynchronizeTest tstObj) {
		tst = tstObj;
	}

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
	

	}

	@Override
	public void run() {
		System.out.println(this.hashCode());
		tst.method1();
		
	}

}
