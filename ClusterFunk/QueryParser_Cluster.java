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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @file QueryParser_Cluster.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class QueryParser_Cluster extends Parser {
    private static final StringHash sh = StringHash.factory();
    private final BufferedReader br;
    
    public QueryParser_Cluster() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(sh.get("queryFile")));
    }
    
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
                String string = sb.toString();
                string = string.replace(sh.get("start"), "");
                string = string.replace(sh.get("body"), "");
                string = string.toLowerCase();
                string = string.replaceAll("[^a-z ]", "");
                String[] array = string.split(" ");
                if (hs != null && i >= 0) {
                    hs[i] = new HashSet();
                    for (String s : array) {
                        if (!analyzerList.contains(s)) {
                            if (hs != null && i >= 0) {
                                hs[i].add(s);
                            }
                            t.addToTrie(s, i);
                        }
                    }
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
            String string = sb.toString();
            string = string.replace(sh.get("start"), "");
            string = string.replace(sh.get("body"), "");
            string = string.toLowerCase();
            string = string.replaceAll("[^a-z ]", "");
            String[] array = string.split(" ");
            for (int j = i; j < hs.length; j++) {
                hs[j] = new HashSet();
            }
            for (String s : array) {
                if (!analyzerList.contains(s)) {
                    if (hs != null && i >= 0) {
                        hs[i].add(s);
                    }
                    t.addToTrie(s, i);
                }
            }
        }
        return t;
    }
}
