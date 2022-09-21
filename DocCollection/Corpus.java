package DocCollection;

import Search.*;
import DocCollection.CollectionIO.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 * @file Corpus.java
 * @author smitbl07
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Utility to create an index provided one does not exist.  All document parsing
 * originates here as well as a connection to the engine class.
 */

public class Corpus {
    
    private final DocumentParser dp;
    private final Engine engine;
    private final Menu m;

    /**
     * Default constructor for Corpus.  Initializes instance variables.
     * 
     * @param dp DocumentParser from menu
     * @throws Exception from DocumentParser_Cranfield
     */
    public Corpus(final DocumentParser dp, final Menu m) throws Exception {
        this.dp = dp;
        this.engine = new Engine();
        this.m = m;
    }

    /**
     * Checks if an index exists.  If it does not exists it creates one using
 the DocumentParser_Cranfield to retrieve the documents.  This is the point where
 an expansion of documents method would be called.
     * 
     * @throws Exception from iWriter
     */
    public void makeIndex() throws Exception {
        if (!this.engine.indexExists()) {
            IndexWriter iWriter = this.engine.getIndex();
            Document d = this.dp.buildDocument();
            int i = 1;
            while (d != null) {
                // add to index
                System.out.println("Document: " + i);
                i++;
                if (m.getExpandDocs()) {
                    d = DocumentParser.expandDocument(d, m.getUseSenseNodesDocs(), m.getTrimPointDocs(), m.getMaxDepthDocs());
                }
                iWriter.addDocument(d);
                d = this.dp.buildDocument();
            }
            iWriter.close();
        }
    }

    /**
     * Accessor method for the engine.
     * 
     * @return engine instance related to this instance of corpus
     */
    public Engine getEngine() {
        return this.engine;
    }

}
