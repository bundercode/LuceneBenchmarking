/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Search;

/**
 * @file DFSnode.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 * 
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class DFSnode {
    public final int value;
    public final double weight;
    public final long depth;
    public DFSnode(final int value, final double weight, final long depth) {
        this.value = value;
        this.weight = weight;
        this.depth = depth;
    }
}
