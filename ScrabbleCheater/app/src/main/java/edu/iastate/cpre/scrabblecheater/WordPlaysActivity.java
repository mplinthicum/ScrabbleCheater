package edu.iastate.cpre.scrabblecheater;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordPlaysActivity extends ActionBarActivity {

    private String wordBank = "";
    private char[] boardState = new char[225];
    private ListView playList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_plays);

        // Get data from the Intent
        wordBank = getIntent().getStringExtra("tiles");
        boardState = getIntent().getCharArrayExtra("board");

        // Debug: view board state
        /*
        playList = (ListView) findViewById(R.id.playList);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String(boardState).substring(1).split(""));
        playList.setAdapter(stringArrayAdapter);
        playList.invalidateViews();
        */

        // Go through each of the letters in the board and calculate words by adding them to the wordbank
        ArrayList<String> plays = new ArrayList<>();
        for(char c : boardState){
            if(c != '\0'){
                String tempWordBank = wordBank + c;
                ArrayList<String> anagrams = findAnagrams(tempWordBank);

                for(String s : anagrams){
                    if (!plays.contains(s) && s.contains(c + "")){
                        plays.add(s);
                    }
                }
            }
        }

        playList = (ListView) findViewById(R.id.playList);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, plays);
        playList.setAdapter(stringArrayAdapter);
        playList.invalidateViews();
    }

    private ArrayList<String> findAnagrams(String bank) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String filename = sharedPreferences.getString(getString(R.string.dictionarypref), "usaEnglish61k.txt");
        ArrayList<String> matches = new ArrayList<>();

        // Make sure dictionary preference is set to a dictionary
        if (!filename.equals("no selection")) {
            try {
                String line = null;
                AssetManager assetManager = getAssets();
                InputStream inputStream = assetManager.open(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // Go through each line in the dictionary file
                while ((line = bufferedReader.readLine()) != null) {
                    if (isAnagram(bank, line) && line.length() > 1) {
                        matches.add(line.toLowerCase());
                    }
                }
            } catch (IOException e) {

            }
        }

        return matches;
    }

    // Helper method to check if string s contains a letter from the word bank
    private static boolean isAnagram(String bank, String s){
        bank = bank.toLowerCase();
        s = s.toLowerCase();

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
