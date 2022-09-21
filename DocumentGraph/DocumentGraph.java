/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

import ClusterFunk.Trie;
import ComputeSimilarity.FindSimilarWords;
import Search.SearchNode;
import Search.WordGraph;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * @file DocumentGraph.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class DocumentGraph extends FindSimilarWords {
    private final HashBiMap<Integer, GraphNode> innerVertexDB;
    private final HashBiMap<Integer, Integer> docIDToNode;
    private final HashMap<Integer, Integer> queryIDToNode;
    private final HashMap<String, Double> innerEdgeDB;
    // private final HashMap<Integer, HashMap<Integer, Double>> docEdge;
    private int maxMapID;
    public DocumentGraph() {
        super();
        // add to relationshipDB as we go
        // add to edgedb as we go
        // extend allvertexdb to contain documents and queries
        innerVertexDB = HashBiMap.create();
        docIDToNode = HashBiMap.create();
        queryIDToNode = new HashMap();
        innerEdgeDB = new HashMap();
        Collection<Integer> x = allVertexDB.keySet();
        this.maxMapID = Integer.MIN_VALUE;
        for (Integer i : x) {
            String s = allVertexDB.get(i);
            if (s.contains(" ")) {
                innerVertexDB.put(i, new SenseNode(i));
            } else {
                innerVertexDB.put(i, new WordNode(s, i));
            }
        }
        Collection<String> e = edgeDB.keySet();
        for (String s : e) {
            innerEdgeDB.put(s, computeMinMax(0, 1, edgeDB.get(s)));
        }
        // this.maxMapID = 0;
        // docEdge = new HashMap();
    }
    
    public double distanceBetweenNodesTotal(final int doc1, final int doc2) {
        int startDoc = docIDToNode.get(doc1);
        int endDoc = docIDToNode.get(doc2);
        PriorityQueue next = new PriorityQueue();
        next.add(new SearchNode(startDoc, 1, false, 0));
        HashSet<Integer> closed = new HashSet();
        double totalWeight = 0;
        double lastPulled = 1;
        while (!next.isEmpty() && lastPulled > .001) {
            SearchNode sn = (SearchNode)next.peek();
            next.poll();
            if (sn.id == endDoc) {
                // return total cost
                totalWeight += sn.weight;
            }
            lastPulled = sn.weight;
            if (!closed.contains(sn.id)) {
                for (int i : relationshipDB.get(sn.id)) {
                    try {
                        next.add(new SearchNode(i, innerEdgeDB.get(sn.id + "-" + i) * sn.weight / Search.WordGraph.graphNormalizer, false, 0));
                    } catch(Exception ex) {}
                }
                closed.add(sn.id);
            }
        }
        return totalWeight;
    }
    
    public double distanceBetweenNodesSmall(final int doc1, final int doc2) {
        int startDoc = docIDToNode.get(doc1);
        int endDoc = docIDToNode.get(doc2);
        PriorityQueue next = new PriorityQueue();
        next.add(new SearchNode(startDoc, 1, false, 0));
        HashSet<Integer> closed = new HashSet();
        double lastWeight = 1;
        while (true) {
            SearchNode sn = (SearchNode)next.peek();
            if (sn == null) {return lastWeight;} // could be a problem?
            lastWeight = sn.weight;
            next.poll();
            if (sn.id == endDoc) {
                // return total cost
                return sn.weight;
            }
            if (!closed.contains(sn.id)) {
                for (int i : relationshipDB.get(sn.id)) {
                    try {
                        /* if (sn.depth <= 2) {
                            next.add(new SearchNode(i, innerEdgeDB.get(sn.id + "-" + i) * sn.weight, false, sn.depth + 1));
                        } else {
                            next.add(new SearchNode(i, .8 * innerEdgeDB.get(sn.id + "-" + i) * sn.weight, false, sn.depth + 1));
                        } */
                        if (sn.depth <= 2) {
                            next.add(new SearchNode(i, edgeDB.get(sn.id + "-" + i) * sn.weight / WordGraph.graphNormalizer, false, sn.depth + 1));
                        } else {
                            next.add(new SearchNode(i, edgeDB.get(sn.id + "-" + i) * sn.weight / WordGraph.graphNormalizer / 20 / sn.depth, false, sn.depth + 1));
                        }
                    } catch(Exception ex) {}
                }
                closed.add(sn.id);
            }
        }
    }
    
    public HashMap<Integer, Double> searchByDocument(final int docID) {
        int startDoc = docIDToNode.get(docID);
        HashMap<Integer, Double> result = new HashMap();
        PriorityQueue next = new PriorityQueue();
        next.add(new SearchNode(startDoc, 1, false, 0));
        HashSet<Integer> closed = new HashSet();
        while (!next.isEmpty() && result.size() != docIDToNode.size()) {
            SearchNode sn = (SearchNode)next.peek();
            next.poll();
            int nodeID = sn.id;
            if (!closed.contains(nodeID)) {
                GraphNode gn = innerVertexDB.get(nodeID);
                try {
                    DocumentNode dn = (DocumentNode)gn;
                    if (!result.containsKey(dn.docID)) {
                        result.put(dn.docID, sn.weight);
                    } else {
                        double weight = result.get(dn.docID);
                        result.put(dn.docID, weight + sn.weight);
                    }
                } catch(Exception ex) {}
                for (int i : relationshipDB.get(nodeID)) {
                    try {
                        next.add(new SearchNode(i, edgeDB.get(nodeID + "-" + i) * sn.weight / WordGraph.graphNormalizer, false, 0));
                    } catch(Exception ex) {}
                }
                closed.add(nodeID);
            }
        }
        return result;
    }
    
    public Collection<Integer> searchByQuery(final int queryID) {
        int vertexID = queryIDToNode.get(queryID);
        ArrayList<Integer> result = new ArrayList();
        ArrayList<SearchNode> resultsAsSearchNode = new ArrayList();
        PriorityQueue next = new PriorityQueue();
        next.add(new SearchNode(vertexID, 1, false, 0));
        HashSet<Integer> closed = new HashSet();
        while (!next.isEmpty() && result.size() != docIDToNode.size()) {
            SearchNode sn = (SearchNode)next.peek();
            next.poll();
            int nodeID = sn.id;
            if (!closed.contains(nodeID)) {
                if (docIDToNode.containsValue((Integer)nodeID)) {
                    resultsAsSearchNode.add(sn);
                } else {
                    for (int i : relationshipDB.get(nodeID)) {
                        try {
                            if (sn.depth <= 2) {
                                next.add(new SearchNode(i, innerEdgeDB.get(nodeID + "-" + i) * sn.weight, false, sn.depth + 1));
                            } else {
                                next.add(new SearchNode(i, .8 * innerEdgeDB.get(nodeID + "-" + i) * sn.weight, false, sn.depth + 1));
                            }
                        }catch(Exception e) {}
                    }
                }
                closed.add(nodeID);
            } else {
                int i = resultsAsSearchNode.indexOf(sn);
                if (i >= 0) {
                    SearchNode old = resultsAsSearchNode.get(i);
                    resultsAsSearchNode.add(new SearchNode(old.id, old.weight + sn.weight, false, Math.min(old.depth, sn.depth)));
                    resultsAsSearchNode.remove(i);
                }
            }
        }
        Collections.sort(resultsAsSearchNode);
        for (SearchNode sn : resultsAsSearchNode) {
            result.add(docIDToNode.inverse().get(sn.id));
        }
        return result;
    }
    
    // linear implementation
    /* public Collection<Integer> searchByQuery(final int queryID) {
        int vertexID = queryIDToNode.get(queryID);
        ArrayList<Integer> result = new ArrayList();
        ArrayList<SearchNode> resultsAsSearchNode = new ArrayList();
        PriorityQueue next = new PriorityQueue();
        next.add(new SearchNode(vertexID, 1, false, 0));
        HashSet<Integer> closed = new HashSet();
        while (!next.isEmpty() && result.size() != docIDToNode.size()) {
            SearchNode sn = (SearchNode)next.peek();
            next.poll();
            int nodeID = sn.id;
            if (!closed.contains(nodeID)) {
                if (docIDToNode.containsValue((Integer)nodeID)) {
                    resultsAsSearchNode.add(sn);
                } else {
                    for (int i : relationshipDB.get(nodeID)) {
                        try {
                            if (sn.depth <= 2) {
                                next.add(new SearchNode(i, edgeDB.get(nodeID + "-" + i) * sn.weight / WordGraph.graphNormalizer, false, sn.depth + 1));
                            } else {
                                next.add(new SearchNode(i, edgeDB.get(nodeID + "-" + i) * sn.weight / WordGraph.graphNormalizer / 20 / sn.depth, false, sn.depth + 1));
                            }
                        }catch(Exception e) {}
                    }
                }
                closed.add(nodeID);
            } else {
                int i = resultsAsSearchNode.indexOf(sn);
                if (i >= 0) {
                    SearchNode old = resultsAsSearchNode.get(i);
                    resultsAsSearchNode.set(i, new SearchNode(old.id, old.weight + sn.weight, false, Math.min(old.depth, sn.depth)));
                }
            }
        }
        Collections.sort(resultsAsSearchNode);
        for (SearchNode sn : resultsAsSearchNode) {
            result.add(docIDToNode.inverse().get(sn.id));
        }
        return result;
    } */
    
    public void insertDocument(final Collection<String> wordsInDocument, final int docID, final Trie docTrie) {
        while (innerVertexDB.containsKey(this.maxMapID)) {
            this.maxMapID++;
        }
        // HashSet<String> wid = new HashSet<>(wordsInDocument);
        System.out.println("words in document: " + wordsInDocument.size());
        DocumentNode dn = new DocumentNode(this.maxMapID, docID + 1);
        innerVertexDB.put(this.maxMapID, dn);
        docIDToNode.put(docID + 1, this.maxMapID);
        ArrayList<Integer> relatedNodes = new ArrayList();
        // determine edges
        // for words in set
        for (String s : wordsInDocument) {
            // add edge based on numOccurances / number of total occurances
            try {
                int sNode = allVertexDB.inverse().get(s);
                edgeDB.put(this.maxMapID + "-" + sNode, .02 * docTrie.occurancesIn(s, docID) * 1.0 / docTrie.maxOccurances(s, docID));
                edgeDB.put(sNode + "-" + this.maxMapID, .1 * docTrie.occurancesIn(s, docID) * 1.0 / docTrie.maxOccurances(s, docID));
                // innerEdgeDB.put(this.maxMapID + "-" + sNode, computeMinMax(0, .2, docTrie.occurancesIn(s, docID) * 1.0 / docTrie.maxOccurances(s, docID)));
                // innerEdgeDB.put(sNode + "-" + this.maxMapID, computeMinMax(0, 1, docTrie.occurancesIn(s, docID) * 1.0 / docTrie.maxOccurances(s, docID)));
                relationshipDB.get(sNode).add(this.maxMapID);
                relatedNodes.add(sNode);
            } catch(Exception ex) {}
        }
        relationshipDB.put(this.maxMapID, relatedNodes);
        System.out.println("docID: " + docID);
    }
    
    public void insertQuery(final Collection<String> wordsInDocument, final int queryID, final Trie queryTrie) {
        /* if (wordsInDocument == null || wordsInDocument.size() == 0) {
            return;
        } */
        while (innerVertexDB.containsKey(this.maxMapID)) {
            this.maxMapID++;
        }
        // HashSet<String> wid = new HashSet<>(wordsInDocument);
        QueryNode qn = new QueryNode(this.maxMapID, queryID);
        innerVertexDB.put(this.maxMapID, qn);
        queryIDToNode.put(queryID, this.maxMapID);
        ArrayList<Integer> relatedNodes = new ArrayList();
        // determine edges
        for (String s : wordsInDocument) {
            try {
                int sNode = allVertexDB.inverse().get(s);
                // number of occurances divided by number of unique words in document ... done 0.025
                // number of occurances divided by number of times word shows up in collection ... done 0.0249
                // number of occurances divided by total number of words in the document ... done 0.0236
                // number of occurances divided by total number of words in document divided by times word shows up in collection ... 0.0238
                // number of words in document divided by number of occurances divided by number of times word shows up in the collection ... 0.02506
                // number of unique words in document divided by number of occurances divided by number of times word shows up in the collection
                // number of times word shows up in collection divided by number of occurances divided by number of words in the document ... 0.025060964
                // number of times word shows up in collection divided by number of occurances divided by number of unique words in the document ... 0.0256
                // number of words in document divided by number of unique words in document divided by number of times word occurs ... 0.025061650848275135
                // number of unique words in document * number of times word occurs in document / number of total words in the document ... bad
                // number of words in document times number of times word shows up in collection divided by occurances in doc divided by unique words in doc ... 0.02573875490170148
                edgeDB.put(this.maxMapID + "-" + sNode, .02 * queryTrie.occurancesIn(s, queryID) * 1.0 / queryTrie.maxOccurances(s, queryID));
                edgeDB.put(sNode + "-" + this.maxMapID, .1 * queryTrie.occurancesIn(s, queryID) * 1.0 / queryTrie.maxOccurances(s, queryID));
                // innerEdgeDB.put(this.maxMapID + "-" + sNode, computeMinMax(0, .2, queryTrie.occurancesIn(s, queryID) * 1.0 / queryTrie.maxOccurances(s, queryID)));
                // innerEdgeDB.put(sNode + "-" + this.maxMapID, computeMinMax(0, 1, queryTrie.occurancesIn(s, queryID) * 1.0 / queryTrie.maxOccurances(s, queryID)));
                relationshipDB.get(sNode).add(this.maxMapID);
                relatedNodes.add(sNode);
            } catch(Exception ex){}
        }
        relationshipDB.put(this.maxMapID, relatedNodes);
        System.out.println("queryID: " + queryID + " documentSize: " + wordsInDocument.size());
    }
    
    public void addCentroid(final HashMap<String, Double> centroidValues, final int centroidID, final Trie docTrie) {
        while (innerVertexDB.containsKey(this.maxMapID)) {
            this.maxMapID++;
        }
        DocumentNode qn = new DocumentNode(this.maxMapID, centroidID);
        innerVertexDB.put(this.maxMapID, qn);
        docIDToNode.put(centroidID, this.maxMapID);
        ArrayList<Integer> relatedNodes = new ArrayList();
        // determine edges
        Collection<String> centroidKeys = centroidValues.keySet();
        for (String s : centroidKeys) {
            try {
                int sNode = allVertexDB.inverse().get(s);
                innerEdgeDB.put(this.maxMapID + "-" + sNode, .02 * centroidValues.get(s) * 1.0 / docTrie.totalNumOccurances(s));
                innerEdgeDB.put(sNode + "-" + this.maxMapID, .1 * centroidValues.get(s) * 1.0 / docTrie.totalNumOccurances(s));
                relationshipDB.get(sNode).add(this.maxMapID);
                relatedNodes.add(sNode);
            } catch(Exception ex){}
        }
        relationshipDB.put(this.maxMapID, relatedNodes);
    }
    
    public static double computeMinMax(final double min, final double max, final double ratio) {
        return min + (max-min) * (-1.0 / Math.log(ratio));
    }
    
    public static double determineIndirectWeight(final double distance) {
        return 0.8 * computeMinMax(0, 1, distance)/* / .1 */;
    }
    
}
