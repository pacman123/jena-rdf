package ai.laer.jena.text;

import org.apache.commons.io.FileUtils;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.util.QueryExecUtils;
//import org.apache.lucene.search.

import java.io.File;
import java.nio.charset.Charset;

public class App {

    static Property NAME_PROP = ResourceFactory.createProperty("http://laer.ai/enron/person/name");

    public static void main(String ... argv)
    {
        Dataset ds = createAssembler() ;
        loadData(ds , "data/triples_big.ttl") ;
	for (int i=0 ; i < 10; i++) {
		queryData(ds) ;
	}
    }

    public static Dataset createAssembler()
    {
        System.out.println("Construct text dataset using an assembler description") ;
        // There are two datasets in the configuration:
        // the one for the base data and one with text index.
        // Therefore we need to name the dataset we are interested in.
	System.out.println("assembling!");
        Dataset ds = DatasetFactory.assemble("data/text-config.ttl", "http://localhost/jena_example/#text_dataset") ;
        return ds ;
    }

    public static void loadData(Dataset dataset, String file)
    {
        System.out.println("Start loading") ;
        long startTime = System.nanoTime() ;
        dataset.begin(ReadWrite.WRITE) ;
        try {
            Model m = dataset.getDefaultModel() ;
            RDFDataMgr.read(m, file) ;
            dataset.commit() ;
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("exception!!");
        }
        finally {
            dataset.end() ;
        }

        long finishTime = System.nanoTime() ;
        double time = (finishTime-startTime)/1.0e6 ;
        System.out.println(String.format("Finish loading - %.2fms", time)) ;
    }

    public static void queryData(Dataset dataset)
    {

        dataset.begin(ReadWrite.READ) ;
        String   filePath   = "data/query.sparql";
        System.out.println("START") ;
        long startTime = System.nanoTime() ;

        try {
            File file   = new File(filePath);
            String sQuery = FileUtils.readFileToString(file, Charset.defaultCharset());

            Query q = QueryFactory.create(sQuery);

            QueryExecution qexec = QueryExecutionFactory.create(q , dataset) ;
            QueryExecUtils.executeQuery(q, qexec) ;
            long finishTime = System.nanoTime() ;
            double time = (finishTime-startTime)/1.0e6 ;
            System.out.println(String.format("FINISH - %.2fms", time)) ;
            


            System.out.println("START") ;
            startTime = System.nanoTime() ;
 	    qexec = QueryExecutionFactory.create(q , dataset) ;
            QueryExecUtils.executeQuery(q, qexec) ;
            finishTime = System.nanoTime() ;
            time = (finishTime-startTime)/1.0e6 ;
            System.out.println(String.format("FINISH - %.2fms", time)) ;


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            dataset.end() ;
        }


    }




}
