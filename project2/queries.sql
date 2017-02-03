-- queries.sql
-- kaitlyn cason : 204411394
-- alexander waz : 504480512

/*1. Find the number of users in the database.*/
SELECT COUNT(*) FROM User;

/*2. Find the number of items in "New York", 
	(i.e., items whose location is exactly the string "New York"). 
	Pay special attention to case sensitivity. 
	You should match the items in "New York" but not in "new york".*/
SELECT COUNT(*) FROM Item
	WHERE Location='New York';

/*3. Find the number of auctions belonging to exactly four categories.*/
SELECT COUNT(cat.ItemID) FROM (SELECT c.ItemID FROM Category c
	GROUP BY c.ItemID
	HAVING COUNT(c.Category)=4) cat;

/*4. Find the ID(s) of current (unsold) auction(s) with the highest bid. 
	Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, 
	so you can use this time point to decide which auction(s) are current. 
	Pay special attention to the current auctions without any bid.*/
SELECT MAX(Amount) FROM Bid b
	WHERE b.ItemID IN (SELECT ItemID FROM Item
		WHERE Ends >= '2001-12-20 00:00:01');


/*5. Find the number of sellers whose rating is higher than 1000.*/
SELECT COUNT(UserID) FROM User
	WHERE S_Rating > 1000;

/*6. Find the number of users who are both sellers and bidders.*/
SELECT COUNT(*) FROM User
	WHERE S_Rating IS NOT NULL
	AND B_Rating IS NOT NULL;

/*7. Find the number of categories that include at least one item with a bid of more than $100.*/
SELECT COUNT(DISTINCT c.Category) FROM Category c
	WHERE c.ItemID IN (SELECT ItemID FROM Item
		WHERE Number_of_Bids > 0 AND Currently > 100);