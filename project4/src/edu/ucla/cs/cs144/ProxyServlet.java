package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        BufferedReader br = null;

        //Grab query, it there is none, don't do a suggest (obviously)
       	String query = request.getParameter("q");
       	if(query == null)
       		return;

       	//Create URL and open the connection
       	String toURL = "http://google.com/complete/search?output=toolbar&q=" + query;
        URL url = new URL(toURL);

       	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

       	//Get response XML data and save it
       	br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        	
       	String line;
       	StringBuilder sb = new StringBuilder();
       	while((line = br.readLine()) != null){
       		sb.append(line);
       	}
       	String xml = sb.toString();

       	//Print out XML data to client
       	response.setContentType("text/xml");
       	PrintWriter pw = response.getWriter();
       	pw.println(xml);

       	//Close and disconnect
       	try {
           	if (br != null) 
           		br.close();
       	} catch (IOException e) {
           	e.printStackTrace();
       	}
        
        urlConnection.disconnect();
    }
}
