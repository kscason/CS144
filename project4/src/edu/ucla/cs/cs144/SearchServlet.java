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
        //Set page title to title_
        request.setAttribute("title", title_);
        
        
        //Forward request to JSP page (the first time page)
        if (request.getParameter("q") == null || request.getParameter("q") == "") { 
          request.getRequestDispatcher("/search.jsp").forward(request, response);
          return;
        }

        //This is the search query
        String search = request.getParameter("q");
        request.setAttribute("search", search);

        //Get number to skip and return
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        String nrts = request.getParameter("skip");
        String nrtr = request.getParameter("howMany");

        if(nrts != null)
            numResultsToSkip = Integer.parseInt(nrts);
        if(nrtr != null)
            numResultsToReturn = Integer.parseInt(nrtr);

        request.setAttribute("skip", numResultsToSkip);
        request.setAttribute("howMany", numResultsToReturn);


        //Do auction search, set results to request
        SearchResult[] results = AuctionSearch.basicSearch(search, numResultsToSkip, numResultsToReturn);
        
        request.setAttribute("result", results);

        //SearchResult[] results = AuctionSearch::basicSearch(search, 0, 1);
        //request.setAttribute("result", results[0].getName());
        
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
