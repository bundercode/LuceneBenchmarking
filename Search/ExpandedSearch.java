package Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @file ExpandedSearch.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Utility class used to interact with the graph search in the ComputeSimilarity
 * package.
 */
public class ExpandedSearch {
    
    private static final WordGraph find = new WordGraph();
    
    /**
     * Utility to interact with the graph to extend Documents and Queries.
     * Searches the graph at search depth numExpandedTerms.
     * 
     * @param terms terms to expand
     * @param numExpandedTerms depth to search the graph
     * @param includeSenseNodes true if result should include sense nodes
     * @param cutOffPoint defines the minimum level (inclusive) of relevance desired
     * @return string containing expanded and original terms
     */
    public static String expandTerms(final String terms, final int numExpandedTerms,
                                     final boolean includeSenseNodes, final double cutOffPoint,
                                     final long depth) {
        StringBuffer buffer = new StringBuffer("");
        //buffer.append(terms.replaceAll(/*"[^\\w\\s]"*/"[^a-zA-Z\\s]", ""));
      
        HashSet<String> termSet = new HashSet<>(Arrays.asList(terms.split(" ")));
        buffer.append(terms);
        buffer.append(" ");
        ArrayList<SearchNode> searchNodes = new ArrayList<>();
        ArrayList<String> result = new ArrayList();
        for (String s : termSet) {
            // searchNodes.addAll(find.searchGraph(s, 100, cutOffPoint, depth));
            result.addAll(find.graphSearchDFS(s, cutOffPoint, depth, includeSenseNodes));
        }
        // Collections.sort(searchNodes);
        termSet.addAll(result);
        /*int i = 0;
        for (SearchNode n : searchNodes) {
            if (numExpandedTerms <= i) {
                break;
            }
            if (!n.isSearchNode || includeSenseNodes) {
                buffer.append(n.getWord().replaceAll("[^a-zA-Z-]", ""));
                buffer.append(" ");
                i++;
            }
        }*/
        return buffer.toString();
    }
}
