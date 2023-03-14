package Mwmaserver;

/**
 * <p>Title: Mobile Extesion</p>
 *
 * <p>Description: All PBX Include</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: mobisma ab</p>
 *
 * @author Peter Albertsson
 * @version 2.0
 */

import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.wireless.messaging.*;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.RecordStoreException;
import MControll.Main_Controll;

public class ConnectServer {

   private String DEFAULT_PORT = "16500";
   private MessageConnection mc;
   private NewMessageHandler newMessageHandler;
   private static ConnectServer instance;
   private MControll.Main_Controll main;

  public ConnectServer() throws RecordStoreNotOpenException,
      InvalidRecordIDException, RecordStoreException {
    this.newMessageHandler = newMessageHandler;
  }
  public ConnectServer(Display display) {
  }

  public static ConnectServer getInstance(Display d) {

        if (instance == null) instance = new ConnectServer(d);
        return instance;
    }
    public String getSMSC() {
    return System.getProperty("wireless.messaging.sms.smsc");
    }

  public void createServerConnection(String urlNumber)  {
        String url = urlNumber;
        if (mc == null) {
            try {
                String smsc = System.getProperty("wireless.messaging.sms.smsc");
                String p = System.getProperty("WMAFW-ServerPort");
                if (p == null)
                    url = "sms://"+ url + DEFAULT_PORT;
                else
                    url = "sms://" + url + ":"+p.trim();
                    mc = (MessageConnection) Connector.open(url);
                    newMessageHandler = new NewMessageHandler(mc);
                    newMessageHandler.start();

            } catch (Exception e) {
                System.out.println("createMessageConnection Exception: " + e);
            }

        }

    }

    public void sendTextMessage(String status, String smsAdress) {

   String address = smsAdress;
    String message = status;

        if (address.startsWith("sms") == false) {
            address = "sms://" + address;
        }
        try {
            TextMessage tmsg = (TextMessage)mc.newMessage(MessageConnection.TEXT_MESSAGE);

            if (address != null) tmsg.setAddress(address);
            tmsg.setPayloadText(message);
            // Make sure the message can be sent before trying...
            int segcount = mc.numberOfSegments(tmsg);
            if (segcount == 0) {
                //  Message is too long for the underlying protocol...
                alertSegmentationError();
            } else
                //  Send the message.
                mc.send(tmsg);

        } catch(Exception e) {
            //  Handle the exception...
            System.out.println("Exception during sendTextMessage " + e.hashCode());
        }

        System.out.println("Attribut skickat till CT-Server!!!");
    }

  /**
   * alertSegmentationError
   */
  private void alertSegmentationError() {
  }
}

