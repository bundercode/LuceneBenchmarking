/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

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
public class DocParser_TIME extends Parser {
    
    /**
     * buildTrie returns a Trie structure
     * buildTrie reads in documents from TIME
     * a document is add to the trie by adding all the lines between *Text(each *TEXT represents a new document) into the String sb
     * sb is then add to the trie
     * and the process is continued to the end of the file(*STOP is the end of the file; *START is the beginning of the file)
     * @param hs
     * @return
     * @throws IOException 
     */

     public Trie buildTrie(final Collection[] hs) throws IOException {
          Trie trie = new Trie();
         
        
          int count=0;
          
          
          
          String line;
          
         BufferedReader  br =  new BufferedReader(new FileReader("time/TIME.ALL"));
          line=br.readLine();
          while(!line.contains("*STOP")){
          String body="";
              if(line.contains("*TEXT")){
                  
                 //System.out.println("Begining: "+line);
                  int first=0;
                  int last=line.indexOf("*TEXT");
                 // count++;
                  String remove=line.substring(first, last+6);
                 line=line.replace(remove," ");
                    body=body+" "+line;
                    line=br.readLine();
                    while(!line.contains("*TEXT") && !line.contains("*STOP")){
                        body=body+" "+line;
                        line=br.readLine();
                    }
                    System.out.println("Hahaha: "+count+" "+body);
                    addToTrie(body,trie,count);
                    if (hs != null) {
                        hs[count] = new HashSet();
                        String[] sArray =body.split(" ");
                        hs[count].addAll(Arrays.asList(sArray));
                    }
                    count++;
                    
              }
              
          //line=br.readLine();
          
          }
          
         
         
         return trie;
     }
    
}
