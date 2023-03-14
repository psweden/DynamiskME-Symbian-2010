package Mwmaserver;

import javax.wireless.messaging.MessageConnection;

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
public class NewMessageHandler implements Runnable  {

    private Thread th = new Thread(this);
    private MessageConnection mc;

    public NewMessageHandler() {

    }
    public NewMessageHandler(MessageConnection mc) {
        this.mc = mc;
    }

  public void start() {// startar med anrop fran Connectserver-classen
      try {
        th.start();
      }
      catch (Exception e) {
      }

    }
    public String getSMSC() {
        return System.getProperty("wireless.messaging.sms.smsc");
    }

  /**
   * synkroniserar och startar alla trad i hela applikationen
   */
  public void run() {
  }

}
