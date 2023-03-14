package MServer;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.UDPDatagramConnection;

import java.io.*;
import MModel.CONF;
import javax.microedition.io.HttpConnection;

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
public class Server {
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



    public Server() {
       if(CONF.DEBUG.equals("1")){
           Server.url = CONF.TESTURL;
       } else {
           Server.url = CONF.INTURL;
       }
    }

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

    public void serverCMDEx(String request){
	String xurl;
        boolean testurl = false;
        request = CMDPRE + request;
        QUIT = false;
        LogToScreen("Satter serverreq till true (servern jobbar)",0);
	MControll.Main_Controll.serverreq = true;
        try {
            // Ta bort vid inte test
            LogToScreen("*-*-*-*-*-*-* START REQUEST *-*-*-*-*-*-*-*",0);
           LogToScreen("ServerCMDEx request: " + request,0);

           OutputStream out;
            InputStream in;
            StreamConnection connstream = null;
            SocketConnection connsock = null;
            if (Server.url.equals(CONF.TESTURL)) {
                testurl = true;
            }
            if (testurl) {
              xurl = Server.url + "?cmd=" + request + ""; // Test
              LogToScreen("Testurl Connector.open: " + xurl,0);
              connstream = (StreamConnection) Connector.open(xurl);
              //LogToScreen("Testurl openOutputStream: ",0);
              out = connstream.openOutputStream();
            } else {
             xurl = Server.url; // Prod
              LogToScreen("Produrl Connector.open: " + xurl,0);
             connsock = (SocketConnection) Connector.open(xurl);
             //LogToScreen("openOutputStream: ",0);
             out = connsock.openOutputStream();

            //client.setSocketOption(DELAY, 0);
            //client.setSocketOption(KEEPALIVE, 0);
           }
            //LogToScreen("ServerCMDex xurl: : " + xurl,0);

                 byte[] buf = request.getBytes();
		 dbgtext = "out.write xurl: " + xurl;
                 out.write(buf, 0, buf.length);
		  dbgtext = "out.flush xurl: " + xurl;
                 out.flush();
                 dbgtext = "out.close xurl: " + xurl;
		 out.close();

                 byte[] data = new byte[256];
		 if (testurl) {
                   // LogToScreen("openInputStream: ",0);
                    in = connstream.openInputStream();
                 } else {
                   // LogToScreen("openInputStream: ",0);
                    in = connsock.openInputStream();
                 }
		 dbgtext = "in.read xurl: " + xurl;
                 int actualLength = in.read(data);
		 dbgtext = "response new String";
                 response = new String(data, 0, actualLength);
                 LogToScreen("ServerCMDex response : " + response,1);
		 dbgtext = "response: " + response;
                 SetResponse(response);
		 dbgtext = "in.close";
                 in.close();
		 if (testurl) {
                    //LogToScreen("connstream.close: ",0);
                    connstream.close();
                 } else {
                    //LogToScreen("connsock.close: ",0);
                    connsock.close();
                 }
             //} catch (IOException ioe) {
             } catch (Exception ioe) {
                 LogToScreen(ioe.getMessage(),0);
		 SetResponse(dbgtext + " " + ioe.getMessage());
                 LogToScreen("serverCMDEx MControll.Main_Controll.serverreq = false",0);
                 MControll.Main_Controll.serverreq = false;
                 LogToScreen("*-*-*-*-*-*-* END REQUEST *-*-*-*-*-*-*-*",0);
                 QUIT = true;

			 }
             LogToScreen("ServerCMDex serverreq satts till, false (servern klar)",0);
             MControll.Main_Controll.serverreq = false;
             LogToScreen("*-*-*-*-*-*-* END REQUEST *-*-*-*-*-*-*-*",0);

    }



    /*
 http://dsc.sun.com/mobility/midp/articles/midp2network/
*/


    public void serverCMDInternal(String request){
        String xurl;
        boolean testurl = false;
        request = CMDPRE + request;
        QUIT = false;
        LogToScreen("Satter serverreq till true (servern jobbar)",0);
	MControll.Main_Controll.serverreq = true;
        try {
            // Ta bort vid inte test
            LogToScreen("*-*-*-*-*-*-* START REQUEST *-*-*-*-*-*-*-*",0);
            Server.response = " ";
            LogToScreen("serverCMDInternal request: " + request,0);
            //LogToScreen("Server.url: " + Server.url,0);
            //LogToScreen("CONF.TESTURL: " + CONF.TESTURL,0);
            OutputStream out;
            InputStream in;
            StreamConnection connstream = null;
            SocketConnection connsock = null;
            if (Server.url.equals(CONF.TESTURL)) {
                testurl = true;
            }
            if (testurl) {
              xurl = Server.url + "?cmd=" + request + ""; // Test
              LogToScreen("Testurl Connector.open: " + xurl,0);
              connstream = (StreamConnection) Connector.open(xurl);
              //LogToScreen("Testurl openOutputStream: ",0);
              out = connstream.openOutputStream();
            } else {
             xurl = Server.url; // Prod
              LogToScreen("Produrl Connector.open: " + xurl,0);
             connsock = (SocketConnection) Connector.open(xurl);
             //LogToScreen("openOutputStream: ",0);
             out = connsock.openOutputStream();

            //client.setSocketOption(DELAY, 0);
            //client.setSocketOption(KEEPALIVE, 0);
           }
            //LogToScreen("serverCMDInternal xurl: : " + xurl,0);
            Server.response = "No response on: " + request;
                 //LogToScreen("serverCMDInternal request.getBytes()" + request,0);
                 byte[] buf = request.getBytes();
                 //LogToScreen("serverCMDInternal out.write" + request,0);
                 out.write(buf, 0, buf.length);
                 //LogToScreen("serverCMDInternal out.flush" + request,0);
                 out.flush();
                 //LogToScreen("serverCMDInternal out.close" + request,0);
                 out.close();

                 byte[] data = new byte[256];
                  //LogToScreen("serverCMDInternal openInputStream" + request,0);

                 if (testurl) {
                   // LogToScreen("openInputStream: ",0);
                    in = connstream.openInputStream();
                 } else {
                   // LogToScreen("openInputStream: ",0);
                    in = connsock.openInputStream();
                 }


               int actualLength = in.read(data);
               //  int actualLength;
               //  while ( (actualLength = in.read()) != -1) {
                //    LogToScreen("serverCMDInternal actualLength" + actualLength,0);
                // }
                 if (actualLength < 1) {
                 Server.response = "NODATA";
                 } else {
                 Server.response = new String(data, 0, actualLength);
                 }
                 LogToScreen("serverCMDInternal response : " + Server.response,1);
                 in.close();
                 if (testurl) {
                    //LogToScreen("connstream.close: ",0);
                    connstream.close();
                 } else {
                    //LogToScreen("connsock.close: ",0);
                    connsock.close();
                 }

             //} catch (IOException ioe) {
                 } catch (Exception ioe) {
                 LogToScreen(ioe.getMessage(),0);
                 Server.response = ioe.getMessage();
                 LogToScreen("serverCMDInternal ERROR: " + Server.response ,0);
                 MControll.Main_Controll.serverreq = false;
                 LogToScreen("*-*-*-*-*-*-* END REQUEST *-*-*-*-*-*-*-*",0);
                 QUIT = true;

             }
             LogToScreen("serverCMDInternal serverreq satts till, false (servern klar)",0);
             MControll.Main_Controll.serverreq = false;
             LogToScreen("*-*-*-*-*-*-* END REQUEST *-*-*-*-*-*-*-*",0);

    }




    /* ==================== SERVER Debug ======================================== */

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

public void sendLogdataExt() {
          try {
              LogToScreen("SendlogdataExt",0);
              MModel.CONF conf = new CONF();
              String ext_url = CONF.EXTURL;
              conf = null;
              LogToScreen("ext_url: " + ext_url,0);

              HttpConnection conn = (HttpConnection)
                                    Connector.open(ext_url);
              String submitstring = "imei=" + URLEncode(imei) + "&confdata=" +
                                    URLEncode(confdata) + "&logdata=" +
                                    URLEncode(logdata) + "&logfilesize=" +
                                    logfilesize + "&icount=" + icount +
                                    "&Submit=Submit";
              LogToScreen("submitstring: " + submitstring,0);
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
                Thread.sleep(500);
                }
                catch (Exception e) {
                   LogToScreen("******** Thread pause ERROR: " + e.getMessage(),0);
            }


           return ok;
      }


      public boolean WaitForServerInt() {
          LogToScreen("******** Wait for server internal",0);
           boolean ok = true;
           int antwaits_count = 0;
          while (MESSAGEINT && antwaits_count < antwaits ) {
             try {
                     Thread.sleep(ms);
                    antwaits_count++;
                      }
                          catch (Exception e) {
                            LogToScreen("******** Thread pause ERROR: " + e.getMessage(),0);
                      }
            LogToScreen("******** INT Waits_count: " + antwaits_count + " MESSAGEINT" + MESSAGEINT,0);

            if (QUIT || (antwaits == antwaits_count)) {
             ok = false;
             QUIT = true;
             LogToScreen("Error Waiting INT: QUIT:" + QUIT + "antwaits: " +  antwaits_count,0);
            }
          }
           LogToScreen("******** INT Waits Finished: MESSAGEINT: " + MESSAGEINT,0);
           return ok;
      }


      public void sendLogdata() {
          LogToScreen("Send logdata: ",0);
          logdata = "";
          LogToScreen("Hamtar IMEI: ",0);
          sendMessageInt("k,IMEI,", IMEIDATA);
          if (WaitForServer()) {
            LogToScreen("Hamtar Confdata: ",0);
            sendMessageInt("i,", CONFDATA);
            if (WaitForServer()) {
                LogToScreen("Hamtar Logsize: ",0);
                sendMessageInt("u", LOGSIZE);
                if (WaitForServer()) {
                    LogToScreen("Hamtar Logdata: ",0);
                    GetLogDataFromServer();
                    LogToScreen("Call SendlogdataExt",0);
                    sendLogdataExt();
                } else {SetResponse("Error fetching logdatasize");}
            } else {SetResponse("Error fetching confdata");}
          } else {SetResponse("Error fetching IMEI data");}

      }


	  public void getLogdata() {

          if (WORKING) {
            LogToScreen("getLogdata Already working: ",0);
            SetResponse("getLogdata already in progress please wait...\n");
          return;
          }

          WORKING = true;

          LogToScreen("Get logdata: ",0);
          LogToScreen("Hamtar IMEI: ",0);
          sendMessageInt("k,IMEI,", IMEIDATA);
          if (WaitForServer()) {
            LogToScreen("Hamtar Confdata: ",0);
            sendMessageInt("i,", CONFDATA);
            if (WaitForServerInt()) {
                LogToScreen("Hamtar Logsize: ",0);
                sendMessageInt("u", LOGSIZE);
                if (WaitForServerInt()) {
                    LogToScreen("Hamtar Logdata: ",0);
                    GetLogDataFromServer();
                    SetResponse("** CONFDATA ***\n" + confdata + "\n*** LOGDATA***\n" + logdata + "\n");
              } else {SetResponse("Error fetching logdatasize");}
            } else {SetResponse("Error fetching confdata");}
          } else {SetResponse("Error fetching IMEI data");}
          WORKING = false;
          }


	  public void GetLogDataFromServer() {
		  int bufsize = 256;
		  logdata = "";
                  icount = 0;
                  LogToScreen("GetLogDataFromServer : MControll.Main_Controll.serverreq = true",0);
		  MControll.Main_Controll.serverreq = true;
		  /*********************/
		  //Loopar och hamtar logdata 256 bytes i taget tills
		  // man har kort igenom hela logfilesize
		  //for varje request till sendMessageInt maste man veta att den gatt klart
		  //Det kollas med MControll.Main_Controll.serverreq om den ar false har den requesten gatt klart
		 LogToScreen("GetLogDataFromServer: icount: " + icount + " logfilesize: " + logfilesize,0);

                for (icount = 0; ((icount <= logfilesize && !QUIT)); icount = icount + bufsize) {
                    if ((logfilesize < bufsize) && ((icount+bufsize) > logfilesize)) {
                        LogToScreen("Kan inte hämta mer data: logfilesize:" + logfilesize + " icount:" + icount,0);
                        QUIT = true;
                       Server.logdata = Server.logdata + "Preamature end of logfile \nicount: " + icount + "\nlogfilesize" + logfilesize;
                       MControll.Main_Controll.serverreq = true;
                    }
                    else {
                    logrequest = "j," + icount + ",";
                        LogToScreen("logrequest: " + logrequest + " av (" + logfilesize + ")" ,0);
			  //MControll.Main_Controll.serverreq = true;
                        sendMessageInt(logrequest, LOGDATA);
			  //Thread.currentThread().sleep(2000);
			WaitForServerInt();
                }
                }


	  }




      public void sendMessageInt(String message, int what) {
          //    try {
        LogToScreen("SendMessageInt: message: " + message + " what: " + what,0);
	dbgtext = "";
	LogToScreen("sendMessageInt : MControll.Main_Controll.serverreq = true",0);
        MControll.Main_Controll.serverreq = true;
        Server.response = "";
        request = message;
        dbgtext = message;
	whatx = what;
        //MESSAGEINT = true;
        LogToScreen("MESSAGEINT = true " + MESSAGEINT,0);
	/*
        new Thread() {
            public void run() {
         */
            MESSAGEINT = true;
            LogToScreen("sendMessageInt Working " + MESSAGEINT,0);
            LogToScreen("Anropar serverCMDInternal: " + request,0);
            serverCMDInternal(request);
            if (WaitForServer()) {
            LogToScreen("Läser Server.response: " + Server.response,1);
            LogToScreen("Server.whatx: " + Server.whatx,0);
             switch (Server.whatx) {
		case CONFDATA:
              Server.confdata = Server.response;
              LogToScreen("smi->confdata: " + Server.confdata,1);
              break;
           case LOGDATA:
              Server.logdata = Server.logdata + Server.response;
              //LogToScreen("logdata: " + logdata,1);
              break;
          case IMEIDATA:
              Server.imei = Server.response;
              LogToScreen("smi->imei: " + imei,0);
              break;
          case SWTBNR:
              Server.swtbnr = Server.response;
              LogToScreen("smi->SWTBNR: " + swtbnr,0);
              break;
          case LA:
              Server.la = Server.response;
              LogToScreen("smi->LA: " + la,0);
              break;
          case CCODE:
              Server.ccode = Server.response;
              LogToScreen("smi->CCODE: " + ccode,0);
              break;
          case VM:
              Server.vm = Server.response;
              LogToScreen("smi->VM: " + vm,0);
              break;
          case IDDCODE:
              Server.iddcode = Server.response;
              LogToScreen("smi->IDDCODE: " + iddcode,0);
              break;
          case AREACODEREMOVE:
              Server.areacoderemove = Server.response;
              LogToScreen("smi->AREACODEREMOVE: " + areacoderemove,0);
              break;
          case LOGSIZE:
              LogToScreen("smi->logfilesize: " + Server.response,0);
			  try {
              Server.logfilesize = Integer.parseInt(Server.response.trim());
              LogToScreen("smi->logfilesize: " + Server.logfilesize,0);
			  } catch (NumberFormatException e) {
                          LogToScreen("smi->logfilesize error set to 0 Server.response: " + Server.response.trim(),0);
			  Server.logfilesize = 0;
                          Server.QUIT = true;
                          MESSAGEINT = false;
                          LogToScreen("sendMessageInt Finished1 " + MESSAGEINT,0);
			  }
              break;
           default:
              LogToScreen("smi->default: " + Server.response,0);
               break;
          }
             MESSAGEINT = false;
            }
             else {
             LogToScreen("Error running serverCMDInternal: " + Server.response,1);
             Server.QUIT = true;

             MESSAGEINT = false;
             LogToScreen("sendMessageInt Finished2 " + MESSAGEINT,0);
             }

        /*
         }
        }.start();
        */
        }


}
