package Search;

import java.text.DecimalFormat;

/**
 * @file SearchNode.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 */
public class SearchNode implements Comparable {
    public final boolean isSearchNode;
    public final double weight;
    public final int id;
    public final long depth;
    private static final DecimalFormat df = new DecimalFormat("#.000000000000000000");
    public SearchNode(final int id, final double weight, final boolean isSearchNode, final long depth) {
        this.id = id;
        this.weight = weight;
        this.isSearchNode = isSearchNode;
        this.depth = depth;
    }
    
    @Override
    public int compareTo(Object o) {
        SearchNode sn = (SearchNode)o;
        // System.out.println(weight + " " + sn.weight);
        if (weight > sn.weight) {
            return -1;
        }
        if (weight < sn.weight) {
            return 1;
        }
        /* if (depth < sn.depth) {
            return -1;
        }
        if (depth > sn.depth) {
            return 1;
        } */
        return 0;
    }
    
    @Override
    public String toString() {
        return (df.format(weight*WordGraph.graphNormalizer)) + " " + WordGraph.getString(id);
    }
    
    public String getWord() {
        return WordGraph.getString(id);
    }
    
    @Override
    public boolean equals(Object o) {
        try {
            SearchNode sn = (SearchNode)o;
            return sn.id == this.id;
        } catch(Exception ex) {return false;}
    }
}
