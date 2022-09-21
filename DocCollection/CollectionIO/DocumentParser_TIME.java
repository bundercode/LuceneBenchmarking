package DocCollection.CollectionIO;

import static DocCollection.CollectionIO.DocumentParser.sh;
import Search.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DocumentParser_TIME extends DocumentParser {

    private int util = Integer.MIN_VALUE;
    private final BufferedReader br;
    private  String title;
    
    /**
     * Constructor for DocumentParser.  Initializes BufferedReader to read from
     * file.
     * 
     * @param filename file where the documents are stored
     * @throws FileNotFoundException from BufferedReader
     */
    public DocumentParser_TIME() throws FileNotFoundException {
        title="";
        br = new BufferedReader(new FileReader("time/TIME.ALL"));
        try {
            title=br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(DocumentParser_TIME.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        
        //String line = br.readLine();
        String line=title;
        if (line == null ||line.contains("*STOP")) {
            return null;
        }
        if(line.contains("*TEXT")){
            String[] lines = line.split(" ");
            d.add(new StringField(sh.get("start"), lines[1], Store.YES));
            d.add(new StringField(".D", lines[2], Store.YES));
            d.add(new StringField(sh.get("information"),lines[3]+"\n"+lines[4], Store.YES));
            line=br.readLine();
        }
        String body="";
        while(!line.contains("*TEXT") && !line.contains("*STOP")){
            body=body+"\n"+line;
            line=br.readLine();
        }
        title=line;
        d.add(new TextField(sh.get("body"), /*line = */body.toLowerCase(), Store.YES));
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
       
        return "void method no longer needed";
    }
    
}
