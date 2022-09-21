/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

/**
 * @file SenseNode.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class SenseNode extends GraphNode {
    // public final String contents;
    public final int graphID;
    public SenseNode(/*final String contents, */final int graphID) {
        // this.contents = contents;
        this.graphID = graphID;
    }
}
