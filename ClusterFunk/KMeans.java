
package ClusterFunk;

import DocumentGraph.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author smitbl07
 * 
 * Utility type class for clustering document
 *  collections using the K-Means algorithm
 * 
 * TODO: finish separation of MAP calculation
 * and centroid selection from KMeans
 * to ensure MAP calculations don't hold bugs.
 */
public class KMeans {
    
    public final static int GRAPH_SIM =0;
    public final static int COSINE_SIM =1;
    
    private ArrayList<DocHash>[] clusters;
    private ArrayList<Set<String>> topicArr;
    private SortedSet<String> topicSet;
    
    /**
     * 
     * @param hs - array of Sets of words in document collection
     * @param docTrie - Trie structure of document collection
     * @param collectionSize - number of documents in Trie
     * @param k - number of clusters
     * @param rounds - number of K-Means iterations
     * @param simType - distance method
     * 
     * @return - clusters: an array[k] of ArrayLists of DocVectors
     * 
     *  centroid = clusters[i].get(0)
     *
     * for details on K-Means algorithm see: 
     *  http://nlp.stanford.edu/IR-book/html/htmledition/k-means-1.html
     * 
     * This is a helper method for kMeans methods
     * 
     */
    public ArrayList<DocHash>[] kMeans(HashSet[] hs, Trie docTrie,
            int collectionSize, int k, int rounds, int simType) {
        
        switch (simType) {
            case GRAPH_SIM:
                return kMeansSemantic(hs, docTrie, collectionSize, k, rounds);
            case COSINE_SIM:
                DocHash[] documentVectors
                        =trieToVectors(docTrie, collectionSize);
                return kMeansCosine(documentVectors, k, rounds);
            default:
                return kMeansSemantic(hs, docTrie, collectionSize, k, rounds);
        }
    }
    
    private DocHash[] trieToVectors(Trie docTrie,
            int collectionSize){
        
        DocHash[] documentVectors = new DocHash[collectionSize];
        
        for (int i = 0; i < documentVectors.length; i++) {
            documentVectors[i] = new DocHash(i);
        }
        
        System.out.println("Starting building vectors...");
        docTrie.addToDocumentVector(documentVectors,
                collectionSize);
        System.out.println("Finished building vectors...");
        
        return documentVectors;
    }
    
    private ArrayList<DocHash>[] kMeansSemantic(
            HashSet[] hs, Trie docTrie, int collectionSize,
            int k, int rounds) {
        
        DocHash[] documentVectors =trieToVectors(docTrie, collectionSize);
        DocumentGraph docGraph = new DocumentGraph();
        
        int i;
        for (i=0; i < hs.length; i++) {
            docGraph.insertDocument(hs[i], i, docTrie);
        }
        
        this.clusters = new ArrayList[k];
        int seed; //randomly chosen initial vector foreach cluster
        
        /* Create k clusters, each with initial seed vector at position 1
            and at position 0 as initial centroid*/
        for (i=0; i < k; i++) {
            seed = (int) (Math.random() * documentVectors.length);
            clusters[i] = new ArrayList();
            clusters[i].add(documentVectors[seed]); //add seed
            //clusters[i].add(0, documentVectors[seed]); //add centroid
        }
        initCentroids(clusters, documentVectors);
        
        /* Find distance between each vector and each cluster centroid,
            then place vector in cluster with lowest distance */
        int j,l, bestCluster =0;
        double newDist, lowestDist;
        DocHash centroid;
        /* foreach round */
        for (i=0; i < rounds; i++) {
            /* clear cluster except centroid */
            for (j=0; j < clusters.length; j++) {
                centroid = clusters[j].get(0);
                clusters[j].clear();
                clusters[j].add(centroid);
            }
            
            /* foreach vector in documentVectors */
            for (j=0; j < documentVectors.length; j++) {
                /* foreach cluster */
                lowestDist =Double.NEGATIVE_INFINITY;
                for (l=0; l < clusters.length; l++) {
                    centroid = clusters[l].get(0);
                    
                    /* Comparison method */
                    newDist = docGraph.distanceBetweenNodesSmall
                            (centroid.getDocID(),
                            documentVectors[j].getDocID());
                    
                    //System.out.println("Distance: " +newDist);
                    if (newDist > lowestDist) {
                        lowestDist = newDist;
                        bestCluster =l;
                    }
                    
                    /*System.out.println("Checking docID " 
                            +documentVectors[j].getDocID()
                            +" against cluster " +l);*/
                }
                clusters[bestCluster].add(documentVectors[j]);
                System.out.println("Added docID ["
                        +documentVectors[j].getDocID()
                        +"] to cluster [" +bestCluster +"]");
            }
            
            /* recompute centroids */
            for (j=0; j < k; j++) {
                clusters[j].add(0, findCentroid(clusters[j],
                        ((j+(rounds+2))*-1)));
                
                docGraph.addCentroid(clusters[j].get(0).getHash(),
                        clusters[j].get(0).getDocID(), docTrie);
            }
            
            System.out.println("Completed round "+ i);
        }
        
        return clusters;
    }
    
    private ArrayList<DocHash>[] kMeansCosine(
            DocHash[] documentVectors, int k, int rounds) {
        
        this.clusters = new ArrayList[k];
        int seed; //randomly chosen initial vector foreach cluster
        
        /* Create k clusters, each with initial seed vector at position 1
            and at position 0 as initial centroid*/
        int i;
        for (i=0; i < k; i++) {
            seed = (int) (Math.random() * documentVectors.length);
            clusters[i] = new ArrayList();
            clusters[i].add(documentVectors[seed]); //add seed
            //clusters[i].add(0, documentVectors[seed]); //add centroid
        }
        initCentroids(clusters, documentVectors);
        
        /* Find distance between each vector and each cluster centroid,
            then place vector in cluster with lowest distance */
        int j,l, bestCluster =0;
        double newDist, lowestDist;
        DocHash centroid;
        /* foreach round */
        for (i=0; i < rounds; i++) {
            /* clear cluster except centroid */
            for (j=0; j < clusters.length; j++) {
                centroid = clusters[j].get(0);
                clusters[j].clear();
                clusters[j].add(centroid);
            }
            
            /* foreach vector in documentVectors */
            for (j=0; j < documentVectors.length; j++) {
                /* foreach cluster */
                lowestDist =Double.POSITIVE_INFINITY;
                for (l=0; l < clusters.length; l++) {
                    centroid = clusters[l].get(0);
                    
                    /* Comparison method */
                    newDist = DocHash.cosine(documentVectors[j], centroid);
                    
                    if (newDist < lowestDist) {
                        lowestDist = newDist;
                        bestCluster =l;
                    }
                    
                    /*System.out.println("Checking docID " 
                            +documentVectors[j].getDocID()
                            +" against cluster " +l);*/
                }
                clusters[bestCluster].add(documentVectors[j]);
                System.out.println("Added docID ["
                        +documentVectors[j].getDocID()
                        +"] to cluster [" +bestCluster +"]");
            }
            
            /* recompute centroids */
            for (j=0; j < k; j++) {
                clusters[j].add(0, findCentroid(clusters[j],
                        ((j+(rounds+2))*-1)));
            }
            
            System.out.println("Completed round "+ i);
        }
        
        return clusters;
    }
    
    /**
     * @param cluster - ArrayList of DocHash objects,
     * @param docID - document ID of centroid
     * 
     * @return - DocHash centroid of the given cluster
     * 
     * TODO: add/rem values from centroid as Vectors removed
     *  from cluster (save calc time)
     * 
     * TODO: handle normalization
     * 
     */
    public static DocHash findCentroid(ArrayList<DocHash> cluster,
            int docID) {
        
        DocHash centroid = new DocHash(docID);
        int clusterSize = cluster.size();
        
        DocHash tempVector;
        Set<String> wordSet;
        Iterator<String> wordIter;
        String index;
        
        /* centroid = add together each vector in cluster */
        for (int j=1; j < clusterSize; j++) {
            tempVector =cluster.get(j);
            wordSet = (Set)tempVector.wordIDSet();
            wordIter = (Iterator)wordSet.iterator();
            
            while (wordIter.hasNext()) {
                index =wordIter.next();
                centroid.append(index, tempVector.get(index));
            }
        }
        
        wordSet = (Set)centroid.wordIDSet();
        wordIter = (Iterator)wordSet.iterator();
        double meanValue;
        
        /* divide each centroid value by size of cluster */
        while (wordIter.hasNext()) {
            index =wordIter.next();
            meanValue =(centroid.get(index) /clusterSize);
            centroid.add(index, meanValue);
        }
        
        return centroid;
    }
    
    public static void printEval(ArrayList<DocHash>[] clusters) {
        // Read in topics as array of white space delimited Strings
        // Total topics = 135 = k =(57 ?)
        
        // Precision = # of docs in cluster that also should be / # of total docs in cluster
        // Recall = # of docs in cluster that also should be / # of total docs that should be in cluster
        
        // Average Precision = precision at each round?
        
        // Print MAP
    }
    
    private void initCentroids(ArrayList<DocHash>[] clusters,
            DocHash[] documentVectors) {
        /*
        for (length of clusters)
            read  corpus till topic i found
            add that document to cluster i
        
        */
        readReut();
        
        this.topicSet = new TreeSet();
        for (Set<String> group : this.topicArr){
            this.topicSet.addAll(group);
        }
        
        Iterator<String> topicIter = this.topicSet.iterator();
        Set<Integer> docUsed = new TreeSet();
        String tempTopic = topicIter.next();
        int clusterNum =0;
        while (topicIter.hasNext()){
            for (int j=0; j < this.topicArr.size(); j++){
                if ((this.topicArr.get(j).contains(tempTopic))
                        && !(docUsed.contains(j))){

                    tempTopic = topicIter.next();
                    docUsed.add(j);
                    clusters[clusterNum].add(0, documentVectors[j]);
                    clusterNum++;
                    if (clusterNum >= clusters.length){
                        return;
                    }
                }
            }
        } 
    }
    
    public ArrayList<Set<String>> readReut(){
        
        this.topicArr = new ArrayList();
        BufferedReader reutReadr;
        Scanner reutScannr;
        String reutLine;
        String topic;
        HashSet<String> docTopics;
        
        for (int i=0; i < 22; i++){
            try {
                reutReadr = new BufferedReader(
                            new FileReader(String.format(
                            "route/reut2-0%02d.sgm", i)));
                
                while((reutLine =reutReadr.readLine()) !=null){
                    if(reutLine.contains("<TOPICS>")){
                        reutScannr = new Scanner(reutLine);
                        reutScannr.useDelimiter("</?[CDIOPST]+>");
                        
                        docTopics = new HashSet();
                        
                        while (reutScannr.hasNext()){
                            topic =reutScannr.next();
                            if (!topic.isEmpty()){
                                docTopics.add(topic);
                                //System.out.println(topic);
                            }
                        }
                        topicArr.add(docTopics);
                    }
                }
            }
            catch(Exception e){
                return null;
            }
        }
        
        return topicArr;
    }
}
