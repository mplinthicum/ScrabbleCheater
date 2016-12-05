package edu.iastate.cpre.scrabblecheater;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnagramSolverActivity extends ActionBarActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_solver);
        editText = (EditText) findViewById(R.id.word_entry);
        editText.setText(getIntent().getStringExtra("tiles"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scrabble_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_preferences:
                // Display Settings page
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onMatchClick(View v) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String filename = sharedPreferences.getString(getString(R.string.dictionarypref), "no selection");
        ArrayList<String> matches = new ArrayList<>();
        String bank = editText.getText().toString();
        ListView listView = (ListView) findViewById(R.id.playable_words);

        // Make sure dictionary preference is set to a dictionary
        if (!filename.equals("no selection")){
            try {
                String line = null;
                AssetManager assetManager = getAssets();
                InputStream inputStream = assetManager.open(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // Go through each line in the dictionary file
                while ((line = bufferedReader.readLine()) != null){
                    if(isAnagram(bank, line) && line.length() > 1){
                        matches.add(line.toLowerCase());
                    }
                }

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches);
                listView.setAdapter(stringArrayAdapter);
                listView.invalidateViews();
            }
            catch (IOException e){

            }
        }

        hideKeyboard(this);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
