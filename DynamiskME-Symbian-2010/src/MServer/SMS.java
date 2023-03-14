package MServer;

import Mwmaserver.ConnectServer;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.wireless.messaging.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */
public class SMS {
    private static MessageConnection mc;
    private NewMessageHandler newMessageHandler;
    private String DEFAULT_PORT = "16500";
    private static String portnumb;
    public static String gsmnumb = "";
    private String address;
    private String text;
    private static String debugx;
    public static boolean debugscreen = false;
    public static boolean QUIT = false;
    public static String response = " ";
    public static String request;
    public static int Maxsize = 100000;
    public static String Screentext = "";
    public static String ScreentextTemp = "";
    private static String dbgtext;
    public int antwaits = 3000;
    public int ms = 150;


    public SMS() {
        String url = "";
        try {
            //String smsc = System.getProperty("wireless.messaging.sms.smsc");
            String p = System.getProperty("WMAFW-ServerPort");
            if (p == null) {
                portnumb = DEFAULT_PORT;
            } else {
                portnumb = p.trim();
            }

            url = "sms://" + gsmnumb + ":" + portnumb;
            mc = (MessageConnection) Connector.open(url);
            newMessageHandler = new NewMessageHandler(mc);
            newMessageHandler.start();
        } catch (Exception e) {
            System.out.println("createMessageConnection Exception: " +
                               e.getMessage());
        }
    }

    public void LogToScreen(String text,int level) {
    System.out.println(text);
    if (debugscreen && level == 0) {
        if ((Screentext.length() + text.length()) <= Maxsize) {
           Screentext = Screentext + text+ "\n";
        }
        ScreentextTemp = text+ "\n";
        dbgtext = text+ "\n";
    }
}


public void SetResponse(String data) {
        if (data.length() > Maxsize) {
         data = data.substring(0, Maxsize-1);
        }
        MControll.Main_Controll.response = data;
                //.Maxsize
    }

    public boolean WaitForServer() {
    LogToScreen("******** Wait for server",0);
     boolean ok = true;
     int antwaits_count = 0;
     //LogToScreen("******** antwaits_count: " + antwaits_count,0);
     //LogToScreen("******** MControll.Main_Controll.serverreq: " + MControll.Main_Controll.serverreq,0);
     //LogToScreen("******** waits: " + waits,0);
    while (MControll.Main_Controll.serverreq == true && antwaits_count < antwaits ) {
       try {
               Thread.sleep(ms);
               //LogToScreen("******** Thread pause: " + antwaits_count,0);
              antwaits_count++;
               //Thread.yield();
                }
                    catch (Exception e) {
                      LogToScreen("******** Thread pause ERROR: " + e.getMessage(),0);
                }
      LogToScreen("******** Waits_count: " + antwaits_count,0);
       //LogToScreen("******** MControll.Main_Controll.serverreq: " + MControll.Main_Controll.serverreq,0);
       //LogToScreen("******** waits: " + waits,0);

      if (QUIT || (antwaits == antwaits_count)) {
       ok = false;
       QUIT = true;
       LogToScreen("Error Waiting: QUIT:" + QUIT + "antwaits: " +  antwaits_count,0);
      }
    }

     /************************/
     // Wait
     try {
         LogToScreen("Ge servern lite tid",0);
          Thread.sleep(500);
          }
          catch (Exception e) {
             LogToScreen("******** Thread pause ERROR: " + e.getMessage(),0);
      }


     return ok;
}


    public boolean SendSMSToGSMServer(String textin) {
        boolean ok = true;
        text = textin;
        MControll.Main_Controll.serverreq = true;
        address = "sms://" + gsmnumb + ":" + portnumb;
        try {

            Thread smssend = new Thread() {
                public void run() {

                    if (address.startsWith("sms") == false) {
                        //address = "sms://" + address;
                        address = "sms://" + gsmnumb + ":" + portnumb;
                    }
                    try {
                        System.out.println("Startar SMS Send thread");
                        TextMessage tmsg =
                                (TextMessage) mc.newMessage
                                (MessageConnection.TEXT_MESSAGE);
                        tmsg.setAddress(address);
                        tmsg.setPayloadText(text);
                        System.out.println("Klar SMS Send thread");
                        MControll.Main_Controll.serverreq = false;
                        mc.send(tmsg);

                    } catch (Exception exc) {
                        System.out.println("Could not send message (" + text +
                                           ") to " + address + " " +
                                           exc.getMessage());
                        MControll.Main_Controll.serverreq = false;
                        LogToScreen(exc.getMessage(),0);
                        SetResponse("Could not send message (" + text + ") to " + address + " " + exc.getMessage());
                        QUIT = true;
                        //ioExc.printStackTrace();
                    }
                    MControll.Main_Controll.serverreq = false;
                }
            };
            smssend.start();

        } catch (Exception exc) {
            System.out.println("Could not send message (" + text + ") to " +
                               address + " " + exc.getMessage());
            ok = false;
            //ioExc.printStackTrace();
        }
        return ok;
    }

}
