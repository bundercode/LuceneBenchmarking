/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import DocCollection.*;
import Search.*;
import java.io.File;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * @file CorpusTest.java
 * @author Andrew Anker <ankeaa01@students.ipfw.edu>
 * @project LuceneBenchmarking
 * @due
 *
 * @section DESCRIPTION
 *
 */
public class CorpusTest {

    public static void main(String[] args) throws Exception {
        /*Corpus c = new Corpus();
        System.out.println("IN CORPUS TEST!!!!!!!!!!!!");
        c.makeIndex();*/
        //indexReader();
    }

    /*public static void indexReader() throws Exception {
        Directory dir = new SimpleFSDirectory(new File("index"));
        IndexReader iReader = DirectoryReader.open(dir);
        IndexSearcher iSearch = new IndexSearcher(iReader);
        //Queries que = new Queries(/*"cran/cran.qry"*///);
        /*Query x = que.makeQuer();
        // TopDocs top = iSearch.search(x, 7);// searches index using query and returns top docs
        while (x != null) {
            //ConstantScoreQuery y = new ConstantScoreQuery(x);
            TopDocs top = iSearch.search(x, 10);
            System.out.println(x);
            System.out.println(top.totalHits);
            System.out.println(top);
            for (ScoreDoc d : top.scoreDocs) {
                System.out.println("Title: "+d.toString() +"  Score: "+d.score);
            }
            x = que.makeQuer();
        }
        // System.out.println("Batman!");
    }*/
}
