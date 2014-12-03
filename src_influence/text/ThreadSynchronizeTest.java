package text;

public class ThreadSynchronizeTest {

	/**
	 * @param args
	 */
	
	
	
	public static void main(String[] args) {
		ThreadSynchronizeTest tstObj = new ThreadSynchronizeTest();
		ThreadSynchronizeTest tstObj2 = new ThreadSynchronizeTest();
		MultiThread mt = new MultiThread(tstObj);
		MultiThread mt2 = new MultiThread(tstObj2);
		Thread t1 = new Thread(mt);
		Thread t2 = new Thread(mt2);
		System.out.println("----"+t1.getId()+"\t"+t1.getName());
		System.out.println("----"+t2.getId()+"\t"+t2.getName());
		t1.start();
		System.out.println("t1 start!");
		t2.start();
		System.out.println("t2 start!");

	}

	public synchronized void method1() {
		System.out.println("method1 come in!");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("method1 come out!");
	}
	
	public  void method2() {
		System.out.println("method1 come in!");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("method1 come out!");
	}

}
