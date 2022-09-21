/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 *
 * @author aaron
 */


/**
     * The constructor substantiates Lucene StandardQueryParser qp
     * and sets qp's analyzer to the EnglishAnalyzer in Lucene 
     */

public class Stemming {
    private static StandardQueryParser qp;
     public Stemming(){
         qp = new StandardQueryParser();
         qp.setAnalyzer(new EnglishAnalyzer(Version.LUCENE_34));
     }
     
     
       /**
      * Stem takes in a string 
      * use qp to  get the basic stem of the word
      * and returns the basic stem
      * 
      * for example looking, looked, and looks all have the basic stem of look
      * @param text
      * @return 
      */
     public  static String stem(String text){
        String stemmed="";
         try {
          stemmed=qp.parse(text.toLowerCase(), " ").toString();
          stemmed=stemmed.replace(':', ' ');
        }catch (QueryNodeException ex){
            Logger.getLogger(Stemming.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stemmed;
     }
    
}
