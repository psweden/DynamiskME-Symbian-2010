package MDataStore;

/**
 * <p>Title: Mobile Extension</p>
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

import MControll.Settings;
import MControll.Main_Controll;
import MModel.CONF;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.io.IOException;

public class DataBase_RMS {

    /* Initiering av Databasen */
    public RecordStore recStore = null;
    static final String REC_STORE = "Data_Store_attendant_145";
    public MControll.Main_Controll main;

    public static final int RMS_setThisDay = 1;
    public static final int RMS_setThis30DAY = 2;
    public static final int RMS_setDay_30DAY = 3;
    public static final int RMS_LineAccess = 4;
    public static final int RMS_SwitchBoardNumber = 5;
    public static final int RMS_Year_TodaysDate = 6;
    public static final int RMS_Month_TodaysDate = 7;
    public static final int RMS_Day_TodaysDate = 8;
    public static final int RMS_ExtensionNumber = 9;
    public static final int RMS_PINCode = 10;
    public static final int RMS_AccessTypeTo = 11;
    public static final int RMS_VoiceMailOperator = 12;
    public static final int RMS_DebugONOFF = 13;
    public static final int RMS_IconNumber = 14;
    public static final int RMS_CountryCode = 15;
    public static final int RMS_MexONOFF = 16;
    public static final int RMS_KeyCode = 17;
    public static final int RMS_VoiceMailSwitchBoard = 18;
    public static final int RMS_Language = 19;
    public static final int RMS_DemoLicens = 20;
    public static final int RMS_CheckStatusPBX = 21;
    public static final int RMS_CompanyName = 22;
    public static final int RMS_UserName = 23;
    public static final int RMS_EditAbsentName_3 = 24;
    public static final int RMS_EditAbsentDTMF_3 = 25;
    public static final int RMS_HHMMTTMM_3 = 26;
    public static final int RMS_PreCode  = 27;
    public static final int RMS_CountryName = 28;
    public static final int RMS_EditAbsentName_1 = 29;
    public static final int RMS_EditAbsentName_2 = 30;
    public static final int RMS_EditAbsentDTMF_1 = 31;
    public static final int RMS_EditAbsentDTMF_2 = 32;
    public static final int RMS_HHMMTTMM_1 = 33;
    public static final int RMS_HHMMTTMM_2 = 34;
    public static final int RMS_Pbx_ID = 35;
    public static final int RMS_PrgName = 36;
    public static final int RMS_TransferCall = 37;
    public static final int RMS_DTMFsend = 38;
    public static final int RMS_IMEI = 39;
    public static final int RMS_Rename_1 = 40;
    public static final int RMS_Rename_2 = 41;
    public static final int RMS_Rename_3 = 42;
    public static final int RMS_DefaultLanguage = 43;
    public static final int RMS_ENGlang = 44;
    public static final int RMS_PhoneBrands = 45;
    public static final int RMS_PhoneModel = 46;
    public static final int RMS_AbsentStatus = 47;
    public static final int RMS_PBX_Name = 48;
    public static final int RMS_IMEI_True = 49;
    public static final int RMS_SystemGetProperty = 50;
    public static final int RMS_Internummer1 = 51;
    public static final int RMS_Internummer2 = 52;
    public static final int RMS_Internummer3 = 53;
    public static final int RMS_Internummer4 = 54;
    public static final int RMS_Internummer5 = 55;
    public static final int RMS_Internummer6 = 56;
    public static final int RMS_Internummer7 = 57;
    public static final int RMS_Internummer8 = 58;
    public static final int RMS_Internummer9 = 59;
    public static final int RMS_Internummer10 = 60;
    public static final int RMS_Internummer11 = 61;
    public static final int RMS_Internummer12 = 62;
    public static final int RMS_Internummer13 = 63;
    public static final int RMS_Internummer14 = 64;
    public static final int RMS_Internummer15 = 65;
    public static final int RMS_Internummer16 = 66;
    public static final int RMS_Internummer17 = 67;
    public static final int RMS_Internummer18 = 68;
    public static final int RMS_Internummer19 = 69;
    public static final int RMS_Internummer20 = 70;
    public static final int RMS_Internummer21 = 71;
    public static final int RMS_Internummer22 = 72;
    public static final int RMS_Internummer23 = 73;
    public static final int RMS_Internummer24 = 74;
    public static final int RMS_Internummer25 = 75;
    public static final int RMS_CalledNumber1 = 76;
    public static final int RMS_CalledNumber2 = 77;
    public static final int RMS_CalledNumber3 = 78;
    public static final int RMS_CalledNumber4 = 79;
    public static final int RMS_CalledNumber5 = 80;
    public static final int RMS_CalledNumber6 = 81;
    public static final int RMS_CalledNumber7 = 82;
    public static final int RMS_CalledNumber8 = 83;
    public static final int RMS_CalledNumber9 = 84;
    public static final int RMS_CalledNumber10 = 85;
    public static final int RMS_CalledNumber11 = 86;
    public static final int RMS_CalledNumber12 = 87;
    public static final int RMS_CalledNumber13 = 88;
    public static final int RMS_CalledNumber14 = 89;
    public static final int RMS_CalledNumber15 = 90;
    public static final int RMS_CalledNumber16 = 91;
    public static final int RMS_CalledNumber17 = 92;
    public static final int RMS_CalledNumber18 = 93;
    public static final int RMS_CalledNumber19 = 94;
    public static final int RMS_CalledNumber20 = 95;
    public static final int RMS_CalledNumber21 = 96;
    public static final int RMS_CalledNumber22 = 97;
    public static final int RMS_CalledNumber23 = 98;
    public static final int RMS_CalledNumber24 = 99;
    public static final int RMS_CalledNumber25 = 100;
    public static final int RMS_GSMModemNr = 101;
    public static final int RMS_LG_TaladHanvisning = 102;
    public static final int RMS_AccessCodeVoicemail = 103;
    public static final int RMS_AccessCodeConference = 104;
    public static final int RMS_ConferenceNumber = 105;
    public static final int RMS_LG_IPLDK_IPECS = 106;
    public static final int RMS_DTMFPAUSE_VALUE = 107;
    public static final int RMS_IDDCODE = 108;
    public static final int RMS_AREACODEREMOVE = 109;
    public static final int RMS_Clientversion = 110;
    public static final int RMS_Serverversion = 111;
    public static final int RMS_DTMF_Sendtype = 112;
    public static final int RMS_Hanguppause= 113;
    public static final int RMS_CTIPPORT = 114;
    public static final int RMS_CTIPURL = 115;
    public static final int RMS_Demo116 = 116;
    public static final int RMS_CtEpost117 = 117;
    public static final int RMS_LedigPlats118 = 118;
    public static final int RMS_LedigPlats119 = 119;
    public static final int RMS_LedigPlats120 = 120;
    public static final int RMS_LedigPlats121 = 121;
    public static final int RMS_LedigPlats122 = 122;
    public static final int RMS_LedigPlats123 = 123;
    public static final int RMS_LedigPlats124 = 124;
    public static final int RMS_LedigPlats125 = 125;
    public static final int RMS_LedigPlats126 = 126;
    public static final int RMS_LedigPlats127 = 127;
    public static final int RMS_LedigPlats128 = 128;
    public static final int RMS_LedigPlats129 = 129;
    public static final int RMS_LedigPlats130 = 130;
    public static final int RMS_LedigPlats131 = 131;
    public static final int RMS_LedigPlats132 = 132;
    public static final int RMS_LedigPlats133 = 133;
    public static final int RMS_LedigPlats134 = 134;
    public static final int RMS_LedigPlats135 = 135;
    public static final int RMS_LedigPlats136 = 136;
    public static final int RMS_LedigPlats137 = 137;
    public static final int RMS_LedigPlats138 = 138;
    public static final int RMS_LedigPlats139 = 139;
    public static final int RMS_LedigPlats140 = 140;
    public static final int RMS_LedigPlats141 = 141;
    public static final int RMS_LedigPlats142 = 142;
    public static final int RMS_LedigPlats143= 143;
    public static final int RMS_LedigPlats144 = 144;
    public static final int RMS_LedigPlats145= 145;
    public static final int RMS_LedigPlats146= 146;
    public static final int RMS_LedigPlats147 = 147;
    public static final int RMS_LedigPlats148 = 148;
    public static final int RMS_LedigPlats149 = 149;
    public static final int RMS_LedigPlats150 = 150;
    public static final int RMS_LedigPlats151 = 151;

     public static final int RMS_TOTALRECORDS = 151;

     public String setDB = "";

    /* Konstruktorn borjar har */
    public DataBase_RMS() throws InvalidRecordIDException,
    RecordStoreNotOpenException, RecordStoreException, IOException {

    }

    /* RMS GENERELLA METODER--------------------------------------------   */

      public boolean setDataGEN(int number,String s) {
        boolean ok = false;
          System.out.println("setDataGEN numb: " + number + " s: " + s);
          System.out.println("openRecStore");
          openRecStore();

        try {
            System.out.println("setRecord");
            recStore.setRecord(number, s.getBytes(),
                               0,
                               s.length());
           ok = true;
        } catch (Exception e) {
          ok = false;
          System.out.println("Error setDataGEN : " + e.getMessage());
        }closeRecStore();
        return ok;
    }



    public String getDataGEN(int number) throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte l[] = recStore.getRecord(number);
        String returndata = new String(l, 0, l.length);

        closeRecStore();

        return returndata;
    }

    /* Andra metoder oppna och stang update read record RSM - metoder */

    public void readRecordsUpdate() {
        try {

            System.out.println("Number of records: " + recStore.getNumRecords());
            if (recStore.getNumRecords() > 0) {
                RecordEnumeration re = recStore.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    System.out.println("Record: " + str);

                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void readRecords() {
        try {
            // Intentionally small to test code below
            byte[] recData = new byte[5];
            int len;

            for (int i = 1; i <= recStore.getNumRecords(); i++) {
                // Allocate more storage if necessary
                if (recStore.getRecordSize(i) > recData.length) {
                    recData = new byte[recStore.getRecordSize(i)];
                }

                len = recStore.getRecord(i, recData, 0);
                    if (Settings.debug) {
                        System.out.println("Record ID#" + i + ": " +
                                           new String(recData, 0, len));
                    }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void writeRecord(String str) {
        byte[] rec = str.getBytes();

        try {
           System.out.println("sparar ");
            recStore.addRecord(rec, 0, rec.length);
            System.out.println("Writing record: " + str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void openRecStore() {
        try {
            // The second parameter indicates that the record store
            // should be created if it does not exist
            recStore = RecordStore.openRecordStore(REC_STORE, true);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void closeRecStore() {
        try {
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /* DATA BASEN  --------------------------------------------   */

    public void setDataStore() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreNotOpenException,
            RecordStoreException, IOException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        // om innehallet i databasen ar '0' sa spara elementen i databasen.

        if (recStore.getNumRecords() != 0) {
            this.setDB = "1";
        }
        if (recStore.getNumRecords() == 0) {
            this.setDB = "0";
            for (int ff = 1; ff < RMS_TOTALRECORDS - 1; ff++) {
                System.out.println("**writeRecord[" + (ff) + "] " + CONF.initdatabasedata[ff]);
                writeRecord(CONF.initdatabasedata[ff]); // Initiera
            }
        }


        closeRecStore();
    }
}
