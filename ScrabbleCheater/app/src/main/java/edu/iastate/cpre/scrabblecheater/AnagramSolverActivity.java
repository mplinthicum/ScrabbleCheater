package edu.iastate.cpre.scrabblecheater;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.Arrays;

public class AnagramSolverActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_solver);
    }

    // Helper method to check if string s contains a letter from the word bank
    private static boolean isAnagram(String bank, String s){

        // Convert bank into char array for easy char removal
        char[] charBank = bank.toCharArray();

        // Check if each character in s is present in the word bank
        for (int i = 0; i < s.length(); i++) {
            if (new String(charBank).indexOf(s.charAt(i)) == -1) {

                // If no letter matched, check if the bank contains a wildcard
                if (new String(charBank).contains("*")){
                    charBank[new String(charBank).indexOf('*')] = '$';
                }
                else{
                    return false;
                }
            }
            else{
                charBank[bank.indexOf(s.charAt(i))] = '$';
            }
        }
        return true;
    }
}
