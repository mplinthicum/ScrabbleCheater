package edu.iastate.cpre.scrabblecheater;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.GridView;

public class ScrabbleBoardActivity extends ActionBarActivity {

    GridView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        board = (GridView) findViewById(R.id.board);
    }
}
