package edu.iastate.cpre.scrabblecheater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpl on 11/10/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageView> mList = new ArrayList<>();
    Integer[] mTiles = new Integer[225];

    public ImageAdapter(Context mContext) {
        this.mContext = mContext;

        for(int i = 0; i < 225; i++){
            mTiles[i] = R.drawable.tile;
        }
    }

    public int getCount() {
        return mTiles.length;
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        GridView board = (GridView) parent;
        ImageView imageView;

        // Set up the ImageView parameters.
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(board.getLayoutParams().width,
                    board.getLayoutParams().height / 15));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(0, 0, 0, 0);

            // Add the ImageView to the list.
            mList.add(position, imageView);
        } else {
            imageView = (ImageView) convertView;
            mList.remove(position);
            mList.add(position, imageView);
        }
        return imageView;
    }
}
