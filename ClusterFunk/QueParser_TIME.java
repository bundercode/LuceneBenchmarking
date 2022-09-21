/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author aaron
 */
public class QueParser_TIME extends Parser {
    
     /**
     * buildTrie returns a Trie structure
     * buildTrie reads in queries from TIME
     * a query is add to the trie by adding all the lines between *Text(each *TEXT represents a new document) into the String sb
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
          
         BufferedReader  br =  new BufferedReader(new FileReader("time/TIME.QUE"));
          line=br.readLine();
          while(!line.contains("*STOP")){
          String body="";
              if(line.contains("*FIND")){
                  
                 //System.out.println("Begining: "+line);
                  int first=0;
                  int last=line.indexOf("*FIND");
                 // count++;
                  String remove=line.substring(first, last+6);
                 line=line.replace(remove," ");
                    body=body+" "+line;
                    line=br.readLine();
                    while(!line.contains("*FIND") && !line.contains("*STOP")){
                        body=body+" "+line;
                        line=br.readLine();
                    }
                    System.out.println(body);
                   // addToTrie(body,trie,count);
                    
                    String[] array = body.split(" ");
                    if (hs != null && count >= 0) {
                    hs[count] = new HashSet();
                    for (String s : array) {
                        if (!analyzerList.contains(s)) {
                            if (hs != null && count>= 0) {
                                hs[count].add(s);
                            }
                            trie.addToTrie(s, count);
                        }
                    }
                }
                    
                    count++;
                    
              }
              
          //line=br.readLine();
          
          }
         
         return trie;
     }
    
}
