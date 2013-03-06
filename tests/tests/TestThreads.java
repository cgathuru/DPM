package tests;

public class TestThreads {
	
	

	/**
	 * @param args
	 */
	public static void main(String []args){
		final int NUMBER_OF_TESTS= 5;
		PrintingExtend[] test = new PrintingExtend[NUMBER_OF_TESTS];
		for(int i=0; i<NUMBER_OF_TESTS;i++){
			test[i] = new PrintingExtend(i);
		}
		for(int i=0; i<NUMBER_OF_TESTS;i++){
			/*final PrintingExtend tester = test[i];
			new Thread(){
				@Override
				public void run(){
					tester.run();
				}
			};
			*/
			test[i].run();
		}
	}

}

class PrintingExtend extends Thread{
	
	private String name; 
	
	public PrintingExtend(int counter){
		name = "Thread" + counter;
	}

	@Override
	public void run() {
		long i =0;
		while(true){	
			System.out.println(name);
			i++;
		
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}