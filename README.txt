

java project which has followint three packages.

1. crawl: 
  It takes urls of the mail content to be downloaded from file data/urls and downloads them sequentially to "rawContentDirectory". One of the issue with this package is it downloads all the content sequentially hence take long time(~15 hrs+ when run in non AWS environment). To solve this, created python script which downloads the content in parallel. Script can be found at scripts/parallelDownloader. Command to run crawler "java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.crawl.contents.ContentFetcher"

2. indexer: 
  Create lucene index from the downloaded mail content. Index is stored at location pointed by property file using the variable "indexDir". Command to run indexer: java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.indexer.Main

3. jena/text
   Driver App which demonstrates the capability of joining lucene index with sparql query. It reads the sparql query from data/query.sparl, executes and prints the result. Since it loads ttl file in memory, it takes roughly 2 mins to execute. To run app program, "java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.jena.text.App -Xmx8192m"


Other files
1. scripts/fuseki_config: Config files that can be used to run fuseki server. 
2. To load ttl to TDB storage: java -cp ../apache-jena-fuseki-3.9.0/fuseki-server.jar tdb.tdbloader --tdb=data/text-config.ttl data/triples_big.ttl



RUN sequence.

1. download all the mail content using command java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.crawl.contents.ContentFetcher
2. Index all mail content using command java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.indexer.Main
3. Loadd all triples using command: java -cp ../apache-jena-fuseki-3.9.0/fuseki-server.jar tdb.tdbloader --tdb=data/text-config.ttl data/triples_big.ttl
4. Run sparql query using command: java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.jena.text.App -Xmx8192m
