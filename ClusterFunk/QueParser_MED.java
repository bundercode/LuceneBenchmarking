/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import Search.StringHash;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author aaron
 */
public class QueParser_MED extends Parser {
   private static final StringHash sh = StringHash.factory();
   private final BufferedReader br;
    
   /**
    * QueParser extends Parser.
    * The constructor reads in a file from med/MED.QRY file
    * @throws FileNotFoundException 
    */ 

   public QueParser_MED() throws FileNotFoundException{
       br = new BufferedReader(new FileReader("med/MED.QRY"));
   }
   
   
   /**
    * The method buildTrie reads the file, and adds each query to the trie, and then returns the trie.
    * Each query begins with the word "start" and "body".
    * Text following the word "body" is the actually query from the file.
    * So when the word "start" is reach the query is add to the trie.
    * After the text file has been completely read.
    * The method returns the trie.
    * @param hs
    * @return
    * @throws IOException 
    */


       public Trie buildTrie(final Collection[] hs) throws IOException {
        int i = -1;
        Trie t = new Trie();
        String line;
        boolean readToggle = false;
        StringBuffer sb = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith(sh.get("start")) && sb != null) {
                readToggle = false;
                // add to query
                String[] array = sb.toString().replaceAll("[^a-zA-Z ]", "").split(" ");
                if (hs != null && i >= 0) {
                    hs[i] = new HashSet();
                }
                for (String s : array) {
                    if (hs != null && i >= 0) {
                        hs[i].add(s);
                    }
                    t.addToTrie(s, i);
                }
            }
            if (readToggle) {
                // add to buffer
                sb.append(line);
            }
            if (line.startsWith(sh.get("body"))) {
                readToggle = true;
                sb = new StringBuffer();
                i++;
            }
        }
        if (hs != null) {
            for (int j = i; j < hs.length; j++) {
                hs[j] = new HashSet();
            }
        }
        String[] array = sb.toString().replaceAll("[^a-zA-Z ]", "").split(" ");
                if (hs != null && i >= 0) {
                    hs[i] = new HashSet();
                }
                for (String s : array) {
                    if (hs != null && i >= 0) {
                        hs[i].add(s);
                    }
                    t.addToTrie(s, i);
                }
        return t;
    }
}
