/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

/**
 * @file WordNode.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class WordNode extends GraphNode {
    public final String contents;
    public final int graphID;
    public WordNode(final String contents, final int graphID) {
        this.contents = contents;
        this.graphID = graphID;
    }
    
    @Override
    public boolean equals(Object o) {
        try {
            WordNode n = ((WordNode)o);
            if (n.contents.equals(this.contents) && n.graphID == this.graphID) {
                return true;
            }
        } catch(Exception ex) {}
        return false;
    }
}
