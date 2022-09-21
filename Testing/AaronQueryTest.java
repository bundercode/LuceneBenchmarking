/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Testing;

import DocCollection.*;
import DocCollection.CollectionIO.DocumentParser;
import DocCollection.CollectionIO.DocumentParser_TIME;
import DocCollection.CollectionIO.Queries;
import DocCollection.CollectionIO.Queries_TIME;
import Search.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;

/**
 * Aaron rules
 * @author Aaron "The Dude" Howard
 */
public class AaronQueryTest {
     public static void main(String[] args) throws ParseException, QueryNodeException, Exception {
         DocumentParser dc=new DocumentParser_TIME();
         
         Document x=dc.buildDocument();
         while(x!=null){
             System.out.println(x);
             x=dc.buildDocument();
         }
     }
/* public static void main(String[] args) throws ParseException, QueryNodeException, Exception {
Queries q=new Queries_TIME();
        ArrayList<Query> queList=q.makeNormalQueries();
        for(Query el: queList){
            System.out.println(el);
            }   
        }*/
 
    }
 
   

