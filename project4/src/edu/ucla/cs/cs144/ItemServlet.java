package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        request.setAttribute("id", id);

       
        //Do auction search, set results to request
        String data = AuctionSearch.getXMLDataForItemId(id);
        
        request.setAttribute("data", data);

        //SearchResult[] results = AuctionSearch::basicSearch(search, 0, 1);
        //request.setAttribute("result", results[0].getName());
        
        request.getRequestDispatcher("/data.jsp").forward(request, response);
    }

    private String title_ = "Kaitlyn and Alex love eBay, the search engine that doesn't provide XML dumps... we're just so ~user friendly~";
}
