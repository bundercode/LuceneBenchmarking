/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Search;

import DocCollection.CollectionIO.DocumentParser;
import DocCollection.CollectionIO.DocumentParser_Cranfield;
import DocCollection.CollectionIO.DocumentParser_MED;
import DocCollection.CollectionIO.DocumentParser_TIME;
import DocCollection.CollectionIO.Queries;
import DocCollection.CollectionIO.Queries_Cranfield;
import DocCollection.CollectionIO.Queries_MED;
import DocCollection.CollectionIO.Queries_TIME;
import DocCollection.CollectionIO.RelevantDocumentReader;
import DocCollection.CollectionIO.RelevantDocumentReader_Cranfield;
import DocCollection.CollectionIO.RelevantDocumentReader_MED;
import DocCollection.CollectionIO.RelevantDocumentReader_TIME;
import DocCollection.Corpus;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @file Menu.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 *
 * @section DESCRIPTION
 */
public class Menu {
    // TODO: add persistant data

    private Queries queries;
    private DocumentParser documentParser;
    private RelevantDocumentReader relevantDocumentReader;
    private int numRelevantDocs;
    private boolean expandDocs;
    private boolean useSenseNodesDocs;
    private double trimPointDocs;
    private long maxDepthDocs;
    private boolean expandQueries;
    private boolean useSenseNodesQueries;
    private double trimPointQueries;
    private long maxDepthQueries;

    public Menu() {
        queries = null;
        documentParser = null;
        relevantDocumentReader = null;
        numRelevantDocs = 0;
        trimPointDocs = .7;
        maxDepthDocs = Long.MAX_VALUE;
        trimPointQueries = .7;
        maxDepthQueries = Long.MAX_VALUE;
    }

    public Corpus openMenu() throws Exception {
        String input;
        whileRepeat:
        while (true) {
            // collection
            // Cranfield 1
            // CISI 2
            // Time 3
            // save correct instances for each
            System.out.println();
            System.out.println("Cranfield:\t\t1");
            System.out.println("MED     :\t\t2");
            System.out.println("Time     :\t\t3");
            input = getFromUser("Which document collection would you like? ");
            try {
                switch (Integer.parseInt(input)) {
                    case 1:
                        queries = new Queries_Cranfield();
                        documentParser = new DocumentParser_Cranfield();
                        relevantDocumentReader = new RelevantDocumentReader_Cranfield();
                        numRelevantDocs = 4;
                        break whileRepeat;
                    case 2:
                        queries = new Queries_MED();
                        documentParser = new DocumentParser_MED();
                        relevantDocumentReader = new RelevantDocumentReader_MED();
                        numRelevantDocs = 1;
                        break whileRepeat;
                    case 3:
                        queries = new Queries_TIME();
                        documentParser = new DocumentParser_TIME();
                        relevantDocumentReader = new RelevantDocumentReader_TIME();
                        numRelevantDocs = 1;
                        break whileRepeat;
                    default:
                        throw new Exception();
                }
            } catch (Exception ex) {
                System.out.println("Incorrect selection!");
            }
        }

        // expand
        // pass boolean to corpus
        expandDocs = getBooleanFromUser("Expand Docs? (y/N) ", false);

        if (expandDocs) {
            double d = getDoubleFromUser("Trim point for docs (default 0.7):  ");
            if (d >= 0) {
                trimPointDocs = d;
            }
            useSenseNodesDocs = getBooleanFromUser("Use sense nodes (y/N) " , false);
            long x = getLongFromUser("Max search depth for docs (default Long.MAX_VALUE): ");
            if (x >= 0) {
                maxDepthDocs = x;
            }
        }

        // pass boolean to queries
        expandQueries = getBooleanFromUser("Expand Queries? (y/N) ", false);

        if (expandQueries) {
            double d = getDoubleFromUser("Trim point for queries (default 0.7):  ");
            if (d >= 0) {
                trimPointQueries = d;
            }
            useSenseNodesQueries = getBooleanFromUser("Use sense nodes (y/N) ", false);
            long x = getLongFromUser("Max search depth for queries (default Long.MAX_VALUE): ");
            if (x >= 0) {
                maxDepthDocs = x;
            }
        }

        // rebuild index
        if (true) {// TODO: see if it needs rebuilding before asking
            if (getBooleanFromUser("Force rebuild index? (Y/n) ", true)) {
                deleteDirectory(new File(StringHash.factory().get("indexFile")));
            }
        }

        // write scores to file y/n
        // if y get filename
        // display graphs
        return new Corpus(documentParser, this);

    }

    public Queries getQueries() {
        return queries;
    }

    public DocumentParser getDocumentParser() {
        return documentParser;
    }

    public RelevantDocumentReader getReleventDocumentReader() {
        return relevantDocumentReader;
    }

    public int getNumRelevantDocs() {
        return numRelevantDocs;
    }

    public boolean getExpandDocs() {
        return expandDocs;
    }
    
    public boolean getUseSenseNodesDocs() {
        return useSenseNodesDocs;
    }
    
    public double getTrimPointDocs() {
        return trimPointDocs;
    }

    public boolean getExpandQueries() {
        return expandQueries;
    }
    
    public boolean getUseSenseNodesQueries() {
        return useSenseNodesQueries;
    }
    
    public double getTrimPointQueries() {
        return trimPointQueries;
    }
    
    public long getMaxDepthDocs() {
        return maxDepthDocs;
    }
    
    public long getMaxDepthQueries() {
        return maxDepthQueries;
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }
    
    public static double getDoubleFromUser(final String prompt) {
        while (true) {
            String s = getFromUser(prompt);
            if (s.equals("")) {
                return -1;
            }
            try {
                return Double.parseDouble(s);
            } catch(NumberFormatException ex) {
                System.out.println("Could not format input!");
                System.out.println("Please input a double!");
            }
        }
    }
    
    public static boolean getBooleanFromUser(final String prompt, final boolean defaultValue) {
        String s = getFromUser(prompt);
        if (s.equals("y")) {
            return true;
        }
        if (s.equals("n")) {
            return false;
        }
        return defaultValue;
    }
    
    public static String getFromUser(final String prompt) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(prompt);
        try {
            return br.readLine().toLowerCase();
        } catch(Exception ex){}
        return "";
    }
    
    public static long getLongFromUser(final String prompt) {
        while (true) {
            String s = getFromUser(prompt);
            if (s.equals("")) {
                return -1;
            }
            try {
                return Long.parseLong(s);
            } catch(NumberFormatException ex) {
                System.out.println("Could not format input!");
                System.out.println("Please input a double!");
            }
        }
    }
    
}
