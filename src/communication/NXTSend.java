package communication;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.TextMenu;

import java.io.*;

import navigaion.Navigation;


/**
 * This class communicates with the slave brick. It sends the required instructions to the slave brick
 * 
 * 
 * @author charles
 *
 */
public final class NXTSend implements Runnable{
	public static int feedback = 0;
	private Navigation nav;

	public NXTSend(Navigation nav){
		this.nav =nav;
	}
	
	private static String slave = "Group34";
	private static NXTCommConnector connector = RS485.getConnector();
	private static NXTConnection con = connector.connect(slave, NXTConnection.PACKET);
	private static DataInputStream dis = con.openDataInputStream();
    private static DataOutputStream dos = con.openDataOutputStream();
    
	/**
	 * Constantly check for new instructions
	 */
    public void run(){
		
		while(true){
			try {
				feedback = dis.readInt();		
				update();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
	protected void update() {
		switch(feedback){
		case Instructions.COLLISION:
			nav.stopMoving();
			break;
		}
	}
    /**
     * Issues an instruction to the slave brick
     * @param command The command
     * @throws IOException Throws and input-output exception
     */
    public static void send(int command) throws IOException{
    	dos.write(command);
    	dos.flush();
    }
    
    /**
     * Closes the communication between the NXT master brick and its slave 
     */
    public static void closeConnection(){
    	 try {
			dis.close();
			dos.close();
	        con.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
    }
}
