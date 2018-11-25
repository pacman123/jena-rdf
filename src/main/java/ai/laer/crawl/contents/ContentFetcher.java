package ai.laer.crawl.contents;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class ContentFetcher {

    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        try {
            URL url = new URL(urlStr);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        Properties prop = new Properties();
        InputStream input = null;
        String outputDir = "";

        try {

            input = new FileInputStream("data/config.properties");

            // load a properties file
            prop.load(input);
            outputDir = prop.getProperty("rawContentDirectory");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        File directory = new File(outputDir);
        if (! directory.exists()){
            directory.mkdir();
        }


        try(BufferedReader reader =
                    new BufferedReader(new FileReader("data/urls"))){
            String line = reader.readLine();
            while(line != null) {

                String[] idArray = line.split(" ")[0].split("/");
                String id = idArray[idArray.length - 1].replace(">","");
                String url = line.split(" ")[2].replace('"',' ');
                System.out.println("id = "+id + "  url:" + url);
                downloadUsingNIO(url, outputDir + id);
                line = reader.readLine();


            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
