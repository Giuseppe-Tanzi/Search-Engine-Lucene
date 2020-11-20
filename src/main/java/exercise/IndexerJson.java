package exercise;

import com.google.gson.Gson;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Giuseppe Tanzi & Michele Stelluti
 */
public class IndexerJson {
    public static void main(String[] args) throws IOException {
        FSDirectory fsdir = FSDirectory.open(new File("./resources/index").toPath());
        IndexWriterConfig iwc = new IndexWriterConfig(new MyAnalyzer());
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(fsdir, iwc);
        File docfile = new File("./resources/cran/cran.all.1400.json");
        BufferedReader reader = new BufferedReader(new FileReader(docfile));
        Gson gson = new Gson();

        while (reader.ready()) {
            CranDocument cranDocument = gson.fromJson(reader.readLine(), CranDocument.class);
            Document doc = new Document();
            doc.add(new StringField("id", cranDocument.getId(), Field.Store.YES));
            doc.add(new TextField("text", cranDocument.getText(), Field.Store.NO));
            doc.add(new TextField("authors", cranDocument.getAuthors(), Field.Store.NO));
            doc.add(new TextField("title", cranDocument.getTitle(), Field.Store.NO));
            doc.add(new TextField("biblio", cranDocument.getBiblio(), Field.Store.NO));
            writer.addDocument(doc);
        }
        reader.close();
        writer.close();

    }
}
