package edu.iastate.cpre.scrabblecheater;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import android.widget.Toast;

public class ScrabbleBoardActivity extends ActionBarActivity {

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
            R.drawable.tile_w, R.drawable.tile_x, R.drawable.tile_y, R.drawable.tile_z, R.drawable.tile_blank};

    // Array containing all user tile IDs for easy looping.
    private Integer[] userImageViewTiles = {R.id.my_tile_1, R.id.my_tile_2, R.id.my_tile_3, R.id.my_tile_4,
            R.id.my_tile_5, R.id.my_tile_6, R.id.my_tile_7};

    // 2D array that holds the char representation of the board state.
    private char[][] boardStateArray = new char[15][15];

    // Array containing tile currently owned by the user.
    private char[] userTilesArray = new char[7];

    private String tiles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        // Build the board.
        board = (GridView) findViewById(R.id.board);
        setBoardFromPrefs(board);
        board.setAdapter(new ImageAdapter(this));
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

        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15),
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z0-9]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

        alertDialog.setView(input);

        // Horizontal word option.
        alertDialog.setPositiveButton("Horizontal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                for(int i = 0; i < word.length(); i++) {
                    // Stop if the word goes past the end of the board.
                    if((p + i) % 15 == 0 && i > 0) break;

                    // Place the tiles.
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(p + i + 1);
                    currentTile.setImageResource(chooseLetter(word.charAt(i)));
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
                    ImageView currentTile = (ImageView) board.getAdapter().getItem(p + i * 15 + 1);
                    currentTile.setImageResource(chooseLetter(word.charAt(i)));
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

        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7),
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z0-9\\*]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        alertDialog.setView(input);

        // Enter the word option.
        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int whichButton) {
                tiles = input.getText().toString();
                Toast.makeText(ScrabbleBoardActivity.this, input.getText().toString(), Toast.LENGTH_SHORT).show();
                
                for(int i = 0; i < tiles.length(); i++) {
                    ImageView tile = (ImageView) findViewById(userImageViewTiles[i]);
                    tile.setImageResource(chooseLetter(tiles.charAt(i)));
                }
            }
        });

        // Cancel your tile entry.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int whichButton) {
                Toast.makeText(ScrabbleBoardActivity.this, "Flaky Hoe.", Toast.LENGTH_SHORT).show();
            }
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

    public void onSolveClick(View v) {
        startActivity(new Intent(this, AnagramSolverActivity.class).putExtra("tiles", tiles));
    }

    private float getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    private Integer chooseLetter(char letter) {
        if(letter == '*')
            return scrabbleDrawableTiles[27];
        else
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
    }
}