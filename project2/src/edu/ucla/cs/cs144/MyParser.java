/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.Element;
//import org.w3c.dom.Text;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

// User object

class User {
  public String userID_ = "\\N";
  public String Location_ = "\\N";
  public String Country_ = "\\N";
  public String S_Rating_ = "\\N";
  public String B_Rating_ = "\\N";

  public String stringify() {
    return "|" + userID_ + "|*"
                + Location_ + "*" + Country_ + "*"
                + S_Rating_ + "*" + B_Rating_;
  }
}



// MyParser Implementation

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        
        
        /**************************************************************/
        
        //System.out.println("sad");
        NodeList nodes = doc.getDocumentElement().getElementsByTagName("Item");
        
        process_items(nodes);
        process_users(nodes);
        process_bids(nodes);
        process_categories(nodes);
    }
    
    public static String tuplify(String s) {
      return s == "" ? "\\N" : "|" + s + "|";
    }

    public static String timify(String t) {
      SimpleDateFormat t_xml = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
      SimpleDateFormat t_sql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      try {
        return t_sql.format(t_xml.parse(t)).toString();
      } catch (ParseException e) {
        return "";
      }
    }

    public static void process_items(NodeList nodes) {
      //Attempt to create items.csv
      String pathname = "./items.csv";
      try{
        File file = new File(pathname);
        FileWriter fw;
        //Append if the file already was created
        if(file.exists())
          fw = new FileWriter(file, true);
        else{
          file.createNewFile();
          fw = new FileWriter(file);
        }

        //Create a tuple to add to the csv file
        for (int i = 0; i < nodes.getLength(); i++) {
          String row = "";
          Element e = (Element) nodes.item(i);
          
          row += tuplify(e.getAttribute("ItemID")) + "*";
          row += tuplify(getElementTextByTagNameNR(e, "Name")) + "*";
          row += tuplify(strip(getElementTextByTagNameNR(e, "Currently"))) + "*";
          row += tuplify(strip(getElementTextByTagNameNR(e, "Buy_Price"))) + "*";
          row += tuplify(strip(getElementTextByTagNameNR(e, "First_Bid"))) + "*";
          row += tuplify(getElementTextByTagNameNR(e, "Number_of_Bids")) + "*";
          row += tuplify(getElementTextByTagNameNR(e, "Location")) + "*";
          row += tuplify(getElementByTagNameNR(e, "Location").getAttribute("Latitude")) + "*";
          row += tuplify(getElementByTagNameNR(e, "Location").getAttribute("Longitude")) + "*";
          row += tuplify(getElementTextByTagNameNR(e, "Country")) + "*";
          row += tuplify(timify(getElementTextByTagNameNR(e, "Started"))) + "*";
          row += tuplify(timify(getElementTextByTagNameNR(e, "Ends"))) + "*";
          row += tuplify(getElementByTagNameNR(e, "Seller").getAttribute("UserID")) + "*";
          row += tuplify(getElementTextByTagNameNR(e, "Description")) + "\n";
          
          fw.write(row);
        }
        fw.close();
      }

      catch(IOException ex){
            System.out.println("FAILED: cannot open the file::" + pathname);
            return;
      }
    }

    public static void process_users(NodeList nodes) {
      Map<String,User> users = new HashMap<String,User>();
      
      for (int i = 0; i < nodes.getLength(); i++) {
        Element e = (Element) nodes.item(i);
        Element s = getElementByTagNameNR(e, "Seller");
        if (users.containsKey(s.getAttribute("UserID"))) {
          users.get(s.getAttribute("UserID")).S_Rating_ = tuplify(s.getAttribute("Rating"));
        } else {
          User u = new User();
          u.userID_ = s.getAttribute("UserID");
          u.S_Rating_ = tuplify(s.getAttribute("Rating"));
          users.put(u.userID_, u);
        }
        
        NodeList bids = getElementByTagNameNR(e, "Bids").getElementsByTagName("Bid");
        for (int j = 0; j < bids.getLength(); j++) {
          Element b_e = (Element) bids.item(j);
          Element b = getElementByTagNameNR(b_e, "Bidder");
          if (users.containsKey(b.getAttribute("UserID"))) {
            users.get(b.getAttribute("UserID")).Location_ = tuplify(getElementTextByTagNameNR(b, "Location"));
            users.get(b.getAttribute("UserID")).Country_ = tuplify(getElementTextByTagNameNR(b, "Country"));
            users.get(b.getAttribute("UserID")).B_Rating_ = tuplify(b.getAttribute("Rating")); 
          } else {
            User u = new User();
            u.userID_ = b.getAttribute("UserID");
            u.Location_ = tuplify(getElementTextByTagNameNR(b, "Location"));
            u.Country_ = tuplify(getElementTextByTagNameNR(b, "Country"));
            u.B_Rating_ = tuplify(b.getAttribute("Rating"));
            users.put(u.userID_, u);
          }
        }
      }

      //Attempt to create users csv
      String pathname = "./users.csv";
      try{
       File file = new File(pathname);
        FileWriter fw;
        //Append if the file already was created
        if(file.exists())
          fw = new FileWriter(file, true);
        else{
          file.createNewFile();
          fw = new FileWriter(file);
        }

        //Add each user tuple to the csv file
        for (User u : users.values()) {
          fw.write(u.stringify());
          fw.write(System.lineSeparator());
        }
        fw.close();
      }

      catch(IOException ex){
            System.out.println("FAILED: cannot open the file::" + pathname);
            return;
      }
    }

    public static void process_bids(NodeList nodes) {
      //Attempt to create bids.csv
      String pathname = "./bids.csv";
      try{
        File file = new File(pathname);
        FileWriter fw;
        //Append if the file already was created
        if(file.exists())
          fw = new FileWriter(file, true);
        else{
          file.createNewFile();
          fw = new FileWriter(file);
        }

        //Create a tuple to add to the csv file
        for (int i = 0; i < nodes.getLength(); i++) {
          Element e = (Element) nodes.item(i);
          NodeList bids = getElementByTagNameNR(e, "Bids").getElementsByTagName("Bid");
          for (int j = 0; j < bids.getLength(); j++) {
            String row = "";
            Element b = (Element) bids.item(j);
            
            row += tuplify(e.getAttribute("ItemID")) + "*";
            row += tuplify(getElementByTagNameNR(b, "Bidder").getAttribute("UserID")) + "*";
            row += tuplify(timify(getElementTextByTagNameNR(b, "Time"))) + "*";
            row += tuplify(strip(getElementTextByTagNameNR(b, "Amount"))) + "\n";

            fw.write(row);
          }
        }
        fw.close();
      }

      catch(IOException ex){
            System.out.println("FAILED: cannot open the file::" + pathname);
            return;
      }
    }

    public static void process_categories(NodeList nodes) {
      //Attempt to create categories.csv
      String pathname = "./categories.csv";
      try{
        File file = new File(pathname);
        FileWriter fw;
        //Append if the file already was created
        if(file.exists())
          fw = new FileWriter(file, true);
        else{
          file.createNewFile();
          fw = new FileWriter(file);
        }

        //Create a tuple to add to the csv file
        for (int i = 0; i < nodes.getLength(); i++) {
          Element e = (Element) nodes.item(i);
          NodeList cats = e.getElementsByTagName("Category");
          for (int j = 0; j < cats.getLength(); j++) {
            String row = "";
            Element c = (Element) cats.item(j);

            row += tuplify(e.getAttribute("ItemID")) + "*";
            row += tuplify(getElementText(c)) + "\n";
            fw.write(row);
          } 
        }
        fw.close();
      }

      catch(IOException ex){
            System.out.println("FAILED: cannot open the file::" + pathname);
            return;
      }
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
