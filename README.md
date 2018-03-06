# quickest quail

## about

QuickestQuail is a utility program to manage movie-files. It allows the end-user to connect a hard drive to the computer running the program, and simply scan the drive for movie-files. 

The program will automatically strip redundant information in the filename (e.g. "1080p", "[dutch version]", etc) and query OMDB. Once a drive has been scanned, all the recognized movies are displayed in a neat table format. 

The custom search bar allows users to set up complex queries that really drill down on their movie genre (e.g. "imdbRating > 7.0 AND language contains 'French' AND imdbVotes > 5000 and metaScore > 75"). 

The entire collection can be exported to a number of formats, and aggregate statistics can be made.

The program can also easily apply a heuristic to find more movies that the end-user would like to see.

## accuracy

The program was tested on roughly 2000 movie filenames. It correctly classified 93.7% of them. Some of these included foreign (e.g. Dutch titles) or titles consisting entirely of numbers.

Leaving out all foreign titles that are not in IMDB raises the accuracy to 95.7%. So if you're mostly interested in English mainstream movies, this program is just right for you.

## legal

This program can be used to easily manage downloaded movies. In the same sense that Microsoft Word can easily be used to draft contracts for blood diamonds, or similar to how Microsoft Excel can be used to calculate howmany child-slave labourors you need to complete the latest model iPhone.

Where I am from, you are allowed to legally copy a movie once. For home use. When I moved away from my parents, I copied my entire dvd-collection unto a hard drive, and left the dvds there. This program was originally meant to make my own life easier. If it helps you, so much the better.

## functionality

### search bar

The program displays a table that can easily be searched. The query language defines the following operators:
* "+" (addition)
* "-" (subtraction)
* "*" (multiplication)
* "/" (division)
* "%" (remainder operator)
* "startsWith" (array/string operator)
* "endsWith" (array/string operator)
* "contains" (array/string operator)
* "AND" (logical conjunction)
* "OR" (logical disjunction)
* "NOT" (logical negation)
* "==" (equality test)
* "!=" (inequality test)

### adding a drive

### removing a drive

### re-scanning a drive

### switching between drives

### showing drive statistics

This program allows the end-user to make statistics such as:
* nr of movies by genre (pie chart)
* nr of movies by country (pie chart)
* nr of movies by language (pie chart)
* nr of movies by year (line chart)
* nr of movies by rating (line chart)
* nr of movies by extension (pie chart)

### exporting to pdf

### exporting to txt

### exporting to xml

