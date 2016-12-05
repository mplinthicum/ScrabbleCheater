package edu.iastate.cpre.scrabblecheater;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScrabbleBoardActivity extends AppCompatActivity {

    // The scrabble board.
    GridView board;

    // Variable to hold the word being entered by the user.
    String word = "";

    // Array containing all scrabble and placeholder tiles.
    private Integer[] scrabbleDrawableTiles = {R.drawable.tile, R.drawable.tile_a, R.drawable.tile_b,
            R.drawable.tile_c, R.drawable.tile_d, R.drawable.tile_e, R.drawable.tile_f, R.drawable.tile_g,
            R.drawable.tile_h, R.drawable.tile_i, R.drawable.tile_j, R.drawable.tile_k, R.drawable.tile_l,
            R.drawable.tile_m, R.drawable.tile_n, R.drawable.tile_o, R.drawable.tile_p, R.drawable.tile_q,
            R.drawable.tile_r, R.drawable.tile_s, R.drawable.tile_t, R.drawable.tile_u, R.drawable.tile_v,
            R.drawable.tile_w, R.drawable.tile_x, R.drawable.tile_y, R.drawable.tile_z};

    // Array containing all user tile IDs for easy looping.
    private Integer[] userImageViewTiles = {R.id.my_tile_1, R.id.my_tile_2, R.id.my_tile_3, R.id.my_tile_4,
            R.id.my_tile_5, R.id.my_tile_6, R.id.my_tile_7};

    // 2D array that holds the char representation of the board state.
    private char[] boardStateArray = new char[225];

    // Array containing tile currently owned by the user.
    private char[] userTilesArray = new char[7];

    /**
     * You know what this does, stupid.
     * @param savedInstanceState
     */
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
                boardAlertBoxBuilder(position);
            }
        });

        // Set up the user tiles input.
        Button enterTiles = (Button) findViewById(R.id.enter_tiles_button);
        enterTiles.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                tilesAlertBoxBuilder();
            }
        });

        // Set all values of board state and tray tiles to '\0'.
        for(int i = 0; i < 7; i ++){
            userTilesArray[i] = '\0';
        }

        for(int i = 0; i < 225; i++) {
            boardStateArray[i] = '\0';
        }
    }

    /**
     * Starts AnagramSolverActivity and sends the board state and user tiles data.
     * @param v
     */
    public void onSolve(View v) {
        Intent intent = new Intent(this, AnagramSolverActivity.class);
        //intent.putExtra("boardstate", boardStateArray);
        //intent.putExtra("usertiles", userTilesArray);
        startActivity(intent);
    }

    /**
     * Builds and shows an alert to allow the user to input a word onto the scrabble board and
     * to decide its orientation (horizontal or vertical).
     */
    private void boardAlertBoxBuilder(int position){
        final int p = position;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Type your word, fool.");
        alertDialog.setMessage("Word goes here, fool");

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        alertDialog.setView(input);

        // Horizontal word option.
        alertDialog.setPositiveButton("Horizontal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                for(int i = 0; i < word.length(); i++) {
                    // Stop if the word goes past the end of the board.
                    if((p + i) % 15 == 0 && i > 0) break;

                    // Place the tiles.
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(p + i);
                    currentTile.setImageResource(chooseLetter(word.charAt(i)));

                    // Set the tile in the array.
                    boardStateArray[p + i] = word.charAt(i);
                }
            }
        });

        // Vertical word option.
        alertDialog.setNegativeButton("Vertical", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                for(int i = 0; i < word.length(); i++) {
                    // Stop if the word goes past the end of the board.
                    if((p + i * 15) > 224) break;

                    // Place the tiles.
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(p + i * 15);
                    currentTile.setImageResource(chooseLetter(word.charAt(i)));

                    // Set the tile in the array.
                    boardStateArray[p + i * 15] = word.charAt(i);
                }
            }
        });
        alertDialog.show();
    }

    /**
     * Builds and shows an alert box that, when the solve button is clicked, allows a user to enter
     * the tiles they have in their hand.
     */
    private void tilesAlertBoxBuilder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Type your tiles, fool.");
        alertDialog.setMessage("Tiles go here, fool.");

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        alertDialog.setView(input);

        // Enter the word option.
        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int whichButton) {
                String tiles = input.getText().toString();

                for(int i = 0; i < 7; i++) {
                    ImageView tile = (ImageView) findViewById(userImageViewTiles[i]);
                    tile.setImageResource(R.drawable.hollow);
                    userTilesArray[i] = '\0';
                }
                
                for(int i = 0; i < tiles.length(); i++) {
                    ImageView tile = (ImageView) findViewById(userImageViewTiles[i]);
                    tile.setImageResource(chooseLetter(tiles.charAt(i)));
                    userTilesArray[i] = tiles.charAt(i);
                }
            }
        });

        // Cancel the tile entry.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int whichButton) {}
        });
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

    /**
     * Private method used to get the width of the screen.  Used in board formatting.
     * @return
     */
    private float getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    /**
     * Private method used to choose the correct scrabble drawable tile corresponding to the letter.
     * @param letter
     * @return Scrabble Drawable
     */
    private Integer chooseLetter(char letter) {
        return scrabbleDrawableTiles[(int) letter - 96];
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