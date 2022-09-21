package ClusterFunk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import Search.StringHash;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * @file DocumentParser.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Reads documents from the document file and returns them as document objects.
 */
public class DocParser_Cran extends Parser{
    private static final StringHash sh = StringHash.factory();
    private final BufferedReader br;
    
    /**
     * Constructor for DocumentParser.  Initializes BufferedReader to read from
     * file.
     * 
     * @throws FileNotFoundException from BufferedReader
     */
    public DocParser_Cran() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(sh.get("documentFile")));
    }
    
    /**
     * buildTrie returns a Trie structure
     * buildTrie reads in documents from cranfield
     * a document is add to the trie by adding all the lines between .W and .I into the StringBuffer sb
     * sb is then add to the trie
     * and the process is continued to the end of the file
     * @param hs
     * @return
     * @throws IOException 
     */
    
    public Trie buildTrie(final Collection[] hs) throws IOException {
        String line;
        Trie trie = new Trie();
        int i = -1;
        boolean readToggle = false;
        String body = sh.get("body");
        String start = sh.get("start");
        String author = sh.get("author");
        String title = sh.get("title");
        StringBuffer sb = new StringBuffer();
        while((line = br.readLine()) != null) {
            // if starts with .W begin reading
            if ((line.startsWith(body) || line.startsWith(title))) {
                // start reading
                readToggle = true;
            }
            boolean b;
            if (b = line.startsWith(start)) {
                //if (b) {
                    // deal with read stuff
                if (i >= 0) {
                    String s = sb.toString();
                    s = s.replace(body, "");
                    s = s.replace(title, "");
                    s = s.toLowerCase();
                    s = s.replaceAll("[^a-z ]", "");
                    String[] array = s.split(" ");
                    // System.out.println("\n\nadded " + i + ":  " + sb.toString() + "\n\n");
                    if (hs != null) {
                        hs[i] = new HashSet();
                        for (String string : array) {
                            if (!analyzerList.contains(string)) {
                                addToTrie(string, trie, i);
                                if (hs != null) {
                                    hs[i].add(string);
                                }
                            }
                        }
                    }
                    
                    
                    sb = new StringBuffer();
                }
                i++;
                readToggle = false;
            }
            if (line.startsWith(author)) {
                readToggle = false;
            }
            if (readToggle) {
                sb.append(line);
            }
        }
        // when line == null
        if (!analyzerList.contains(sb.toString())) {
            addToTrie(sb.toString(), trie, i);
        }
        if (hs != null) {
            String s = sb.toString();
            s = s.replace(body, "");
            s = s.replace(title, "");
            s = s.toLowerCase();
            s = s.replaceAll("[^a-z ]", "");
            String[] array = s.split(" ");
            for (int j = i; j < hs.length; j++) {
                hs[j] = new HashSet();
            }
            for (String string : array) {
                if (!analyzerList.contains(string)) {
                    addToTrie(string, trie, i);
                        if (hs != null) {
                            hs[i].add(string);
                        }
                }
            }
        }
        return trie;
    }
    
}
