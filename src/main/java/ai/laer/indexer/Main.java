package ai.laer.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    static String indexDir = "";
    static String rawContentDirectory = "";
    static Indexer indexer;

    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;
        String outputDir = "";

        try {

            input = new FileInputStream("data/config.properties");

            // load a properties file
            prop.load(input);
            indexDir=prop.getProperty("indexDir");
            rawContentDirectory=prop.getProperty("rawContentDirectory");

        }
        catch(Exception e){
            e.printStackTrace();
        }


        File directory = new File(indexDir);
        if (! directory.exists()){
            directory.mkdir();
        }

        try{
            createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(rawContentDirectory, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }
}
