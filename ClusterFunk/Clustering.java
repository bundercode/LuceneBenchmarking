package ClusterFunk;

import DocumentGraph.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList; /* ArrayList */
import java.util.HashSet;
import java.util.Scanner; /* Scanner */

/**
 * @file Clustering.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class Clustering {
    
    // read in documents
    // create dictionary *
    // create trie *
        // node: data structure for all documents *
        // node: quantity for each term *
    // int overall quantity (idf)
    // int[] quantityInDoc {0, 5, 7}
    // create a vector for each document
    
    // normalize vectors *
    // find distance between vectors *
    
    // k-means
    
    static final int CRANFIELD =0;
    static final int REUTERS =1;
    
    static final int CRANSIZE =1400;
    static final int REUTSIZE =21578;
    
    public static void main(String[] args) {
        
        Scanner userArgs;
        
        DocParser_Cran cranParser; //=null;
        DocParser_Route reutParser; //=null;
        Trie corpusTrie =null;
        HashSet<String>[] hs =null;
        
        int collectionSize =CRANSIZE;
        int numRounds; //=100;
        int k; //=(int)(Math.sqrt(collectionSize) /2);
        int simMethod =KMeans.GRAPH_SIM;
        String trieFile ="";
        
        if (args.length != 4) {
            System.err.println("Usage: java ClusterFunk/Clustering "
                    +"[cranfield | reuters] [ROUNDS] [k] "
                    +"[cosine | graph] [trie file name]");
            System.exit(0);
        }
        
        switch (args[0].toLowerCase()) {
            case "cranfield":
                try {
                    trieFile ="cran.trie";
                    cranParser = new DocParser_Cran();
                    long start = System.currentTimeMillis();
                    collectionSize =CRANSIZE;
                    hs = new HashSet[collectionSize];
                    /*try {
                        System.out.println("reading trie");
                        FileInputStream fis = new FileInputStream(trieFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        corpusTrie = (Trie)ois.readObject();
                        ois.close();
                        fis.close();
                        System.out.println("done reading trie");
                    } catch(Exception ex) {
                        if (args.length == 5){
                            trieFile =args[4];
                        }*/
                        System.out.println("building trie");
                        FileOutputStream fos = new FileOutputStream(trieFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        corpusTrie = cranParser.buildTrie(hs);
                        oos.writeObject(corpusTrie);
                        System.out.println("done building trie");
                        oos.close();
                        fos.close();
                    //}
                    System.out.println("Trie took " + (System.currentTimeMillis() - start) + "ms");
                } catch (Exception e) {
                    System.out.println("Failed to create Trie: ");
                    e.printStackTrace();
                    System.exit(-1);
                }
                break;
            case "reuters":
                try {
                    trieFile ="reut.trie";
                    reutParser = new DocParser_Route();
                    long start = System.currentTimeMillis();
                    collectionSize =REUTSIZE;
                    hs = new HashSet[collectionSize];
                    /*try {
                        System.out.println("reading trie");
                        FileInputStream fis = new FileInputStream(trieFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        corpusTrie = (Trie)ois.readObject();
                        ois.close();
                        fis.close();
                        System.out.println("done reading trie");
                    } catch(Exception ex) {
                        if (args.length == 5){
                            trieFile =args[4];
                        }*/
                        System.out.println("building trie");
                        FileOutputStream fos = new FileOutputStream(trieFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        corpusTrie = reutParser.buildTrie(hs);
                        oos.writeObject(corpusTrie);
                        System.out.println("done building trie");
                        oos.close();
                        fos.close();
                    //}
                    System.out.println("Trie took " + (System.currentTimeMillis() - start) + "ms");
                } catch (Exception e) {
                    System.out.println("Failed to create Trie: ");
                    e.printStackTrace();
                    System.exit(-1);
                }
                break;
        }
        
        userArgs = new Scanner(args[1]);
        numRounds = userArgs.nextInt();
        
        userArgs = new Scanner(args[2]); 
        k = userArgs.nextInt();
        
        switch (args[3].toLowerCase()) {
            case "cosine":
                simMethod =KMeans.COSINE_SIM;
                break;
            case "graph":
                simMethod =KMeans.GRAPH_SIM;
                break;
        }
        
        /*System.out.println("Starting buidling vectors...");
        corpusTrie.addToDocumentVector(corpusVectors, collectionSize);
        System.out.println("Finished buidling vectors...");*/
        
        /*for (int i = 0; i < corpusVectors.length; i++) {
            //System.out.println("Vector " + i + ":  " +corpusVectors[i].toString());
            
            corpusVectors[i].normalize();
            System.out.println("normalizing vector: " + i);
        }*/
        
        //Normalize vectors
        /*for (int i = 0; i < corpusVectors.length; i++) {
            corpusVectors[i].normalize();
        }*/
        
        KMeans kMeans = new KMeans();
        ArrayList<DocHash>[] clusters =null;
        
        //kMeans.readReut();
        
        System.out.println("Starting k-means clustering...");
        clusters = kMeans.kMeans(hs, corpusTrie, collectionSize,
                k, numRounds, simMethod);
        System.out.println("Finished k-means clustering...");
        
        /* Print clusters */
        /*int groupSize = clusters.length;
        int clusterSize;
        for (int i=0; i < groupSize; i++) {
            clusterSize = clusters[i].size();
            System.out.println("\nCluster #: "+ i);
            for (int j=1; i < clusterSize; j++) {
                System.out.format("%d, ",
                    (clusters[i].get(j)).getDocID());
            }
        }*/
        
        System.exit(0);
    }
}
