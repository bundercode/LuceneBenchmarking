package Testing;

import Search.Menu;
import Search.WordGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @file GraphSearchTest.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 *
 * @section DESCRIPTION
 *
 * @section CONCURRENCY
 */
public class GraphSearchTest {

    public static void main(String[] args) {
        /*
         * FindSimilarWords find = new FindSimilarWords(); Scanner sc = new
         * Scanner(System.in); String s = ""; System.out.println("Ready to
         * read"); while(!(s = sc.next()).equals("")) {
         * System.out.println("Related words: "); for (String x
         * :find.searchGraph(s, 10)) { System.out.println(x); }
         * System.out.println("Done\n");
        }
         */
        final WordGraph find = new WordGraph();
        while (true) {
            double trimPoint = .7;
            double trim = Menu.getDoubleFromUser("trim point: ");
            if (trim >= 0 && trim < 1) {
                trimPoint = trim;
            }
            long maxDepth = Long.MAX_VALUE;
            long depth = Menu.getLongFromUser("max depth: ");
            if (depth >= 0) {
                maxDepth = depth;
            }

            boolean useSenseNodes = Menu.getBooleanFromUser("Use sense nodes (y,N): ", false);

            Scanner sc = new Scanner(System.in);
            String[] s = null;
            System.out.println("Ready to read");
            while (true) {
                s = sc.nextLine().split(" ");
                if (s[0].equals("")) {
                    break;
                }
                ArrayList<String> results = new ArrayList();
                for (String searchString : s) {
                    results.addAll(find.graphSearchDFS(searchString, trimPoint, maxDepth, useSenseNodes));
                }
                Collections.sort(results);
                int i = 0;
                for (String r : results) {
                    // if (i > 99) {
                    //    break;
                    // }
                    //if (!r.isSearchNode || useSenseNodes) {
                        System.out.println(r);
                    //}
                    i++;
                }
                System.out.println("Done\n");
            }
            if (!Menu.getBooleanFromUser("Keep going (Y,n) ", true)) {
                return;
            }
        }
    }
}
