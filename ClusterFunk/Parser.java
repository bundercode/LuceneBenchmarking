/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import Search.StringHash;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author andrew
 */
public abstract class Parser {
    
    public abstract Trie buildTrie(final Collection[] hs) throws IOException;
    protected static HashSet<String> analyzerList = null;
    protected static final StringHash sh = StringHash.factory();
    
    /**
      * Constructor for Parser
      * substantiates analyzerList HashSet
      */

    protected Parser() {
        //creates an ArrayList for stopWords
        if (analyzerList == null) {
            analyzerList = new HashSet<String>();
            try {
                //adds words to analyzerList
                makeAnalyzerList();
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * The addToTrie method takes in string variable(s1), trie variable(t),and integer variable(docID).
     * All punctuation and special characters(0-9, ".,;:'"!@#$%^&*()+=-/\") are removed from the string.
     * Then the string is split into an array of strings (array) separated by white space.
     * Then each word is add to the trie.
     * @param s1
     * @param t
     * @param docID 
     */

    protected void addToTrie(final String s1, final Trie t, final int docID) {
        String s2 = s1.replaceAll("[^a-zA-Z ]", "");
        String[] array = s2.split(" ");
        for (String s : array) {
            t.addToTrie(s, docID);
        }
    }
    
        /**
     * method that adds stop words to analyzerList
     * @throws Exception 
     */
    private static void makeAnalyzerList() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sh.get("analyzer"))));
        String line = null;
        while ((line = br.readLine()) != null) {
            analyzerList.add(line);
        }
    }
    
}
