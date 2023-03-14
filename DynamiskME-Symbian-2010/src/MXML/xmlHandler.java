package MXML;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.util.*;
import MControll.Main_Controll;
import javax.microedition.lcdui.AlertType;
import MModel.LANG;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Alert;
//import MCTParse.CtParser;
import java.util.Hashtable;


public class xmlHandler extends DefaultHandler
{
  private Main_Controll midlet;
  private Vector nodes = new Vector();
  private Stack tagStack = new Stack();
  public String rootxml;
  public String[] respelem;
  public int antrespelem;
  //public MCTParse.CtParser.ResponseClass returnclass;
  public Hashtable responsehash;
  public int c = 0;
  //public xmlHandler (Main_Controll midlet)
  public xmlHandler( Hashtable rchash,String rxml,String[] relem)
  {
      rootxml = rxml;
      respelem = relem;
      antrespelem = respelem.length;
      responsehash = rchash;
      c = 0;
   // this.midlet = midlet;
  }

  public void startDocument() throws SAXException {

  System.out.println("** xmlHandler startDocument **");
  }

  public void startElement(String uri, String localName, String qName,
    Attributes attributes) throws SAXException
  {
  System.out.println("** xmlHandler startElement **");
    if(qName.equals(rootxml))
    {
      System.out.println("** xmlHandler addElement **" + qName);
      //Noden node = new Noden();
      //nodes.addElement(node);
    }
    tagStack.push(qName);
  }

  public void characters(char[] ch, int start, int length) throws SAXException
  {
    String chars = new String(ch, start, length).trim();
    System.out.println("** xmlHandler characters **");
    if(chars.length() > 0)
    {
      String qName = (String)tagStack.peek();
      //Noden currentnode = (Noden)nodes.lastElement();
      System.out.println("** xmlHandler characters qName : " + qName);

      for (int rr=0;rr < antrespelem;rr++) {
      if (qName.equals(respelem[rr]))
      {
        responsehash.put(qName + "_" + c, chars); //Adda responsedata
        System.out.println(qName + "_" + c + ": " + qName);
        c++;
        //currentnode.setName(chars);
      }
      else if(qName.equals("type"))
      {
        //currentnode.setType(chars);
      }

      }
    }
  }

  public void endElement(String uri, String localName, String qName,
    Attributes attributes) throws SAXException
  {
      System.out.println("** xmlHandler endElement **");
    tagStack.pop();
  }

  public void endDocument() throws SAXException
  {
       System.out.println("** xmlHandler endDocument **");
    StringBuffer result = new StringBuffer();
    for (int i=0; i<nodes.size(); i++)
    {
      //Noden currentnode = (Noden)nodes.elementAt(i);
      //result.append("Name : "  + currentnode.getName() + " Type : " + currentnode.getType() + "\n");
    }
    //midlet.alert(result.toString());
    //midlet.SetAlert("","XML Error",AlertType.INFO,Alert.FOREVER,result.toString(),LANG.genDefaultYes_DEF,LANG.exitDefaultNo_DEF,Command.EXIT,Command.CANCEL);
  }



}
