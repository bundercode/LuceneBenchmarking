/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DocCollection;

import Search.*;
import ComputeSimilarity.FindSimilarWords;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Aaron
 */
@Deprecated
public class TokeTerm {
    public TokeTerm(){
        
    }
     public String createNewQueStr(String text) throws Exception{
         String ret="";
          StringTokenizer tk=new StringTokenizer(text," ():,.?!\n\t");
         ArrayList<String> ray = new ArrayList<>();
         FindSimilarWords find=new FindSimilarWords();
        
		while(tk.hasMoreTokens()){
		find.getSimilarWords(tk.nextToken());
               
		}
                 for(String el:ray){
                    ret+=" "+el;
                }
                System.out.println(ret);
        return ret;
    }
   
}
