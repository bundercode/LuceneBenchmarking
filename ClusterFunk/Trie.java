package ClusterFunk;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @file Trie.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 *
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class Trie implements Serializable {

    private final TrieNode head;
    private final HashSet<String> hs;

    public Trie() {
        head = new TrieNode(' ', "");
        hs = new HashSet();
    }
    
    public void addToTrie(final String word, final int documentID) {
        String s = word.toLowerCase().replaceAll("[^a-z]", "");
        hs.add(s);
        head.addWord(s, s.toCharArray(), documentID, 0);
    }
    
    public void addToDocumentVector(DocHash[] documentVectors, final int maxDocumentID) {
        Queue<TrieNode> queue = new LinkedList();
        queue.addAll(Arrays.asList(head.children));
        while (!queue.isEmpty()) {
            TrieNode n = queue.poll();
            if (n == null) {continue;}
            if (n.numOccurances > 0) {
                Collection containingDocuments = n.inDocument.keySet();
                // System.out.println("occurances: " + n.numOccurances);
                for (int i = 0; i < maxDocumentID; i++ ) {
                    //for (int j =0; j < documentVectors.length; j++) {
                        if (containingDocuments.contains(i)) {
                            /*
                            documentVectors[i].add(String word, int count);
                            */
                            documentVectors[i].add(n.value, n.inDocument.get(i));
                        }
                    //}*/
                }
            }
            queue.addAll(Arrays.asList(n.children));
        }
        // System.out.println("Total number of words: " + hs.size());
    }
    
    public int totalNumOccurances(final String word) {
        if (!hs.contains(word)) {
            return 0;
        }
        String s = word.toLowerCase().replaceAll("[^a-z]", "");
        return head.totalNumOccurances(s.toCharArray(), 0);
    }
    
    public int occurancesIn(final String word, final int docID) {
        if (!hs.contains(word)) {
            return 0;
        }
        String s = word.toLowerCase().replaceAll("[^a-z]", "");
        return head.occurancesIn(s.toCharArray(), docID, 0);
    }
    
    public int maxOccurances(final String word, final int docID) {
        if (!hs.contains(word)) {
            return 0;
        }
        String s = word.toLowerCase().replaceAll("[^a-z]", "");
        return head.maxOccurances(s.toCharArray(), docID, 0);
    }
    
    public int numDocsContaining(final String word, final int docID) {
        if (!hs.contains(word)) {
            return 0;
        }
        String s = word.toLowerCase().replaceAll("[^a-z]", "");
        return head.numDocsContaining(s.toCharArray(), docID - 1, 0);
    }

    private static class TrieNode implements Serializable {

        private final TrieNode[] children;

        private final char c;
        
        private final String value;

        private int numOccurances;

        private final HashMap<Integer, Integer> inDocument;
        
        public TrieNode(final char c, final String value) {

            this.c = c;

            this.children = new TrieNode[26];

            numOccurances = 0;

            inDocument = new HashMap();
            
            this.value = value;

        }

        public void addWord(final String value, final char[] word, final int documentID, final int currentIndex) {

            if (word.length - 1 <= currentIndex) {
                if (null == inDocument.get(documentID)) {
                    inDocument.put(documentID, 1);
                } else {
                    inDocument.put(documentID, inDocument.get(documentID) + 1);
                }
                numOccurances++;
                // System.out.print("added: ");
                // for (char c : word) {
                    // System.out.print(c);
                // }
                // System.out.println();
                return;
            }
            
            if (children[word[currentIndex] -'a'] == null) {
                children[word[currentIndex] -'a'] = new TrieNode(word[currentIndex], value);
            }
            
            children[word[currentIndex] - 'a'].addWord(value, word, documentID, currentIndex + 1);

        }
        
        public int totalNumOccurances(final char[] word, final int currentIndex) {
            if (word.length - 1 <= currentIndex) {
                return this.numOccurances;
            }
            return this.children[word[currentIndex] - 'a'].totalNumOccurances(word, currentIndex + 1);
        }
        
        public int occurancesIn(final char[] word, final int docID, final int currentIndex) {
            if (word.length - 1 <= currentIndex) {
                if (!inDocument.containsKey(docID)) {
                    return 0;
                }
                return this.inDocument.get(docID);
            }
            return this.children[word[currentIndex] - 'a'].occurancesIn(word, docID, currentIndex + 1);
        }
        
        public int maxOccurances(final char[] word, final int docID, final int currentIndex) {
            if (word.length - 1 <= currentIndex) {
                if (!inDocument.containsKey(docID)) {
                    return 0;
                }
                return Collections.max(this.inDocument.values());
            }
            return this.children[word[currentIndex] - 'a'].maxOccurances(word, docID, currentIndex + 1);
        }
        
        public int numDocsContaining(final char[] word, final int docID, final int currentIndex) {
            if (word.length - 1 <= currentIndex) {
                if (!inDocument.containsKey(docID)) {
                    return 0;
                }
                return this.inDocument.size();
            }
            return this.children[word[currentIndex] - 'a'].numDocsContaining(word, docID, currentIndex + 1);
        }

    }

}
