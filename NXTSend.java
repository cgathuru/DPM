package dpmMaster;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.TextMenu;

import java.io.*;


/**
 * This class communicates with the slave brick. It sends the required instructions to the slave brick
 * 
 * 
 * @author charles
 *
 */
public class NXTSend extends Thread{
	public static final int SHOOT = 10;
	public static int feedback = 0;
	public static final int COLLISION = -1;

	
	private static String slave = "Group34";
	private static NXTCommConnector connector = RS485.getConnector();
	private static NXTConnection con = connector.connect(slave, NXTConnection.PACKET);
	private static DataInputStream dis = con.openDataInputStream();
    private static DataOutputStream dos = con.openDataOutputStream();
    
	/**
	 * Constantly check for new inctructions
	 */
    public void run(){
		
		while(true){
			try {
				feedback = dis.readInt();
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
    public void closeConnection(){
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
