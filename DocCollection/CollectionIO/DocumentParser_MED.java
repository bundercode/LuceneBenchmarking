package DocCollection.CollectionIO;

import static DocCollection.CollectionIO.DocumentParser.sh;
import Search.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * @file DocumentParser.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Reads documents from the document file and returns them as document objects.
 */
public class DocumentParser_MED extends DocumentParser {

    private int util = Integer.MIN_VALUE;
    private final BufferedReader br;
    
    /**
     * Constructor for DocumentParser.  Initializes BufferedReader to read from
     * file.
     * 
     * @param filename file where the documents are stored
     * @throws FileNotFoundException from BufferedReader
     */
    public DocumentParser_MED() throws FileNotFoundException {
        br = new BufferedReader(new FileReader("med/MED.ALL"));
    }
    
    /**
     * Method that builds a document and returns a document
     * Method calls StingHash to read lines from cranfield doc files
     * A document is broken down into four sections(title,author,information,body)
     * 
     * @return newly created Document object
     * @throws IOException from BufferedReader
     */
    public Document buildDocument() throws IOException, Exception {
        Document d = new Document();
        String line = br.readLine();
        if (line == null) {
            return null;
        }
        if (line.startsWith(sh.get("start"))) {
            String[] lines = line.split(" ");
            // System.out.println(lines[1]);
            // System.exit(0);
            d.add(new StringField(sh.get("start"), lines[1], Store.YES));
            br.readLine();
        }else {
            d.add(new IntField(sh.get("start"), util, Store.YES));
        }
        
        d.add(new TextField(sh.get("body"), /*line = */readInput().toLowerCase(), Store.YES));
        return d;
    }
    /**
     * reads input from cranfield file using StringHash class
     * using the keyword start
     * 
     * @return input from cranfield document
     * @throws IOException from BufferedReader
     */
    private String readInput() throws IOException {
        String line = "";
        StringBuffer field = new StringBuffer();
        do {
            field.append(line);
            line = br.readLine();
            if (line == null) {return field.toString();}    
        } while (!line.startsWith("."));
        if (line.startsWith(sh.get("start"))) {
            String[] lines = line.split(" ");
            util = Integer.parseInt(lines[1]);
        }
        return field.toString();
    }
    
}
