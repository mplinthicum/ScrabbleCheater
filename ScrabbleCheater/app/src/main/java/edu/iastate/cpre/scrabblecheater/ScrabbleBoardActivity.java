package edu.iastate.cpre.scrabblecheater;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ScrabbleBoardActivity extends ActionBarActivity {

    GridView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        board = (GridView) findViewById(R.id.board);
        board.setAdapter(new ImageAdapter(this));

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(ScrabbleBoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
