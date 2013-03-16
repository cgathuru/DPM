package brick2brcik.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import navigaion.Launcher;

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
public final class NXTReceive {


	public static int feedback = 0;
	
	private static String slave = "Group34";
	private static NXTCommConnector connector = RS485.getConnector();
	private static NXTConnection con = connector.waitForConnection(0, NXTConnection.PACKET);
	private static DataInputStream dis = con.openDataInputStream();
    private static DataOutputStream dos = con.openDataOutputStream();
    
    public static void main(String[] args) throws IOException {
    	Launcher.drive(Motor.A, Motor.B);
    	while(true){
    		//latch the new instruction;
			try {
				feedback = dis.readInt();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//execute and then reset feedback
			if(feedback == Instructions.SHOOT){
				resetFeedback();	
			}
			if(feedback == Instructions.CLOSE_CONNECTION){
				closeConnection();
				System.exit(0);
			}
			
			//Sleep to prevent crashing
			 try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

	private static void resetFeedback() {
		feedback =0;
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

