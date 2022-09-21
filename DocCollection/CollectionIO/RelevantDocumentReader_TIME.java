package DocCollection.CollectionIO;

import Search.*;
import DocCollection.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @file RelevantDocumentReader.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Utility class to create the RelevantDocuments structure for all queries.
 */
public class RelevantDocumentReader_TIME extends RelevantDocumentReader {

    
    private final BufferedReader br;
    
    /**
     * list containing relevant documents corresponding to given query number
     */
    private final ArrayList<RelevantDocuments> queries;
    
    /**
     * Default constructor for RelevantDocumentReader.  Initializes all instance
     * variables.
     * 
     * @throws FileNotFoundException from BufferedReader
     */
    public RelevantDocumentReader_TIME() throws FileNotFoundException {
        br = new BufferedReader(new InputStreamReader(
             new FileInputStream("time/TIME.REL")));
        queries = new ArrayList<RelevantDocuments>();
    }
    
    /**
     * Reads the related documents file and the documents to their appropriate 
     * place in the ArrayList.
     * 
     * @throws Exception from BufferedReader
     */
    public void read() throws Exception {
       // BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(StringHash.factory().get("queryRelevance"))));
        
        String line;
        int max = Integer.MIN_VALUE;
        int counter = 0;
        int maxPosition = 0;
        
        while ((line = br.readLine()) != null) {
            line=line.replace("  ", " ");
            line=line.replace("  ", " ");
            //line=line.replace("   ", " ");
            //line=line.replace("    ", " ");
            String[] lines = line.split(" ");
            try {
                queries.get(Integer.parseInt(lines[0]) - 1);
            }catch(Exception ex) {
                queries.add(new RelevantDocuments());
                if (counter > max) {
                    max = counter;
                    maxPosition = Integer.parseInt(lines[0]);
                }
                counter = 0;
            }
            //System.out.println("ReleventDoc TIME: "+line);
            for(int i=1;i<lines.length;i++){
            //    System.out.println("ReleventDoc TIME: "+lines[0]+"  "+lines[i]+"  i value: "+i);
            queries.get(Integer.parseInt(lines[0]) - 1).putInt(
                    Integer.parseInt(lines[i]),1);
            }
            br.readLine();
            counter++;
            
        }
        // System.out.println("query " + maxPosition + " with value " + max);
    }
    
    /**
     * Accessor method for total number of relevant documents for a specific 
     * query at a relevance level.
     * 
     * @param queryID query which is being asked about
     * @param relevance relevance level at which to look
     * @return number of relevant documents at the at point
     */
    public int totalRelevantDocuments(final int queryID, final int relevance) {
        try {
            return queries.get(queryID).totalRelevantDocuments(relevance);
        } catch(NullPointerException ex) {
            return 0;
        }
    }
    
    /**
     * Determines if a document is relevant to a specific query.
     * 
     * @param queryID query which is being asked about
     * @param docID document which is being asked about
     * @param maxRelevance relevance level to consider
     * @return true if it is relevant, false if it is not
     */
    public boolean docIsRelevant(final int queryID, final int docID, 
                                                    final int maxRelevance) {
        try {
            return queries.get(queryID).docIsRelevant(docID, maxRelevance);
        } catch(NullPointerException ex){
            return false;
        }
    }
}
