#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
#ant run
ant run-all

# If the Java code does not handle duplicate removal, do this now
#sort ... maybe
awk '!a[$0]++' items.csv > temp
cp temp items.csv
awk '!a[$0]++' users.csv > temp
cp temp users.csv
awk '!a[$0]++' bids.csv > temp
cp temp bids.csv
awk '!a[$0]++' categories.csv > temp
cp temp categories.csv
rm temp

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
rm *.csv
ant clean
#items.csv, users.csv, bids.csv, categories.csv
