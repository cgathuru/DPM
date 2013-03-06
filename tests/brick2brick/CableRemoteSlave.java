package brick2brick;


import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.TextMenu;

/**
 * Create an LCP responder to handle LCP requests. Allow the
 * User to choose between Bluetooth, USB and RS485 protocols.
 * 
 * @author Andy Shaw
 *
 */
public class CableRemoteSlave
{
    /**
     * Our local Responder class so that we can over-ride the standard
     * behaviour. We modify the disconnect action so that the thread will
     * exit.
     */
    static class Responder extends LCPResponder
    {
        Responder(NXTCommConnector con)
        {
            super(con);
        }

        protected void disconnect()
        {
            super.disconnect();
            super.shutdown();
        }
    }

    public static void main(String[] args) throws Exception{
    	
        NXTCommConnector connector = RS485.getConnector();

        LCD.clear();
        LCD.clear();
        LCD.drawString("Type: " + connector, 0, 0);
        LCD.drawString("Running...", 0, 1);
        Responder resp = new Responder(connector);
        resp.start();
        resp.join();
        LCD.drawString("Closing...  ", 0, 1);
    }
}

