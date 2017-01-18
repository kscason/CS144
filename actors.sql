/*Step 1:
	Create a table with the following schema
	Actors(Name:VARCHAR(40), Movie:VARCHAR(80), Year:INTEGER, Role:VARCHAR(40))*/
CREATE TABLE Actor (
    Name VARCHAR(40),
    Movie VARCHAR(80),
    Year INT NOT NULL,
    Role VARCHAR(40)
);

/*Step 2:
	Load the actors.csv file into the Actors table.*/
LOAD DATA LOCAL INFILE "~/data/actors.csv" INTO TABLE Actor
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

/*Step 3:
	Give me the names of all the actors in the movie 'Die Another Day'.*/
SELECT Name FROM Actor 
WHERE Movie='Die Another Day';

/*Step 4:
	Drop the Actors table from MySQL.*/
DROP TABLE Actor;