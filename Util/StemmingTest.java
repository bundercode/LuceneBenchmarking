/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Util.Stemming;

/**
 *
 * @author aaron
 */
public class StemmingTest {
    public static void main(String[] args){
        Stemming stem=new Stemming();
        String text="too many puppies, Soap for Dirty Hippies, Jeb for President ";
        System.out.println(Stemming.stem(text));
    }
}
