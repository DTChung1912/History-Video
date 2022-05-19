package com.example.historyvideo.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Phim;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Phim_Admin extends ArrayAdapter<Phim> {

    private List<Phim> phimList;
    private LayoutInflater inflater;


    public Adapter_Phim_Admin(@NonNull Context context, int resource, @NonNull List<Phim> objects) {
        super(context, resource, objects);
        this.phimList = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView== null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_single_item, parent, false);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgAnh);
            holder.tvTen = (TextView) convertView.findViewById(R.id.tvTenPhimAdmin);


            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        final Phim phim = phimList.get(position);


        Picasso.get().load(Uri.parse(phim.getPosterPhim()))
                .resize(6000,2000)
                .onlyScaleDown()
                .into(holder.imgHinh);
        holder.tvTen.setText(phim.getTenPhim());

        return convertView;
    }

    public static class ViewHolder{
        public ImageView imgHinh;
        public TextView tvTen;
    }
}
