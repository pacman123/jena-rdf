package ai.laer.indexer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SimpleSearch {

	
	
	
	private static void searchIndex(String path, String queryStr, int maxHits) throws Exception {        
      
		int hitsPerPage = 10;
		Directory index = FSDirectory.open(Paths.get(path));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Query q = new QueryParser("contents",analyzer).parse(queryStr);
		
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		
		
		 // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". "+"\t" + d.get("uri"));
        }
    }
	
	
	
	public static void main(String args[]) throws Exception {
		SimpleSearch.searchIndex("data/indexedFull", "hello", 10);
		
	}
}
