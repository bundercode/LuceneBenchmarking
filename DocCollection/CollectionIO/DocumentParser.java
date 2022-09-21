package DocCollection.CollectionIO;

import Search.*;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

/**
 * @file DocumentParser.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Reads documents from the document file and returns them as document objects.
 */
public abstract class DocumentParser {

    protected static final StringHash sh = StringHash.factory();
    
    /**
     * Method that builds a document and returns a document
     * Method calls StingHash to read lines from cranfield doc files
     * A document is broken down into four sections(title,author,information,body)
     * 
     * @return newly created Document object
     * @throws IOException from BufferedReader
     */
    public abstract Document buildDocument() throws IOException, Exception;
    
    /**
     * Expands Document d using the expansion code in ExpandedSearch.  Search is
     * performed to max depth for a document.
     * 
     * @param d document to expand
     * @return new expanded document.\
     */
    public static Document expandDocument(final Document d) {
        return expandDocument(d, true, .7, Long.MAX_VALUE);
    }
    
    public static Document expandDocument(final Document d, final boolean includeSenseNodes,
                                          final double trimPoint, final long depth) {
        String content = d.getField(sh.get("body")).stringValue();
        String[] contents = content.split(" ");
        d.removeField(sh.get("body"));// TODO: propogate to menu
        d.add(new TextField(sh.get("body"), ExpandedSearch.expandTerms(content,
                 20*contents.length, includeSenseNodes, trimPoint, depth), Store.YES));
        return d;
    }
    
}
