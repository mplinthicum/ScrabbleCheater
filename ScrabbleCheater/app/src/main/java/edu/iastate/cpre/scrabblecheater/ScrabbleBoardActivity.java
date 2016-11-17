package edu.iastate.cpre.scrabblecheater;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
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

    GridView board;
    String word = "";
    ImageView tile;

    private Integer[] scrabbleTiles = {
            R.drawable.tile,
            R.drawable.tile_a,
            R.drawable.tile_b,
            R.drawable.tile_c,
            R.drawable.tile_d,
            R.drawable.tile_e,
            R.drawable.tile_f,
            R.drawable.tile_g,
            R.drawable.tile_h,
            R.drawable.tile_i,
            R.drawable.tile_j,
            R.drawable.tile_k,
            R.drawable.tile_l,
            R.drawable.tile_m,
            R.drawable.tile_n,
            R.drawable.tile_o,
            R.drawable.tile_p,
            R.drawable.tile_q,
            R.drawable.tile_r,
            R.drawable.tile_s,
            R.drawable.tile_t,
            R.drawable.tile_u,
            R.drawable.tile_v,
            R.drawable.tile_w,
            R.drawable.tile_x,
            R.drawable.tile_y,
            R.drawable.tile_z
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrabble_board);

        board = (GridView) findViewById(R.id.board);
        board.setAdapter(new ImageAdapter(this));

        board.getLayoutParams().height = (int) getScreenWidth();

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onTileClick(v);
                Toast.makeText(ScrabbleBoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onTileClick(View tile) {
        alertBoxBuilder();
        this.tile = (ImageView) tile;
    }

    private void alertBoxBuilder(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Type your word, fool.");
        alertDialog.setMessage("Word goes here, fool");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Horizontal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                Toast.makeText(ScrabbleBoardActivity.this, "HORIZONTAL: " + word, Toast.LENGTH_SHORT).show();
                tile.setImageResource(chooseLetter(word.charAt(0)));
                tile.setAlpha(1.0f);
            }
        });

        alertDialog.setNegativeButton("Vertical", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                word = input.getText().toString();
                Toast.makeText(ScrabbleBoardActivity.this, "VERTICAL: " + word, Toast.LENGTH_SHORT).show();
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

    private float getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    private Integer chooseLetter(char letter){
        switch (letter){
            case 'a': return scrabbleTiles[1];
            case 'b': return scrabbleTiles[2];
            case 'c': return scrabbleTiles[3];
            case 'd': return scrabbleTiles[4];
            case 'e': return scrabbleTiles[5];
            case 'f': return scrabbleTiles[6];
            case 'g': return scrabbleTiles[7];
            case 'h': return scrabbleTiles[8];
            case 'i': return scrabbleTiles[9];
            case 'j': return scrabbleTiles[10];
            case 'k': return scrabbleTiles[11];
            case 'l': return scrabbleTiles[12];
            case 'm': return scrabbleTiles[12];
            case 'n': return scrabbleTiles[14];
            case 'o': return scrabbleTiles[15];
            case 'p': return scrabbleTiles[16];
            case 'q': return scrabbleTiles[17];
            case 'r': return scrabbleTiles[18];
            case 's': return scrabbleTiles[19];
            case 't': return scrabbleTiles[20];
            case 'u': return scrabbleTiles[21];
            case 'v': return scrabbleTiles[22];
            case 'w': return scrabbleTiles[23];
            case 'x': return scrabbleTiles[24];
            case 'y': return scrabbleTiles[25];
            case 'z': return scrabbleTiles[26];
            default: return scrabbleTiles[0];
        }
    }
}
