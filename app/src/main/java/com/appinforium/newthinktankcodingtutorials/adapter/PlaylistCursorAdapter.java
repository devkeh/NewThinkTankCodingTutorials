package com.appinforium.newthinktankcodingtutorials.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appinforium.newthinktankcodingtutorials.R;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class PlaylistCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    String[] loadingColors;
    int max;

    public PlaylistCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        inflater = LayoutInflater.from(context);
        loadingColors = context.getResources().getStringArray(R.array.thumbnail_loading_colors);
        max = loadingColors.length - 1;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.playlist_list_item, null);

        ViewHolder holder = new ViewHolder();

        Random rand = new Random();
        int randomNum = rand.nextInt(max + 1);

        ColorDrawable loadingColorDrawable = new ColorDrawable(Color.parseColor(loadingColors[randomNum]));

        Bitmap bitmap = Bitmap.createBitmap(320, 180, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        loadingColorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        loadingColorDrawable.draw(canvas);


        holder.thumbnailImageView = (ImageView) itemLayout.findViewById(R.id.videoThumbnailImageView);
        holder.loadingThumbnailBitmap = bitmap;
        holder.titleTextView = (TextView) itemLayout.findViewById(R.id.videoTitleTextView);

        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.titleTextView.setText(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_TITLE)));

        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_THUMBNAIL_URL)))
                .placeholder(new BitmapDrawable(holder.loadingThumbnailBitmap))
                .error(R.drawable.no_thumbnail)
                .into(holder.thumbnailImageView);
    }

    private static class ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        Bitmap loadingThumbnailBitmap;
    }
}
