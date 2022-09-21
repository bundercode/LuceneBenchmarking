# LuceneBenchmarking
LuceneBenchmarking

By using Apache’s Lucene project and sets of standard document collection benchmarks, we will be evaluating several search algorithms to determine which will produce the best results when combined with weight given by a semantic graph.  Each algorithm will be modified to include results which relate not only to the given search queries, but also results for terms which are similar to these queries based on their meaning.  In doing so, we hope to provide more precise results, based not just on the query terms themselves, but on the users actual information need.
The following is a brief description of some of the initial algorithms we will be testing with:
    
    • Expand the query to include terms from the graph which are semantically similar to the query terms.  In tf-idf, this would be expanding the domain (q) of terms when computing a document score.

    • Document scoring would be achieved by computing relationships between search terms and a graph of related documents.  This would require documents in a collection to be organized in a graph, similar to the term graph in the 1st algorithm.

    • Expand the index of terms for each document to include terms from the graph which are semantically similar.  Most of the work would then be done during the indexing stage as opposed to the search stage.
      
We will be tweaking and testing these and other search algorithms to find the most effective way to search a document collection with queries weighted by a semantic graph.
If time allows, we hope to use what we have learned to possibly create a browser plugin which would bookmark documents of interest based on queries.
    
    • Apache’s Lucene
    • Collections of documents for benchmarking
    • Netbeans IDE
    • Java Programming Experience
    • Understanding of algorithms
    • Basic understanding of graph theory and linear algebra 
