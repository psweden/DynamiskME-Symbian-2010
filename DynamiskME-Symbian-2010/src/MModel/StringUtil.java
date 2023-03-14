package MModel;

import java.util.Vector;

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
public class StringUtil {
    public StringUtil() {
    }
    public static String[] Splitstring(String splitStr, String delimiter, int limit){

    // some input validation / short-circuiting
       if (delimiter == null || delimiter.length() == 0)
       {
            return new String[] { splitStr };
        }
        else if (splitStr == null)
        {
           return new String[0];
        }

        // enabling switches based on the 'limit' parameter
       boolean arrayCanHaveAnyLength = false;
        int maximumSplits = Integer.MAX_VALUE;
        boolean dropTailingDelimiters = true;
        if (limit < 0)
        {
            arrayCanHaveAnyLength = true;
            maximumSplits = Integer.MAX_VALUE;
            dropTailingDelimiters = false;
        }
        else if (limit > 0)
        {
            arrayCanHaveAnyLength = false;
            maximumSplits = limit - 1;
            dropTailingDelimiters = false;
        }

        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        char[] chars = splitStr.toCharArray();
        boolean lastWasDelimiter = false;
        int splitCounter = 0;
        for (int i = 0; i < chars.length; i++)
       {
            // check for a delimiter
            if (i + delimiter.length() <= chars.length && splitCounter < maximumSplits)
            {
                String candidate = new java.lang.String(chars, i, delimiter.length());
                if (candidate.equals(delimiter))
                {
                    tokens.addElement(token.toString());
                    token.setLength(0);

                    lastWasDelimiter = true;
                    splitCounter++;
                    i = i + delimiter.length() - 1;

                   continue; // continue the for-loop
                 }
             }

             // this character does not start a delimiter -> append to the token
             token.append(chars[i]);
             lastWasDelimiter = false;
       }
        // don't forget the "tail"...
         if (token.length() > 0 || (lastWasDelimiter && !dropTailingDelimiters))
        {
            tokens.addElement(token.toString());
         }
        // convert the vector into an array
        String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++)
         {
            splitArray[i] = (String) tokens.elementAt(i);
        }
         return splitArray;
    }

}
