package edu.iastate.cpre.scrabblecheater;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordPlaysActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_plays);
    }

    private ArrayList<String> findAnagrams() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String filename = sharedPreferences.getString(getString(R.string.dictionarypref), "usaEnglish61k.txt");
        ArrayList<String> matches = new ArrayList<>();
        String bank = getIntent().getStringExtra("tiles");

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
