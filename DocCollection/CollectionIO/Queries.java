package DocCollection.CollectionIO;

import Search.*;
import java.io.BufferedReader;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;

/**
 * Example: cran.qry Test test
 *
 * @author Aaron "The Dude" Howard
 */
public abstract class Queries {

    //private Globals
    protected static final StringHash sh = StringHash.factory();
    protected static ArrayList<String> analyzerList = null;
    /**
     * Constructor for Queries Taking in a file name
     */
    public Queries() {
        //creates an ArrayList for stopWords
        if (analyzerList == null) {
            analyzerList = new ArrayList<String>();
            try {
            //adds words to analyzerList
            makeAnalyzerList();
        } catch (Exception ex) {
        }
        }
    }

    /**
     * method that adds stop words to analyzerList
     * @throws Exception 
     */
    private static void makeAnalyzerList() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sh.get("analyzer"))));
        String line = null;
        while ((line = br.readLine()) != null) {
            analyzerList.add(line);
        }
    }

    /**
     * oneQuery is a method that with separates the a query from all irrelevant
     * data in a file and returns a query
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    protected abstract String oneQuery() throws FileNotFoundException;

    /*
     * moves a scanner to the end of last query found moves scanner to lineCount
     * position
     */
    protected static void catchUp(Scanner infile, int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            infile.nextLine();
        }
    }
    /**
     * method that packages and returns a Query
     * calls oneQuery to get a query from the cranfield docs
     * delimits the query by the stop list (analyzerlist)
     * @return
     * @throws ParseException
     * @throws QueryNodeException 
     */
    public ArrayList<Query> makeNormalQueries(final boolean expand, final boolean useSenseNodes,
                                              final double trimPoint, final long depth) throws ParseException, QueryNodeException, Exception {
        ArrayList<Query> queList = new ArrayList<Query>();
        try {
            String text = oneQuery();
              while (text.compareTo("EOF") != 0) {
             
            StandardQueryParser qp = new StandardQueryParser();
            qp.setAnalyzer(
                    new StopAnalyzer(Version.LUCENE_4_10_0,
                                     StopFilter.makeStopSet(analyzerList)));
            Query stop=qp.parse(text, " ");
            System.out.println("Query before expansion: "+stop.toString());
           
            String[] array = stop.toString().split(" ");
            if (expand) {
               // TODO:  propagate to menu
               text = ExpandedSearch.expandTerms(stop.toString().replace(":", " "), 1024/array.length, useSenseNodes, trimPoint, depth);
            }
            System.out.println(text);
            Query q = qp.parse(text, sh.get("body"));
                queList.add(q);
                text=oneQuery();
              }

           return queList;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch blo
            e.printStackTrace();
        }
        return null;
    }
}
