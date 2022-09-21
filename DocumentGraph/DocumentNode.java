/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DocumentGraph;

/**
 * @file Document.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class DocumentNode extends GraphNode {
    // public final String[] contents;
    public final int graphID;
    public final int docID;
    public DocumentNode(/*final String[] contents,*/ final int graphID, final int docID) {
        // this.contents = new String[contents.length];
        // System.arraycopy(contents, 0, this.contents, 0, contents.length);
        this.graphID = graphID;
        this.docID = docID;
    }
}
