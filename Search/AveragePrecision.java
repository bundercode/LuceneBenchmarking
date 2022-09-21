/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Search;

import DocCollection.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @file AveragePrecision.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 */
@Deprecated
public class AveragePrecision extends HashMap<Integer, Integer> {
    
    //private globals for AveragePrecision
    private int relRetrieved;
    private int[] relevance;
    private int numRetrieved;
    
    /**
     * Constructor for AveragePrecision
     * substantiate numRetrieved, relevance[]
     * stores 0 at every position in relevance[]
     */
    public AveragePrecision() {
        this.relRetrieved = 0;
        numRetrieved=0;
        relevance = new int[5];
        for (int i = 0; i < relevance.length; i++) {
            relevance[i] = 0;
        }
    }
    /**
     * Method created to count the # of relevant docs at a specified relevance level
     * @param docID
     * @param rel 
     */
    public void putInt(final int docID, final int rel) {
        super.put(docID, rel);
        if (rel >= 0 && rel < relevance.length) {
            relevance[rel]++;
        }
    }
    
    /*public static double avp(ArrayList<AveragePrecision> ap) {
        
        double precision = 0;
        
        for (AveragePrecision pre : ap) {
            
            precision = pre.precision() * pre.recall(4)/pre.size();
            
        }
        
        return precision;
        
    }*/
    // num retrieved documents 10 - num documents 
    /**
     * precision is calculated by dividing # of relevant docs from cran docs divided by the # of relevant found by our search 
     * @return 
     */
    public double precision() {
        return relRetrieved/1.0/numRetrieved;
    }
    /**
     * Method that calculates the recall
     * Recall is calculated by finding the total # of relevant documents found by our search
     * Then dividing # of relevant document found in the cranfield docs divide by # of relevant documents found by our search
     * @param k
     * @return 
     */
    public double recall(final int k) {
        int sum=0;
        for (int i = 1; i <= k; i++) {
            sum += relevance[i];
        }
        return relRetrieved/1.0/sum;
    }
    public void setRetrieved(int num){
      numRetrieved=num;
    }
    
    /**
     * checkDoc Method calculates the # of relevant documents retrieved from the cran docs for a query
     * @param docID 
     */
    public void checkDoc(final int docID) {
        if (this.containsKey(docID) && this.get(docID) >= 1 && this.get(docID) <= 4) {
            relRetrieved++;
        }
    }
    /**
     * A toString method returns a string of  # of relevant docs found in cran docs, and then # of docs for each relevance
     * @return 
     */
    public String toString() {
        // return ("Present: " + relRetrieved + "  " + super.toString() + "\n" + "Precision: " + precision() + " Recall: " + recall(4) + "\n");
        return ("Present: " + relRetrieved + "\tFor numbers 1: " + relevance[1] + "\tFor numbers 2: " + relevance[2] + "\tFor numbers 3: " + relevance[3] + "\tFor numbers 4: " + relevance[4]);
    }
}
