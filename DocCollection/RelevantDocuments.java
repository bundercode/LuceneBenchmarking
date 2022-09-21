package DocCollection;

import Search.*;
import java.util.HashMap;

/**
 * @file RelevantDocuments.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 * Used to determine if a document is a relevant result for a given query.
 */
public class RelevantDocuments {
    
    private final HashMap<Integer, Integer> allDocuments;
    private final int[] relevant;
    
    public RelevantDocuments() {
        allDocuments = new HashMap<Integer, Integer>();
        relevant = new int[4];
        for (int i = 0; i < relevant.length; i++) {
            relevant[i] = 0;
        }
    }
    
    /**
     * Method created to count the # of relevant docs at a specified relevance level
     * @param docID
     * @param rel 
     */
    public void putInt(final int docID, final int relevance) {
        allDocuments.put(docID, relevance);
        if (relevance < relevant.length && relevance >= 0) {
            relevant[relevance - 1]++;
        }
    }
    
    /**
     * calculates the total # of relevant documents
     * @param relevance
     * @return 
     */
    public int totalRelevantDocuments(final int relevance) {
        int total = 0;
        /*for (int i = 0; i < relevance; i++) {
            total += relevant[i];
        }*/
        for (Integer a : allDocuments.values()) {
            if (a <= relevance && a >= 1) {
                total++;
            }
        }
        return total;
    }
    
    /**
     * Determines if a document is in a list of the relevant documents for a
     * query.
     * 
     * @param docID document to check
     * @param maxRelevance how related the document should be ie. 1, 2, 3 ...
     * @return true if relevant, false if not relevant
     */
    public boolean docIsRelevant(final int docID, final int maxRelevance) {
        return (allDocuments.containsKey(docID) && 
                allDocuments.get(docID) >= 1 && 
                allDocuments.get(docID) <= maxRelevance);
    }
    
}
