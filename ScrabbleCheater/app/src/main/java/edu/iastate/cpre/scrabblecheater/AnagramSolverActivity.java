package edu.iastate.cpre.scrabblecheater;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AnagramSolverActivity extends ActionBarActivity {

    private char[] boardStateArray = new char[255];
    private char[] userTilesArray = new char[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_solver);

        //Intent intent = getIntent();
        //boardStateArray = intent.getCharArrayExtra("boardstate");
        //userTilesArray = intent.getCharArrayExtra("usertiles");

        //String test = "";

        //for(int i = 0; i < 225; i++) {
        //    while(boardStateArray[i] != '\0') {
        //        test += boardStateArray[i];
        //    }
        //}

        Toast.makeText(AnagramSolverActivity.this, "hello"t, Toast.LENGTH_SHORT).show();
    }
}
