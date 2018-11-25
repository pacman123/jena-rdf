rm -r target
mvn clean install
java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.indexer.Main
java -cp bin/apache-jena-fuseki-3.9.0/fuseki-server.jar tdb.tdbloader --tdb=text-config.ttl data/triples_big.ttl
java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.jena.text.App -Xmx8192m
