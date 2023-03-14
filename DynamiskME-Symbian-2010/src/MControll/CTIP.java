package MControll;

import MModel.StringUtil;
import MCTParse.CtParser;
import java.util.Hashtable;




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
public class CTIP {
    public static final int STATUSCOLLEGUES = 1;
    public static String splitvalue;
    public static String[] returnvalfields;
    public static String[] returnvalvalues;
    public static String okstring;
    public static String resptype;
    public static int antdata = 0;
    public CTIP() {

    }

    public static String GetSubmitString(String submitstring) {
        //*cmd:s#*date:1214*ankn:201
         String returnstring = "";
        String formdel = "";
        int count = 0;
        String[] formdata = StringUtil.Splitstring(submitstring, "*", -1);
        for (int i = 0; i < formdata.length; i++) {
            //cmd:s#
//date:1214
//ankn:201
            System.out.println("formdata[" + i + "]: " + formdata[i]);
            if (formdata[i].length() > 0) {
                String[] formdata2 = StringUtil.Splitstring(formdata[i], ":",
                        -1);
                System.out.println("formdata2[0]: " + formdata2[0]);
                String[] formdata3 = StringUtil.Splitstring(formdata2[1], "#",
                        -1);
                System.out.println("formdata3[0]: " + formdata3[0]);

                if (count > 0) {
                    formdel = "&";
                }
                returnstring = returnstring + formdel + formdata2[0] + "=" +
                               formdata3[0];
                count++;
                //cmd:s#
                //date:1214
                //ankn:201
            }
        }
        returnstring = returnstring + "&Submit=Submit";
        System.out.println("returnstring: " + returnstring);
        /*
         String submitstring = "imei=" + ipserver.URLEncode(imei) + "&confdata=" +
         ipserver.URLEncode(confdata) + "&logdata=" +
         ipserver.URLEncode(logdata) + "&logfilesize=" +
                                          logfilesize + "&icount=" + icount +
                                          "&Submit=Submit";

         */


        return returnstring;
    }

    public static Hashtable GetRespData(String resp,String replystring) {
     Hashtable responsehash = new Hashtable();
     if (SetXMLSendData(replystring)) {
         MCTParse.CtParser cp = new CtParser();
         if (resptype.equals("0")) {
             // String res = cp.CheckResultTkn(resp,splitvalue,returnvalfields,okstring,responsehash);
         }
         if (resptype.equals("1")) {
            String res = cp.CheckResultXML(resp,splitvalue,returnvalfields,okstring,responsehash);
         }
         antdata = cp.c;
         System.out.println("CTIP Antal rader tillbaka: " + antdata);
         cp = null;
     }
     return responsehash;
    }


    public static boolean SetXMLSendData(String replystring) {
        boolean ok = true;
        String[] replystringsplit = StringUtil.Splitstring(replystring, "#", -1); //Splitta replystring med #
        int headerant = 5;
        int antal = replystringsplit.length-headerant;
        if (antal < 0) {
          antal = 0;
        }
        String blank = replystringsplit[0];
        resptype = replystringsplit[1]; //0 teckenseparerat eller 1 XML
        System.out.println("resptype: " + resptype);
        /*
         Nästa fält innehåller olika beroende på vilket resptype det är
         0; Då innehåller den vilket tecken vi ska splitta indatat med
         1; Då innehåller den vilket XML root element vi ska använda
         */
        splitvalue =  replystringsplit[2];
        System.out.println("splitvalue: " + splitvalue);
        /*
         Nästa fält innehåller olika beroende på vilket resptype det är
         0; Då innehåller det en siffra på vilket fält i ordningen som är returkodsfältet
         1; Då innehåller den vilket XML element som innehåller returkoden
         */
        String returnvalfield = replystringsplit[3];
        System.out.println("returnvalfield: " + returnvalfield);
        /*
         Sista fältet innehåller vilket värde returnvaluefield ska innehålla om allt går bra
         */
         okstring = replystringsplit[4];
         System.out.println("okstring: " + okstring);

         returnvalfields = new String[antal+1];
         returnvalvalues = new String[antal+1];
         returnvalfields[0] =   returnvalfield;
         returnvalvalues[0] =   "";
         for (int rf=0;rf < antal;rf++) {
             String[] replystringpair = StringUtil.Splitstring(replystringsplit[(headerant)+rf], "*", -1); //Splitta replystring med *
             if (replystringpair.length > 0) {
              returnvalfields[rf+1] = replystringpair[0];
             } else {
              returnvalfields[rf+1] = replystringsplit[rf];
             }
             if (replystringpair.length > 1) {
               returnvalvalues[rf+1] = replystringpair[1];
             } else {
               returnvalvalues[rf+1] = "";
             }
         }
         return ok;
    }

    public static String[] CheckResponse(String resp,String replystring) {
     System.out.println("** CheckResponse **");
     String[] response = new String[2]; //Fixa svarsarray

     MCTParse.CtParser cp = new CtParser();
     String res = "Error setting XMLData";
     if (SetXMLSendData(replystring)) {
     if (resptype.equals("0")) {
        // res = cp.CheckResultTkn(resp,splitvalue,returnvalfields,okstring);
     }
     if (resptype.equals("1")) {
          res = cp.CheckResultXML(resp,splitvalue,returnvalfields,okstring,null);
     }
     cp = null;
     }
     if (res.equals("OK")) {
         response[0] = "OK";
         response[1] = "";
     } else {
         response[0] = "NOK";
         response[1] = res;
     }

     return response;
    }
}


