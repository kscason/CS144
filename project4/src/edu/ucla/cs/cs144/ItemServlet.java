package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        //Set page title to title_
        request.setAttribute("title", title_);
        
        //Forward request to JSP page
        if (request.getParameter("id") == null || request.getParameter("id") == "") { 
          request.getRequestDispatcher("/item.jsp").forward(request, response);
          return;
        }

        //This is the item ID
        String id = "";
        if(request.getParameter("id") != null)
        	id = request.getParameter("id");
        request.setAttribute("itemid", id);
       
        //Do auction search, set results to request
        String data = AuctionSearch.getXMLDataForItemId(id);
        request.setAttribute("data", data);
        request.getRequestDispatcher("/data.jsp").forward(request, response);

        //SearchResult[] results = AuctionSearch::basicSearch(search, 0, 1);
        //request.setAttribute("result", results[0].getName());
        /*
        try {
          // similar to project 2
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          factory.setValidating(false);
          factory.setIgnoringElementContentWhitespace(true);      
          DocumentBuilder builder = factory.newDocumentBuilder();
          InputSource source = new InputSource(new StringReader(data));
          Document doc = null;                                                                    
          doc = builder.parse(source);

          request.setAttribute("name", doc.getElementsByTagName("Name").item(0).getTextContent());



          request.getRequestDispatcher("/data.jsp").forward(request, response);
          return;
        } catch(Exception e) {
          e.printStackTrace();
        }
        request.getRequestDispatcher("/item.jsp").forward(request, response);
        */
    }

    private String title_ = "Kaitlyn and Alex love eBay, the search engine that doesn't provide XML dumps... we're just so ~user friendly~";
}
