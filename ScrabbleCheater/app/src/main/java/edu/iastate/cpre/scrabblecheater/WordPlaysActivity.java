package edu.iastate.cpre.scrabblecheater;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

public class WordPlaysActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_plays);

        Toast.makeText(WordPlaysActivity.this, "WHADDUP FOOL", Toast.LENGTH_SHORT).show();
    }
}
