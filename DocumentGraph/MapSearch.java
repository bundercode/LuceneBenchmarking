/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

import ClusterFunk.DocParser_Cran;
import ClusterFunk.DocParser_MED;
import ClusterFunk.DocParser_TIME;
import ClusterFunk.Parser;
import ClusterFunk.QueParser_MED;
import ClusterFunk.QueParser_TIME;
import ClusterFunk.QueryParser_Cluster;
import ClusterFunk.Trie;
import DocCollection.CollectionIO.RelevantDocumentReader;
import DocCollection.CollectionIO.RelevantDocumentReader_Cranfield;
import DocCollection.CollectionIO.RelevantDocumentReader_MED;
import DocCollection.CollectionIO.RelevantDocumentReader_TIME;
import java.util.Collection;
import java.util.HashSet;

/**
 * @file MapSearch.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class MapSearch {
    
    /*
        Stanford MAP info: 
            http://nlp.stanford.edu/IR-book/html/htmledition/evaluation-of-ranked-retrieval-results-1.html
        Wikipedia MAP info: 
            https://en.wikipedia.org/wiki/Information_retrieval#Mean_average_precision
        
    
        MAP(Queries Q) {
            relevantDocs = documents relevant to q;
            docsBeforeRel = # number of total docs compared to q
        
            for (q=1; q <= Q.size; q++) {
                for (r=1; r <= relevantDocs; r++) {
                    do {
                        if (isRelevant(doc_i, q) {
                            count++;
                            if (count >= r) break;
                        }
                        docsBeforeRel++;
                    } while (the next relevance level has NOT been reached);
                
                    precision = r / docsBeforeRel;
                    averagePrecision += precision;
                }
                averagePrecision /= relevantDocs;
                mapScore += averagePrecision;
            }
            mapScore /= Q.size;
        
            return mapScore;
        }               
    */
    final static Collection[] graphQueries = new Collection[225];
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("%Usage: java MapSearch [cranfield | med | time]");
            System.exit(0);
        }
        Parser documentParser = null;
        Parser qParser = null;
        HashSet[] documentWords = null;
        HashSet[] queryWords = null;
        RelevantDocumentReader rdr = null;
        int relevanceLevel = 1;
        // if cran
        switch(args[0]) {
            case "cranfield" :
                documentParser = new DocParser_Cran();
                qParser = new QueryParser_Cluster();
                documentWords = new HashSet[1400];
                queryWords = new HashSet[225];
                relevanceLevel = 4;
                rdr = new RelevantDocumentReader_Cranfield();
                break;
            case "med" :
                documentParser = new DocParser_MED();
                qParser = new QueParser_MED();
                documentWords = new HashSet[1033];
                queryWords = new HashSet[30];
                relevanceLevel = 1;
                rdr = new RelevantDocumentReader_MED();
                break;
            case "time" :
                documentParser = new DocParser_TIME();
                qParser = new QueParser_TIME();
                documentWords = new HashSet[423];
                queryWords = new HashSet[83];
                relevanceLevel = 1;
                rdr = new RelevantDocumentReader_TIME();
                break;
        }
        // if time
        // if med
        Trie documentTrie = documentParser.buildTrie(documentWords);
        Trie queryTrie = qParser.buildTrie(queryWords);
        DocumentGraph dg = new DocumentGraph();
        for (int i = 0; i < documentWords.length; i++) {
           // System.out.println("error in mapscore docs: "+i);
            dg.insertDocument(documentWords[i], i, documentTrie);
        }
        for (int i = 0; i < queryWords.length; i++) {
           // System.out.println("error in mapscore que: "+i);
            dg.insertQuery(queryWords[i], i, queryTrie);
        }
        rdr.read();
        // note for changes on comparison to actual results
        double[] mapScores = new double[4];
        
        /* for each relevance level */
        for (int i = 1; i <= relevanceLevel; i++) {
            double mapScore = 0;
            int j = 0;
            
            /* for each query */
            for (int k = 1; j < queryWords.length; j++, k++) {
                double averagePrecision = 0;
                long y = System.currentTimeMillis();
                int relevantDocs = rdr.totalRelevantDocuments(j, i);
                System.out.println("Query " + (k) + ": " + relevantDocs);
                
                /* for each relevant document related to query k (j) */
                for (int l = 1; l <= relevantDocs; l++) {
                    int count = 0;
                    int docsBeforeRel = 1;

                    if (graphQueries[j] == null) {
                        long x = System.currentTimeMillis();
                        graphQueries[j] = dg.searchByQuery(j);
                        System.out.println(System.currentTimeMillis() - x);
                    }
                    Collection<Integer> c = graphQueries[j];
                    // System.out.println("c size: " + c.size());
                    /* Should run until l relevant documents retrieved */
                    for (int docID : c) {
                        if (rdr.docIsRelevant(j, docID, i)) {
                            count++;
                            if (count >= l /*|| count >= j*/) {
                                break;
                            }
                        }
                        docsBeforeRel++;
                    }
                    
                    //double precision = Math.min(1, l) / 1.0 / docsBeforeRel;
                    double precision = l / 1.0 / Math.max(docsBeforeRel, l);
                    if ((c.size() + 1) < documentWords.length) {
                        System.out.println("c is too small: setting precision to zero: " + c.size());
                        System.exit(-1);
                        precision = 0;
                    }
                    
                    // recall ?

                    // print precision and recall
                    System.out.println("\t" + j + ": " + precision);

                    // save precision for average precision
                    averagePrecision += precision;
                }
                // pull out documents
                // if relevant at level i
                // get equation to calculate map score
                System.out.println();
                if (relevantDocs != 0) {
                    averagePrecision /= relevantDocs;
                }
                
                // print ap for this query
                if (averagePrecision > 1) {
                    System.out.println("cannot be greater than 1");
                    System.exit(-1);
                }
                System.out.println("Average Precision: " + averagePrecision);
                System.out.println();
                System.out.println("Time: " + (System.currentTimeMillis() - y));
                // save map for this query
                mapScore += averagePrecision;
            }
            mapScore /= j;
            
            // print map score
            System.out.println();
            System.out.println("MAP SCORE: " + mapScore);
            mapScores[i-1] = mapScore;
        }
                // print average precisions chart
        // System.out.println(this.scoreKeepr.toString(qList.size()));
        
        System.out.println("\n****************************************\n MAP SCORES");
        for(int lvl=0; lvl < relevanceLevel; lvl++) {
            System.out.println("Level:1-" + (lvl+1) +"   Score:" + mapScores[lvl]);
        }
    }
}
