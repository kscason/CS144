README.txt
kaitlyn cason : 204411394
alexander waz : 504480512

Part B:

1. List your relations.
	Item(
		ItemID PRIMARY KEY, 
		Name, 
		Currently, 
		Buy_Price, 
		First_Bid, 
		Number_of_Bids, 
		Location, 
		Latitude, 
		Longitude, 
		Country, 
		Started, 
		Ends, 
		UserID, 
		Description
	)
	User(
		UserID PRIMARY KEY, 
		Location, 
		Country, 
		S_Rating, 
		B_Rating
	)
	Bid(
		ItemID PRIMARY KEY, 
		UserID PRIMARY KEY, 
		Time PRIMARY KEY, 
		Amount
	)
	Category(
		ItemID PRIMARY KEY, 
		Category PRIMARY KEY
	)

2. All nontrivial functional dependencies that hold on each relation effectively specify keys.

3. Yes, all the relations are in Boyce-Codd Normal Form (BCNF).

4. Yes, all the relations are in Fourth Normal Form (4NF).