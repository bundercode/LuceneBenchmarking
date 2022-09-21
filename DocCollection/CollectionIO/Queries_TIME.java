/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DocCollection.CollectionIO;

import static DocCollection.CollectionIO.Queries.sh;
import Search.ExpandedSearch;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 *
 * @author Aaron
 */
 
public class Queries_TIME extends Queries{
    private final File file;
   
    public Queries_TIME() {
        super();
        file = new File("time/TIME.QUE");
       
        
        // changed to assertion
       // assert file.exists() : (sh.get("queryFile") + " is not a vaild file or does not exist!!!!");
    }

    /**
     * oneQuery is a method that with separates the a query from all irrelevant
     * data in a file and returns a query
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    protected String oneQuery() throws FileNotFoundException {
        // Scanner var
       return "void in TIME";
    }
    public ArrayList<Query> makeNormalQueries(final boolean expand, final boolean useSenseNodes,
                                              final double trimPoint, final long depth) throws ParseException, QueryNodeException, Exception {
        ArrayList<Query> queList = new ArrayList<Query>();
        StandardQueryParser qp = new StandardQueryParser();
            qp.setAnalyzer(
                    new StopAnalyzer(Version.LUCENE_4_10_0,
                                     StopFilter.makeStopSet(analyzerList)));
        try {
            int first=0;
            Scanner inFile=new Scanner(file);
            String query="";
            while(inFile.hasNextLine()){
                String line=inFile.nextLine();
                if(line.contains("*FIND")||line.contains("*STOP")){
                    if(first>0){
                         Query stop=qp.parse(query, " ");
                         String[] array = stop.toString().split(" ");
                         if (expand) {
                            query = ExpandedSearch.expandTerms(stop.toString().replace(":", " "), 1024/array.length, useSenseNodes, trimPoint, depth);
                         }
                         Query q = qp.parse(query, sh.get("body"));
                         queList.add(q);
                         query="";
                    }
                }else{
                   query=query+" "+line; 
                }
                first++;
                
            }
            
           return queList;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch blo
            e.printStackTrace();
        }
        return null;
    }
}
