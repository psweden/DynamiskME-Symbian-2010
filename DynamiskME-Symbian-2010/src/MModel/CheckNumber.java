package MModel;

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

import MDataStore.DataBase_RMS;
import java.io.IOException;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;

public class CheckNumber {

    public String

    lineAccess_PBX,
    countryCode_PBX,
    iddCode,
    areaRemoveCode,
    dtmfCode;

    public CheckNumber() throws IOException, RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        DataBase_RMS rms = new DataBase_RMS();
        this.lineAccess_PBX = rms.getDataGEN(rms.RMS_LineAccess);
        this.countryCode_PBX = rms.getDataGEN(rms.RMS_CountryCode);
        this.iddCode = rms.getDataGEN(rms.RMS_IDDCODE);
        this.areaRemoveCode = rms.getDataGEN(rms.RMS_AREACODEREMOVE);
        rms = null;

    }

    public String checkCallNumber(String s) {

     int internLenghtInputField = CONF.IS;

     if (s.length() > internLenghtInputField) {
         //Nummret ar storre an IS dvs externsamtal

         boolean comprlk = (s.substring(0, 1).equals("+")) && (this.countryCode_PBX.equals(s.substring(1, this.countryCode_PBX.length()+1)));
         boolean comprlk2 = (s.substring(0,
                             // '00' OCH
                             this.iddCode.length()).equals(this.iddCode)) &&
                            // '46' == '00', '46+00'
                            (this.countryCode_PBX.equals(s.substring(this.iddCode.length(), this.iddCode.length() + this.countryCode_PBX.length())));

         if ((comprlk == true) || comprlk2 == true) {
             //Jag ringer ett utlandskt samtal som har samma landskod som
             if (comprlk == true) {
                 // Jag har ringt med +
                 dtmfCode = this.lineAccess_PBX + this.areaRemoveCode +
                            s.substring(this.countryCode_PBX.length() + 1);
             } else {
                 // Jag har ringt med 00
                 dtmfCode = this.lineAccess_PBX + this.areaRemoveCode + s.substring(this.iddCode.length() + 2);
             }
         } else {
             if (s.substring(0, 1).equals("+")) {
                 // Jag ringer utomlands med + , byt ut mot IDDCODE
                 dtmfCode = this.lineAccess_PBX + this.iddCode +
                            s.substring(1);
             } else {
                 dtmfCode = this.lineAccess_PBX + s;
             }
         }
     } else {
         dtmfCode = s;
     }
     return dtmfCode;
 }

}
