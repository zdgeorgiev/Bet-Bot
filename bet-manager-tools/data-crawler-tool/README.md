## Data Crawler Tool

* Generate matches with their metadata for given years. The minimum year is 2011 ( No results before 2011 ) and maximum round is 34.
The tools creates 2 files in the destination folder each for every given year. First file is .json containing all matches representations
and the second .txt file contains each match match metadata Neural Network representation (NOTE : Also containing the result as last column)

##Usage : After the mvn clean install should run:

* First build the whole project with <code>mvn clean install</code>
* <code>java -jar target/data-crawler-tool.jar [start.year] [end.year] [path.to.destination.folder]</code>

NOTE : End year is optional and if is not presented will get the value from the start year
