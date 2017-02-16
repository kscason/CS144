package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    //Index connection to send queries down
    private IndexWriter indexWriter = null;
    
    //Create one so we can send some queries
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }   

    //Close indexwriter connection at the end
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    public void indexItem(String itemID, String name, String description, String categories) throws IOException {

        //System.out.println("Indexing item: " + itemID + " " + name + " " + categories);
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();

        //Important information for Item is "id" and "name", so we store these
        //Description is searchable, but not necessary for query results
        //Category is also searchable, and not necessary to query results
        doc.add(new StringField("id", itemID, Field.Store.YES));
        doc.add(new TextField("name", name, Field.Store.YES));
        doc.add(new TextField("description", description, Field.Store.NO));
        doc.add(new TextField("category", categories, Field.Store.NO));

        //Full searchable text is name, description, and category
        String fullSearchableText = name + " " + description + " " + categories;
        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
    }

    private String getCategories(ResultSet categories) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while(categories.next()){
            sb.append(" " + categories.getString("Category"));
        }
        return sb.toString();
    }
 
    public void rebuildIndexes() {

        Connection conn = null;
        PreparedStatement preparedCategories= null;
        String itemQ = "SELECT ItemID, Name, Description FROM Item ORDER BY ItemID";
        String catQ = "SELECT Category FROM Category WHERE ItemID = ?";
        
        String id, name, description, all_categories;
        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);

        //Erase existing index
        getIndexWriter(true);

        //Execute queries for items
        Statement s = conn.createStatement();
        ResultSet items = s.executeQuery(itemQ);
        preparedCategories = conn.prepareStatement(catQ);

        //Index all Items
        while(items.next()){
            //Get item attributes
            id = items.getString("ItemID");
            name = items.getString("Name");
            description = items.getString("Description");

            //Set the ItemID for category query
            preparedCategories.setInt(1, items.getInt("ItemID"));

            //Get categories per item
            ResultSet categories = preparedCategories.executeQuery();
            all_categories = getCategories(categories);
            categories.close();

            //Put item in index
            indexItem(id, name, description, all_categories);
        }

        //Close IndexWriter, ResultSets, Statements, and Connection
        items.close();
        s.close();
        preparedCategories.close();
        closeIndexWriter();
        conn.close();

    } catch (SQLException ex){
        System.out.println("SQLException caught");
        System.out.println("---");
        while ( ex != null ){
            System.out.println("Message   : " + ex.getMessage());
            System.out.println("SQLState  : " + ex.getSQLState());
            System.out.println("ErrorCode : " + ex.getErrorCode());
            System.out.println("---");
            ex = ex.getNextException();
        }
    } catch (IOException ex){
            ex.printStackTrace();
    }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
