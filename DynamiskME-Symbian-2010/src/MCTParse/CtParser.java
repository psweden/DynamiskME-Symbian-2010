package MCTParse;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import MXML.xmlHandler;
import java.io.InputStreamReader;
import javax.microedition.io.StreamConnection;
import java.io.InputStream;
import org.xml.sax.InputSource;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import MModel.StringUtil;

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
public class CtParser {
    public int c = 0;
    public CtParser() {
    }
    public Hashtable responsehash;

    public class ResponseClass {
        private String resp;

        public ResponseClass() {}

        public void setResp(String resp) {
            System.out.println("** ResponseClass setName **" + resp);
            this.resp = resp;
        }

        public String getResp() {
            System.out.println("** ResponseClass getName **" + resp);
            return resp;
        }
    };




    public String CheckResultTkn(String resp, String splitvalue,
                                 String returnvalfield, String okstring) {
        System.out.println("** CheckResultTkn **");
        /*
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

         */

        return resp;
    }

    public String CheckResultXML(String resp, String splitvalue,
                                 String[] returnvalfields, String okstring,Hashtable inresphash) {
        //http://www.java-tips.org/java-me-tips/midp/introducing-xml-parsing-in-j2me-devices-4.html

        System.out.println("** CheckResultXML **");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            //InputStream is = new ByteArrayInputStream(resp.getBytes("UTF-8"));
            InputStream is = new ByteArrayInputStream(resp.getBytes());
            //saxParser.parse();
            //InputSource s = new InputSource(new StringReader(resp));
            //InputSource s = new InputStreamReader( new ByteArrayReader( string.getBytes() ) );
            //InputSource s = new InputStreamReader(§;
            //saxParser.parse(is
            //ResponseClass rc = new ResponseClass();
            if (inresphash == null) {
               responsehash = new Hashtable();
            } else {
               responsehash = inresphash;
            }
            MXML.xmlHandler xh = new MXML.xmlHandler(responsehash,splitvalue, returnvalfields);
            saxParser.parse(is, xh);
            System.out.println("** saxParser done **");
            c = xh.c;
            System.out.println("CTParser Antal rader tillbaka: " + c);
            String retvaluefield = returnvalfields[0]+"_0"; //Hämta första fältet med returnvaluefield
            System.out.println("retvaluefield: " + retvaluefield);
            if (responsehash.size() > 0 &&  responsehash.containsKey(retvaluefield)) {
                resp = (String)responsehash.get(retvaluefield);
            } else {
                resp = "Error reading XML response";
            }

            System.out.println("resp: " + resp);

        } catch (SAXException sex) {
            System.out.println("** SAXException **" + sex.getMessage());
        } catch (ParserConfigurationException ex) {
            System.out.println("** ParserConfigurationException **" +
                               ex.getMessage());
        } catch (Exception e) {
            System.out.println("** Exception **" + e.getMessage());
        }
        /*
           int repstringl = resp.length();
          System.out.println("repstringl: " + repstringl);
          String respcheck =  resp.substring(0,repstringl);
          System.out.println("replystring: " + resp);
          System.out.println("respcheck: " + respcheck);

         */

        return resp;
    }
}
