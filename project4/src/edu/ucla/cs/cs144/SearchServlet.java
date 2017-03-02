package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearch;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        request.setAttribute("title", title_);
        
        
        if (request.getParameter("q") == null || request.getParameter("q") == "") { 
          request.getRequestDispatcher("/search.jsp").forward(request, response);
          return;
        }

        String search = request.getParameter("q");
        request.setAttribute("search", search);

        SearchResult[] results = AuctionSearch::basicSearch(search, 0, 1);
        request.setAttribute("result", results[0].getName());
        
        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }

/*    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("result", search_string);
        request.setAttribute("title", title_);
        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
*/
    private String title_ = "Kaitlyn and Alex love eBay, the search engine";
}
