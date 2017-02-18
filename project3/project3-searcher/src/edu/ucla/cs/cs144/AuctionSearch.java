package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {

		TopDocs topDocs = null;
		String id, name;
	try{
		//Perform search by creating a new search engine, and performing query search
        SearchEngine se = new SearchEngine();
        topDocs = se.performSearch(query, numResultsToSkip + numResultsToReturn);

        //System.out.println("Results found: " + topDocs.totalHits);
        
        //Grab the results from the topDocs
        ScoreDoc[] hits = topDocs.scoreDocs;

        //Determine SearchResult[] max size
        int r_length = ((hits.length - numResultsToSkip) < numResultsToReturn)? (hits.length - numResultsToSkip) : numResultsToReturn; 
        SearchResult[] results = new SearchResult[r_length];

        //Populate the SearchResult[] with (id, name) key pairs
        for (int i = numResultsToSkip; i < r_length+numResultsToSkip; i++) {
            Document doc = se.getDocument(hits[i].doc);
            id = doc.get("id");
			name = doc.get("name");
 
            SearchResult result = new SearchResult(id, name);
            results[i - numResultsToSkip] = result;
        }
        return results;

	} catch (IOException ex){
		ex.printStackTrace();
	} catch (ParseException ex){
		ex.printStackTrace();
	}
		return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!

    // handle invalid inputs
    if (numResultsToReturn <= 0)
      return new SearchResult[0];
    if (numResultsToSkip < 0)
      return new SearchResult[0];
    
    List<SearchResult> SearchResultList = new ArrayList<SearchResult>();
    HashSet<String> SpatialSearchResults = new HashSet<String>();
    SearchResult[] ReturnResults = new SearchResult[0];

    try {
      Connection conn = DbManager.getConnection(true);
      Statement st = conn.createStatement();
      String sql_query = 
          "SELECT * FROM ItemLocation WHERE " +
          "MBRContains(GeomFromText('Polygon((" +
          region.getLx() +" "+ region.getLy()+", "+
          region.getRx() +" "+ region.getLy()+", "+
          region.getRx() +" "+ region.getRy()+", "+
          region.getLx() +" "+ region.getRy()+", "+
          region.getLx() +" "+ region.getLy()+
          "))'), Coordinate) ORDER BY ItemID;";
      ResultSet rs = st.executeQuery(sql_query);
      //System.out.println(sql_query);      
      while(rs.next()) {
          SpatialSearchResults.add(rs.getString("ItemID"));
          //System.out.println(SpatialSearchResults.contains(rs.getString("ItemID")));
      }
      
      SearchResult[] BasicSearchResults = basicSearch(query, 0, 19532);
      System.out.println(query);
      for (SearchResult sr: BasicSearchResults) {
        if (SpatialSearchResults.size() == numResultsToReturn + numResultsToSkip) {
          break; // full result list
        } else if (SpatialSearchResults.contains((String)sr.getItemId())) {
          SearchResultList.add(sr);
          //System.out.println(sr.getItemId());
        }
      }
      ReturnResults = SearchResultList.toArray(ReturnResults);
      st.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println("ERROR !!!!");
      System.out.println(e.getMessage());
    }

    return ReturnResults;
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		
    try {
      Connection conn = DbManager.getConnection(true);
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM Item WHERE ItemID=" + itemId);
      StringBuffer output_xml = new StringBuffer();
      
      if (!rs.next())
        return ""; // not found
      
      // no tab depth
      output_xml.append("<Item ItemID=\"" + itemId + "\">\n");
      
      // one tab depth
      output_xml.append("\t<Name>" + rs.getString("Name") + "</Name>\n");

//System.out.println(output_xml.toString());

      Statement st_cat = conn.createStatement();
      ResultSet cats = st_cat.executeQuery("SELECT * FROM Category WHERE ItemID=" + itemId);
      while(cats.next()) {
        output_xml.append("\t<Category>" + cats.getString("Category") + "</Category>\n");
      }
      st_cat.close();

      output_xml.append("\t<Currently>$" + rs.getString("Currently") + "</Currently>\n");

      if (rs.getString("Buy_Price") != null) {
        output_xml.append("\t<Buy_Price>$" + rs.getString("Buy_Price") + "</Buy_Price>\n");
      }

      output_xml.append("\t<First_Bid>$" + rs.getString("First_Bid") + "</First_Bid>\n");

//System.out.println(output_xml.toString());

      int num_bids = 0;
      Statement st_bid = conn.createStatement();
      ResultSet bids = st_bid.executeQuery("SELECT * FROM Bid WHERE ItemID=" + itemId);
      StringBuffer bid_xml = new StringBuffer();
      while (bids.next()) {
        // two tab depth
        bid_xml.append("\t\t<Bid>\n");
        Statement st_bidder = conn.createStatement();
        ResultSet bidder = st_bidder.executeQuery("SELECT * FROM User WHERE UserID=" + bids.getString("UserID"));
        // three tab depth
        if (bidder.next())
          bid_xml.append("\t\t\t<Bidder Rating=\"" + bidder.getString("B_Rating") +
              "\" UserID=\"" + bidder.getString("UserID") + "\">\n");
        // four tab depth
        if (bidder.getString("Location") != null)
          bid_xml.append("\t\t\t\t<Location>" + bidder.getString("Location") + "</Location>\n");
        if (bidder.getString("Country") != null)
          bid_xml.append("\t\t\t\t<Country>" + bidder.getString("Country") + "</Country>\n");
        // three tab depth
        bid_xml.append("\t\t\t</Bidder>\n");
        bid_xml.append("\t\t\t<Time>" + timify(bids.getString("Time")) + "</Time>\n");
        bid_xml.append("\t\t\t<Amount>$" + bids.getString("Amount") + "</Amount>\n");
        // two tab depth
        bid_xml.append("\t\t</Bid>\n");
        st_bidder.close();
        num_bids++;
      }
      // one tab depth
      if (num_bids > 0) {
        bid_xml.append("\t</Bids>\n");
        bid_xml.insert(0, "\t<Bids>\n");
      } else {
        bid_xml.append("\t\t<Bids />\n");
      }
      st_bid.close();

      output_xml.append("\t<Number_of_Bids>" + num_bids + "</Number_of_Bids>\n");
      
      output_xml.append(bid_xml.toString());

//System.out.println(output_xml.toString());

      output_xml.append("\t<Location/");
      if (rs.getString("Latitude") != null)
        output_xml.append(" Latitude=\"" + rs.getString("Latitude") + "\"");
      if (rs.getString("Longitude") != null)
        output_xml.append(" Longitude=\"" + rs.getString("Longitude") + "\"");
      output_xml.append(">" + rs.getString("Location") + "</Location>\n");

      output_xml.append("\t<Country>" + rs.getString("Country") + "</Country>\n");

      output_xml.append("\t<Started>" + timify(rs.getString("Started")) + "</Started>\n");

      output_xml.append("\t<Ends>" + timify(rs.getString("Ends")) + "</Ends>\n");

//System.out.println(output_xml.toString());

      Statement st_seller = conn.createStatement();
      ResultSet seller = st_seller.executeQuery("SELECT * FROM User WHERE UserID=\"" + rs.getString("UserID") + "\"");
      if (seller.next())
        output_xml.append("\t<Seller Rating=\"" + seller.getString("S_Rating") +
            "\" UserID=\"" + seller.getString("UserID") + "\" />\n");
      st_seller.close();

      output_xml.append("\t<Description>" + rs.getString("Description") + "</Description>\n");

//System.out.println(output_xml.toString());

      // no tab depth
      output_xml.append("</Item>\n");
      st.close();
      return output_xml.toString();
    } catch (SQLException e) {
      System.out.println("SQL EXCEPTION");
      e.printStackTrace();
    } catch (java.text.ParseException e) {
      System.out.println("PARSE EXCEPTION");
    }

    return "";
	}
	
	public String echo(String message) {
		return message;
	}

  public String timify(String timestamp) throws java.text.ParseException {
    SimpleDateFormat sql_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat xml_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
    Date time = null;
    time = sql_format.parse(timestamp);
    return xml_format.format(time);
  }

}
