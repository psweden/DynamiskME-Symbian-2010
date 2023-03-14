package MServer;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.UDPDatagramConnection;
import java.io.*;
import MModel.CONF;
import javax.microedition.io.HttpConnection;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */




public class IP {
    public static String port;
  // private static String url = "socket://127.0.0.1:8100"; // Produrl
    public static String url; // Produrl
    private static String debugx;
    public static boolean debugscreen = false;
    public static boolean QUIT = false;

    // private static String url =  "http://www.mobisma.com:80/socketapi/mobismaSRV.php"; //Testurl
    public static String response = " ";
    public static String request;
    public static int Maxsize = 100000;
    //private static String url = "socket://127.0.0.1:8100";

    // Portnummer for TCP/IP connection
    // private static String url = "socket://127.0.0.1:8100";

    public static final int NONE = 0;
    public static final int CONFDATA = 1;
    public static final int LOGDATA = 2;
    public static final int IMEIDATA = 3;
    public static final int LOGSIZE = 4;
    public static final int SWTBNR = 5;
    public static final int LA = 6;
    public static final int CCODE = 7;
    public static final int VM = 8;
    public static final int IDDCODE = 9;
    public static final int AREACODEREMOVE = 10;
    public static final String CMDPRE = "*";

    public static String CPYLOG ="E:\\mobismadebug.txt";

    public static String swtbnr;
    public static String la;
    public static String ccode;
    public static String vm;
    public static String iddcode;
    public static String areacoderemove;

    public static String confdata;
    public static String logdata;
	//private static String responsex;
    public static int logfilesize;
    public static int icount;
    public static int whatx;
    private static String logrequest;
    private static String imei;
    private static String dbgtext;
    public static String Screentext = "";
    public static String ScreentextTemp = "";
    public static boolean WORKING = false;
    public static boolean MESSAGEINT = false;
    //public boolean WORKING = false;
    //public boolean MESSAGEINT = false;
    public int antwaits = 3000;
    public int ms = 150;

    public void SetResponse(String data) {
        if (data.length() > Maxsize) {
         data = data.substring(0, Maxsize-1);
        }
        MControll.Main_Controll.response = data;
                //.Maxsize
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


    
    public void serverCMD(String message) {
        LogToScreen("Satter serverreq till true (servern jobbar)",0);
	MControll.Main_Controll.serverreq = true;
        dbgtext = "";
        request = message;
        dbgtext = message;
		new Thread() {
            public void run() {
            LogToScreen("Anropar serverCMDex",0);
             dbgtext = "serverCMDEx";
			 serverCMDEx(request);
            }
        }.start();
    }

    public void serverCMDEx(String submitstring){
	 try {
              LogToScreen("SendlogdataExt",0);
              MModel.CONF conf = new CONF();
              String ext_url = url;
              conf = null;
              LogToScreen("ext_url: " + ext_url,0);

              HttpConnection conn = (HttpConnection)
                                    Connector.open(ext_url);
              /*
              String submitstring = "imei=" + URLEncode(imei) + "&confdata=" +
                                    URLEncode(confdata) + "&logdata=" +
                                    URLEncode(logdata) + "&logfilesize=" +
                                    logfilesize + "&icount=" + icount +
                                    "&Submit=Submit";
              LogToScreen("submitstring: " + submitstring,0);
              */
              byte[] data = submitstring.getBytes();

              conn.setRequestMethod(HttpConnection.POST);
              conn.setRequestProperty("User-Agent",
                                      "Profile/MIDP-1.0 Configuration/CLDC-1.0");
              conn.setRequestProperty("Content-Language", "en-US");
              conn.setRequestProperty("Content-Type",
                                      "application/x-www-form-urlencoded");
              OutputStream os = conn.openOutputStream();
              os.write(data);
              os.close();

              byte[] data2 = new byte[2048];
              InputStream in = conn.openInputStream();
              int actualLength = in.read(data2);
              response = new String(data2, 0, actualLength);
              SetResponse(response);

              // setAlertMEXONOFF(response);
              in.close();
              conn.close();
              MControll.Main_Controll.serverreq = false;

          //} catch (IOException ioe) {
          } catch (Exception ioe) {
              MControll.Main_Controll.serverreq = false;
              SetResponse(ioe.getMessage());
              LogToScreen("SendlogdataExt" + ioe.getMessage(),0);
              QUIT = true;
          }
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
                Thread.sleep(1000);
                }
                catch (Exception e) {
                   LogToScreen("******** Thread pause ERROR: " + e.getMessage(),0);
            }


           return ok;
      }

    private String URLEncode(String s) {

          StringBuffer sbuf = new StringBuffer();
          int ch;
          for (int i = 0; i < s.length(); i++) {
              ch = s.charAt(i);
              switch (ch) {
              case ' ': {
                  sbuf.append("+");
                  break;
              }
              case '!': {
                  sbuf.append("%21");
                  break;
              }
              case '*': {
                  sbuf.append("%2A");
                  break;
              }
              case '\'': {
                  sbuf.append("%27");
                  break;
              }
              case '(': {
                  sbuf.append("%28");
                  break;
              }
              case ')': {
                  sbuf.append("%29");
                  break;
              }
              case ';': {
                  sbuf.append("%3B");
                  break;
              }
              case ':': {
                  sbuf.append("%3A");
                  break;
              }
              case '@': {
                  sbuf.append("%40");
                  break;
              }
              case '&': {
                  sbuf.append("%26");
                  break;
              }
              case '=': {
                  sbuf.append("%3D");
                  break;
              }
              case '+': {
                  sbuf.append("%2B");
                  break;
              }
              case '$': {
                  sbuf.append("%24");
                  break;
              }
              case ',': {
                  sbuf.append("%2C");
                  break;
              }
              case '/': {
                  sbuf.append("%2F");
                  break;
              }
              case '?': {
                  sbuf.append("%3F");
                  break;
              }
              case '%': {
                  sbuf.append("%25");
                  break;
              }
              case '#': {
                  sbuf.append("%23");
                  break;
              }
              case '[': {
                  sbuf.append("%5B");
                  break;
              }
              case ']': {
                  sbuf.append("%5D");
                  break;
              }
              default:
                  sbuf.append((char) ch);
              }
          }
          return sbuf.toString();
      }
}
