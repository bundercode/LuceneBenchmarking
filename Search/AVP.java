/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Search;

import DocCollection.*;
import java.util.ArrayList;

/**
 * @file AVP.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 */
@Deprecated
public class AVP {
    
    final ArrayList<Double> averagePrecisions;
    
    public AVP() {
        averagePrecisions = new ArrayList<Double>();
    }
    
    public void addAVP(final int index, final AveragePrecision ap, final double deltaRecall) {
        assert index <= averagePrecisions.size();
        if (index == averagePrecisions.size()) {
            averagePrecisions.add(0.0);
            //summation of (precisions)
        }
        averagePrecisions.set(index, averagePrecisions.get(index) + (ap.precision() * deltaRecall));
    }
    
    /*TODO:  multiply each precision value by the difference between the recall
            at that level and the recall at the previous level.
    */
    public void printAVP() {
        for (int i = 0, j = 1; i < averagePrecisions.size(); i++, j++) {
            //averagePrecisions.set(i, averagePrecisions.get(i)/10);
            System.out.println("Query: " + j + "\taverage precision: " + averagePrecisions.get(i));
        }
    }
    
    public double mapScore() {
        double map = 0;
        for (int i = 0; i < averagePrecisions.size(); i++) {
            map += averagePrecisions.get(i);
        }
        map/=averagePrecisions.size();
        return map;
    }
}
