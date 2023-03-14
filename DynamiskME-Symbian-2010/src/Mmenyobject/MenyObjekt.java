package Mmenyobject;
import MModel.LANG;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Image;
import MModel.CONF;
import javax.microedition.lcdui.TextField;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Date;
import javax.microedition.lcdui.Choice;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.DateField;
import java.util.Calendar;

public class MenyObjekt {
public static Form f_currentform;
public static List f_currentlist;
public static int[] i_currentlistcommand = new int[4];
public static int currTimeformat;
public static int currDateformat;
public Hashtable confitems;
public Form[][][][] forms;
public List[][][] lists;
public Command[][][][] listcommands;
public Image[][][][] listimages;
public Image[][][][] listimages_off;
public String[][][][] listtexts;
public int[][][][] listlist;
private MModel.LANG langobj;
public TextField[][][][][] formtextfields;
public DateField[][][][][] formdatefields;
public ChoiceGroup[][][][][] formchoicefields;
public String[][][][][] formtextfieldnames;
public int[][][][][] formfieldtype;
public Command[][][][][] formcommands;
public String[][][][] formservercommand;
public String[][][][] listactions;
public int[][][][][] formservercommandcheck;
public int[][][][] listcommandcheck;
private Date currentTime;
private Calendar cal;
public final static String CONFDELIM = "%";
public final static String FORMDELIM = "_";
public final static String CONFFORMEND = ";";

public final static int BACK =0;
public final static int SAVE =1;
public final static int CANCEL =2;
public final static int SEND =3;
public final static int ABOUT =4;
public final static int EXIT =5;
public final static int EDIT =6;

public final static int C_SHOW_CONFIG = 49;// 1 Visa conffilen
public final static int C_SYSTEMADMIN = 50;// 2 Logga in och ur Debugläget
public final static int C_REMOVE_FORWARD_PRESENCE_DTMF = 51;// 3 Tabort Hänvisning / Vidarekoppling DTMF
public final static int C_REMOVE_FORWARD_PRESENCE_SMS = 52;// 4 Tabort Hänvisning / Vidarekoppling SMS
public final static int C_SET_FORWARD_PRESENCE_IP = 53;// 5 Hänvisning / Vidarekoppling IP
public final static int C_REMOVE_FORWARD_PRESENCE_IP = 54;// 6 Tabort Hänvisning / VIdarekoppling IP
public final static int C_SET_FORWARD_PRESENCE_SMS_TOTALVIEW = 55;// 7 Send sms to TotalView Gsm server
public final static int C_STORE_LOGDATA_SDCARD = 56;// 8 Store Logdata to SDCard
public final static int C_REMOVE_FORWARD_PRESENCE_NODTMF = 57;// 9 Call specific number and hangup remove pres
public final static int C_CALLPBX_DTMF = 97;// a Ring anknytning, Ringer upp växel skickar DTMF, Lägger inte på
public final static int C_EDIT_CONFDATA = 98;// b Editera Confdata
public final static int C_STATUSCOLLEGUES = 99;// c Get status on collegues
public final static int C_DEBUG_ON_OFF = 100;// d Sätt Debug ON/OFF
public final static int C_MEXOFF = 101;// e Stänger av MEX
public final static int C_MEXSTATUS = 102;// f Kollar status på MEX
public final static int C_DTMF_ONGOINGCALL = 103;// g Skicka DTMF toner under pågående samtal
public final static int C_SET_FORWARD_PRESENCE_DTMF = 104;// h Hänvisning, Rnger upp växel skicka DTMF, Lägg på
public final static int C_GETALLCONFDATA_FROM_SERVER = 105;// i Hämtar all configdata
public final static int C_GETLOGDATA_FROM_SERVER = 106;// j Hämtar logdata
public final static int C_GETCONFVALUE_FROM_SERVER = 107;// k Hämtar specifikt confvalue
public final static int C_IMEI_EXPIRED = 108;// l Demo expired Sätter IMEI expired och stänger av
public final static int C_MINIMISE = 109;// m Minimerar Java applikationen
public final static int C_SET_FORWARD_PRESENCE_SMS = 110;// n Send sms to GSM Server
public final static int C_SEND_LOGDATA_TO_MOBISMA = 111;// o Skickar logdata till mobisma
public final static int C_ABOUT = 112;// p Om mobisma
public final static int C_SHOW_LOGFILE = 113;// q Visa logfilen
public final static int C_REFRESH_MEX = 114;// r Refresh Mexdata
public final static int C_MEXON = 115;// s Starta Mex
public final static int C_TEST_SERVER_CONNECTION = 116;// t Testar connection
public final static int C_GETLOGFILESIZE = 117;// u Hämtar logfilens storlek
public final static int C_MAXIMIZE = 118;// v Maximerar Java applikationen
public final static int C_SET_FORWARD_PRESENCE_NODTMF = 119;// w Call specific number and hangup
public final static int C_SLUTMENYOBJEKT = 120;// x Slutmenyobjekt
public final static int C_SAVECONF = 121;// y Sparar data i configfilen
public final static int C_UNKNOWN = 122;// z Används? Skickar DTMF

public final static String CT_TV = "TV";// Total View (SMS)
public final static String CT_8020I = "8020I";// 8020 (IP)
public final static String CT_CTSERVER = "CTSERVER";// CT Server (SMS)
public final static String CT_CTSERVERI = "CTSERVERI";// CT Server (IP)
public final static String CT_TVI = "TVI";// Total View (IP)
public final static String CT_CASERVERI = "CASERVERI";// CA Server (IP)
public final static String CT_8020D = "8020D";// 8020 (DTMF)
public final static String CT_MOBCTBI = "MOBCTBI";// Mobisma CT-bridge (IP)
public final static String CT_MOBCTEX = "MOBCTEX";// Mobisma CT-bridge (Exchange)

public final static int FI_TEXTBOX = 1;
public final static int FI_DATEFIELD = 2;
public final static int FI_PHONENUMBER = 3;
public final static int FI_ANY = 4;
public final static int FI_CHOICEGROUP = 5;
public final static int FI_CHECKBOX = 6;
public final static int FI_TIMEFIELD = 7;
public final static int FI_NUMERIC = 8;
public final static int FI_EXTENSION = 9;
public final static int FI_TEXTBOXMI = 10;

public final static int FORMAT_DATE_STD = 0;
public final static int FORMAT_DATE_TV = 1;
public final static int FORMAT_DATE_8020 = 2;

public final static int FORMAT_TIME_STD = 0;
public final static int FORMAT_TIME_TV = 1;
public final static int FORMAT_TIME_8020 = 2;
public final static int ARRAY_ANTLIST_0 = 2; //Mainlist alltid 2
public final static int ARRAY_ANTLIST_1 = 3; //Max antal listor i Mainlist
public final static int ARRAY_SUBLIST = 1; //Max Antal sublistor i en lista under Mainlist
public final static int ARRAY_FORMS = 41; //Max antal forms per listitem
public final static int ARRAY_FORMFIELDS = 6; //Max antal formfields per form
public final static int ARRAY_LISTITEMS = 11; //Max antal items i en lista
public final static int ARRAY_FORMCOMMANDS = 6; //Max antal commands per form
public final static int ARRAY_LISTCOMMANDS = 6; //Max antal commands per lista

public MenyObjekt() throws IOException, InvalidRecordIDException,RecordStoreNotOpenException, RecordStoreException {
cal= Calendar.getInstance();
currentTime = new Date();

confitems = new Hashtable();
confitems.put("LA", new Integer(4)); //Line access
confitems.put("SWTBNR", new Integer(5)); //Switchboard number
confitems.put("ANKN", new Integer(9)); //Extension
confitems.put("PCODE", new Integer(10)); //Pin code
confitems.put("VM", new Integer(12)); //Operator Voicemailnumber
confitems.put("DBG", new Integer(13)); //Debug status
confitems.put("CCODE", new Integer(15)); //Country code
confitems.put("MEX", new Integer(16)); //Mex status
confitems.put("PBXVM", new Integer(18)); //Switchboards voicemail
confitems.put("LANG", new Integer(19)); //Language number
confitems.put("DEMO", new Integer(20)); //Demoversion
confitems.put("COMPANYNAME", new Integer(22)); //Company name of registrated owner
confitems.put("NAME", new Integer(23)); //Name of registrated owner
confitems.put("PRECODE", new Integer(27)); //Pre edit code
confitems.put("PBXID", new Integer(35)); //Id number of Pbx name
confitems.put("PRGNAME ", new Integer(36)); //Midlet name
confitems.put("IMEI", new Integer(39)); //Imei number decoded
confitems.put("DL", new Integer(43)); //Default lang 1 = inte engelska, 0 = engelska
confitems.put("PBRAND", new Integer(45)); //Phone brand
confitems.put("PMODEL", new Integer(46)); //Phone model
confitems.put("EXT1", new Integer(51)); //Saved Extensionlist
confitems.put("EXT2", new Integer(52)); //Saved Extensionlist
confitems.put("EXT3", new Integer(53)); //Saved Extensionlist
confitems.put("EXT4", new Integer(54)); //Saved Extensionlist
confitems.put("EXT5", new Integer(55)); //Saved Extensionlist
confitems.put("EXT6", new Integer(56)); //Saved Extensionlist
confitems.put("EXT7", new Integer(57)); //Saved Extensionlist
confitems.put("EXT8", new Integer(58)); //Saved Extensionlist
confitems.put("GSMNUMBER", new Integer(101)); //Number of possible GSM Modem
confitems.put("ACCESSVM", new Integer(103)); //Access kod till Voicemail
confitems.put("ACCESSKONF", new Integer(104)); //Access kodr till Konferens
confitems.put("DTMFP", new Integer(107)); //DTMF-tecken för paus
confitems.put("IDDCODE", new Integer(108)); //International direct dialing code
confitems.put("AREACODEREMOVE", new Integer(109)); //Number to remove on areacode when dialing abroad
confitems.put("JAVAVERSION", new Integer(110)); //Java version from version.txt
confitems.put("SERVERVERSION", new Integer(111)); //Server version
confitems.put("DTMFSENDTYPE", new Integer(112)); //Wich sendtype for dtmf to be used
confitems.put("HANGUPTYPE", new Integer(113)); //If New called will be dialed directly or when old call is disconnected
confitems.put("IHPORT", new Integer(114)); //Wich Port to use for CT-IP communication
confitems.put("IHURL", new Integer(115)); //Wich URL adress to use for CT-IP communication
confitems.put("JDEMO", new Integer(116)); //Check av Demoperiod
confitems.put("CTEPOST", new Integer(117)); //Id for Exchangeserver

forms = new Form[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS];
lists = new List[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST];
listcommands = new Command[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTCOMMANDS];
listcommandcheck = new int[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTCOMMANDS];
listimages = new Image[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTITEMS];
listimages_off = new Image[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTITEMS];
listtexts = new String[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTITEMS];
listlist = new int[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_LISTITEMS];
formtextfields = new TextField[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMFIELDS];
formchoicefields = new ChoiceGroup[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMFIELDS];
formdatefields = new DateField[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMFIELDS];
formtextfieldnames = new String[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMFIELDS];
formfieldtype = new int[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMFIELDS];
formcommands = new Command[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMCOMMANDS];
formservercommandcheck = new int[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS][ARRAY_FORMCOMMANDS];
formservercommand = new String[ARRAY_ANTLIST_0][ARRAY_ANTLIST_1][ARRAY_SUBLIST][ARRAY_FORMS];
/*
SELECT m_menuelements.value,m_menuelements.root,m_menuelements.name,m_servercommands.confparams,m_pbxmenu.formdata,tblphonelangvariables.svarname,m_pbxmenu.formitemid1,m_pbxmenu.formitemid2,m_pbxmenu.formitemid3,m_pbxmenu.formitemid4,m_menuelements.mainmenu,m_xmlnames.value, m_menuelements.rootid, m_menuelements.iconname, m_menuelements.undermenyid, m_servercommands.cmd,sort,m_menuelements.id,tblctseparotor.ctipseparator,tblctseparotor.id,ipokresponse,ctiprespdataid,tblctseparotor.type from m_pbxmenu inner join tblpbx on tblpbx.id = m_pbxmenu.mpbxid inner join tblctseparotor on tblctseparotor.id = tblpbx.idctipsep inner join m_menuelements on m_pbxmenu.menuitemid = m_menuelements.id inner join tblphonelangvariables on tblphonelangvariables.svarid = m_menuelements.varlangid inner join m_servercommands on m_servercommands.id = m_menuelements.servercmdid inner join m_xmlnames on m_xmlnames.id = m_menuelements.rootid where (tblpbx.id = 15 or tblpbx.id=0) and (m_menuelements.pbxspecific LIKE '%00%' or m_menuelements.pbxspecific LIKE '%15%' or m_menuelements.pbxspecific LIKE '%22%') and m_menuelements.value <> 'Back' and m_menuelements.value <> 'back' and m_xmlnames.id <> 4 and m_xmlnames.id <> 14 and m_xmlnames.id <> 22 and m_xmlnames.id <> 23 and tblphonelangvariables.vlanggroupid=2 and m_menuelements.posspecific LIKE '%01%' and mainmenu < 2 order by m_menuelements.mainmenu,sort,m_menuelements.rootid,m_menuelements.root
*/
//Startmenu
/*
Menuroot
*/

System.out.println("***Menuroot List***");
lists[0][0][0] = new List(LANG.DefMenu_DEF, Choice.IMPLICIT);
listcommands[0][0][0][0] = new Command(LANG.genDefaultExit_DEF,Command.EXIT, 1);
listcommandcheck[0][0][0][0] = EXIT;
listcommands[0][0][0][1] = new Command(LANG.genDefaultEdit_DEF,Command.HELP, 1);
listcommandcheck[0][0][0][1] = EDIT;
listcommands[0][0][0][2] = new Command(LANG.settingsDefaultAbout_DEF,Command.HELP, 1);
listcommandcheck[0][0][0][2] = ABOUT;
listcommands[0][0][0][3] = new Command(LANG.genDefaultExit_DEF,Command.EXIT, 1);
listcommandcheck[0][0][0][3] = EXIT;

//Vi är i mainmenu
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//Transfer Listitem
System.out.println("Transfer Listitem");
listimages[0][0][0][0] = Image.createImage(CONF.ICONDIR + "vidarekoppling24.png");
//m_menuelements_id: 14
listtexts[0][0][0][0] = LANG.callForwardTransfer_DEF;
listlist[0][0][0][0] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Transfer form
System.out.println("Transfer form");
forms[0][0][0][0] = new Form(LANG.callForwardTransfer_DEF);
formcommands[0][0][0][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][0][0][0][0] = BACK;
formcommands[0][0][0][0][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][0][0][0][1] = SEND;
/*Start formitems */
formtextfieldnames[0][0][0][0][0] = "_NewExtension";
formfieldtype[0][0][0][0][0] = FI_PHONENUMBER;
formtextfields[0][0][0][0][0] = new TextField(LANG.enterDefaultEnterExtension_DEF,"", CONF.IS,TextField.PHONENUMBER);
formchoicefields[0][0][0][0][1] = new ChoiceGroup(LANG.enterDefaultEnterExtension_DEF,Choice.EXCLUSIVE);
formchoicefields[0][0][0][0][1].append(CONF.EXT1, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT2, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT3, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT4, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT5, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT6, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT7, null);
formchoicefields[0][0][0][0][1].append(CONF.EXT8, null);
/*End formitems */
formservercommand[0][0][0][0] = "g,,#_NewExtension;";
//Flytta oss fram i mainlist
/* End for start with CT */
/*
SELECT m_menuelements.value,m_menuelements.root,m_menuelements.name,m_servercommands.confparams,m_pbxmenu.formdata,tblphonelangvariables.svarname,m_pbxmenu.formitemid1,m_pbxmenu.formitemid2,m_pbxmenu.formitemid3,m_pbxmenu.formitemid4,m_menuelements.mainmenu,m_xmlnames.value, m_menuelements.rootid, m_menuelements.iconname, m_menuelements.undermenyid, m_servercommands.cmd,sort,m_menuelements.id,tblctseparotor.ctipseparator,tblctseparotor.id,ipokresponse,ctiprespdataid,tblctseparotor.type from m_pbxmenu inner join tblpbx on tblpbx.id = m_pbxmenu.mpbxid inner join tblctseparotor on tblctseparotor.id = tblpbx.idctipsep inner join m_menuelements on m_pbxmenu.menuitemid = m_menuelements.id inner join tblphonelangvariables on tblphonelangvariables.svarid = m_menuelements.varlangid inner join m_servercommands on m_servercommands.id = m_menuelements.servercmdid inner join m_xmlnames on m_xmlnames.id = m_menuelements.rootid where (tblpbx.id = 22 ) and (m_menuelements.pbxspecific LIKE '%00%' or m_menuelements.pbxspecific LIKE '%22%' ) and m_menuelements.value <> 'Back' and m_menuelements.value <> 'back' and tblphonelangvariables.vlanggroupid=2 and m_menuelements.posspecific LIKE '%01%' and mainmenu < 2 order by m_menuelements.mainmenu,sort,m_menuelements.rootid,m_menuelements.root
*/
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//menyitemet i mainmenu har en sublista
//ctmobexmain Listitem
System.out.println("ctmobexmain Listitem");
listimages[0][0][0][0] = Image.createImage(CONF.ICONDIR + "me_small.png");
//m_menuelements_id: 334
listtexts[0][0][0][0] = LANG.absentDefaultSetPresence_DEF;
listlist[0][0][0][0] = 1; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][0][0][0] = null;
formservercommand[0][0][0][0] = " ,#1#Exchange#response#OK#,";
/*
ctmobexmain
*/

System.out.println("***ctmobexmain List***");
lists[0][1][0] = new List(LANG.absentDefaultSetPresence_DEF, Choice.IMPLICIT);
listcommands[0][1][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
listcommandcheck[0][1][0][0] = BACK;

//Flytta oss fram i mainlist
//***************ARRAY_ANTLIST_1: 1
//***************ARRAY_LISTITEMS: 1
//Vi är i en submenu lägg till item itemcount[1]: 0
//semester_ctmobex Listitem
System.out.println("semester_ctmobex Listitem");
listimages[0][1][0][0] = Image.createImage(CONF.ICONDIR + "semester24.png");
//m_menuelements_id: 330
listtexts[0][1][0][0] = LANG.absentVacation_DEF;
listlist[0][1][0][0] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//semester_ctmobex form
System.out.println("semester_ctmobex form");
forms[0][1][0][0] = new Form(LANG.absentVacation_DEF);
formcommands[0][1][0][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][1][0][0][0] = BACK;
formcommands[0][1][0][0][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][1][0][0][1] = SEND;
/*Start formitems */
formtextfieldnames[0][1][0][0][0] = "_DateOfReturn";
formfieldtype[0][1][0][0][0] = FI_DATEFIELD;
formdatefields[0][1][0][0][0] = new DateField("", DateField.DATE);
formdatefields[0][1][0][0][0].setDate(currentTime);
formtextfieldnames[0][1][0][0][1] = "_CTINFO";
formfieldtype[0][1][0][0][1] = FI_TEXTBOXMI;
formtextfields[0][1][0][0][1] = new TextField(LANG.alertDefaultInfo_DEF,"",50,TextField.ANY);
formtextfields[0][1][0][0][1].setString(LANG.absentVacation_DEF);
/*End formitems */
formservercommand[0][1][0][0] = "6,#1#Exchange#response#OK#,*cmd:s#*postvar1:%CTEPOST;*postvar2:_DateOfReturn;*postvar3:_CTINFO;";
//itemcount[1] uppräknad: 1
//Vi är i en submenu lägg till item itemcount[1]: 1
//lunch_ctmobex Listitem
System.out.println("lunch_ctmobex Listitem");
listimages[0][1][0][1] = Image.createImage(CONF.ICONDIR + "lunch24.png");
//m_menuelements_id: 331
listtexts[0][1][0][1] = LANG.absentLunchOO_DEF;
listlist[0][1][0][1] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//lunch_ctmobex form
System.out.println("lunch_ctmobex form");
forms[0][1][0][1] = new Form(LANG.absentLunchOO_DEF);
formcommands[0][1][0][1][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][1][0][1][0] = BACK;
formcommands[0][1][0][1][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][1][0][1][1] = SEND;
/*Start formitems */
formtextfieldnames[0][1][0][1][0] = "_TimeOfReturn";
formfieldtype[0][1][0][1][0] = FI_TIMEFIELD;
formdatefields[0][1][0][1][0] = new DateField("", DateField.TIME);
formdatefields[0][1][0][1][0].setDate(currentTime);
formtextfieldnames[0][1][0][1][1] = "_CTINFO";
formfieldtype[0][1][0][1][1] = FI_TEXTBOXMI;
formtextfields[0][1][0][1][1] = new TextField(LANG.alertDefaultInfo_DEF,"",50,TextField.ANY);
formtextfields[0][1][0][1][1].setString(LANG.absentLunchOO_DEF);
/*End formitems */
formservercommand[0][1][0][1] = "6,#1#Exchange#response#OK#,*cmd:l#*postvar1:%CTEPOST;*postvar2:_TimeOfReturn;*postvar3:_CTINFO;";
//itemcount[1] uppräknad: 2
//***************ARRAY_LISTITEMS: 2
//Vi är i en submenu lägg till item itemcount[1]: 2
//ute_ctmobex Listitem
System.out.println("ute_ctmobex Listitem");
listimages[0][1][0][2] = Image.createImage(CONF.ICONDIR + "ute24.png");
//m_menuelements_id: 332
listtexts[0][1][0][2] = LANG.absentBackAtLG_DEF;
listlist[0][1][0][2] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//ute_ctmobex form
System.out.println("ute_ctmobex form");
forms[0][1][0][2] = new Form(LANG.absentBackAtLG_DEF);
formcommands[0][1][0][2][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][1][0][2][0] = BACK;
formcommands[0][1][0][2][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][1][0][2][1] = SEND;
/*Start formitems */
formtextfieldnames[0][1][0][2][0] = "_TimeOfReturn";
formfieldtype[0][1][0][2][0] = FI_TIMEFIELD;
formdatefields[0][1][0][2][0] = new DateField("", DateField.TIME);
formdatefields[0][1][0][2][0].setDate(currentTime);
formtextfieldnames[0][1][0][2][1] = "_CTINFO";
formfieldtype[0][1][0][2][1] = FI_TEXTBOXMI;
formtextfields[0][1][0][2][1] = new TextField(LANG.alertDefaultInfo_DEF,"",50,TextField.ANY);
formtextfields[0][1][0][2][1].setString(LANG.absentBackAtLG_DEF);
/*End formitems */
formservercommand[0][1][0][2] = "6,#1#Exchange#response#OK#,*cmd:u#*postvar1:%CTEPOST;*postvar2:_TimeOfReturn;*postvar3:_CTINFO;";
//itemcount[1] uppräknad: 3
//***************ARRAY_LISTITEMS: 3
//Vi är i en submenu lägg till item itemcount[1]: 3
//status_ctmobex Listitem
System.out.println("status_ctmobex Listitem");
listimages[0][1][0][3] = Image.createImage(CONF.ICONDIR + "user.png");
//m_menuelements_id: 335
listtexts[0][1][0][3] = LANG.statusToday_DEF;
listlist[0][1][0][3] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][1][0][3] = null;
formservercommand[0][1][0][3] = "c,#1#Exchange#response#OK#Name*#status*1#text*,*cmd:c#*postvar1:%CTEPOST;*postvar2:x#;*postvar3:x#";
//itemcount[1] uppräknad: 4
//***************ARRAY_LISTITEMS: 4
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//Pvoicemail Listitem
System.out.println("Pvoicemail Listitem");
listimages[0][0][0][1] = Image.createImage(CONF.ICONDIR + "rostbrevlada24.png");
//m_menuelements_id: 246
listtexts[0][0][0][1] = LANG.voiceMailDefaultVoiceMail_DEF;
listlist[0][0][0][1] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][0][0][1] = null;
formservercommand[0][0][0][1] = "a,%SWTBNR;,%PBXVM;";
//Flytta oss fram i mainlist
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//menyitemet i mainmenu har en sublista
//Groups Listitem
System.out.println("Groups Listitem");
listimages[0][0][0][2] = Image.createImage(CONF.ICONDIR + "konference24.png");
//m_menuelements_id: 15
listtexts[0][0][0][2] = LANG.groupsDefaultGroups_DEF;
listlist[0][0][0][2] = 2; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][0][0][2] = null;
formservercommand[0][0][0][2] = " , ,";
/*
Groups
*/

System.out.println("***Groups List***");
lists[0][2][0] = new List(LANG.groupsDefaultGroups_DEF, Choice.IMPLICIT);
listcommands[0][2][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
listcommandcheck[0][2][0][0] = BACK;

//Flytta oss fram i mainlist
//***************ARRAY_ANTLIST_1: 2
//Vi är i en submenu lägg till item itemcount[1]: 0
//Login_All_Groups Listitem
System.out.println("Login_All_Groups Listitem");
listimages[0][2][0][0] = Image.createImage(CONF.ICONDIR + "konference24.png");
//m_menuelements_id: 30
listtexts[0][2][0][0] = LANG.groupsDefaultLoginAllGroups_DEF;
listlist[0][2][0][0] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][2][0][0] = null;
formservercommand[0][2][0][0] = "h,%SWTBNR;,%PRECODE;361*";
//itemcount[1] uppräknad: 1
//Vi är i en submenu lägg till item itemcount[1]: 1
//Logout_All_Groups Listitem
System.out.println("Logout_All_Groups Listitem");
listimages[0][2][0][1] = Image.createImage(CONF.ICONDIR + "konference24.png");
//m_menuelements_id: 31
listtexts[0][2][0][1] = LANG.groupsDefaultLogoutAllGroups_DEF;
listlist[0][2][0][1] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][2][0][1] = null;
formservercommand[0][2][0][1] = "h,%SWTBNR;,%PRECODE;360*";
//itemcount[1] uppräknad: 2
//Vi är i en submenu lägg till item itemcount[1]: 2
//Login_Specific_Group Listitem
System.out.println("Login_Specific_Group Listitem");
listimages[0][2][0][2] = Image.createImage(CONF.ICONDIR + "konference24.png");
//m_menuelements_id: 32
listtexts[0][2][0][2] = LANG.groupsDefaultLoginSpecificGroup_DEF;
listlist[0][2][0][2] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Login_Specific_Group form
System.out.println("Login_Specific_Group form");
forms[0][2][0][2] = new Form(LANG.groupsDefaultLoginSpecificGroup_DEF);
formcommands[0][2][0][2][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][2][0][2][0] = BACK;
formcommands[0][2][0][2][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][2][0][2][1] = SEND;
/*Start formitems */
formtextfieldnames[0][2][0][2][0] = "_GroupNr";
formfieldtype[0][2][0][2][0] = FI_TEXTBOX;
formtextfields[0][2][0][2][0] = new TextField(LANG.enterDefaultEnterGroupNumber_DEF,"",5,TextField.ANY);
/*End formitems */
formservercommand[0][2][0][2] = "h,%SWTBNR;,%PRECODE;361_GroupNr;";
//itemcount[1] uppräknad: 3
//Vi är i en submenu lägg till item itemcount[1]: 3
//Logout_Specific_Group Listitem
System.out.println("Logout_Specific_Group Listitem");
listimages[0][2][0][3] = Image.createImage(CONF.ICONDIR + "konference24.png");
//m_menuelements_id: 33
listtexts[0][2][0][3] = LANG.groupsDefaultLogoutSpecificGroup_DEF;
listlist[0][2][0][3] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Logout_Specific_Group form
System.out.println("Logout_Specific_Group form");
forms[0][2][0][3] = new Form(LANG.groupsDefaultLogoutSpecificGroup_DEF);
formcommands[0][2][0][3][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][2][0][3][0] = BACK;
formcommands[0][2][0][3][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][2][0][3][1] = SEND;
/*Start formitems */
formtextfieldnames[0][2][0][3][0] = "_GroupNr";
formfieldtype[0][2][0][3][0] = FI_TEXTBOX;
formtextfields[0][2][0][3][0] = new TextField(LANG.enterDefaultEnterGroupNumber_DEF,"",5,TextField.ANY);
/*End formitems */
formservercommand[0][2][0][3] = "h,%SWTBNR;,%PRECODE;360_GroupNr;";
//itemcount[1] uppräknad: 4
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//Pconference Listitem
System.out.println("Pconference Listitem");
listimages[0][0][0][3] = Image.createImage(CONF.ICONDIR + "conference.png");
//m_menuelements_id: 248
listtexts[0][0][0][3] = LANG.programConference_DEF;
listlist[0][0][0][3] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Pconference form
System.out.println("Pconference form");
forms[0][0][0][3] = new Form(LANG.programConference_DEF);
formcommands[0][0][0][3][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[0][0][0][3][0] = BACK;
formcommands[0][0][0][3][1] = new Command(LANG.genDefaultSend_DEF,Command.HELP, 1);
formservercommandcheck[0][0][0][3][1] = SEND;
/*Start formitems */
formtextfieldnames[0][0][0][3][0] = "_ACCESSKONF";
formfieldtype[0][0][0][3][0] = FI_TEXTBOX;
formtextfields[0][0][0][3][0] = new TextField(LANG.accessCode_DEF,"",5,TextField.ANY);
formtextfieldnames[0][0][0][3][1] = "_KONFRUMSNR";
formfieldtype[0][0][0][3][1] = FI_TEXTBOX;
formtextfields[0][0][0][3][1] = new TextField(LANG.editConferenceRoomNr_DEF,"",5,TextField.ANY);
/*End formitems */
formservercommand[0][0][0][3] = "a,%SWTBNR;,*32#_KONFRUMSNR;_ACCESSKONF;#";
//Flytta oss fram i mainlist
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//Mex Listitem
System.out.println("Mex Listitem");
listimages[0][0][0][4] = Image.createImage(CONF.ICONDIR + "on24.png");
//m_menuelements_id: 5
listimages_off[0][0][0][4] = Image.createImage(CONF.ICONDIR + "off24.png");
listtexts[0][0][0][4] = LANG.mex_DEF;
listlist[0][0][0][4] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][0][0][4] = null;
formservercommand[0][0][0][4] = "s,|e,,,";
//Flytta oss fram i mainlist
//***************ARRAY_LISTITEMS: 5
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//Minimise Listitem
System.out.println("Minimise Listitem");
listimages[0][0][0][5] = Image.createImage(CONF.ICONDIR + "minimera24.png");
//m_menuelements_id: 6
listtexts[0][0][0][5] = LANG.genDefaultMinimise_DEF;
listlist[0][0][0][5] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[0][0][0][5] = null;
formservercommand[0][0][0][5] = "m,,";
//Flytta oss fram i mainlist
//***************ARRAY_LISTITEMS: 6
//Startmenu
/*
Systemroot
*/

System.out.println("***Systemroot List***");
lists[1][0][0] = new List(LANG.DefOption_DEF, Choice.IMPLICIT);
listcommands[1][0][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
listcommandcheck[1][0][0][0] = BACK;

//Vi är i mainmenu
//***************ARRAY_ANTLIST_0: 1
//ABOUT Borttagen
//ADMIN Borttagen
//SYSTEM_LOGIN Borttagen
//SYSTEM_LOGOUT Borttagen
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//menyitemet i mainmenu har en sublista
//system Listitem
System.out.println("system Listitem");
listimages[1][0][0][0] = null;
//m_menuelements_id: 82
listtexts[1][0][0][0] = LANG.mSystemUtilites_DEF;
listlist[1][0][0][0] = 1; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[1][0][0][0] = null;
formservercommand[1][0][0][0] = " , ,";
/*
system
*/

System.out.println("***system List***");
lists[1][1][0] = new List(LANG.mSystemUtilites_DEF, Choice.IMPLICIT);
listcommands[1][1][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
listcommandcheck[1][1][0][0] = BACK;

//Flytta oss fram i mainlist
//Vi är i en submenu lägg till item itemcount[1]: 0
//debugon Listitem
System.out.println("debugon Listitem");
listimages[1][1][0][0] = null;
//m_menuelements_id: 83
listtexts[1][1][0][0] = LANG.sysDebugOn_DEF;
listlist[1][1][0][0] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[1][1][0][0] = null;
formservercommand[1][1][0][0] = "d,,1";
//itemcount[1] uppräknad: 1
//Vi är i en submenu lägg till item itemcount[1]: 1
//debugoff Listitem
System.out.println("debugoff Listitem");
listimages[1][1][0][1] = null;
//m_menuelements_id: 84
listtexts[1][1][0][1] = LANG.sysDebugOff_DEF;
listlist[1][1][0][1] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[1][1][0][1] = null;
formservercommand[1][1][0][1] = "d,,0";
//itemcount[1] uppräknad: 2
//Vi är i en submenu lägg till item itemcount[1]: 2
//debugshow Listitem
System.out.println("debugshow Listitem");
listimages[1][1][0][2] = null;
//m_menuelements_id: 86
listtexts[1][1][0][2] = LANG.sysShowLog_DEF;
listlist[1][1][0][2] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//debugshow form
System.out.println("debugshow form");
forms[1][1][0][2] = new Form(LANG.sysShowLog_DEF);
formcommands[1][1][0][2][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][1][0][2][0] = BACK;
formcommands[1][1][0][2][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][1][0][2][1] = SEND;
/*Start formitems */
formtextfieldnames[1][1][0][2][0] = "_Logfile";
formfieldtype[1][1][0][2][0] = FI_TEXTBOX;
formtextfields[1][1][0][2][0] = new TextField(LANG.sysShowLog_DEF,"",10000,TextField.ANY);
/*End formitems */
formservercommand[1][1][0][2] = "q, ,_Logfile;";
//itemcount[1] uppräknad: 3
//Vi är i en submenu lägg till item itemcount[1]: 3
//sendlog Listitem
System.out.println("sendlog Listitem");
listimages[1][1][0][3] = null;
//m_menuelements_id: 85
listtexts[1][1][0][3] = LANG.sysDebugSend_DEF;
listlist[1][1][0][3] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[1][1][0][3] = null;
formservercommand[1][1][0][3] = "o,http://www.mobisma.com:80/socketapi/mobilesock.php,";
//itemcount[1] uppräknad: 4
//Vi är i mainmenu
//Vi har ett menyitem som ska va i mainmenu
//menyitemet i mainmenu har en sublista
//Edit Listitem
System.out.println("Edit Listitem");
listimages[1][0][0][1] = null;
//m_menuelements_id: 11
listtexts[1][0][0][1] = LANG.genDefaultEdit_DEF;
listlist[1][0][0][1] = 2; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
forms[1][0][0][1] = null;
formservercommand[1][0][0][1] = " , ,";
/*
Edit
*/

System.out.println("***Edit List***");
lists[1][2][0] = new List(LANG.genDefaultEdit_DEF, Choice.IMPLICIT);
listcommands[1][2][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
listcommandcheck[1][2][0][0] = BACK;

//Flytta oss fram i mainlist
//Vi är i en submenu lägg till item itemcount[1]: 0
//Access_Pbx Listitem
System.out.println("Access_Pbx Listitem");
listimages[1][2][0][0] = null;
//m_menuelements_id: 61
listtexts[1][2][0][0] = LANG.accessPBXDefault_DEF;
listlist[1][2][0][0] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Access_Pbx form
System.out.println("Access_Pbx form");
forms[1][2][0][0] = new Form(LANG.accessPBXDefault_DEF);
formcommands[1][2][0][0][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][0][0] = BACK;
formcommands[1][2][0][0][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][0][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][0][0] = "_ANKN";
formfieldtype[1][2][0][0][0] = FI_NUMERIC;
formtextfields[1][2][0][0][0] = new TextField(LANG.settingsDefaultExtension_DEF,"", CONF.IS,TextField.NUMERIC);
formtextfieldnames[1][2][0][0][1] = "_LA";
formfieldtype[1][2][0][0][1] = FI_TEXTBOX;
formtextfields[1][2][0][0][1] = new TextField(LANG.settingsDefaultLineAccess_DEF,"",1,TextField.ANY);
formtextfieldnames[1][2][0][0][2] = "_PCODE";
formfieldtype[1][2][0][0][2] = FI_TEXTBOX;
formtextfields[1][2][0][0][2] = new TextField(LANG.settingsDefaultPINcode_DEF,"",9,TextField.ANY);
formtextfieldnames[1][2][0][0][3] = "_SWTBNR";
formfieldtype[1][2][0][0][3] = FI_PHONENUMBER;
formtextfields[1][2][0][0][3] = new TextField(LANG.settingsDefaultSwitchboardNumber_DEF,"", 30,TextField.PHONENUMBER);
formchoicefields[1][2][0][0][4] = new ChoiceGroup(LANG.settingsDefaultSwitchboardNumber_DEF,Choice.EXCLUSIVE);
formchoicefields[1][2][0][0][4].append(CONF.EXT1, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT2, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT3, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT4, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT5, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT6, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT7, null);
formchoicefields[1][2][0][0][4].append(CONF.EXT8, null);
/*End formitems */
formservercommand[1][2][0][0] = "b, ,_LA;_SWTBNR;_ANKN;_PCODE;";
//itemcount[1] uppräknad: 1
//Vi är i en submenu lägg till item itemcount[1]: 1
//Pbx_int Listitem
System.out.println("Pbx_int Listitem");
listimages[1][2][0][1] = null;
//m_menuelements_id: 247
listtexts[1][2][0][1] = LANG.m_PbxIntSett_DEF;
listlist[1][2][0][1] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//Pbx_int form
System.out.println("Pbx_int form");
forms[1][2][0][1] = new Form(LANG.m_PbxIntSett_DEF);
formcommands[1][2][0][1][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][1][0] = BACK;
formcommands[1][2][0][1][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][1][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][1][0] = "_IDDCODE";
formfieldtype[1][2][0][1][0] = FI_TEXTBOX;
formtextfields[1][2][0][1][0] = new TextField(LANG.conf_Iddcode_DEF,"",3,TextField.ANY);
formtextfieldnames[1][2][0][1][1] = "_AREACODEREMOVE";
formfieldtype[1][2][0][1][1] = FI_TEXTBOX;
formtextfields[1][2][0][1][1] = new TextField(LANG.conf_PrefixAreaRemove_DEF,"",1,TextField.ANY);
formtextfieldnames[1][2][0][1][2] = "_CCODE";
formfieldtype[1][2][0][1][2] = FI_TEXTBOX;
formtextfields[1][2][0][1][2] = new TextField(LANG.confPBXCcode_DEF,"",4,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][1] = "b, ,_CCODE;_IDDCODE;_AREACODEREMOVE;";
//itemcount[1] uppräknad: 2
//Vi är i en submenu lägg till item itemcount[1]: 2
//operatorvoicemail Listitem
System.out.println("operatorvoicemail Listitem");
listimages[1][2][0][2] = null;
//m_menuelements_id: 80
listtexts[1][2][0][2] = LANG.operatorVoicemail_DEF;
listlist[1][2][0][2] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//operatorvoicemail form
System.out.println("operatorvoicemail form");
forms[1][2][0][2] = new Form(LANG.operatorVoicemail_DEF);
formcommands[1][2][0][2][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][2][0] = BACK;
formcommands[1][2][0][2][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][2][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][2][0] = "_VM";
formfieldtype[1][2][0][2][0] = FI_TEXTBOX;
formtextfields[1][2][0][2][0] = new TextField(LANG.operatorVoicemail_DEF,"",5,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][2] = "b, ,_VM;";
//itemcount[1] uppräknad: 3
//Vi är i en submenu lägg till item itemcount[1]: 3
//ppbxvoicemail Listitem
System.out.println("ppbxvoicemail Listitem");
listimages[1][2][0][3] = null;
//m_menuelements_id: 249
listtexts[1][2][0][3] = LANG.voiceMailDefaultEditVoicemail_DEF;
listlist[1][2][0][3] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//ppbxvoicemail form
System.out.println("ppbxvoicemail form");
forms[1][2][0][3] = new Form(LANG.voiceMailDefaultEditVoicemail_DEF);
formcommands[1][2][0][3][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][3][0] = BACK;
formcommands[1][2][0][3][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][3][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][3][0] = "_PBXVM";
formfieldtype[1][2][0][3][0] = FI_TEXTBOX;
formtextfields[1][2][0][3][0] = new TextField(LANG.voiceMailDefaultVoiceMail_DEF,"",5,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][3] = "b, ,_PBXVM;";
//itemcount[1] uppräknad: 4
//Vi är i en submenu lägg till item itemcount[1]: 4
//precode Listitem
System.out.println("precode Listitem");
listimages[1][2][0][4] = null;
//m_menuelements_id: 81
listtexts[1][2][0][4] = LANG.settingsDefaultPreEditCode_DEF;
listlist[1][2][0][4] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//precode form
System.out.println("precode form");
forms[1][2][0][4] = new Form(LANG.settingsDefaultPreEditCode_DEF);
formcommands[1][2][0][4][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][4][0] = BACK;
formcommands[1][2][0][4][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][4][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][4][0] = "_PRECODE";
formfieldtype[1][2][0][4][0] = FI_TEXTBOX;
formtextfields[1][2][0][4][0] = new TextField(LANG.settingsDefaultPreEditCode_DEF,"",10,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][4] = "b, ,_PRECODE;";
//itemcount[1] uppräknad: 5
//Vi är i en submenu lägg till item itemcount[1]: 5
//systemjava Listitem
System.out.println("systemjava Listitem");
listimages[1][2][0][5] = null;
//m_menuelements_id: 323
listtexts[1][2][0][5] = LANG.system_DEF;
listlist[1][2][0][5] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//systemjava form
System.out.println("systemjava form");
forms[1][2][0][5] = new Form(LANG.system_DEF);
formcommands[1][2][0][5][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][5][0] = BACK;
formcommands[1][2][0][5][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][5][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][5][0] = "_LOGIN";
formfieldtype[1][2][0][5][0] = FI_TEXTBOX;
formtextfields[1][2][0][5][0] = new TextField(LANG.enterDefaultEnterNumber_DEF,"",4,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][5] = "2,,_LOGIN;";
//itemcount[1] uppräknad: 6
//Vi är i en submenu lägg till item itemcount[1]: 6
//languagesjava Listitem
System.out.println("languagesjava Listitem");
listimages[1][2][0][6] = null;
//m_menuelements_id: 322
listtexts[1][2][0][6] = LANG.settingsDefaultLanguage_DEF;
listlist[1][2][0][6] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//languagesjava form
System.out.println("languagesjava form");
forms[1][2][0][6] = new Form(LANG.settingsDefaultLanguage_DEF);
formcommands[1][2][0][6][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][6][0] = BACK;
formcommands[1][2][0][6][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][6][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][6][0] = "_DL";
formfieldtype[1][2][0][6][0] = FI_CHOICEGROUP;
formchoicefields[1][2][0][6][0] = new ChoiceGroup(LANG.settingsDefaultLanguage_DEF,Choice.EXCLUSIVE);
formchoicefields[1][2][0][6][0].append("Dutch", null);
formchoicefields[1][2][0][6][0].append("English", null);
/*End formitems */
formservercommand[1][2][0][6] = "b, ,_DL;";
//itemcount[1] uppräknad: 7
//***************ARRAY_LISTITEMS: 7
//Vi är i en submenu lägg till item itemcount[1]: 7
//EXTLIST1 Listitem
System.out.println("EXTLIST1 Listitem");
listimages[1][2][0][7] = null;
//m_menuelements_id: 195
listtexts[1][2][0][7] = LANG.editExtList1_DEF;
listlist[1][2][0][7] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//EXTLIST1 form
System.out.println("EXTLIST1 form");
forms[1][2][0][7] = new Form(LANG.editExtList1_DEF);
formcommands[1][2][0][7][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][7][0] = BACK;
formcommands[1][2][0][7][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][7][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][7][0] = "_EXT1";
formfieldtype[1][2][0][7][0] = FI_TEXTBOX;
formtextfields[1][2][0][7][0] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][7][1] = "_EXT2";
formfieldtype[1][2][0][7][1] = FI_TEXTBOX;
formtextfields[1][2][0][7][1] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][7][2] = "_EXT3";
formfieldtype[1][2][0][7][2] = FI_TEXTBOX;
formtextfields[1][2][0][7][2] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][7][3] = "_EXT4";
formfieldtype[1][2][0][7][3] = FI_TEXTBOX;
formtextfields[1][2][0][7][3] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][7] = "y, ,_EXT1;_EXT2;_EXT3;_EXT4;";
//itemcount[1] uppräknad: 8
//***************ARRAY_LISTITEMS: 8
//Vi är i en submenu lägg till item itemcount[1]: 8
//EXTLIST2 Listitem
System.out.println("EXTLIST2 Listitem");
listimages[1][2][0][8] = null;
//m_menuelements_id: 196
listtexts[1][2][0][8] = LANG.editExtList2_DEF;
listlist[1][2][0][8] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//EXTLIST2 form
System.out.println("EXTLIST2 form");
forms[1][2][0][8] = new Form(LANG.editExtList2_DEF);
formcommands[1][2][0][8][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][8][0] = BACK;
formcommands[1][2][0][8][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][8][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][8][0] = "_EXT5";
formfieldtype[1][2][0][8][0] = FI_TEXTBOX;
formtextfields[1][2][0][8][0] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][8][1] = "_EXT6";
formfieldtype[1][2][0][8][1] = FI_TEXTBOX;
formtextfields[1][2][0][8][1] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][8][2] = "_EXT7";
formfieldtype[1][2][0][8][2] = FI_TEXTBOX;
formtextfields[1][2][0][8][2] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
formtextfieldnames[1][2][0][8][3] = "_EXT8";
formfieldtype[1][2][0][8][3] = FI_TEXTBOX;
formtextfields[1][2][0][8][3] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",CONF.IS,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][8] = "y, ,_EXT5;_EXT6;_EXT7;_EXT8;";
//itemcount[1] uppräknad: 9
//***************ARRAY_LISTITEMS: 9
//Vi är i en submenu lägg till item itemcount[1]: 9
//mobctexchangespec Listitem
System.out.println("mobctexchangespec Listitem");
listimages[1][2][0][9] = null;
//m_menuelements_id: 333
listtexts[1][2][0][9] = LANG.ctExchangeSpec_DEF;
listlist[1][2][0][9] = 0; //Visa lista nr om 0 visa ingen, Om formdata inte e blank
//mobctexchangespec form
System.out.println("mobctexchangespec form");
forms[1][2][0][9] = new Form(LANG.ctExchangeSpec_DEF);
formcommands[1][2][0][9][0] = new Command(LANG.genDefaultBack_DEF,Command.BACK, 1);
formservercommandcheck[1][2][0][9][0] = BACK;
formcommands[1][2][0][9][1] = new Command(LANG.genDefaultSave_DEF,Command.HELP, 1);
formservercommandcheck[1][2][0][9][1] = SEND;
/*Start formitems */
formtextfieldnames[1][2][0][9][0] = "_CTEPOST";
formfieldtype[1][2][0][9][0] = FI_TEXTBOX;
formtextfields[1][2][0][9][0] = new TextField(LANG.enterDefaultEnterExtension_DEF,"",40,TextField.ANY);
formtextfieldnames[1][2][0][9][1] = "_IHPORT";
formfieldtype[1][2][0][9][1] = FI_NUMERIC;
formtextfields[1][2][0][9][1] = new TextField(LANG.ihPortnumber_DEF,"", 4,TextField.NUMERIC);
formtextfieldnames[1][2][0][9][2] = "_IHURL";
formfieldtype[1][2][0][9][2] = FI_TEXTBOX;
formtextfields[1][2][0][9][2] = new TextField(LANG.ihURL_DEF,"",100,TextField.ANY);
/*End formitems */
formservercommand[1][2][0][9] = "y, ,_IHURL;_IHPORT;_CTEPOST;";
//itemcount[1] uppräknad: 10
//***************ARRAY_LISTITEMS: 10
//EXIT Borttagen
/* Ingen CT gjord End menu*/
/* End options */
}

} //End Class
