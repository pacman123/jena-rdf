

Project file overview
=========================
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
========================

1. download all the mail content using command java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.crawl.contents.ContentFetcher
2. Index all mail content using command java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.indexer.Main
3. Loadd all triples using command: java -cp ../apache-jena-fuseki-3.9.0/fuseki-server.jar tdb.tdbloader --tdb=data/text-config.ttl data/triples_big.ttl
4. Run sparql query using command: java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.jena.text.App -Xmx8192m




Docker Image
=====================
1. cd /home/dev/jenaText_Docker
2. docker build -t laerai/sparqltext .
3. docker run -it --mount src="$(pwd)",target=/app,type=bind laerai/sparqltext



Description for data/text-config.ttl
======================================
Refering to https://jena.apache.org/documentation/query/text-query.html, text-config can be read as hierarchial structure where each node has be constructed using combining functionality of it childs node

Level0: text_dataset, which is of type TextDataset. This is constructed using text:dataset+text:index. Details of text:dataset can be found in Level1_dataset. and details of text:index can be found in Level1_index.

Level1_index: text:index has been constructed by using 
			1. Directory where lucene index is placed. Note: if the location is relative, should be relative to config.ttl
			2. Entity map which maps field name with the labels of the triplets. details of entity mp is present in Level2_index.


Level2_index: 1. text:entityField      "uri" ; says if query matches the index, which fields should be returned from the index
	      2. text:defaultField     "contents" ; says which fields should be lookup in in the lucene index when query is fired.
              3. text:graphField       "graph" ; this i am did not understand
              4. text:map (
         [ text:field "contents";
            text:predicate rdfs:label ]  : do not recall why but looked like mandatory syntax
        )

	index part of the config ends at level2 branch


Level1_dataset: Level1_dataset which contains infenrence capability is composed by
			<#dataset_inf> rdf:type ja:RDFDataset ;  		//is of RDFDataset type
 			 ja:defaultGraph       <#model_inf>			//includes graph model as described by Model_inf and detaild in Level2_dataset

Level2_Dataset:  <#model_inf> rdf:type ja:Model ;				//is a type of Model
		  ja:baseModel <#tdbGraph> ;					//whose basemodel is as descriobed in tdbGraph and detailed in Level3_daetaset
 		  ja:reasoner [ ja:reasonerURL <http://jena.hpl.hp.com/2003/OWLMicroFBRuleReasoner> ]	//and includes reasoner as details in the url

Level3_Dataset: <#tdbGraph> rdf:type tdb:GraphTDB ;                           //is a type of GraphDB
  			tdb:dataset <#tdbDataset>                              //details of which can be found in tdbDataset(Level4_dataset)

Level4_Dataset: <#tdbDataset> rdf:type      tdb:DatasetTDB ;                //is a type of TDB dataset
    		tdb:location "./data/TDB" ;					//and location of TDB is at ./data/TDB. Note, if path is relative, location should be relative to worskapce from where java command is running


