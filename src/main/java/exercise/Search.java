package exercise;

import com.google.gson.Gson;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.HashMap;

/**
 * @author Giuseppe Tanzi & Michele Stelluti
 */
public class Search {
    public static void main(String[] args) throws IOException, ParseException {
        int queryId = 0;
        String numExperiment = "1";
        FSDirectory indexDir = FSDirectory.open(new File("./resources/index").toPath());
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(indexDir));
        File topicfile = new File("./resources/cran/cran.qry.json");
        BufferedReader reader = new BufferedReader(new FileReader(topicfile));
        Gson gson = new Gson();
        String[] fields = {"title", "text", "biblio", "authors"};
        QueryParser qp = new MultiFieldQueryParser(fields, new MyAnalyzer(), new HashMap<String, Float>() {{
            put("text", (float) 2.5);
            put("title", (float) 1.2);
            put("biblio", (float) 0.7);
            put("authors", (float) 0.3);
        }});
        BufferedWriter docSearch = new BufferedWriter(new FileWriter("./resources/search.out"));

        while (reader.ready()) {
            int rank = 0;
            queryId++;
            CranQuery query = gson.fromJson(reader.readLine(), CranQuery.class);
            Query q = qp.parse(query.getQuery());
            TopDocs topdocs = searcher.search(q, 100);
            for (ScoreDoc sdoc : topdocs.scoreDocs) {
                rank++;
                docSearch.append(String.valueOf(queryId))
                        .append(" ")
                        .append(numExperiment)
                        .append(" ")
                        .append(searcher.doc(sdoc.doc).get("id"))
                        .append(" ")
                        .append(String.valueOf(rank))
                        .append(" ")
                        .append(String.valueOf(sdoc.score))
                        .append(" ")
                        .append("experiment");
                docSearch.newLine();
            }

        }
        reader.close();
        docSearch.close();
    }
}
