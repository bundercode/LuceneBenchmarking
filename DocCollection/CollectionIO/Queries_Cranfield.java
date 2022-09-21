package DocCollection.CollectionIO;

import Search.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Example: cran.qry Test test
 *
 * @author Aaron "The Dude" Howard
 */
public class Queries_Cranfield extends Queries {

    //private Globals
    private final File file;
    private int lineCount;
    /**
     * Constructor for Queries Taking in a file name
     */
    public Queries_Cranfield() {
        super();
        file = new File(sh.get("queryFile"));
        lineCount =0;
        
        // changed to assertion
        assert file.exists() : (sh.get("queryFile") + " is not a vaild file or does not exist!!!!");
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
        Scanner infile = new Scanner(file);

        //moves scanner to the end of last query in a file
        catchUp(infile, lineCount);

        String query = "";

        //vars for keeping track of .I#'s and query spacing
        int countI = 0;
        int countQ = 0;

        // Scanner check
        if (!infile.hasNext()) {
            query = "EOF";
        }

        //While loop that finds a query
        while (infile.hasNext()) {

            if (countI > 1) {
                lineCount--;
                infile.reset();
                break;
            }

            String test = infile.nextLine();
            lineCount++;

            if (test.charAt(0) == '.' && test.charAt(1) == 'I') {
                countI++;
            } else if (test.charAt(0) == '.' && test.charAt(1) == 'W') {

            } else {
                if (countQ == 0) {
                    query += test;
                    countQ++;
                } else {
                    query = query + " " + test;
                    query = query.replace("?", " ");
                }
            }
        }
        return query;
    }
}
