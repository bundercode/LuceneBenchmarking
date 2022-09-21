package Search;

import DocCollection.*;
/**
 * LIBS USED
 * ----------------------------
 * 
 * lucene-core
 * lucene-benchmark
 * lucene-analyzers-common
 * lucene-queries
 * lucene-queryparser
 * 
 */

/**
 * @file LuceneBenchmarking.java
 * @author smitbl07
 * @project LuceneBenchmarking
 * 
 * @section DESCRIPTION
 * Main driver class.
 */
public class LuceneBenchmarking {
    
    /**
     * Entrance point for the benchmark program.  Builds index and tests
     * queries.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        // Setup Engine
        // Build Corpus
        Menu m = new Menu();
        Corpus c = m.openMenu();
        
        // Build Engine.index
        c.makeIndex();
        
        // Test Queries
        c.getEngine().indexReader(m);

        // Measure results (Benchmark)
    }
}
