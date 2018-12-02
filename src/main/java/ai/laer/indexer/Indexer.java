package ai.laer.indexer;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Indexer {
	private IndexWriter writer;
	private String uriPrefix = "http://laer.ai/enron/email/id/";

	public Indexer(String indexDirectoryPath) throws IOException {
		// this directory will contain the indexes

		// Directory index = new RAMDirectory();
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));

		// create the indexer
		writer = new IndexWriter(indexDirectory, new IndexWriterConfig());
	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	private Document getDocument(File file) throws IOException {
	   
	   try (InputStream stream = new FileInputStream(file))
       {
		   Document document = new Document();
		   document.add(new TextField("contents", IOUtils.toString(stream, StandardCharsets.UTF_8.name()), 
				   Field.Store.YES));
		   document.add(new StringField("uri", uriPrefix + file.getName(), Field.Store.YES));

		   return document;
       }
   }

	private void indexFile(File file) throws IOException {
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	public int createIndex(String dataDirPath, FileFilter filter) throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
				indexFile(file);
			}
		}
		return writer.numDocs();
	}
}
