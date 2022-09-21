/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author aaron
 */
public class DocParser_Route extends Parser {
     
    
    

    /**
     * buildTrie returns a Trie structure
     * buildTrie reads in documents from Reuter
     * all of the the files names of the different Reuter files are added to the ArrayList fileNames
     * a for each for loop is run for each file name
     * a document is add to the trie by adding all the lines between <BODY> and &#3;</BODY></TEXT> into the String sb
     * sb is then add to the trie
     * and the process is continued to the end of the file
     * @param hs
     * @return
     * @throws IOException 
     */

    
     public Trie buildTrie(final Collection[] hs) throws IOException {
          Trie trie = new Trie();
         
        
          int count=0;
          ArrayList<String> fileNames=new ArrayList<String>();
          fileNames.add("route/reut2-000.sgm");
          fileNames.add("route/reut2-001.sgm");
          fileNames.add("route/reut2-002.sgm");
          fileNames.add("route/reut2-003.sgm");
          fileNames.add("route/reut2-004.sgm");
          fileNames.add("route/reut2-005.sgm");
          fileNames.add("route/reut2-006.sgm");
          fileNames.add("route/reut2-007.sgm");
          fileNames.add("route/reut2-008.sgm");
          fileNames.add("route/reut2-009.sgm");
          fileNames.add("route/reut2-010.sgm");
          fileNames.add("route/reut2-011.sgm");
          fileNames.add("route/reut2-012.sgm");
          fileNames.add("route/reut2-013.sgm");
          fileNames.add("route/reut2-014.sgm");
          fileNames.add("route/reut2-015.sgm");
          fileNames.add("route/reut2-016.sgm");
          fileNames.add("route/reut2-017.sgm");
          fileNames.add("route/reut2-018.sgm");
          fileNames.add("route/reut2-019.sgm");
          fileNames.add("route/reut2-020.sgm");
          fileNames.add("route/reut2-021.sgm");
          
          for(String el:fileNames){
              System.out.println("Reading in: "+el);
          String line;
          
         BufferedReader  br = new BufferedReader(new FileReader(el));  
          line=br.readLine();
          while(line!=null){
          String body="";
              if(line.contains("<BODY>")){
                  
                 //System.out.println("Begining: "+line);
                  int first=0;
                  int last=line.indexOf("<BODY>");
                 // count++;
                  String remove=line.substring(first, last+6);
                 line=line.replace(remove," ");
                    body=body+" "+line;
                    line=br.readLine();
                    while(!line.contains("&#3;</BODY></TEXT>")){
                        body=body+" "+line;
                        line=br.readLine();
                    }
                    addToTrie(body,trie,count);
                    if (hs != null) {
                        try {
                            hs[count] = new HashSet();
                            String[] sArray = body.split(" ");
                            hs[count].addAll(Arrays.asList(sArray));
                        } catch (Exception ex) {/*System.out.println("count: " + count);*/}
                    }
                  }else if(line.contains("NEWID=")){
                      //System.out.println(line+ " my count:"+count);
                      if (hs != null && hs[count] == null) {
                        // for (int j = count; j < hs.length; j++) {
                            hs[count] = new HashSet();
                        // }
                      }
                      count++;
                      line=br.readLine();
                  }else if(line.contains("<TITLE>")){
                      //System.out.println(line);
                      line=br.readLine();
                  }
                    else{
                    line=br.readLine();
                    }
              
          //line=br.readLine();
          
          }
          
          //System.out.println("Done Reading: "+el+" "+count);
          }
         
         return trie;
     }

}
