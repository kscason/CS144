-- create.sql
-- kaitlyn cason : 204411394
-- alexander waz : 504480512

CREATE TABLE User(
		UserID VARCHAR(40) NOT NULL, 
		Location VARCHAR(400), 
		Country VARCHAR(400), 
		S_Rating INT DEFAULT -1, 
		B_Rating INT DEFAULT -1,
		PRIMARY KEY(UserID)
);

CREATE TABLE Item(
		ItemID INT NOT NULL, 
		Name VARCHAR(400) NOT NULL, 
		Currently DECIMAL(8,2) NOT NULL, 
		Buy_Price DECIMAL(8,2) DEFAULT -1.00, 
		First_Bid DECIMAL(8,2) NOT NULL, 
		Number_of_Bids INT NOT NULL, 
		Location VARCHAR(400) NOT NULL, 
		Latitude DECIMAL(8,6), 
		Longitude DECIMAL(8,6), 
		Country VARCHAR(400) NOT NULL, 
		Started TIMESTAMP NOT NULL, 
		Ends TIMESTAMP NOT NULL, 
		UserID VARCHAR(40) NOT NULL, 
		Description VARCHAR(4000) NOT NULL,  #???????? idk if null
		PRIMARY KEY(ItemID),
		FOREIGN KEY (UserID) REFERENCES User(UserID)
			ON DELETE CASCADE
);

CREATE TABLE Bid(
		ItemID INT NOT NULL, 
		UserID VARCHAR(40) NOT NULL, 
		Time TIMESTAMP NOT NULL, 
		Amount DECIMAL(8,2) NOT NULL,
		PRIMARY KEY(ItemID, UserID, Time),
		FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
		FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE Category(
		ItemID INT NOT NULL, 
		Category VARCHAR(400) NOT NULL,
		PRIMARY KEY(ItemID, Category),
		FOREIGN KEY (ItemID) REFERENCES Item(ItemID)
);