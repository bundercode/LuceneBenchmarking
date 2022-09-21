/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

/**
 * @file QueryNode.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class QueryNode extends GraphNode {
    // public final String[] contents;
    public final int graphID;
    public final int queryID;
    public QueryNode(/*final String[] contents, */final int graphID, final int queryID) {
        // this.contents = new String[contents.length];
        // System.arraycopy(contents, 0, this.contents, 0, contents.length);
        this.graphID = graphID;
        this.queryID = queryID;
    }
}
