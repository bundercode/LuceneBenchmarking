package DocCollection.CollectionIO;

/**
 * @file RelevantDocumentReader.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Utility class to create the RelevantDocuments structure for all queries.
 */
public abstract class RelevantDocumentReader {

    
    /**
     * Reads the related documents file and the documents to their appropriate 
     * place in the ArrayList.
     * 
     * @throws Exception from BufferedReader
     */
    public abstract void read() throws Exception;
    
    /**
     * Accessor method for total number of relevant documents for a specific 
     * query at a relevance level.
     * 
     * @param queryID query which is being asked about
     * @param relevance relevance level at which to look
     * @return number of relevant documents at the at point
     */
    public abstract int totalRelevantDocuments(final int queryID, final int relevance);
    
    /**
     * Determines if a document is relevant to a specific query.
     * 
     * @param queryID query which is being asked about
     * @param docID document which is being asked about
     * @param maxRelevance relevance level to consider
     * @return true if it is relevant, false if it is not
     */
    public abstract boolean docIsRelevant(final int queryID, final int docID, 
                                                    final int maxRelevance);
    
}
