FROM  openjdk:8-jdk
RUN apt-get update
RUN apt-get install -y maven
WORKDIR /app
EXPOSE 8080
#CMD java -cp target/jena-1.0-SNAPSHOT.jar:target/lib/* ai.laer.jena.text.App -Xmx8192m
CMD ["sh", "index_and_run.sh"]
