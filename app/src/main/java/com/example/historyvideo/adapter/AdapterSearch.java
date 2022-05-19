package com.example.historyvideo.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Phim;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearch extends ArrayAdapter<Phim> {
    private List<Phim> phimList;
    private Context context;
    private LayoutInflater layoutInflater;


    public AdapterSearch(@NonNull Context context, int resource, @NonNull List<Phim> objects) {
        super(context, resource, objects);
        this.phimList = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.custom_gridview_item_search, parent, false);
            holder.imgHinh = convertView.findViewById(R.id.imgHinhSearch);
            holder.tvName = convertView.findViewById(R.id.tvTenSearch);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Phim phim = phimList.get(position);


        Picasso.get().load(Uri.parse(phim.getPosterPhim()))
                .resize(6000, 2000)
                .onlyScaleDown()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.imgHinh);
        holder.tvName.setText(phim.getTenPhim());

        return convertView;

    }

    public static class ViewHolder {
        public ImageView imgHinh;
        public TextView tvName;

    }

}
