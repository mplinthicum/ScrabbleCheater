package edu.iastate.cpre.scrabblecheater;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScrabbleBoardActivity extends ActionBarActivity {

    // The scrabble board.
    GridView board;

    // Variable to hold the word being entered by the user.
    String word = "";

    // Array containing all scrabble and placeholder tiles.
    private Integer[] scrabbleTiles = {R.drawable.tile, R.drawable.tile_a, R.drawable.tile_b,
            R.drawable.tile_c, R.drawable.tile_d, R.drawable.tile_e, R.drawable.tile_f, R.drawable.tile_g,
            R.drawable.tile_h, R.drawable.tile_i, R.drawable.tile_j, R.drawable.tile_k, R.drawable.tile_l,
            R.drawable.tile_m, R.drawable.tile_n, R.drawable.tile_o, R.drawable.tile_p, R.drawable.tile_q,
            R.drawable.tile_r, R.drawable.tile_s, R.drawable.tile_t, R.drawable.tile_u, R.drawable.tile_v,
            R.drawable.tile_w, R.drawable.tile_x, R.drawable.tile_y, R.drawable.tile_z
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        // Build the board.
        board = (GridView) findViewById(R.id.board);
        setBoardFromPrefs(board);

        board.getLayoutParams().height = (int) getScreenWidth();

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(ScrabbleBoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                alertBoxBuilder(position);
            }
        });
    }

    /**
     * Builds and shows an alert to allow the user to input a word onto the scrabble board and
     * to decide its orientation (horizontal or vertical).
     */
    private void alertBoxBuilder(int position){
        final int p = position;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Type your word, fool.");
        alertDialog.setMessage("Word goes here, fool");

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        alertDialog.setView(input);

        // Horizontal word option.
        alertDialog.setPositiveButton("Horizontal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                Toast.makeText(ScrabbleBoardActivity.this, "HORIZONTAL: " + word, Toast.LENGTH_SHORT).show();
                for(int i = p; i < p + word.length(); i++) {
                    if(i >= 225) break;
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(i);
                    currentTile.setImageResource(chooseLetter(word.charAt(i - p)));
                }
            }
        });

        // Vertical word option.
        alertDialog.setNegativeButton("Vertical", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                Toast.makeText(ScrabbleBoardActivity.this, "VERTICAL: " + word, Toast.LENGTH_SHORT).show();
                for(int i = 0; i < word.length(); i++) {
                    if(p + i * 15 >= 225) break;
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(p + i * 15);
                    currentTile.setImageResource(chooseLetter(word.charAt(i)));
                }
            }
        });

        // Show the word input alert.
        alertDialog.show();
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

    private Integer chooseLetter(char letter) {
        return scrabbleTiles[(int) letter - 96];
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
