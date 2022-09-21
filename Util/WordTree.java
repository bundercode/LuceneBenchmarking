/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import edu.smu.tspell.wordnet.*;

import java.io.*;
import java.util.*;

public class WordTree {

    private WordNetDatabase database;
    
  

    public WordNetDatabase getDatabase() {
        return database;
    }

    public WordTree() throws IOException {
        //System.setProperty("wordnet.database.dir", "/home/lubo/wordnet/WordNet-3.0/dict");
        System.setProperty("wordnet.database.dir", "wordnet/WordNet-3.0/dict");
        database = WordNetDatabase.getFileInstance();
    }

    public String getWordForms(String word) {
        String result = "";
        NounSynset nounSynset;
        NounSynset[] hyponyms;
        Synset[] synsets = database.getSynsets(word);

        for (int i = 0; i < synsets.length; i++) { //iteratre over all senses
            String[] wordForms = synsets[i].getWordForms();
            for (int j = 0; j < wordForms.length; j++) {
                result = result + ((j > 0 ? ", " : "")
                        + wordForms[j]);
            }
            result = result + ": " + synsets[i].getDefinition();
            result = result + "<BR>";
        }
        return result;
    }

    public ArrayList<String> getSenses(String word) {//complete?
        ArrayList<String> result = new ArrayList<String>();
        Synset[] synsets = database.getSynsets(word);
        
        return result;
    }

    public String getSense(String word) {

        String result = "";
        NounSynset nounSynset;//?
        NounSynset[] hyponyms;//?
        Synset[] synsets = database.getSynsets(word);

        for (int i = 0; i < synsets.length; i++) {
            result = result + "Frequency: " + synsets[i].getTagCount(word) + " Example use: ";

            for (String s1 : synsets[i].getUsageExamples()) {
                result = result + "\t" + s1;
            }
            result = result + ": " + synsets[i].getDefinition();
            result = result + "<BR>";
        }
        return result;
    }

    public String getHypernyms(String word) {
        String result = "";
        NounSynset nounSynset;
        NounSynset[] hypernyms;
        Synset[] synsets = database.getSynsets(word);
        for (int i = 0; i < synsets.length; i++) { //iteratre over all senses
            if (synsets[i].getType() == SynsetType.NOUN) {
                nounSynset = (NounSynset) synsets[i];
                hypernyms = nounSynset.getHypernyms();
                for (int j = 0; j < hypernyms.length; j++) {
                    result = result + ((j > 0 ? ", " : "")
                            + hypernyms[j].getDefinition());
                }
                result = result + ": " + synsets[i].getDefinition();
                result = result + "<BR>";
            }
        }
        return result;
    }

    public String getHyponyms(String word) {
        String result = "";
        NounSynset nounSynset;
        NounSynset[] hypernyms;//hyponyms
        Synset[] synsets = database.getSynsets(word);
        for (int i = 0; i < synsets.length; i++) { //iteratre over all senses
            if (synsets[i].getType() == SynsetType.NOUN) {
                nounSynset = (NounSynset) synsets[i];
                hypernyms = nounSynset.getHyponyms();//hyponyms
                for (int j = 0; j < hypernyms.length; j++) {
                    result = result + ((j > 0 ? ", " : "")
                            + hypernyms[j].getDefinition());
                }
                result = result + ": " + synsets[i].getDefinition();
                result = result + "<BR>";
            }
        }
        return result;
    }

    public double getFrequency(String wordForm, Synset s) {
        String[] wordForms = s.getWordForms();//
        boolean OK = false;
        for(String word: wordForms){
            if(wordForm.equals(word)){
                OK = true;
            }
        }
        if(!OK) return 1.0;
        if (s.getTagCount(wordForm) != 0) {
            return s.getTagCount(wordForm) * 1.0;
        } else {
            return 1.0;
        }
    }

    public double getTotalFrequency(String wordForm) {
        Synset[] synsets = database.getSynsets(wordForm);
        double totalFrequency = 0;
        for (Synset s : synsets) {
            totalFrequency += getFrequency(wordForm, s);
        }
        return (totalFrequency==0) ? 1 : totalFrequency;
    }
}