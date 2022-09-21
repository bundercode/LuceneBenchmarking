/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import Search.StringHash;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author aaron
 */
public class DocParser_MED extends Parser {
    
    
    /**
     * buildTrie returns a Trie structure
     * buildTrie reads in documents from medline
     * a document is add to the trie by adding all the lines between .W and .I into the String sb
     * sb is then add to the trie
     * and the process is continued to the end of the file
     * @param hs
     * @return
     * @throws IOException 
     */
public Trie buildTrie(final Collection[] hs) throws IOException {
        String line;
        StringHash sh = StringHash.factory();
       BufferedReader br = new BufferedReader(new FileReader("med/MED.ALL"));
        Trie trie = new Trie();
        int i = -1;
        boolean readToggle = false;
        String body = sh.get("body");
        String start = sh.get("start");
        String author = sh.get("author");
        String title = sh.get("title");
        String sb = "";
        boolean read=false;
         line = br.readLine();
        while(line != null) {
            if(line.contains(".W")){
                i++;
                read=true;
                }
            
            if(line.contains(".I") && i!=-1){
                addToTrie(sb, trie, i);
                if (hs != null) {
                        hs[i] = new HashSet();
                        String[] sArray = sb.split(" ");
                        hs[i].addAll(Arrays.asList(sArray));
                    }
                    
               sb= sb.replaceFirst(".W ", " ");
                System.out.println(sb);
                sb="";
                read=false;
                
            }
           if(read){
               
                    sb=sb+" "+line;
                    //System.out.println(sb);
           }
           line=br.readLine();
        }
        // when line == null
        //addToTrie(sb, trie, i);
           
        //System.out.println(i);
        
        addToTrie(sb, trie, i);
                if (hs != null) {
                        hs[i] = new HashSet();
                        String[] sArray = sb.split(" ");
                        hs[i].addAll(Arrays.asList(sArray));
                    }
        
        return trie;
    }
    
}
