package Search;

import DocCollection.CollectionIO.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;

/**
 * @file Engine.java
 * @author smitbl07
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Contains main loop of the benchmarking application.  Handles reading from the
 * index as well as computing average precision scores for each query read.
 */
public class Engine {

    private final StandardAnalyzer stAnalyzer;
    private final IndexWriterConfig iwConfig;
    private final IndexWriter iWriter;
    private final SimpleFSDirectory iDir;
    private final File iFile;
    // public static final int numRetrieved = 100;
    private final double[] mapScores = new double[4];
    private final Benchmark scoreKeepr = new Benchmark();

    /**
     * Default constructor for Engine.  Initializes instance variables.  Sets 
     * configuration versions.
     * 
     * @throws IOException from iFile
     */
    public Engine() throws IOException {
        this.stAnalyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
        this.iwConfig = new IndexWriterConfig(Version.LUCENE_4_10_0, stAnalyzer);
        this.iFile = new File(StringHash.factory().get("indexFile"));
        this.iDir = new SimpleFSDirectory(this.iFile);
        this.iWriter = new IndexWriter(this.iDir, this.iwConfig);
    }

    /**
     * Accessor method for writer to index.
     * 
     * @return writer to index
     */
    public IndexWriter getIndex() {
        return this.iWriter;
    }
    
    /**
     * Main processing loop for the program.  Reads from the created index for
     * each query and saves average precisions for printing afterward.
     * 
     * @throws Exception from iReader
     */
    public void indexReader(final Menu m) throws Exception {
        /*IndexReader iReader = DirectoryReader.open(this.iDir);
        IndexSearcher iSearch = new IndexSearcher(iReader);
        AVP avp = new AVP();
        for (int i = 10; i <= numRetrieved; i += 10) {
            Map map = new Map();
            map.read();
            Queries que = new Queries();
            Query x = que.makeQuer();
            int queryNum = 0;
            while (x != null) {
                TopDocs top = iSearch.search(x, i);
                    // System.out.println(x);
                // System.out.println(top.totalHits);
                for (ScoreDoc d : top.scoreDocs) {
                    Document result = iSearch.doc(d.doc);
                    try {
                        map.checkDoc(queryNum, Integer.parseInt(result.get(StringHash.factory().get("start"))), i);
                    } catch (Exception ex) {
                        System.out.println(result.get(StringHash.factory().get("start")));
                        // System.exit(0);
                    }
                    // System.out.println("Title: " + result.get(StringHash.factory().get("title")) + " Score: " + d.score);
                }
                x = que.makeQuer();
                queryNum++;
            }
            System.out.println("DOCS RETRIEVED: " + i);
            map.print();
            map.addAVPS(avp);

        }
        avp.printAVP();
        System.out.println("Map Score: " + avp.mapScore());
        // System.out.println("Batman!");*/
        
        /**
         * for i <- 1 to 4 do
         * // i is maximum value for relevance in cranquel
         *      mapSave <- 0
         *      for each query do
         *          relevantDocs <- query's relevant documents from cranquel for value i
         *          precisionSave <- 0
         *          for j <- 0 to relevantDocs do
         *              print precision and recall at this point
         *              precisionSave <- precisionSave + precision
         *          done
         *          ap <- precisionSave / relevantDocs
         *          map <- map + ap
         *          print ap for this query
         *      done
         *      map <- mapSave / numQueries
         *      print map
         * done
         */

        IndexReader iReader = DirectoryReader.open(this.iDir);
        IndexSearcher iSearch = new IndexSearcher(iReader);
        // perhaps make searcher class?
        int documentsSearched = iReader.numDocs();
        
        RelevantDocumentReader relevanceReader = m.getReleventDocumentReader();
        relevanceReader.read();
        // FindSimilarWords find = new FindSimilarWords();
            // ComputeSimilarity compare=new ComputeSimilarity ();
            
            Queries que = m.getQueries();
           ArrayList<Query> qList = que.makeNormalQueries(m.getExpandQueries(), m.getUseSenseNodesQueries(), m.getTrimPointQueries(), m.getMaxDepthQueries());
           
        int numRelevantDocs = m.getNumRelevantDocs();
        for (int i = 1; i <= numRelevantDocs; i++) {
            // create map score
            double mapScore = 0.0;
            
          //  Queries que = new Queries(find,compare);
            //Query x = que.makeQuer();
            int queryNum = 0;
            
            for(Query x:qList){
            //while (null != x) {
                int relevantDocs = relevanceReader.totalRelevantDocuments(queryNum, i);
                System.out.println(relevantDocs);
                
                // create average precision score
                double averagePrecision = 0.0;
                
                System.out.println("Query " + (queryNum + 1) + ":");
                
                
                for (int j = 1; j <= relevantDocs; j++) {
                    // num relevent greater than num searched
                    
                    // expand topdocs until j relevant ones are found
                    
                    TopDocs top = iSearch.search(x, documentsSearched);
                    //while (true) {
                        int count = 0;
                        int docsBeforeRel = 1;
                        
                        for (ScoreDoc d : top.scoreDocs) {
                            Document result = iSearch.doc(d.doc);
                            if (relevanceReader.docIsRelevant(queryNum,
                                   Integer.parseInt(result.get(
                                      StringHash.factory().get("start"))), i)) {
                                count++;
                                if (count >= j) {
                                    break;
                                }
                            }
                            docsBeforeRel++;
                        }
                        //documentsSearched+=100;
                        //if (documentsSearched > 1400) {
                            //System.out.println("Couldn't find it...");
                            //break;
                        //}
                        //top = iSearch.search(x, documentsSearched);
                    //}
                    
                    // precision j / 1.0 / documentsSearched
                    double precision = j / 1.0 / docsBeforeRel;

                    // recall ?
                    
                    // print precision and recall
                    System.out.println("\t" + j + ": " + precision);
                    
                    // save precision for average precision
                    averagePrecision += precision;
                }
                System.out.println();
                
                // finalize average precision score
                if (relevantDocs != 0) {
                    averagePrecision /= relevantDocs;
                }
                
                // print ap for this query
                System.out.println("Average Precision: " + averagePrecision);
                System.out.println();
                this.scoreKeepr.addAveP(queryNum, i-1, averagePrecision);
                
                // save map for this query
                mapScore += averagePrecision;
                
                queryNum++;
            }
            
            // finalize map score
            mapScore /= queryNum;
            
            // print map score
            System.out.println();
            System.out.println("MAP SCORE: " + mapScore);
            this.mapScores[i-1] = mapScore;
        }
        // print average precisions chart
        System.out.println(this.scoreKeepr.toString(qList.size()));
        
        System.out.println("\n****************************************\n MAP SCORES");
        for(int lvl=0; lvl < numRelevantDocs; lvl++){
            System.out.println("Level:1-" +(lvl+1) +"   Score:" + this.mapScores[lvl]);
        }
    }

    /**
     * Determines if the index exists using try-catch.  Assumes an IOException
     * occurs because the index does not exist.
     * 
     * @return true if index exists, false if it does not exist
     */
    public boolean indexExists() {
        try {
            DirectoryReader.open(this.iDir);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
