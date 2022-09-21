/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Search;

import DocCollection.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.Math;

/**
 * @file Map.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 */
@Deprecated
public class Map {
    
    private final ArrayList<AveragePrecision> queries;
    
    /**
     * Public Constructor for Map
     * substantiate queries to a new ArrayList
     */
    public Map() {
        queries = new ArrayList<>();
    }
    
    /**
     * reads the expected result from the cranfield files
     * for each query the documents that cranfield consider relevant are read in with their relevance 
     * @throws Exception 
     */
    public void read() throws Exception {
        // change this so it only happens once instead of 10 times
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(StringHash.factory().get("queryRelevance"))));
        
        String line;
        int max = Integer.MIN_VALUE;
        int counter = 0;
        int maxPosition = 0;
        
        while ((line = br.readLine()) != null) {
            
            String[] lines = line.split(" ");
            // System.out.println(line);
            try {
                queries.get(Integer.parseInt(lines[0]) - 1);
            }catch(Exception ex) {
                queries.add(new AveragePrecision());
                if (counter > max) {
                    max = counter;
                    maxPosition = Integer.parseInt(lines[0]);
                }
                counter = 0;
            }
            queries.get(Integer.parseInt(lines[0]) - 1).putInt(Integer.parseInt(lines[1]), Integer.parseInt(lines[2]));
            counter++;
        }
        // System.out.println("query " + maxPosition + " with value " + max);
    }
    
    public void checkDoc(final int queryNum, final int docID,final int numRetrieved) {
        queries.get(queryNum).checkDoc(docID);
        queries.get(queryNum).setRetrieved(numRetrieved);
    }
   /**
    * Method that prints the query number, precision and recall
    */
    public void print() {
        /*for (int i = 0; i < queries.size(); i++) {
            System.out.println("Query: " + (i+1) + " " + queries.get(i).toString());
        }*/
        for (int i = 0; i < queries.size(); i++) {
            System.out.println("Query: "+ (i+1) +"\tprecision: "+ queries.get(i).precision() +"\trecall: "+ queries.get(i).recall(4) +"\n\t"+ queries.get(i).toString() +"\n");
        }
        System.out.println("***************************************************************************************************************\n");
    }
    public void addAVPS(final AVP avp) {
        double deltaRecall = queries.get(0).recall(4);
        for (int i = 0; i < queries.size(); i++) {
            if (i != 0){
                deltaRecall = Math.abs(queries.get(i).recall(4) - queries.get(i-1).recall(4));
            }
            avp.addAVP(i, queries.get(i), deltaRecall);
        }
    }
}
