package edu.iastate.cpre.scrabblecheater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class ScrabbleBoardActivity extends ActionBarActivity {

    GridView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        board = (GridView) findViewById(R.id.board);
        setBoardFromPrefs(board);

        board.getLayoutParams().height = (int) getScreenWidth();

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(ScrabbleBoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onResume(){
        super.onResume();

        setBoardFromPrefs(board);
    }

    private float getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    private void setBoardFromPrefs(GridView boardID){
        // Get board settings from preferences and set to either Scrabble or WWF board
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Resources res = getResources();
        boolean isWWFB = prefs.getBoolean(res.getString(R.string.boardpref), false);

        // Set the background to whatever the setting is
        if(isWWFB)
            boardID.setBackgroundResource(R.drawable.wwfb);
        else
            boardID.setBackgroundResource(R.drawable.board);

        board.setAdapter(new ImageAdapter(this));
    }
}
