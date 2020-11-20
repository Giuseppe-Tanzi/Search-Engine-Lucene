/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Tanzi & Michele Stelluti
 */
public class MyAnalyzer extends Analyzer {

    /**
     *
     */
    public static final CharArraySet STOP_WORDS;

    static {
        File stopWordsFile = new File("./resources/stopwords.txt");
        List<String> stopWords = new ArrayList<>();
        String stopWord;
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(stopWordsFile));
            while ((stopWord = inputStream.readLine()) != null) {
                stopWords.add(stopWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final CharArraySet stopSet = new CharArraySet(stopWords, false);
        STOP_WORDS = CharArraySet.unmodifiableSet(stopSet);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new StandardTokenizer();
        TokenStream filter = new LowerCaseFilter(source);
        filter = new StopFilter(filter, STOP_WORDS);
        filter = new PorterStemFilter(filter);

        return new TokenStreamComponents(source, filter);
    }
}
