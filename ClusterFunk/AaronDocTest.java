/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClusterFunk;

import static ClusterFunk.Clustering.CRANSIZE;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aaron
 */
@Deprecated
public class AaronDocTest {
  public static void main(String[] args) {
        
        DocParser_Route docParser;
        Trie corpusTrie = null;
        ArrayList<DocHash>[] clusters;
        
        try {
            docParser = new DocParser_Route();
            System.out.println("building trie");
            corpusTrie = docParser.buildTrie(null);
            System.out.println("done building trie");
        } catch (Exception e) {
            System.out.println("Failed to create Trie: ");
            e.printStackTrace();
            System.exit(-1);
        }
        
        /*for (int i = 0; i < corpusVectors.length; i++) {
            //System.out.println("Vector " + i + ":  " +corpusVectors[i].toString());
            
            corpusVectors[i].normalize();
            System.out.println("normalizing vector: " + i);
        }*/
        /*
        clusters = KMeans.kMeans(corpusTrie, 21578, 73, 25, KMeans.GRAPH_SIM);
        int groupSize = clusters.length;
        int clusterSize;
        for (int i=0; i < groupSize; i++) {
            clusterSize = clusters[i].size();
            for (int j=1; i < clusterSize; j++) {
                System.out.format("DocID: %d\t Values: "
                    +((DocHash)clusters[i].get(j)).toString()
                    +"\n",
                    ((DocHash)clusters[i].get(j)).getDocID());
            }
        }
        */
        
        System.exit(0);
    }
}
