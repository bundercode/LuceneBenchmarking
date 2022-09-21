package Search;

import ComputeSimilarity.FindSimilarWords;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @file WordGraph.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 *
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class WordGraph extends FindSimilarWords {

    public static final int graphNormalizer = 5;

    public WordGraph() {
        super();
    }

    /**
     * Performs depth first search on the graph providing strongly related words
     * in a results array. Searched edges will have weights equal to one.
     *
     * @param word             initial vertex from which to start the search
     * @param numExpandedTerms number of terms to find
     * @param trimPoint_       point where search will not continue
     * @param depth            distance allowed from starting node
     *
     * @return results stored in a string array with length <= numExpandedTerms
     */
    public Collection<SearchNode> searchGraph(final String word, final int numExpandedTerms,
                                              final double trimPoint_, final long depth) {
        final double trimPoint = trimPoint_ / graphNormalizer;
        long d = 0;
        if (allVertexDB.inverse().get(word) == null) {
            return new ArrayList<>();
        }
        int vertexID = allVertexDB.inverse().get(word);
        PriorityQueue pq = new PriorityQueue();
        HashSet<Integer> visited = new HashSet<>();
        HashSet<SearchNode> toReturn = new HashSet<>();
        HashMap<Integer, SearchNode> stored = new HashMap<>();
        pq.add(new SearchNode(vertexID, 1, false, d));
        stored.put(vertexID, (SearchNode) pq.peek());
        while (visited.size() <= numExpandedTerms && !pq.isEmpty()) {
            int rootID = ((SearchNode) pq.peek()).id;
            if (!((SearchNode) pq.peek()).isSearchNode) {
                visited.add(rootID);
            }
            double rootWeight = ((SearchNode) pq.peek()).weight;
            d = ((SearchNode) pq.peek()).depth + 1;
            pq.poll();
            for (int node : relationshipDB.get(rootID)) {
                double compositeWeight = edgeDB.get(rootID + "-" + node);
                compositeWeight *= rootWeight / graphNormalizer;
                assert compositeWeight > 1 : "Composite weight (" + compositeWeight + ") > 1";
                if (compositeWeight >= trimPoint && depth >= d) {
                    boolean isSenseNode = allVertexDB.get(node).contains(" ");
                    SearchNode n = new SearchNode(node, compositeWeight, isSenseNode, d);
                    if (!stored.containsKey(node)) {
                        pq.add(n);
                        stored.put(node, n);
                        toReturn.add(n);// added
                    } else {
                        // get node
                        // see if it can be replaced
                        if (n.weight > stored.get(node).weight) {
                            pq.remove(stored.get(node));
                            pq.add(n);
                            toReturn.remove(stored.get(node));// added
                            toReturn.add(n);// added
                            stored.put(node, n);
                        }
                    }

                }
            }
        }
        visited.remove(vertexID); // removes initial word
        stored.remove(vertexID);
        return stored.values();
    }

    public Collection<String> graphSearchDFS(final String word, final double trimPoint, final long depth, final boolean useSenseNodes) {
        Integer vertexID = allVertexDB.inverse().get(word);
        ArrayList<String> results = new ArrayList();
        HashMap <Integer, Integer> timesVisited = new HashMap<>();
        if (vertexID != null) {
            HashMap<Integer, Double> searchResults = DFS(vertexID, trimPoint, depth, timesVisited);
            for (int x : searchResults.keySet()) {
                String[] s = allVertexDB.get(x).split(" ");
                if (s.length == 1 || useSenseNodes) {
                    results.add((searchResults.get(x)) + ""/*  + "\t" +  allVertexDB.get(x) + "\t" + timesVisited.get(x)*/);
                }
            }
        }
        
        
        return results;
    }
    
    public HashMap<Integer, Double> DFS(final int vertexID, final double trimPoint_, final long depth, HashMap<Integer, Integer> timesVisited) {
        final double trimPoint = trimPoint_;
        HashMap<Integer, Double> results = new HashMap<>();
        results.put(vertexID, 1.0);
        Queue<DFSnode> next = new LinkedList();
        next.add(new DFSnode(vertexID, 1, 0));
        timesVisited.put(vertexID, 1);
        while (next.size() > 0) {
            DFSnode wordID = next.poll();
            for (int node : relationshipDB.get(wordID.value)) {
                double edge = edgeDB.get(wordID.value + "-" + node);
                double compositeWeight = wordID.weight * ((edge > 1) ? 1 : edge);
                if (!results.containsKey(node)) {
                    if (compositeWeight > trimPoint_) {
                        results.put(node, compositeWeight);
                        if (wordID.depth != depth) {
                            next.add(new DFSnode(node, compositeWeight, wordID.depth + 1));
                            timesVisited.put(node, 1);
                        }
                    }
                } else {
                    double originalWeight = results.get(node);
                    results.remove(node);
                    compositeWeight += originalWeight;
                    assert originalWeight < compositeWeight;
                    results.put(node, compositeWeight);
                    int tv = timesVisited.get(node);
                    timesVisited.put(node, tv + 1);
                }
            }
        }
        results.remove(vertexID);
        return results;
    }

    public static String getString(final int id) {
        return allVertexDB.get(id);
    }

    public void insertDocuments(final Collection<SearchDocument> docs) {
        // TODO:  find appropriate integers for documents
        // TODO:  documents extends string
        for (SearchDocument d : docs) {

            // TODO:  add document to graph
            //
            // foreach distinct word in document; do
            //    if word exists in graph; then
            //       compute tf
            //       compute idf
            //       calculate final score
            //       add edge from word to document and document to word given score
            //    fi
            // done
        }

    }

    public Collection<SearchDocument> graphSearchDocument(final int numDocuments) {
        // search graph to find documents and place them in order in arraylist
        ArrayList<SearchDocument> result = new ArrayList<>();
        while (result.size() != numDocuments) {
            // result.add(/*getDocumentsFromGraph*/);
        }
        return result;
    }

}
