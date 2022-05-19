package com.example.historyvideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Phim;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Phim_Adapter extends ArrayAdapter<Phim> {
    private List<Phim> phimList;
    private Context context;
    private LayoutInflater layoutInflater;

    public Phim_Adapter(@NonNull Context context, int resource, @NonNull List<Phim> objects) {
        super(context, resource, objects);
        this.phimList = objects;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.custom_grid_item, parent, false);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgHinh);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvTen);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Phim phim = phimList.get(position);


        Picasso.get().load(Uri.parse(phim.getPosterPhim()))
                .resize(6000,2000)
                .onlyScaleDown()
                .into(holder.imgHinh);
        holder.tvName.setText(phim.getTenPhim());

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imgHinh;
        public TextView tvName;

    }
}
