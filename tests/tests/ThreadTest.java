package tests;

public class ThreadTest {

	public static void main(String []args){
		Printing printer = new Printing();
		Thread[] test = new Thread[20];
		for(int i=0; i<20;i++){
			test[i] = new Thread(printer);
		}
		for(int i=0; i<20;i++){
			test[i].run();
		}
	}

}

class Printing implements Runnable{

	@Override
	public void run() {
		long i =0;
		while(true){	
			System.out.println(Thread.currentThread());
			i++;
		}
		
	}
	
}
