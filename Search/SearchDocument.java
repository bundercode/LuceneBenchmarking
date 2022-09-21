package Search;

import java.util.HashMap;

/**
 * @file SearchDocument.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class SearchDocument {
    
    private final HashMap<String, Integer> contents;
    
    private final int documentID;
    
    public SearchDocument(final String contents, final int documentID) {
        this.contents = new HashMap<>();
        this.documentID = documentID;
        addToDocument(contents);
    }
    
    private void addToDocument(final String contents) {
        String[] words = contents.split(" ");
        for (String w : words) {
            int i = 0;
            if (this.contents.containsKey(w)) {
                i = this.contents.get(w);
            }
            this.contents.put(w, i + 1);
        }
    }
    
}
