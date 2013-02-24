package dpmSlave;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.TextMenu;

import java.io.*;

/**
 * Receive data from another NXT using RS485 using a packet based
 * connection
 * 
 * Waits for a connection, receives an int and performs
 * an action based on the  ints value.
 * 
 * Initializes all sensors and motors attached to the the slave brick.
 * 
 * Contains the main method in the salve brick
 * @author charles
 *
 */
public class NXTReceive {

	private static final int SHOOT = 10;
	private static final int COLLISION = -1;
	public static int feedback = 0;
	
	private static String slave = "Group34";
	private static NXTCommConnector connector = RS485.getConnector();
	private static NXTConnection con = connector.waitForConnection(0, NXTConnection.PACKET);
	private static DataInputStream dis = con.openDataInputStream();
    private static DataOutputStream dos = con.openDataOutputStream();
    
    public static void main(String[] args) throws IOException {
    	Launcher.drive(Motor.A, Motor.B);
    	while(true){
			try {
				feedback = dis.readInt();
				if(feedback == SHOOT){
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				feedback = 0;
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
     * 
     * @param command The action to be taken as determine from the master brick
     * @throws IOException throws input output exception
     */
    public static void send(int command) throws IOException{
    	dos.write(command);
    	dos.flush();
    }
    
    /**
     * Closes the communication between the two NXTs.
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

