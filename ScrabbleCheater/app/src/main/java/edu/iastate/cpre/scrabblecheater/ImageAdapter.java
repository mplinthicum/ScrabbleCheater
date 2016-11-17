package edu.iastate.cpre.scrabblecheater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by mpl on 11/10/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mTiles = new Integer[225];

    public ImageAdapter(Context c) {
        mContext = c;

        for (int i = 0; i < 225; i++) {
            mTiles[i] = R.drawable.tile;
        }
    }

    public int getCount() {
        return mTiles.length;
    }

    public Object getItem(int position) {
        return mTiles[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        GridView board = (GridView) parent;
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(board.getLayoutParams().width, board.getLayoutParams().height / 15));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(6, 0, 0, 0);
            imageView.setAlpha(0.0f);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mTiles[position]);
        return imageView;
    }
}
