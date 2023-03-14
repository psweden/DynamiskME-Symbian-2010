package MControll;

/**
 * <p>Title: Mobile Extension</p>
 *
 * <p>Description:(Native) Panasonic PBX Include </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: mobisma ab</p>
 *
 * @author Peter Albertsson
 * @version 2.0
 */

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.*;
import java.util.Vector;
import java.util.Date;
import MServer.Server;
import java.util.Date;
import java.util.Calendar;
import MServer.IP;
import MServer.SMS;
import MLicense.Date_Time;
import MControll.CTIP.*;

/* Model */
import MDataStore.DataBase_RMS;
import MModel.CONF;
import MModel.LANG;
import MModel.StringUtil;
import Mwmaserver.ConnectServer;
import Mmenyobject.MenyObjekt;
import java.util.Hashtable;

/* Klassen Main_Controll startar har. */
public class Main_Controll extends MIDlet implements
        Runnable,CommandListener  {

    /*
     - Konfigueringsfilen CONF och Language --- initierar programmets
         - Sprak, Ikoner och annan information.
     */

    ItemStateListener listener;
    private Mmenyobject.MenyObjekt mobj;
    private MModel.CONF conf;
    private MLicense.Date_Time dateTime;
    public MDataStore.DataBase_RMS rms;
    public MServer.Server server;
    public MServer.SMS smsserver;
    public MServer.IP ipserver;
    public Alert alertgen;
    public Ticker statusTicker;

    public static final int CONFDATA = 1;
    public static final int LOGDATA = 2;
    public static final int IMEIDATA = 3;
    public static final int LOGSIZE = 4;
    public int[] currentform = new int[5];
    public static String [] tickertext = new String[4];
    public static String tickertextfinal = "";
    public Date currentTime;
    public Calendar cal;
    private static String confdata;
    private static String logdata;
    private static String imei;
    private static int logfilesize;
    private static int icount;
    private static String logrequest;
    public static String response;

    private ConnectServer wma_server;
    public ChoiceGroup radioButtons = new ChoiceGroup("", Choice.EXCLUSIVE);
    private ChoiceGroup editButtons = new ChoiceGroup("", Choice.EXCLUSIVE);
    private int defaultIndex, editButtonIndex;
    public int IDInternNumber;
    public String dbP;

    public boolean isInitialized;
    public boolean splashIsShown;
    public static boolean serverreq = false;
    public static boolean sendlogreq = false;
    public String ViewDateString = "";
    public String TWO = "";

    Command thCmd;

    private String
    /*Tid och Datum*/
    setYear_30DAY, setDay_30DAY, setMounth_30DAY, setMounthName_30DAY,
    setDay_TODAY, setMounth_TODAY, setYear_TODAY, setMounthNameToday;

    /* ovriga settings som Display, Alert, Commands och Form osv. */
    private Display display;

    /* Konstruktorn startar har. */

    public Main_Controll() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException, IOException {

        //------------ kontrollerar om CONF.java ar satt -----------------------
        System.out.println("Main_Controll");
        cal= Calendar.getInstance();
        currentTime = new Date();
        conf = new MModel.CONF();
        rms = new DataBase_RMS();
        rms.setDataStore();
        dateTime = new Date_Time();
        this.dbP = rms.setDB;

        System.out.println("VARDET >> " + dbP);

        // Forsta gangen programmet startar upp --> Spar datumen i postfack
        if(CONF.DEMO.equals("1") && dbP.equals("0")){

        // - Satter dagens datum och spar i RMS.
        dateTime.countThisDay();
        // - Satter datumet om 30 dagar och spar i RMS
        dateTime.countXDigitsDay();

        // Initierar dagens datum.
        String getThisDay = rms.getDataGEN(DataBase_RMS.RMS_setThisDay); //RMS_setThisDay
        String getThis30Day = rms.getDataGEN(DataBase_RMS.RMS_setThis30DAY); //RMS_setThis30DAY

        int index1 = 0, index2 = 0, index3 = 0, index4 = 0;
        index1 = getThisDay.indexOf('-');
        index2 = getThisDay.lastIndexOf('-');
        index3 = getThis30Day.indexOf('-');
        index4 = getThis30Day.lastIndexOf('-');

        String subDay = getThisDay.substring(0, index1);
        String subMonth = getThisDay.substring(index1 + 1, index2);
        String subYear = getThisDay.substring(index2 + 1, getThisDay.length());

        String sub30Day = getThis30Day.substring(0, index3);
        String sub30Month = getThis30Day.substring(index3 + 1, index4);
        String sub30Year = getThis30Day.substring(index4 + 1, getThis30Day.length());

        System.out.println("DAGENS DATUM MAINCONTROLL >> " + subDay + "-" + subMonth + "-" + subYear);
        System.out.println("30 DAGAR DATUM MAINCONTROLL >> " + sub30Day + "-" + sub30Month + "-" + sub30Year);

        this.setDay_TODAY = subDay;     // Dag
        this.setMounth_TODAY = subMonth;  // Manad
        this.setYear_TODAY = subYear;    // Ar
        // Satt namn pa manaden. tex --> July
        this.setMounthNameToday = dateTime.setMounth(setMounth_TODAY);

        // Initierar dagens datum, om 30 dagar.
        this.setDay_30DAY = sub30Day;     // Dag om 30 dagar.
        this.setMounth_30DAY = sub30Month;  // Manad om 30 dagar.
        this.setYear_30DAY = sub30Year;    // Ar om 30 dagar.
        // Satt namn pa manaden om 30 dagar. tex --> August
        this.setMounthName_30DAY = dateTime.setMounth(setMounth_30DAY);

        // Om licensen ar en demo-licens '1' kontrollera datumet.
        ControllDateTime();

        }

        // Under demotiden som licencen existerar kontrollera om licensen har gatt ut eller inte.
        else if(CONF.DEMO.equals("1") && dbP.equals("1")){

        /* ================== LICENS KONTROLL ============================= */

        // - Satter dagens datum och spar i RMS.
        dateTime.countThisDay();

        // Initierar dagens datum.
        String getThisDay = rms.getDataGEN(DataBase_RMS.RMS_setThisDay); //RMS_setThisDay
        String getThis30Day = rms.getDataGEN(DataBase_RMS.RMS_setThis30DAY); //RMS_setThis30DAY

        int index1 = 0, index2 = 0, index3 = 0, index4 = 0;
        index1 = getThisDay.indexOf('-');
        index2 = getThisDay.lastIndexOf('-');
        index3 = getThis30Day.indexOf('-');
        index4 = getThis30Day.lastIndexOf('-');

        String subDay = getThisDay.substring(0, index1);
        String subMonth = getThisDay.substring(index1 + 1, index2);
        String subYear = getThisDay.substring(index2 + 1, getThisDay.length());

        String sub30Day = getThis30Day.substring(0, index3);
        String sub30Month = getThis30Day.substring(index3 + 1, index4);
        String sub30Year = getThis30Day.substring(index4 + 1, getThis30Day.length());

        System.out.println("DAGENS DATUM MAINCONTROLL >> " + subDay + "-" + subMonth + "-" + subYear);
        System.out.println("30 DAGAR DATUM MAINCONTROLL >> " + sub30Day + "-" + sub30Month + "-" + sub30Year);


        this.setDay_TODAY = subDay;     // Dag
        this.setMounth_TODAY = subMonth;  // Manad
        this.setYear_TODAY = subYear;    // Ar
        // Satt namn pa manaden. tex --> July
        this.setMounthNameToday = dateTime.setMounth(setMounth_TODAY);

        // Initierar dagens datum, om 30 dagar.
        this.setDay_30DAY = sub30Day;     // Dag om 30 dagar.
        this.setMounth_30DAY = sub30Month;  // Manad om 30 dagar.
        this.setYear_30DAY = sub30Year;    // Ar om 30 dagar.
        // Satt namn pa manaden om 30 dagar. tex --> August
        this.setMounthName_30DAY = dateTime.setMounth(setMounth_30DAY);

        // Om licensen ar en demo-licens '1' kontrollera datumet.
        ControllDateTime();

        }
        // Ar en enterprise licens allt ar okey
        if (!CONF.DEMO.equals("1")) {

            this.ViewDateString = "Enterprise License";

            System.out.println("************************************************\n");
            System.out.println("ViewDateString >>>>> " + ViewDateString + "\n");
            System.out.println("************************************************\n");
        }

       // pbxobj = new MModel.PBXSETTINGS();
        LoadLang("");
        CreateSMSServer(GetDatabaseData(DataBase_RMS.RMS_GSMModemNr));
        CreateIPServer(GetDatabaseData(DataBase_RMS.RMS_CTIPPORT),GetDatabaseData(DataBase_RMS.RMS_CTIPURL));
        server = new Server();
        statusTicker = new Ticker(" ");
        //dateTime = new Date_Time();
        System.out.println("Create mobj");
        mobj = new MenyObjekt();
        System.out.println("Efter mobj");
        // --- SplashScreen
        //display = Display.getDisplay(this);
        //splashCanavas = new SplashCanvas();

    } // Konstruktorn slutar har.

    // --- metoden kontrollerar datum for demo-licenser
    public void ControllDateTime() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException, IOException {

        MLicense.Date_Time dateTime = new Date_Time();

        String s1 = setDay_30DAY; // Dag (30 dagar framat i tiden)
        String s2 = setMounth_30DAY; // Manad (30 dagar framat i tiden)
        String s3 = setYear_30DAY; // ar (30 dagar framat i tiden)
        String s4 = setYear_TODAY; // ar (dagens datum)
        String s5 = setMounth_TODAY; // Manad (dagens datum)
        String s6 = setDay_TODAY; // Dag (dagens datum)

        dateTime.controllDate(s1, s2, s3, s4, s5, s6);
        this.ViewDateString = setDay_30DAY + " " + setMounthName_30DAY + " " + setYear_30DAY;
        String licensStart = setDay_TODAY + " " + setMounthNameToday + " " + setYear_TODAY;

        System.out.println("*************************************************************************\n");
        //System.out.println("Licensen Startdatum >> " + licensStart);
        System.out.println("Licensen Galler till >> " + this.ViewDateString);
        System.out.println("Licensen Dagens datum >> " + s6 + "-" + setMounthNameToday + "-" + s4 );
         MDataStore.DataBase_RMS rms = new DataBase_RMS();
         String checkDate = rms.getDataGEN(116);
         this.TWO = checkDate;
         System.out.println("Vardet pa plats 116 'Om det ar 2 stang programmet' >> " + checkDate + "\n");
         System.out.println("*************************************************************************");
         rms = null;
        dateTime = null;

    }

    /* ===== Huvudklassens tre metoder, main osv. ========================== */
    public void LoadLang(String lang) {
       if (lang.length()< 1) {
          lang = GetDatabaseData(DataBase_RMS.RMS_DefaultLanguage);
       }
       System.out.println("Database Lang: " + lang);
       LANG.LoadLang(lang);
    }

    public void CreateSMSServer(String gsmnumb) {
        System.out.println("Kolla om vi ska ha nån SMS funktionalitet: " + gsmnumb);
        if (gsmnumb.length() > 0) {
            MServer.SMS.gsmnumb = gsmnumb;
             System.out.println("Skapa gsmnummer: " + gsmnumb);
            if (smsserver == null) {
                 System.out.println("Skapa smsobjekt: " + gsmnumb);
                smsserver = new MServer.SMS();
            }

        } else {
        System.out.println("Nej det ska vi inte gsmnummer e blankt");
        }
    }
public void CreateIPServer(String port,String url) {
        System.out.println("Kolla om vi ska ha nån IP kommnikation: " + port + " " + url);
        if (url.length() > 0 && port.length() > 0 && !port.equals("0")) {
            MServer.IP.url = url;
            MServer.IP.port = port;
             System.out.println("Sätt url: " + url);
             System.out.println("Sätt port: " + port);
            if (ipserver == null) {
                 System.out.println("Skapa ipobjekt: " + url + " " + port);
                ipserver = new MServer.IP();
            }
        } else {
        System.out.println("Nej det ska vi inte url eller portnummer e blankt");
        }
    }


    public static void SaveTickerText(int level, String text) {
      System.out.println("SaveTickerText level: " + level +  " text: " + text);
        switch (level) {
                case 0:
                    tickertext[0] = text;
                    tickertext[1] = "";
                    tickertext[2] = "";
                    tickertext[3] = "";
                 break;
                 case 1:
                    tickertext[1] = text;
                    tickertext[2] = "";
                    tickertext[3] = "";
                 break;
                 case 2:
                    tickertext[2] = text;
                    tickertext[3] = "";
                 break;
                 case 3:
                    tickertext[3] = tickertext[3] + " " + text;
                 break;
       }
       tickertextfinal = tickertext[0] + " " + tickertext[1] + tickertext[2] + tickertext[3];
    }

    public void SetTickerText() {
        Date currentTime2 = new Date();
        String tickerinfo = "";
        if (tickertextfinal.trim().length() > 0) {
          tickerinfo = " (" + LANG.genExecuted_DEF + ": " + currentTime2.toString() + ")";
        }
     statusTicker.setString((tickertextfinal + tickerinfo).trim());
    }

   public void startApp() {

       System.out.println("startApp");
       System.out.println("startSplash");
       startSplash();

       if(this.TWO.equals("2")){

           //Sätt
           System.out.println("Demo gått ut!!!");
           SetAlertProps(AlertType.ERROR,"OK",Command.OK);

           // (AlertType altype,String YESString)
           Display.getDisplay(this).setCurrent(SetAlert(CONF.ICONDIR + "exit2_64.png","License expired",
           AlertType.INFO,Alert.FOREVER,
           "Your license has expired\n" + "Please contact your PBX dealer" + "\n\nwww.mobisma.com" ,
           LANG.genDefaultYes_DEF,"",
           Command.EXIT,Command.CANCEL));


       } if(!this.TWO.equals("2")){

           //CreateMenus();
           //System.out.println("Hashtable contains " + mobj.confitems.size() + " key value pairs.");

           // System.out.println(Language.GetText("centrex_TSdirectIn_1"));
           System.out.println("Visa mainlist");
           //Display.getDisplay(this).setCurrent(this);
           SetListDisplay(mobj.lists[0][0][0]);

       }

   }

    public void pauseApp() {
         System.out.println("pauseApp");
    }

    public void destroyApp(boolean unconditional) {
        System.out.println("destroyApp");
        server = null;
        rms = null;
        conf = null;
        mobj = null;
        smsserver = null;
        ipserver=null;
        statusTicker = null;
    }

    public void startSplash() {

        try {

            if (!splashIsShown) {
                System.out.println("Skapa SplashCanvas");
                Displayable k = new SplashCanvas(ViewDateString);
                System.out.println("Visa SplashCanvas");
                //display.setCurrent(new SplashCanvas());
                Display.getDisplay(this).setCurrent(k);
                //display.setCurrent(k);
            }
            System.out.println("CreateMenus");
            CreateMenus();
            System.out.println("CreateMenus klar");
            //if (true) {
                // Game loop
            //}

        } catch (Exception ex) {
        }
    }

    public void CreateMenus() {


        System.out.println("*** CREATE MENUS *****");
        try {
          int i1 = 0;
          int i1b = 0;
          int i2 = 0;
          int i3 = 0;
          int i4 = 0;
          //startup();
           //Skapa alla listor, forms m.m
           //Antal listor
           int antlists = mobj.lists.length;
           //Snurra igenom antal listor
           //System.out.println("Snurra igenom antal listor: " + antlists );
           while (i1 < antlists && mobj.lists[i1] != null) {
               //Smurra igenom sublistor
               int antsublist2 = (mobj.lists[i1].length);
               //System.out.println("Snurra igenom antal sublistor: " + antsublist2 );
               i1b = 0;
               while (i1b < antsublist2 ) {
                  if (mobj.lists[i1][i1b] != null) {
               //Smurra igenom sublistor
               int antsublists = (mobj.lists[i1][i1b].length);
               //System.out.println("Snurra igenom antal slut sublistor: " + antsublists );
               i2 = 0;
               while (i2 < antsublists ) {
                   if (mobj.lists[i1][i1b][i2] != null) {
                       mobj.lists[i1][i1b][i2].setTicker(statusTicker);
                   //Skrivut namn
                   //System.out.println("Listnamn: " + mobj.lists[i1][i1b][i2].getTitle());
                   //Add listener
                   //System.out.println("Add listener to  mobj.lists["+i1+"]["+i1b+"]["+i2+"]");
                   mobj.lists[i1][i1b][i2].setCommandListener(this);
                   //Append list
                   int antitems = (mobj.listimages[i1][i1b][i2].length);
                   //System.out.println("Antal items for  mobj.lists["+i1+"]["+i1b+"]["+i2+"]:" + antitems);
                   i3 = 0;
                   while (i3 < antitems ) {
                       if ((i1 == 1 && i1b == 0 && i2 == 0 && i3 == 0 )) {
                          System.out.println("mobj.listtexts["+i1+"]["+i1b+"]["+i2+"]["+i3+"]=systemmenu vi exluderar");
                       } else {
                       if (mobj.listtexts[i1][i1b][i2][i3] != null) {
                       //Adda inte original systemmeyn
                       //System.out.println("Add listimages to  mobj.lists["+i1+"]["+i1b+"]["+i2+"] ["+ i3 +"]");
                            mobj.lists[i1][i1b][i2].append(mobj.listtexts[i1][i1b][i2][i3],mobj.listimages[i1][i1b][i2][i3]);
                       }
                       else {
                           System.out.println("mobj.listtexts["+i1+"]["+i1b+"]["+i2+"]["+i3+"]=null");
                        }
                       }

                       i3++;
                    }
                    //Commands
                    int antcommands = (mobj.listcommands[i1][i1b][i2].length);
                    //System.out.println("Antal commands for  mobj.lists["+i1+"]["+i1b+"]["+i2+"]: " + antcommands);
                    i3 = 0;
                    while (i3 < antcommands ) {
                        if (mobj.listcommands[i1][i1b][i2][i3] != null) {
                        //System.out.println("Add listcommands to  mobj.lists["+i1+"]["+i1b+"]["+i2+"] ["+ i3 +"]");
                        mobj.lists[i1][i1b][i2].addCommand(mobj.listcommands[i1][i1b][i2][i3]);
                        } else {
                        // System.out.println("mobj.listimages["+i1+"]["+i1b+"]["+i2+"]["+i3+"]=null");
                        }
                        i3++;
                    }
                    //Skapa forms
                    int antforms = (mobj.forms[i1][i1b][i2].length);
                    //System.out.println("Antal forms for  mobj.lists["+i1+"]["+i1b+"]["+i2+"]: " + antforms);
                    i3 = 0;
                    while (i3 < antforms ) {
                         i4 = 0;

                         if (mobj.forms[i1][i1b][i2][i3] != null) {


                         //Skrivut namn
                         // System.out.println("Formnamn: " + mobj.lists[i1][i1b][i2].getTitle());
                         //Add listener
                          mobj.forms[i1][i1b][i2][i3].setCommandListener(this);
                          mobj.forms[i1][i1b][i2][i3].setItemStateListener(listener);
                          //adda TextFields
                          int anttextfields = mobj.formtextfields[i1][i1b][i2][i3].length;
                         // System.out.println("Antal textfields for  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"]: " + anttextfields);
                          while (i4 < anttextfields ) {
                             if (mobj.formtextfields[i1][i1b][i2][i3][i4] != null) {
                            // System.out.println("Add formtextfields to  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"] ["+i4+"]");
                             mobj.forms[i1][i1b][i2][i3].append(mobj.formtextfields[i1][i1b][i2][i3][i4]);

                             } else {
                               // System.out.println("mobj.formtextfields["+i1+"]["+i1b+"]["+i2+"]["+i3+"]["+i4+"]=null");
                             }
                              i4++;
                          }
                           i4 = 0;
                           //adda DateFields
                          int antdatefields = mobj.formdatefields[i1][i1b][i2][i3].length;
                          //System.out.println("Antal textfields for  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"]: " + anttextfields);
                          while (i4 < antdatefields ) {
                             if (mobj.formdatefields[i1][i1b][i2][i3][i4] != null) {
                             //System.out.println("Add formdatefields to  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"] ["+i4+"]");
                             mobj.forms[i1][i1b][i2][i3].append(mobj.formdatefields[i1][i1b][i2][i3][i4]);

                             } else {
                               // System.out.println("mobj.formdatefields["+i1+"]["+i1b+"]["+i2+"]["+i3+"]["+i4+"]=null");
                             }
                              i4++;
                          }
                          i4 = 0;
                           //adda ChoiceFields
                          int antchoicefields = mobj.formchoicefields[i1][i1b][i2][i3].length;
                          //System.out.println("Antal formchoicefields for  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"]: " + antchoicefields);
                          while (i4 < antchoicefields ) {
                             if (mobj.formchoicefields[i1][i1b][i2][i3][i4] != null) {
                             //System.out.println("Add formchoicefields to  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"] ["+i4+"]");
                             mobj.forms[i1][i1b][i2][i3].append(mobj.formchoicefields[i1][i1b][i2][i3][i4]);

                             } else {
                               // System.out.println("mobj.formchoicefields["+i1+"]["+i1b+"]["+i2+"]["+i3+"]["+i4+"]=null");
                             }
                              i4++;
                          }
                          i4 = 0;
                          //adda Form commands
                          int antformcommands = (mobj.formcommands[i1][i1b][i2][i3].length);
                          //System.out.println("Antal formcommands for  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"]: " + antformcommands);
                          while (i4 < antformcommands ) {

                              if (mobj.formcommands[i1][i1b][i2][i3][i4] != null) {
                             //System.out.println("Add formcommands to  mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"] ["+i4+"]");
                             mobj.forms[i1][i1b][i2][i3].addCommand(mobj.formcommands[i1][i1b][i2][i3][i4]);

                              } else {
                                //System.out.println("mobj.formcommands["+i1+"]["+i1b+"]["+i2+"]["+i3+"]["+i4+"]=null");
                              }
                              i4++;
                          }

                         } else {
                           // System.out.println("mobj.forms["+i1+"]["+i1b+"]["+i2+"]["+i3+"]=null");
                         }
                          i3++;
                     }
                   } else {
                           //System.out.println("mobj.lists["+i1+"]["+i1b+"]["+i2+"]=null");
                       }
                     i2++;
                  }
               i1b++;
               } else {
                   //System.out.println("mobj.lists["+i1+"]["+i1b+"]=null");
               }
            }
               i1++;
            }
           //MenyObjekt.f_currentlist =mobj.lists[0][0][0];
           //Display.getDisplay(this).setCurrent(mobj.lists[0][0][0]);
           System.out.println("*** END CREATE MENUS *****");
           MenyObjekt.f_currentform =null;
           SetMexStatus();
           mobj.lists[0][0][0].setTitle((CONF.CLIENT + " " + CONF.PBXNAME + " " + CONF.CTSYSTEM).trim());
           //SetListDisplay(mobj.lists[0][0][0]);

           DoServerCMD(GetMexParams(new Character((char) MenyObjekt.C_REFRESH_MEX).toString()),true,"Refreshing...");
           //DoServerCMD(GetMexParams(new Character((char) MenyObjekt.C_MEXSTATUS).toString()),false,"");
                //startSplash();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            currentform[0] = 0;
            currentform[1] = 0;
            currentform[2] = 0;
            currentform[3] = 0;
            currentform[4] = 0; //Aktiv eller inte



        listener = new ItemStateListener(){

      public void itemStateChanged(Item item){
        //calendar.setTime(((DateField)item).getDate());
          System.out.println("itemStateChanged");
      }
    };
    isInitialized = true;
    }




    // --- Satter java.lang.string
    public String toString(String b) {

        String s = b;

        return s;
    }

    public void commandAction(Command c, Displayable d) {
        Thread th = new Thread(this);
        th.start();
        //thCmd = c;
        System.out.println("CommandAction" + c.getLabel());
        int listindex[] = new int[3];

        if (currentform[4] == 0) {
            /* Inget aktivt form eller alertruta, det måste vara en lista */
            System.out.println("Inget aktivt form currentform[4] == 0, det måste då vara en lista");
            listindex = GetCurrentList(d);
            if (listindex[3] == 0) {
             //Hitta inget lista, gör nåt
             System.out.println("listindex[3] == 0 Hittade inte listan " + d.getTitle() + " Visa första listan");
             SetListDisplay(mobj.lists[0][0][0]);
            } else {
             //Vi har våran lista
            System.out.println("Vi har våran lista listindex" + listindex);
             switch (c.getCommandType()) {
                case Command.EXIT:
                //Man har avslutat
                System.out.println("Avsluta programmet");
                ExitProgram();
                //notifyDestroyed();
                break;
                case Command.BACK:
                //Man har använt bakåt
                System.out.println("Tryckt bakåt");
                //Display.getDisplay(this).setCurrent(mobj.lists[0][0][0]);
                //MenyObjekt.f_currentlist = mobj.lists[0][0][0];
                SaveTickerText(0,"");
                SetListDisplay(mobj.lists[0][0][0]);
                break;
                case Command.HELP:
                case Command.OK:
                //Man har använt nåt kommando
                System.out.println("Använt nåt kommando");
                int listcmdindex[] = new int[2];
                listcmdindex = GetListCommand(listindex[0],listindex[1],listindex[2],c.getLabel());
                if (listcmdindex[1] != 0) {
                    System.out.println("**Hittade listkommandot: " + c.getLabel() + " i " + d.getTitle());
                    int fcmd = mobj.listcommandcheck[listindex[0]][listindex[1]][listindex[2]][listcmdindex[0]];
                    System.out.println("fcmd: " + fcmd);
                    System.out.println("HandleListCMD(" + listindex[0] + "," + listindex[1] + "," + listindex[2] + "," + fcmd+")");
                    HandleListCMD(listindex[0],listindex[1],listindex[2],fcmd);
                    //SetListDisplay(mobj.lists[0][0][0]);
                } else {
                  System.out.println("Hittade inte listkommandot: " + c.getLabel() + " i " + d.getTitle());
                }
                //Display.getDisplay(this).setCurrent(mobj.lists[0][0][0]);
                //MenyObjekt.f_currentlist = mobj.lists[0][0][0];
                break;
                case Command.SCREEN:
                /*Man har klickat på listan*/
                System.out.println("Klickat i listan");
                System.out.println("listindex[0]" + listindex[0]);
                System.out.println("listindex[1]" + listindex[1]);
                System.out.println("listindex[2]" + listindex[2]);
                System.out.println("mobj.lists[listindex[0]][listindex[1]][listindex[2]].getSelectedIndex()" + mobj.lists[listindex[0]][listindex[1]][listindex[2]].getSelectedIndex());
                System.out.println("c.getCommandType()" + c.getCommandType());
                System.out.println("c.getLabel()" + c.getLabel());
                int SelIndex = mobj.lists[listindex[0]][listindex[1]][listindex[2]].getSelectedIndex();
                System.out.println("HandleListCommand(" + listindex[0]+"," + listindex[1]+"," + listindex[2]+","+ mobj.lists[listindex[0]][listindex[1]][listindex[2]].getSelectedIndex() +"," + c.getCommandType()+"," + c.getLabel()+")");
                int tickerlevel;
                if (listindex[1] == 0 ) {
                    //Main level
                    tickerlevel = 0;
                    tickertext[3] = "";
                } else if (listindex[2] > 0 ) {
                   //SubSubmeny
                   tickerlevel = 1;
                   tickertext[3] = "";
                } else {
                   //Submeny
                   tickerlevel = 2;
                   tickertext[3] = "";
                }
                SaveTickerText(tickerlevel,mobj.listtexts[listindex[0]][listindex[1]][listindex[2]][SelIndex]);
                //SelIndex
                //listtexts[0][0][0][1]
                HandleListCommand(listindex[0],listindex[1],listindex[2],mobj.lists[listindex[0]][listindex[1]][listindex[2]].getSelectedIndex(),c.getCommandType(),c.getLabel());
               break;
              default:
              }
            }
        } else {
            if (currentform[4] == 1) {
            /* Det är ett form aktivt */
            System.out.println("Ett form är aktivt, vilket?");
            currentform = GetCurrentForm(d);
            System.out.println("Efter currentform");
            if (currentform[4] == 1) {
                System.out.println("HandleFormCommand(" + currentform[0] + "," + currentform[1] + "," + currentform[2] + "," + c.getCommandType() + "," + c.getLabel()+ ")");
                HandleFormCommand(currentform[0],currentform[1],currentform[2],currentform[3],c.getCommandType(),c.getLabel());
            } else {
                System.out.println("Hittade inte form:" + d.getTitle());
            }
            } else {
             /* Det är en alertruta */
             HandleAlertCommand(c.getCommandType(),c.getLabel(),d.getTitle());
            }
            }
}

public boolean ExitProgram() {
boolean ok = true;
Display.getDisplay(this).setCurrent(SetAlert("",LANG.AlertMessageExitText_DEF,AlertType.INFO,Alert.FOREVER,LANG.exitDefaultExitTheProgramYesOrNo_DEF,LANG.genDefaultYes_DEF,LANG.exitDefaultNo_DEF,Command.EXIT,Command.CANCEL));
return ok;
}


public boolean HandleAlertCommand(int c,String label,String formlabel) {
  System.out.println("HandleAlertCommand Command Type: " + c);
  boolean ok = false;
  switch (c) {
       case Command.BACK:
         System.out.println("HandleAlertCommand Tryckt bakåt");
         SetCurrentDisplay();
         ok=false;
         break;
       case Command.OK:
         System.out.println("HandleAlertCommand Tryckt OK");
         SetCurrentDisplay();
         ok=true;
         break;
       case Command.CANCEL:
         System.out.println("HandleAlertCommand Tryckt Cancel");
         SetCurrentDisplay();
         ok=false;
         break;
       case Command.EXIT:
         System.out.println("HandleAlertCommand Tryckt Exit");
         notifyDestroyed();
         ok=false;
         break;
       case Command.HELP:
         System.out.println("HandleAlertCommand Tryckt Help");
         SetCurrentDisplay();
         ok=true;
         break;
       case Command.STOP:
         System.out.println("HandleAlertCommand Tryckt Stop");
         SetCurrentDisplay();
         ok=false;
         break;
       case Command.ITEM:
         System.out.println("HandleAlertCommand Tryckt Item");
         SetCurrentDisplay();
         ok=false;
         break;
       case Command.SCREEN:
           SetCurrentDisplay();
         ok=false;
         break;
         default:
    }
return ok;
}

public void HandleFormCommand(int i,int j, int k, int l, int c,String label) {
   int formcommandindex[] = new int[2];
    //mobj.forms[currentform[0]][currentform[1]][currentform[2]].§
    System.out.println("Command Type: " + c);
    //Hela formets uppgift
    System.out.println("Hämta formets uppgift");
    System.out.println("mobj.formservercommand[" + i +"][" + j +"][" + k+"][" + l+"]");
    String servercommand = mobj.formservercommand[i][j][k][l];
    System.out.println("servercommand" + servercommand);
    //Vilket av kommandona har usern valt
    System.out.println("Hämta formcommandindex");
    System.out.println("mobj.GetFormCommand(" + currentform[0] +"," + currentform[1] + "," + currentform[2] + "," + currentform[3] + "," + label + ")");
    formcommandindex = GetFormCommand(currentform[0],currentform[1],currentform[2],currentform[3],label);
    System.out.println("formcommandindex[0]:" + formcommandindex[0]);
    System.out.println("formcommandindex[1]:" + formcommandindex[1]);
    if (formcommandindex[1] != 0) {
        System.out.println("servercheck = mobj.formservercommandcheck[" + currentform[0] + "][" + currentform[1] +"][" + currentform[2]+ "][" + currentform[3] + "][" + formcommandindex[0]+ "]: " + mobj.formservercommandcheck[currentform[0]][currentform[1]][currentform[2]][currentform[3]][formcommandindex[0]]);
        int servercheck = mobj.formservercommandcheck[currentform[0]][currentform[1]][currentform[2]][currentform[3]][formcommandindex[0]];
        System.out.println("servercheck: " + servercheck);
        switch (servercheck) {
           case MenyObjekt.BACK:
               System.out.println("BACK");
               //Display.getDisplay(this).setCurrent(MenyObjekt.f_currentlist);
               SetListDisplay(MenyObjekt.f_currentlist);
           break;
           case MenyObjekt.ABOUT:
               System.out.println("ABOUT");
           break;
           case MenyObjekt.CANCEL:
               System.out.println("CANCEL");
           break;
           case MenyObjekt.EDIT:
               System.out.println("EDIT");
           break;
           case MenyObjekt.EXIT:
               System.out.println("EXIT");
           break;
           case MenyObjekt.SAVE:
               System.out.println("SAVE");
               HandleRequest(unmaskcommand(servercommand,i,j,k,l),servercheck);
           break;
           case MenyObjekt.SEND:
               System.out.println("SEND");
               HandleRequest(unmaskcommand(servercommand,i,j,k,l),servercheck);
           break;
           default:
       }
    } else {
      System.out.println("Hittade inte kommandot " + label);
    }
}


public void HandleRequest(String servercmd,int cmdtype) {
    System.out.println("HandleRequest " + servercmd);
     switch (cmdtype) {
           case MenyObjekt.SAVE:
               System.out.println("SAVE " + servercmd);
               HandleCommand(servercmd);
           break;
           case MenyObjekt.SEND:
               System.out.println("SEND "+ servercmd);
               //SetTickerText();
               HandleCommand(servercmd);
           break;
           default:
           System.out.println("Unknown command" + cmdtype);
       }

}


public String OnOffHandleCommand(String onstring,String offstring) {
System.out.println("OnOffHandleCommand onstring: " + onstring + " offstring: " + offstring);
if (offstring.length() < 0 ) {
  System.out.println("Inget OnOffHandleCommand return");
  return onstring;
}

switch ((int)onstring.charAt(0)) {
   case MenyObjekt.C_MEXON:
   System.out.println("C_MEXON");
   String mexonoff = GetDatabaseData(DataBase_RMS.RMS_MexONOFF);
   System.out.println("mexonoff: " + mexonoff);
   if (mexonoff.equals("1")) {
    System.out.println("mex is on turn it off");
    SetDatabaseData(DataBase_RMS.RMS_MexONOFF,"0");
    SwitchOnOffMenu(false,LANG.mex_DEF + " " + LANG.isOFF_DEF);
    System.out.println("Return: " + offstring );
    return offstring;
   } else {
    System.out.println("mex is off turn it on");
    SetDatabaseData(DataBase_RMS.RMS_MexONOFF,"1");
    SwitchOnOffMenu(true,LANG.mex_DEF + " " + LANG.isON_DEF);
    System.out.println("Return: " + onstring );
    return onstring;
   }
   default:
   System.out.println("Return: " + onstring );
   return onstring;
}
}

public void SwitchOnOffMenu(boolean on,String text) {
System.out.println("SwitchOnOffMenu");
//mobj.listimages[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]][MenyObjekt.i_currentlistcommand[3]] = Image.createImage(CONF.ICONDIR + "on24.png");
System.out.println(mobj.listimages[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]][MenyObjekt.i_currentlistcommand[3]].toString());
System.out.println(mobj.listtexts[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]][MenyObjekt.i_currentlistcommand[3]].toString());
Image ImageIcon;
if (on) {
ImageIcon = mobj.listimages[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]][MenyObjekt.i_currentlistcommand[3]];
} else {
ImageIcon = mobj.listimages_off[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]][MenyObjekt.i_currentlistcommand[3]];
}
System.out.println("SwitchOnOffMenu set text: " + text );
mobj.lists[MenyObjekt.i_currentlistcommand[0]][MenyObjekt.i_currentlistcommand[1]][MenyObjekt.i_currentlistcommand[2]].set(MenyObjekt.i_currentlistcommand[3], text, ImageIcon);
System.out.println("SwitchOnOffMenu set text done!");
}


public void HandleCommand(String servercmd) {
/****************************************/
/* Hantering av alla kommandon
/****************************************/
System.out.println("HandleCommand " + servercmd);
String srvcmdstring = "";
String servercmdoff = "";
System.out.println("Splitta kommandot för att se om det är ett on/off kommando typ MEX " + servercmd);
String[] servercmdcheck = StringUtil.Splitstring(servercmd,"|",-1);
if (servercmdcheck.length > 1) {
 System.out.println("Ja det var det, servercmdoff: " + servercmdcheck[1]);
 servercmdoff = servercmdcheck[1];
} else {
 System.out.println("Nej det är ett vanligt kommando");
}
System.out.println("HandleCommand servercmd " + servercmd);
System.out.println("HandleCommand servercmdcheck[0] " + servercmdcheck[0]);
System.out.println("HandleCommand servercmdoff " + servercmdoff);


String servercmd1 = OnOffHandleCommand(servercmdcheck[0],servercmdoff);
System.out.println("servercmd1: " + servercmd1);
String[] servercmdsplit1 = StringUtil.Splitstring(servercmd1,",",-1);
String cmd = servercmdsplit1[0];
String replystring = "";
String ipformdata = servercmdsplit1[1];
System.out.println("ipformdata: " + ipformdata);
System.out.println("1");
if (servercmdsplit1[1] != null && servercmdsplit1[1].length() > 0 ) {
  replystring = servercmdsplit1[1];
}
/*
System.out.println("2");
if (servercmdsplit1[2] != null && servercmdsplit1[2].length() > 0 ) {
System.out.println("3");
String[] replyarr = StringUtil.Splitstring(servercmdsplit1[2],"#",-1);
System.out.println("4");
System.out.println("replyarr.length: " + replyarr.length);
    if (replyarr.length > 1 && replyarr[1].length() > 0 ) {
        System.out.println("5");
        replystring = replyarr[1];
    }
}
 */
System.out.println("5");
int antparms = servercmdsplit1.length;
int j = 0;

System.out.println("Executing command " + cmd);
System.out.println("Params: " + antparms);
for (j=1;j<antparms;j++) {
    System.out.println("servercmdsplit[" + j + "]" + servercmdsplit1[j]);
}

switch ((int)cmd.charAt(0)) {
    case MenyObjekt.C_SHOW_CONFIG:
    //Visa conffilen
        System.out.println("**Visa conffilen " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Show configfile");
        break;
    case MenyObjekt.C_SYSTEMADMIN:
    //Logga in och ur Debugläget
        System.out.println("**Logga in och ur Debugläget " + cmd);
        if (servercmdsplit1[2].equals("4321")) {
           SetListDisplay(mobj.lists[1][1][0]);
        }
        break;
    case MenyObjekt.C_REMOVE_FORWARD_PRESENCE_DTMF:
    //Tabort Hänvisning / Vidarekoppling DTMF
        System.out.println("**Tabort Hänvisning / Vidarekoppling DTMF " + cmd);
        SaveTickerText(0,"");
        SetTickerText();
        srvcmdstring = cmd + "," + servercmdsplit1[1] + "," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Removing presence/forward");
        break;
    case MenyObjekt.C_REMOVE_FORWARD_PRESENCE_SMS:
    //Tabort Hänvisning / Vidarekoppling SMS
        SaveTickerText(0,"");
        SetTickerText();
        srvcmdstring = servercmdsplit1[2];
        DoSMSCMD(srvcmdstring,true,"Removing presence/forward");

        System.out.println("**Tabort Hänvisning / Vidarekoppling SMS " + cmd);
        break;
    case MenyObjekt.C_SET_FORWARD_PRESENCE_IP:
    //Hänvisning / Vidarekoppling IP
        SetTickerText();
        srvcmdstring = servercmdsplit1[2];
        System.out.println("Hänvisning / Vidarekoppling IP " + cmd);
        DoIPCMD(srvcmdstring,true,"Set presence/forward IP",replystring,0);
        break;
    case MenyObjekt.C_REMOVE_FORWARD_PRESENCE_IP:
        SaveTickerText(0,"");
        SetTickerText();
    //Tabort Hänvisning / VIdarekoppling IP
        srvcmdstring = servercmdsplit1[2];
         System.out.println("Tabort Hänvisning / VIdarekoppling IP " + cmd);
        DoIPCMD(srvcmdstring,true,"Set presence/forward IP",replystring,0);
        break;
    case MenyObjekt.C_SET_FORWARD_PRESENCE_SMS_TOTALVIEW:
    //Send sms to TotalView Gsm server
        SetTickerText();
        System.out.println("Send sms to TotalView Gsm server " + cmd);
        srvcmdstring = servercmdsplit1[2];
        DoSMSCMD(srvcmdstring,true,"Setting Presence/Forward");
        break;
    case MenyObjekt.C_STORE_LOGDATA_SDCARD:
    //Store Logdata to SDCard
        System.out.println("Store Logdata to SDCard " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Storing logdata");
        break;
    case MenyObjekt.C_SET_FORWARD_PRESENCE_NODTMF:
    //Not used
        SetTickerText();
        System.out.println("Not used " + cmd);
        break;
    case MenyObjekt.C_CALLPBX_DTMF:
    //Ring anknytning, Ringer upp växel skickar DTMF, Lägger inte på
        System.out.println("Ring anknytning, Ringer upp växel skickar DTMF, Lägger inte på " + cmd);
        srvcmdstring = cmd + "," + servercmdsplit1[1] + "," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Calling PBX Do not hangup");
        break;
    case MenyObjekt.C_EDIT_CONFDATA:
    case MenyObjekt.C_SAVECONF:
    //Editera Confdata
        System.out.println("Spara Confdata " + cmd);
        System.out.println("Formnamn: " + mobj.forms[currentform[0]][currentform[1]][currentform[2]][currentform[3]].getTitle());
        SaveFormdata(currentform[0],currentform[1],currentform[2],currentform[3]);
        break;
    case MenyObjekt.C_STATUSCOLLEGUES:
    //Stänger av Serverapplikationen
        System.out.println("Status collegues " + cmd);

        //srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        srvcmdstring = servercmdsplit1[2];
        //ShowColleguesForm(DoIPCMD(srvcmdstring,true,"Status Collegues",replystring),ipformdata);
         DoIPCMD(srvcmdstring,true,"Status Collegues",replystring,CTIP.STATUSCOLLEGUES);
        //DoIPCMD(srvcmdstring,true,"Closing server");
        break;
    case MenyObjekt.C_DEBUG_ON_OFF:
    //Sätt Debug ON/OFF
        System.out.println("//Sätt Debug ON/OFF " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Debug ON/OFF");
        break;
    case MenyObjekt.C_MEXOFF:
    //Stänger av MEX
        System.out.println("Stänger av MEX " + cmd);
        srvcmdstring = GetMexParams(cmd);
        System.out.println("srvcmdstringx " + srvcmdstring);
        DoServerCMD(srvcmdstring,true,"Mex off");
        break;
    case MenyObjekt.C_MEXSTATUS:
    //Kollar status på MEX
        System.out.println("Kollar status på MEX " + cmd);
        //srvcmdstring = GetMexParams(cmd);
        System.out.println("srvcmdstringx " + srvcmdstring);
        String mexstat = DoServerCMD(srvcmdstring,false,"");
        System.out.println("mexstat " + mexstat);
        SetCurrentListCommandByCommand("s");
        //SwitchOnOffMenu(true,LANG.mex_DEF + " " + LANG.isON_DEF);
        //SwitchOnOffMenu(false,LANG.mex_DEF + " " + LANG.isOFF_DEF);
        //SetDatabaseData(DataBase_RMS.RMS_MexONOFF,"1");

        break;
    case MenyObjekt.C_DTMF_ONGOINGCALL:
    //Skicka DTMF toner under pågående samtal
        MenyObjekt.currDateformat = MenyObjekt.FORMAT_DATE_STD;
        MenyObjekt.currTimeformat = MenyObjekt.FORMAT_TIME_STD;
        System.out.println("Skicka DTMF toner under pågående samtal " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Sending dtmf");
        break;
    case MenyObjekt.C_SET_FORWARD_PRESENCE_DTMF:
    //Hänvisning, Rnger upp växel skicka DTMF, Lägg på
        SetTickerText();
        System.out.println("Hänvisning, Rnger upp växel skicka DTMF, Lägg på " + cmd);
        srvcmdstring = cmd + "," + servercmdsplit1[1] + "," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Setting forward/presence");
        break;
    case MenyObjekt.C_GETALLCONFDATA_FROM_SERVER:
    //Hämtar all configdata
        System.out.println("Hämtar all configdata " + cmd);
        break;
    case MenyObjekt.C_GETLOGDATA_FROM_SERVER:
    //Hämtar logdata
        System.out.println("Hämtar logdata " + cmd);
        break;
    case MenyObjekt.C_GETCONFVALUE_FROM_SERVER:
    //Hämtar specifikt confvalue
        System.out.println("Hämtar specifikt confvalue " + cmd);
        break;
    case MenyObjekt.C_IMEI_EXPIRED:
    //Demo expired Sätter IMEI expired och stänger av
        System.out.println("Demo expired Sätter IMEI expired och stänger av " + cmd);
        break;
    case MenyObjekt.C_MINIMISE:
    //Minimerar Java applikationen
        System.out.println("Minimerar Java applikationen " + cmd);
        if (CONF.MINIMISE ==1) {
          srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
          DoServerCMD(srvcmdstring,false,"");
        } else {
          Display.getDisplay(this).setCurrent(null);
        }
        break;
    case MenyObjekt.C_SET_FORWARD_PRESENCE_SMS:
    //Send sms to GSM Server
        SetTickerText();
        System.out.println("Send sms to GSM Server " + cmd);
        srvcmdstring = servercmdsplit1[2];
        DoSMSCMD(srvcmdstring,true,"Setting presence/forward SMS");
        break;
    case MenyObjekt.C_SEND_LOGDATA_TO_MOBISMA:
    //Skickar logdata till mobisma
        System.out.println("Skickar logdata till mobisma " + cmd);
        break;
    case MenyObjekt.C_ABOUT:
    //Om mobisma
        System.out.println("//Om mobisma " + cmd);
        break;
    case MenyObjekt.C_SHOW_LOGFILE:
    //Visa logfilen
        System.out.println("Visa logfilen " + cmd);
        break;
    case MenyObjekt.C_REFRESH_MEX:
    //Refresh Mexdata
        System.out.println("Refresh Mexdata " + cmd);
        srvcmdstring = GetMexParams(cmd);
        System.out.println("srvcmdstringx " + srvcmdstring);
        DoServerCMD(srvcmdstring,true,"Refreshing");
        break;
    case MenyObjekt.C_MEXON:
    //Starta Mex
        System.out.println("Starta Mex " + cmd);
        srvcmdstring = GetMexParams(cmd);
        System.out.println("srvcmdstringx " + srvcmdstring);
        DoServerCMD(srvcmdstring,true,"Mex on");
        break;
    case MenyObjekt.C_TEST_SERVER_CONNECTION:
    //Testar connection
        System.out.println("Testar connection " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,true,"Testing server connection");
        break;
    case MenyObjekt.C_GETLOGFILESIZE:
    //Hämtar logfilens storlek
        System.out.println("Hämtar logfilens storlek " + cmd);
        break;
    case MenyObjekt.C_MAXIMIZE:
    //Maximerar Java applikationen
        System.out.println("Maximerar Java applikationen " + cmd);
        srvcmdstring = cmd +"," + servercmdsplit1[2] + ",";
        DoServerCMD(srvcmdstring,false,"");
        break;
    case MenyObjekt.C_REMOVE_FORWARD_PRESENCE_NODTMF:
    //Avaya
        SaveTickerText(0,"");
        SetTickerText();
        System.out.println("Not used " + cmd);
        break;
    case MenyObjekt.C_SLUTMENYOBJEKT:
    //Slutmenyobjekt
        System.out.println("Slutmenyobjekt " + cmd);
        break;
    case MenyObjekt.C_UNKNOWN:
    //Används? Skickar DTMF
        System.out.println("Används? Skickar DTMF " + cmd);
        break;
    default:
    System.out.println("Unknown command " + cmd);
}

}

    public void ShowColleguesForm(String input,String showconfig) {
    System.out.println("** ShowColleguesForm **");
    System.out.println("ShowColleguesForm input: " + input);
    System.out.println("ShowColleguesForm showconfig: " + showconfig);

    Hashtable indataarray = CTIP.GetRespData(input,showconfig);
    String[] cmpvalvalarray = CTIP.returnvalfields;
    String[] cmpvalvararray = CTIP.returnvalvalues;
    int antfields = CTIP.antdata;

    List collequeslist = new List(LANG.statusContact_DEF, Choice.IMPLICIT);

    Command StatusBackCommand;
    StatusBackCommand = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
    collequeslist.addCommand(StatusBackCommand);
    collequeslist.setCommandListener(this);
    Image available = null;
    Image busy=null;
    try {
     available = Image.createImage(CONF.ICONDIR + "user.png");
     busy = Image.createImage(CONF.ICONDIR + "upptagen24.png");
    } catch (Exception ioe) {
        System.out.println("Hittade inte collegues pic " + ioe.getMessage());
    }

    for (int uc = 0; uc < antfields; uc++)
    {
        String cName = (String)indataarray.get(cmpvalvalarray[0]+"_"+uc);
        String cStatus = (String)indataarray.get(cmpvalvalarray[1]+"_"+uc);
        String cText = (String)indataarray.get(cmpvalvalarray[1]+"_"+uc);

        String listtext = cName + "-" + cText;

        if (cStatus.equals(cmpvalvararray[0]))
           {
             collequeslist.append(listtext,busy);
           }
           else {
              collequeslist.append(listtext,available);
           }
    }

    Display.getDisplay(this).setCurrent(collequeslist);


}



/*
public void ShowColleguesForm(String input,String showconfig) {
System.out.println("** ShowColleguesForm **");
System.out.println("ShowColleguesForm input: " + input);
System.out.println("ShowColleguesForm showconfig: " + showconfig);
String[] showconfigarr;
String Separator = "";
showconfigarr = StringUtil.Splitstring(showconfig,"#",-1);
System.out.println("ShowColleguesForm showconfigarr.length: " + showconfigarr.length);
String separatorin = showconfigarr[0];
System.out.println("ShowColleguesForm separatorin: " + separatorin);
int statusfield = 0;
int userfield = 0;
int infofield = 0;
int Sep = 0;

try {
  Sep = Integer.parseInt(separatorin);
  Separator = new Character((char)Sep).toString();
} catch (NumberFormatException e) {
   System.out.println("Kunde inte konvertera separatorin to int " + e.getMessage());
}
System.out.println("ShowColleguesForm Sep: " + Sep);
System.out.println("ShowColleguesForm Separator: " + Separator);

String[] inputarray = StringUtil.Splitstring(input,"\n",-1);
System.out.println("ShowColleguesForm inputarray.length: " + inputarray.length);
//String replystring = showconfigarr[1];

String[] userarray = StringUtil.Splitstring(showconfigarr[2],"*",-1);
System.out.println("ShowColleguesForm userarray.length: " + userarray.length);
try {
   userfield = Integer.parseInt(userarray[0]);
} catch (NumberFormatException e) {
   System.out.println("Kunde inte konvertera userfield to int " + e.getMessage());
}
String userreply = userarray[1];
String[] statusarray = StringUtil.Splitstring(showconfigarr[3],"*",-1);
System.out.println("ShowColleguesForm statusarray.length: " + statusarray.length);
try {
    statusfield = Integer.parseInt(statusarray[0]);
} catch (NumberFormatException e) {
    System.out.println("Kunde inte konvertera statusfield to int " + e.getMessage());
}
String statusreply = statusarray[1];

String[] infoarray = StringUtil.Splitstring(showconfigarr[4],"*",-1);
System.out.println("ShowColleguesForm infoarray.length: " + infoarray.length);

try {
   infofield = Integer.parseInt(infoarray[0]);
} catch (NumberFormatException e) {
    System.out.println("Kunde inte konvertera infofield to int " + e.getMessage());
}
String inforeply = infoarray[1];
List collequeslist = new List(LANG.statusContact_DEF, Choice.IMPLICIT);

Command StatusBackCommand;
StatusBackCommand = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
collequeslist.addCommand(StatusBackCommand);
collequeslist.setCommandListener(this);
Image available = null;
Image busy=null;
try {
 available = Image.createImage(CONF.ICONDIR + "user.png");
 busy = Image.createImage(CONF.ICONDIR + "upptagen24.png");
} catch (Exception ioe) {
    System.out.println("Hittade inte collegues pic " + ioe.getMessage());
}

for (int uc = 0; uc < inputarray.length - 1; uc++)
{
    String u = inputarray[uc];
    String[] inputparams = StringUtil.Splitstring(u,Separator,-1);

    String listtext = inputparams[userfield] + "-" + inputparams[infofield];
    if (inputparams[statusfield].equals(statusreply))
       {
         collequeslist.append(listtext,busy);
       }
       else {
          collequeslist.append(listtext,available);
       }
}

Display.getDisplay(this).setCurrent(collequeslist);


}
*/

public String GetMexParams(String cmd) {
    String mexdata = "";
    mexdata = cmd + "," + GetDatabaseData(DataBase_RMS.RMS_SwitchBoardNumber) + "," + GetDatabaseData(DataBase_RMS.RMS_LineAccess) + "," + GetDatabaseData(DataBase_RMS.RMS_CountryCode) + "," + GetDatabaseData(DataBase_RMS.RMS_VoiceMailOperator) + "," + CONF.IDDCODE + "," + CONF.AREACODEREMOVE + ",";
    return mexdata;
}

public String DoSMSCMD(String srvcmdstring,boolean waitdisplay,String waittext) {
  System.out.println("DoSMS Command " + srvcmdstring);
  String resp = "";
  System.out.println("Send SMS servercmd");
  smsserver.SendSMSToGSMServer(srvcmdstring);
  if (waitdisplay) {
   SetWaitForServerDisplay(waittext,srvcmdstring);
  }
  if (smsserver.WaitForServer()) {
    resp = MControll.Main_Controll.response;
    SetCurrentDisplay();
 } else {
    System.out.println("Server error");
    resp = "SMS Server Error: " + MControll.Main_Controll.response;
    System.out.println("Response: " + resp);
    alertgen.setString(resp);
     if (waitdisplay) {
        System.out.println("Update display");
        SetAlertProps(AlertType.ERROR,"OK",Command.OK);
        SetAlertDisplay();
     }
 }
 System.out.println("SMS Servercmd finsihed: " + resp);

  return "OK";
}

public String DoServerCMD(String srvcmdstring,boolean waitdisplay,String waittext) {
 String resp = "";
 System.out.println("Send servercmd");
 server.serverCMD(srvcmdstring);
 if (waitdisplay) {
    SetWaitForServerDisplay(waittext,srvcmdstring);
 }
if (server.WaitForServer()) {
    resp = MControll.Main_Controll.response;
    //SetCurrentDisplay();
     SetListDisplay(mobj.lists[0][0][0]);
 } else {
    System.out.println("Server error");
    resp = "Server Error: " + MControll.Main_Controll.response;
    System.out.println("Response: " + resp);
    alertgen.setString(resp);
     if (waitdisplay) {
        System.out.println("Update display");
        SetAlertProps(AlertType.ERROR,"OK",Command.OK);
        SetAlertDisplay();
     }
 }
 System.out.println("Servercmd finsihed: " + resp);

 return resp;
}



public String DoIPCMD(String srvcmdstring,boolean waitdisplay,String waittext,String replystring,int action) {
 String resp = "";
 System.out.println("IP Send servercmd: " + srvcmdstring);

 srvcmdstring =CTIP.GetSubmitString(srvcmdstring);

 ipserver.serverCMD(srvcmdstring);
 if (waitdisplay) {
    SetWaitForServerDisplay(waittext,srvcmdstring);
 }
if (ipserver.WaitForServer()) {
    resp = MControll.Main_Controll.response;

    String[] ctipsvar = new String[2];
    ctipsvar = CTIP.CheckResponse(resp,replystring);

    if (ctipsvar[0].equals("OK")) {
        //SetCurrentDisplay();


         switch (action) {
           case CTIP.STATUSCOLLEGUES:
            ShowColleguesForm(resp,replystring);
            break;
           default:
             SetListDisplay(mobj.lists[0][0][0]);
             break;
         }



    } else {
     alertgen.setString(ctipsvar[1]);
     if (waitdisplay) {
        System.out.println("Error IP update display");
        SetAlertProps(AlertType.ERROR,"OK",Command.OK);
        SetAlertDisplay();
     }
    }
 } else {
    System.out.println("IP Server error");
    resp = "IP Server Error: " + MControll.Main_Controll.response;
    System.out.println("IP Response: " + resp);
    alertgen.setString(resp);
     if (waitdisplay) {
        System.out.println("IP update display");
        SetAlertProps(AlertType.ERROR,"OK",Command.OK);
        SetAlertDisplay();
     }
 }
 System.out.println("IP Servercmd finsihed: " + resp);

 return resp;
}

public  String GetDatabaseData(int what) {
 String data="";
 try {
  data = rms.getDataGEN(what);
 } catch (Exception ex) {
    System.out.println(ex.getMessage());
 }
return data;
}

public boolean SetDatabaseData(int what,String data) {
  boolean ok = true;
 try {
  rms.setDataGEN(what,data);
  Thread.sleep(500);
 } catch (Exception ex) {
    System.out.println(ex.getMessage());
    ok = false;
 }
  return ok;
}




public String unmaskcommand(String s,int i,int j,int k,int l) {
     System.out.println("unmaskcommand");
/*
    Maska av servercmd och matcha vareiablar med formitems
     och databasitems
     gör generella funktioner för det
     om variablen börjar med _ så är det ett fortmitem
     om den börjar med %  (kom inte ihåg)
     matcha variablar i servercmd med java variabler, vi får kolla
    Om det innehåller_ så måste vi använda i j k får att läsa av formet
*/
     String decodedstring = "";
     String confvar = "";
     String formvar = "";
     String tkn = "";
     boolean confvarflag = false;
     boolean formvarflag = false;

     for (int strcount = 0; strcount < s.length(); strcount++)
         {
            //System.out.println("strcount: " + strcount + " Length: " + s.length() );
            tkn = s.substring(strcount,strcount+1);
            //System.out.println("tkn " + tkn + " strcount: " + strcount + " Length: " + s.length() );
            if (tkn.equals(MenyObjekt.CONFDELIM)) {
                //System.out.println("Confvariable " + tkn );
                confvarflag = true;
            }
            else if (tkn.equals(MenyObjekt.FORMDELIM)) {
                //System.out.println("formvar " + tkn );
                formvarflag = true;
            }
            else if (tkn.equals(MenyObjekt.CONFFORMEND)) {
                //System.out.println("Slut variable " + tkn );
                if (confvarflag) { decodedstring = decodedstring + GetConfVar(confvar); confvarflag = false; }
                if (formvarflag) { decodedstring = decodedstring + GetFormInputItemValue(i,j,k,l,formvar); formvarflag = false; }
                confvar = "";
                formvar = "";
            } else {
              if (confvarflag) { confvar = confvar + tkn; }
              if (formvarflag) { formvar = formvar + tkn; }
              if ((!confvarflag) && (!formvarflag))
                {
                  decodedstring = decodedstring + tkn;
                  //System.out.println("Annan data " + decodedstring );
                }
            }
            //System.out.println("Loop " +  strcount);
        }
     System.out.println("return decodedstring " +  decodedstring);
    return decodedstring;
}

public String GetConfVar(String confvar) {
    confvar = confvar.trim();
    System.out.println("GetConfVar in: " + confvar);
    if(mobj.confitems.containsKey(confvar)){
        System.out.println(confvar + "Fanns i MenyConfVars");
        Integer dbidI = (Integer) mobj.confitems.get(confvar);
        System.out.println("dbidI: " + dbidI);
        int dbid = dbidI.intValue();
        System.out.println("dbid: " + dbid);
        confvar = GetDatabaseData(dbid);
        System.out.println("GetConfVar out: " + confvar);
        }else{
            System.out.println(confvar + "Fanns inte registrerad som confvalue i MenyConfVars");
        }

 return confvar;
}

public boolean SetConfVar(String confvar, String data) {
    boolean ok = false;
    confvar = confvar.trim();
    System.out.println("SetConfVar in: " + confvar + " data: " + data);
    if(mobj.confitems.containsKey(confvar)){
        System.out.println(confvar + "Fanns i MenyConfVars");
        Integer dbidI = (Integer) mobj.confitems.get(confvar);
        System.out.println("dbidI: " + dbidI);
        int dbid = dbidI.intValue();
        System.out.println("dbid: " + dbid);
        if (SetDatabaseData(dbid,data)) {
          System.out.println("SetConfVar out: " + data + " confvar " + confvar );
          ActionWhenSaved(dbid,data);
          ok = true;
        } else {
          System.out.println("Kunde inte spara data: " + data + " för confvar " + confvar );
        }
        }else{
            System.out.println(confvar + "Fanns inte registrerad som confvalue i MenyConfVars");
        }

 return ok;
}

public void ActionWhenSaved(int dbid, String data) {
switch (dbid) {
    //case DataBase_RMS.RMS_Language:
     case DataBase_RMS.RMS_DefaultLanguage:
      LoadLang(data);
      System.out.println("Create mobj");
        mobj = null;
        try {
            mobj = new MenyObjekt();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InvalidRecordIDException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        System.out.println("Efter mobj");
        CreateMenus();
    break;
    case DataBase_RMS.RMS_GSMModemNr:
         CreateSMSServer(data);
    break;
    case DataBase_RMS.RMS_CTIPPORT:
         CreateIPServer(data,GetDatabaseData(DataBase_RMS.RMS_CTIPURL));
    break;
    case DataBase_RMS.RMS_CTIPURL:
         CreateIPServer(GetDatabaseData(DataBase_RMS.RMS_CTIPPORT),data);
    break;
    default:
}
}

public void HandleListCMD(int i, int j, int k, int cmd) {
   System.out.println("HandleListCMD: " + cmd);
    int listlist = k + 1;
    switch (cmd) {
           case MenyObjekt.BACK:
               System.out.println("LIST BACK");
               SetListDisplay(MenyObjekt.f_currentlist);
           break;
           case MenyObjekt.ABOUT:
               System.out.println("LIST ABOUT");
               System.out.println("Visa SplashCanvas");
               Displayable about = new SplashCanvas(this.ViewDateString);
               System.out.println("create back Command");
               //display.setCurrent(new SplashCanvas());
               Command goGraphicsBackCommand;
               goGraphicsBackCommand = new Command(LANG.genDefaultBack_DEF,
                                            Command.BACK, 1);
               System.out.println("addCommand Command");
               about.addCommand(goGraphicsBackCommand);
               System.out.println("setCommandListener");
               about.setCommandListener(this);
               System.out.println("display.setCurrent");
               //display.setCurrent(about);
               Display.getDisplay(this).setCurrent(about);

           break;
           case MenyObjekt.CANCEL:
               System.out.println("LIST CANCEL");
           break;
           case MenyObjekt.EDIT:
               System.out.println("LIST EDIT");

               System.out.println("Finns det nån lista på: mobj.lists[1][2][0]");
               if (mobj.lists[1][2][0] != null) {
                 System.out.println("Ja, visa den");
                 SetListDisplay(mobj.lists[1][2][0]);
               } else {
                 System.out.println("Finns ingen Redigerings meny på mobj.lists[1][2][0]");
               }

           break;
           case MenyObjekt.EXIT:
               System.out.println("LIST EXIT");
               notifyDestroyed();
           break;
           case MenyObjekt.SAVE:
               System.out.println("LIST SAVE");
               //HandleRequest(unmaskcommand(servercommand,i,j,k,l),servercheck);
           break;
           case MenyObjekt.SEND:
               System.out.println("LIST SEND");
               //HandleRequest(unmaskcommand(servercommand,i,j,k,l),servercheck);
           break;
           default:
       }

    //SetListDisplay(mobj.lists[0][0][0]);
}

public boolean SetMexStatus() {
  boolean ok = false;
  ok = SetCurrentListCommandByCommand("s");
  if (ok) {
     String mexonoff = GetDatabaseData(DataBase_RMS.RMS_MexONOFF);
     System.out.println("mexonoff: " + mexonoff);
     if (mexonoff.equals("1")) {
       System.out.println("mex is on");
       SwitchOnOffMenu(true,LANG.mex_DEF + " " + LANG.isON_DEF);
     } else {
       SwitchOnOffMenu(false,LANG.mex_DEF + " " + LANG.isOFF_DEF);
     }
    } else {
     System.out.println("SetMexStatus failed");
    }
return ok;
}


public boolean SetCurrentListCommandByCommand(String name) {
//Only mainmenu at the moment
int selectedindex = 0;
int l;
boolean found = false;
int mainlistlength = mobj.formservercommand[0][0][0].length;

for (l=0;l < mainlistlength;l++) {
    if (mobj.formservercommand[0][0][0][l].substring(0,1).equals(name)) {
        found=true;
        MenyObjekt.i_currentlistcommand[0] = 0;
        MenyObjekt.i_currentlistcommand[1] = 0;
        MenyObjekt.i_currentlistcommand[2] = 0;
        MenyObjekt.i_currentlistcommand[3] = l;
        break;
        }
}
return found;
}
/*
public boolean CheckSecurePage(int i,int j, int k, int selectedindex, int c,String label) {
    boolean secure = false;
    if (mobj.listtexts[i][j][k][selectedindex].equals(LANG.mSystemUtilites_DEF)) {

      secure = true;
    }
    return secure;
}
*/

public void HandleListCommand(int i,int j, int k, int selectedindex, int c,String label) {

   MenyObjekt.i_currentlistcommand[0] = i;
   MenyObjekt.i_currentlistcommand[1] = j;
   MenyObjekt.i_currentlistcommand[2] = k;
   MenyObjekt.i_currentlistcommand[3] = selectedindex;

   System.out.println("Command Type: " + c);

   //if (!CheckSecurePage(i,j,k,selectedindex,c,label)) {
   int nextindex = selectedindex +1;
   int anvisadlista = mobj.listlist[i][j][k][selectedindex];
   boolean SubListexists = false;
   if (anvisadlista == j && anvisadlista != 0) {
       System.out.println("Det finns en sublista på " + c);
       SubListexists = true;
   }

   if (mobj.forms[i][j][k][selectedindex] == null) {
             //Inget form för denna menuitem isf visa nästa anvisad lista
             System.out.println("Inget form för denna menuitem isf visa nästa lista");
             System.out.println("Kolla om det finns en sublista på mobj.lists[" + i + "][" + j +"][" + selectedindex+"] om man inte redan är inne i en");
             if (SubListexists) {
                 //System.out.println("Det Finns en sublista visa den");
                 //Display.getDisplay(this).setCurrent(mobj.lists[i][j][nextindex]);
                 System.out.println("Det finns en sublista");
                 currentform[4] = 0;
                 SetListDisplay(mobj.lists[i][j][nextindex]);
             } else if (anvisadlista != 0) {
                 System.out.println("Det fanns en lista på selected index mobj.lists[" + i +"][" + anvisadlista +"][0]");
                 //MenyObjekt.f_currentlist = mobj.lists[i][nextindex][0];
                 //Display.getDisplay(this).setCurrent(mobj.lists[i][nextindex][0]);
                 currentform[4] = 0;
                SetListDisplay(mobj.lists[i][anvisadlista][0]);
             } else {
                 System.out.println("Slut på listor och inga forms");
                 System.out.println("Finns det ett direktkommando? på [" + i + "][" + j + "][" + k + "][" + selectedindex + "]");
                 if (mobj.formservercommand[i][j][k][selectedindex] != null) {
                    System.out.println("Det fanns ett direktkommando skicka direkt");

                    String srvcommand = mobj.formservercommand[i][j][k][selectedindex];
                    HandleRequest(unmaskcommand(srvcommand,i,j,k,selectedindex),MenyObjekt.SEND);
                 } else {
                    System.out.println("Det finns inget att visa eller gora");
                 }
             }
         } else {
             //Visa formet
            System.out.println("Visa formet ["+i+"]["+j+"]["+k+"]["+selectedindex+"]");
            SaveTickerText(3,mobj.listtexts[i][j][k][selectedindex]);
            //MenyObjekt.f_currentform = mobj.forms[i][j][k][0];
             //Display.getDisplay(this).setCurrent(mobj.forms[i][j][k][0]);
             //Kolla om formet är editering, fyll formitems från databasen
            System.out.println("Kolla om det är ett editform");
            CheckEditForm(i,j,k,selectedindex);
            System.out.println("SetFormDisplay");
             SetFormDisplay(mobj.forms[i][j][k][selectedindex]);
             currentform[4] = 1;

         }
  // }
}

public void CheckEditForm(int i,int j,int k,int selectedindex){
    //Form curform = mobj.forms[i][j][k][selectedindex];
    //(int)cmd.charAt(0) = MenyObjekt.C_EDIT_CONFDATA

    System.out.println("Kolla om det är ett editform: " + mobj.forms[i][j][k][selectedindex].getTitle());
    if ((int)mobj.formservercommand[i][j][k][selectedindex].charAt(0) == MenyObjekt.C_EDIT_CONFDATA || (int)mobj.formservercommand[i][j][k][selectedindex].charAt(0) == MenyObjekt.C_SAVECONF ) {
        System.out.println("Ja detta är ett editform: " + mobj.forms[i][j][k][selectedindex].getTitle());

        int ff=0,found=0;
        //text = mobj.FORMDELIM + text.trim();
        //String data = "?" + text;
        int anttextfields = (mobj.formtextfieldnames[i][j][k][selectedindex].length-1);
        for (ff=0;ff<anttextfields;ff++) {
           if (mobj.formtextfieldnames[i][j][k][selectedindex][ff] != null) {
           String confvar = mobj.formtextfieldnames[i][j][k][selectedindex][ff].substring(1);
           System.out.println("confvar: " + confvar);
           if(mobj.confitems.containsKey(confvar)){
             if (mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_TEXTBOX || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_TEXTBOXMI ||mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_ANY || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_PHONENUMBER || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_NUMERIC) {
                 String confdatat = GetConfVar(confvar).toString();
                 System.out.println("Sätter confdata i mobj.formfieldtype[" + i +"][" +j+"]["+ k + "][" + selectedindex+ "][" +ff+"]: " + confdatat);
                 mobj.formtextfields[i][j][k][selectedindex][ff].setString(confdatat);
             } else if (mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_CHOICEGROUP) {
                 String confdatat = GetConfVar(confvar).toString();
                 int confindex = Integer.parseInt(confdatat);
                 System.out.println("Sätter index i mobj.formchoicefields[" + i +"][" +j+"]["+ k + "][" + selectedindex+ "][" +ff+"]: " + confindex);
                 mobj.formchoicefields[i][j][k][selectedindex][ff].setSelectedIndex(confindex, true);
                 //mobj.formchoicefields[i][j][k][selectedindex][ff].setSelectedIndex(0, true);
                 System.out.println("Efter set choice index");
             } else {
               System.out.println("Konstigt formtype: " + mobj.formfieldtype[i][j][k][selectedindex][ff]);
             }
            }
        }
        //End if null
         }
        System.out.println("End Kolla CheckEditForm");
    }
    System.out.println("End CheckEditForm");
}

public void SaveFormdata(int i,int j,int k,int selectedindex){
    //Form curform = mobj.forms[i][j][k][selectedindex];
    //(int)cmd.charAt(0) = MenyObjekt.C_EDIT_CONFDATA
    InfoAlert(LANG.alertDefaultSaveChanges_DEF, LANG.genDefaultSave_DEF + mobj.forms[i][j][k][selectedindex].getTitle());
    int ff=0,found=0;
        //text = mobj.FORMDELIM + text.trim();
        //String data = "?" + text;
        int anttextfields = (mobj.formtextfieldnames[i][j][k][selectedindex].length-1);
        for (ff=0;ff<anttextfields;ff++) {
          if (mobj.formtextfieldnames[i][j][k][selectedindex][ff] != null) {
            String confvar = mobj.formtextfieldnames[i][j][k][selectedindex][ff].substring(1);
           System.out.println("confvar: " + confvar);
           if(mobj.confitems.containsKey(confvar)){
             if (mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_TEXTBOX || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_TEXTBOXMI ||mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_ANY || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_PHONENUMBER || mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_NUMERIC) {

                 String confdatat = mobj.formtextfields[i][j][k][selectedindex][ff].getString();
                 System.out.println("Sätter confdata i mobj.formfieldtype[" + i +"][" +j+"]["+ k + "][" + selectedindex+ "][" +ff+"]: " + confdata + "i databasen");
                 if (SetConfVar(confvar,confdatat)) {
                  System.out.println("** Confvar sparad: " + confvar + " data: " + confdatat);
                 } else {
                  System.out.println("Kunde inte spara confvar: " + confvar+ " data: " + confdatat);
                 }
             } else if (mobj.formfieldtype[i][j][k][selectedindex][ff] == MenyObjekt.FI_CHOICEGROUP) {
                 int selindex = mobj.formchoicefields[i][j][k][selectedindex][ff].getSelectedIndex();
                 String confdatat = Integer.toString(selindex);
                 if (SetConfVar(confvar,confdatat)) {
                  System.out.println("** Confvar sparad: " + confvar + " data: " + confdatat);
                 } else {
                  System.out.println("Kunde inte spara confvar: " + confvar+ " data: " + confdatat);
                 }
             } else {
               System.out.println("Konstigt formtype: " + mobj.formfieldtype[i][j][k][selectedindex][ff]);
             }
            }
         }
     }
}

public void SetFormDisplay(Form currentform1) {
    System.out.println("Set currentform");
    MenyObjekt.f_currentform = currentform1;
    System.out.println("Current form: " + MenyObjekt.f_currentform.getTitle());
    currentform[4] = 1;
    System.out.println("Display getDisplay");
    Display.getDisplay(this).setCurrent(currentform1);
}

public void SetListDisplay(List currenlist) {
    MenyObjekt.f_currentform =null;
    MenyObjekt.f_currentlist = currenlist;
    System.out.println("Current list: " + MenyObjekt.f_currentlist.getTitle());
    currentform[4] = 0;
    Display.getDisplay(this).setCurrent(currenlist);
}

public void SetAlertDisplay() {
    currentform[4] = 2;
    Display.getDisplay(this).setCurrent(alertgen);
}

public void SetCurrentDisplay() {
 if (MenyObjekt.f_currentform  == null) {
     currentform[4] = 0;
   Display.getDisplay(this).setCurrent(MenyObjekt.f_currentlist);
 } else {
     if (MenyObjekt.f_currentform  == null) {
         currentform[4] = 0;
        Display.getDisplay(this).setCurrent(mobj.lists[0][0][0]);

     } else {
         currentform[4] = 1;
         Display.getDisplay(this).setCurrent(MenyObjekt.f_currentform);

     }
  }
 }

public void UpdateDisplay() {
Display.getDisplay(this).setCurrent(Display.getDisplay(this).getCurrent());
}

public void InfoAlert(String rub, String text) {
 Display.getDisplay(this).setCurrent(SetAlert("",rub,AlertType.INFO,1000,text,"","",0,0));
}


public void SetWaitForServerDisplay(String waittext,String cmdstring) {
System.out.println("SetWaitForServerDisplay");
//Display.getDisplay(this).setCurrent(SetAlert(CONF.ICONDIR + "info.png","Server request",AlertType.INFO,Alert.FOREVER,"Waiting for server","",""));
//Display.getDisplay(this).setCurrent(SetAlert(CONF.ICONDIR + "info.png","Server request",AlertType.CONFIRMATION,Alert.FOREVER,"Waiting for server","",""));
String stext = "Waiting for server";
if (CONF.DBG.equals("1")) {
 stext = cmdstring;
}
Display.getDisplay(this).setCurrent(SetAlert("",waittext,AlertType.INFO,Alert.FOREVER,stext,"","",0,0));
}

    public Alert SetAlert(String icon,String rubrik,AlertType altype,int showtime,String text,String YESString,String NOString,int YesCMD,int NoCMD) {
    try {
         System.out.println("SetAlert");
         Image alertInfo = null;
         if (icon.length() > 0 ) {
           alertInfo = Image.createImage(icon);
         }

         //alertgen = new Alert(rubrik,"", alertInfo, altype);
         alertgen = new Alert(rubrik,"", alertInfo, altype);
         alertgen.setString(text);
         alertgen.setTimeout(showtime);
         //alertgen.removeCommand(thCmd);
                if (showtime==Alert.FOREVER && (YESString.length() > 0 || NOString.length() > 0) ) {
                  if (YESString.length() > 0) {
                    Command YESCommand = new Command(YESString, YesCMD, 1);
                    alertgen.addCommand(YESCommand);
                  }
                  if ( NOString.length() >0) {
                  Command NOCommand = new Command(NOString, NoCMD, 2);
                  alertgen.addCommand(NOCommand);
                  }
                  alertgen.setCommandListener(this);
                }
            } catch (IOException ex11) {
                System.out.println("Error showing alert " + ex11.getMessage());
            }
    System.out.println("currentform[4] = 2");
    currentform[4] = 2;
    return alertgen;
    }



    public boolean SetAlertProps(AlertType altype,String YESString,int cmdtype) {
         System.out.println("SetAlertCMDOK");
         boolean ok = true;
         try {
           alertgen.setType(altype);
           if (alertgen.getTimeout()==Alert.FOREVER && (YESString.length() > 0) ) {
                  if (YESString.length() > 0) {
                    Command YESCommand = new Command(YESString, cmdtype, 1);
                    alertgen.addCommand(YESCommand);
                  }
                }
         } catch (Exception ex) {
                System.out.println("Error SetAlertProps" + ex.getMessage());
                ok = false;
            }
    return ok;
    }



public void run() {
        System.out.println("Run");

    }

//Menyobjektfunktioner
public int[] GetCurrentList(Displayable d) {
int i=0,j=0,k=0,found=0;
int[] result = new int[4];
int antlists = (mobj.lists.length);
System.out.println("GetCurrentList antlists " + antlists);
for (i=0;i < antlists;i++) {
int antlists2 = (mobj.lists[i].length);
System.out.println("GetCurrentList antlists2 " + antlists2);
for (j=0;j < antlists2;j++) {
int antlists3 = (mobj.lists[i][j].length);
System.out.println("GetCurrentList antlists3 " + antlists3);
for (k=0;k < antlists3;k++) {
if (d.equals(mobj.lists[i][j][k])) {
found=1;
break;
}
}
if (found == 1) {break;}
}
if (found == 1) {break;}
}

result[0] = i;
result[1] = j;
result[2] = k;
result[3] = found;
return result;
}

public int[] GetCurrentForm(Displayable d) {
int i=0,j=0,k=0,l=0,found=0;
int[] result = new int[5];
int antforms = (mobj.forms.length);
System.out.println("GetCurrentForm antforms " + antforms);
for (i=0;i < antforms;i++) {
int antforms2 = (mobj.forms[i].length);
System.out.println("GetCurrentForm antforms2 " + antforms2);
for (j=0;j < antforms2;j++) {
int antforms3 = (mobj.forms[i][j].length);
System.out.println("GetCurrentForm antforms3 " + antforms3);
for (k=0;k < antforms3;k++) {
int antforms4 = (mobj.forms[i][j][k].length);
System.out.println("GetCurrentForm antforms4 " + antforms4);
for (l=0;l < antforms4;l++) {
if (d.equals(mobj.forms[i][j][k][l])) {
found=1;
break;
}
}
if (found == 1) {break;}
}
if (found == 1) {break;}
}
if (found == 1) {break;}
}
result[0] = i;
result[1] = j;
result[2] = k;
result[3] = l;
result[4] = found;
return result;
}

public int[] GetListCommand(int i, int j, int k,String text) {
int ff=0,found=0;
int[] result = new int[2];
int antformcommands = (mobj.listcommands[i][j][k].length-1);
for (ff=0;ff < antformcommands;ff++) {
if (text.equals(mobj.listcommands[i][j][k][ff].getLabel())) {
found=1;
break;
}
if (found == 1) {break;}
}
result[0] = ff;
result[1] = found;
return result;
}

public int[] GetFormCommand(int i, int j, int k,int l,String text) {
int ff=0,found=0;
int[] result = new int[2];
int antformcommands = (mobj.formcommands[i][j][k][l].length-1);
System.out.println("GetFormCommand antformcommands " + antformcommands);
for (ff=0;ff < antformcommands;ff++) {
    System.out.println("mobj.formcommands[" + i +"][" + j +"][" + k+"]["+l+"]["+ff+"].getLabel(): " + mobj.formcommands[i][j][k][l][ff].getLabel());
    if (text.equals(mobj.formcommands[i][j][k][l][ff].getLabel())) {
        System.out.println("FOUND!! ff: " + ff);
        found=1;
        break;
    }
    if (found == 1) {break;}
}
result[0] = ff;
result[1] = found;
return result;
}

public String zeroPad (int value, int size) {
String s = "0000000000"+value;
return s.substring(s.length() - size);
}

public String GetFormInputItemValue(int i, int j, int k,int l,String text) {
int ff=0,found=0;
text = MenyObjekt.FORMDELIM + text.trim();
String data = "?" + text;
int antformcommands = (mobj.formtextfieldnames[i][j][k][l].length-1);
for (ff=0;ff < antformcommands;ff++) {
if (text.equals(mobj.formtextfieldnames[i][j][k][l][ff])) {
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_NUMERIC) {
data = mobj.formtextfields[i][j][k][l][ff].getString();
SaveTickerText(3,data);
}
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_PHONENUMBER) {
data = mobj.formtextfields[i][j][k][l][ff].getString();
SaveTickerText(3,data);
}
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_DATEFIELD) {
cal.setTime(mobj.formdatefields[i][j][k][l][ff].getDate());
//cal.get(Calendar.);
//MControll.Main_Controll.SaveTickerText(3, LANG.genDateThe_DEF + " " + formdatefields[i][j][k][l][ff].getDate().toString());
SaveTickerText(3,mobj.formdatefields[i][j][k][l][ff].getDate().toString());
data = FormatDate();
}
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_TIMEFIELD) {
cal.setTime(mobj.formdatefields[i][j][k][l][ff].getDate());
//MControll.Main_Controll.SaveTickerText(3, LANG.genKl_DEF + " " + formdatefields[i][j][k][l][ff].getDate().toString());
SaveTickerText(3,mobj.formdatefields[i][j][k][l][ff].getDate().toString());
data = FormatTime();
}
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_TEXTBOX || mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_TEXTBOXMI || mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_ANY) {
data = mobj.formtextfields[i][j][k][l][ff].getString();
SaveTickerText(3,data);
}
if (mobj.formfieldtype[i][j][k][l][ff] == MenyObjekt.FI_CHOICEGROUP) {
int choice = mobj.formchoicefields[i][j][k][l][ff].getSelectedIndex();
data = Integer.toString(choice);
}
break;
}
if (found == 1) {break;}
}
return data;
}

public String FormatDate() {
String data = "";
if (CONF.CTSYSTEM.equals(MenyObjekt.CT_TV)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_MOBCTEX)) {
    int y = cal.get(Calendar.YEAR);
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String year = zeroPad(m, 4);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = year + month + day;
}
else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_8020I)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CTSERVER)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CTSERVERI)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_TVI)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CASERVERI)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_8020D)) {
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
} else {
//Default
    int m = cal.get(Calendar.MONTH) + 1;
    int d = cal.get(Calendar.DAY_OF_MONTH);
    String month = zeroPad(m, 2);
    String day = zeroPad(d, 2);
    data = month + day;
}
return data;
}

public String FormatTime() {
String data = "";

if (CONF.CTSYSTEM.equals(MenyObjekt.CT_TV)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_MOBCTEX)) {
    int h = cal.get(Calendar.HOUR_OF_DAY);
    int m = cal.get(Calendar.MINUTE);
    int s = cal.get(Calendar.SECOND);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    String second = zeroPad(s, 2);
    data = hour + minute+second;
}
else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_8020I)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CTSERVER)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CTSERVERI)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_TVI)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_CASERVERI)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else if (CONF.CTSYSTEM.equals(MenyObjekt.CT_8020D)) {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
} else {
    int h = cal.get(Calendar.HOUR);
    int m = cal.get(Calendar.MINUTE);
    String hour = zeroPad(h, 2);
    String minute = zeroPad(m, 2);
    data = hour + minute;
}
return data;
}

// Tar emot varden fran huvudclassen i konstruktorn.
public class SplashCanvas extends Canvas {

      private String viewDateString;

      // Tar emot varden fran huvudclassen i konstruktorn.
      public SplashCanvas(String ViewDateString) {

         this.viewDateString = ViewDateString;
          //k.addCommand(goGraphicsBackCommand);
          //k.setCommandListener(this);

      }

      protected void paint(Graphics g) {
          int width = getWidth();
          int height = getHeight();
          System.out.println("About paint ");
          // Create an Image the same size as the
          // Canvas.
          System.out.println("createImage");
          Image image = Image.createImage(width, height);
          Graphics imageGraphics = image.getGraphics();

          // Fill the background of the image black
          imageGraphics.setColor(0x000000);
          imageGraphics.fillRect(0, 0, width, height);

          // Draw a pattern of lines
          int count = 10;
          int yIncrement = height / count;
          int xIncrement = width / count;
          for (int i = 0, x = xIncrement, y = 0; i < count; i++) {
              imageGraphics.setColor(0xC0 + ((128 + 10 * i) << 8) +
                                     ((128 + 10 * i) << 16));
              imageGraphics.drawLine(0, y, x, height);
              y += yIncrement;
              x += xIncrement;
          }

          // Add some text
         imageGraphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, 0,
                                            Font.SIZE_SMALL));
         imageGraphics.setColor(0xffffff);
         imageGraphics.drawString("Patent Pending", width/2, 0, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.setColor(0xffff00);
         imageGraphics.drawString(CONF.PRGNAME, width / 2, 25, Graphics.TOP | Graphics.HCENTER);

         try {
             Image image1 = Image.createImage("/mobisma_icon/mexa.png");
             imageGraphics.drawImage(image1, width / 2, 47,
                                     Graphics.TOP | Graphics.HCENTER);
         } catch (IOException ex) {
         }

         imageGraphics.drawString(viewDateString, width / 2, 100, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.drawString("Client Ver. " + CONF.JAVAVERSION, width / 2, 125, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.drawString(" Server Ver. " + CONF.SERVERVERSION,  width / 2, 150, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.setColor(0xffffff);

         imageGraphics.drawString(CONF.NAME, width/2, 175, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.drawString(CONF.COMPANYNAME, width/2, 195, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.setColor(0xffff00);
         imageGraphics.drawString("PBX: " + CONF.PBXNAME, width/2, 220, Graphics.TOP | Graphics.HCENTER);
         imageGraphics.drawString("Phone: " + CONF.PBRAND + " " + CONF.PMODEL, width/2, 245, Graphics.TOP | Graphics.HCENTER);


         // Copy the Image to the screen
         g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);

          splashIsShown = true;
      }

  }

}
