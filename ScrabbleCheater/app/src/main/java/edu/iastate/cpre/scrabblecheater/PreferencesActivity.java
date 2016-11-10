/*
 *  The preferences activity will allow the user to choose from a few dictionaries (large and small) as
 *   well as switch between the original Scrabble board and the Words with Friends board.
 *
 */

package edu.iastate.cpre.scrabblecheater;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ScrabblePreferencesFragment()).commit();
    }

    public static class ScrabblePreferencesFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity(), R.xml.scrabble_preferences, false);
            addPreferencesFromResource(R.xml.scrabble_preferences);
        }
    }
}
