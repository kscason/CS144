This example contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To build and run the sample code, use the "run" ant target inside
the directory with build.xml by typing "ant run".

Decide which index(es) to create on which attribute(s). 

Indexes on Items and Categories.
	Item attributes: ItemID (String Field), Name (Text Field),
	  Description (Text Field)
	Category attributes: ItemID (String Field), Category (Text Field)

We combined name, description, and category into a full searchable text
for an index called content.

 
Basic search function should return the (id, name) pairs of 
matching items that have the keyword "Disney" in the union of the name, category or description attributes. 

Thus, no need for an index on Bids or Users.