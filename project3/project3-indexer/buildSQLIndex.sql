CREATE TABLE ItemLocation (
    ItemID INT NOT NULL,
    Coordinate GEOMETRY NOT NULL
)   ENGINE = MYISAM;

INSERT INTO ItemLocation (ItemID, Coordinate)
SELECT ItemID, Point(Latitude, Longitude) FROM Item
WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;

CREATE SPATIAL INDEX Coordinate_Index ON ItemLocation(Coordinate);
