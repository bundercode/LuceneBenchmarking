/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Search;

import java.util.HashMap;

/**
 * @file StringHash.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Centralized place to store strings for the project.  Uses factory method so
 * only one instance can exist at a time.
 */
public class StringHash extends HashMap<String, String> {
    
    private static StringHash sh = null;
    
    /**
     * Default constructor for StringHash.  Places all relevant text in the
     * hash.  This is the only place these strings should be listed.
     */
    private StringHash() {
        this.put("author", ".A");
        this.put("analyzer", "analyzerWords.txt");
        this.put("body", ".W");
        this.put("documentFile", "cran/cran.all.1400");
        this.put("information", ".B");
        this.put("indexFile", "index");
        this.put("queryFile", "cran/cran.qry");
        this.put("queryRelevance","cran/cranqrel");
        this.put("start", ".I");
        this.put("title", ".T");
    }
    
    /**
     * Creates and/or returns the only instance of StringHash.
     * 
     * @return instance of string hash.
     */
    public static StringHash factory() {
        if (sh == null) {
            sh = new StringHash();
        }
        return sh;
    }
    
}
