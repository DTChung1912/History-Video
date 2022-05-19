package com.example.historyvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.activities.DetailActivity;
import com.example.historyvideo.model.Phim;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Section_List_Adapter extends RecyclerView.Adapter<Section_List_Adapter.SingleItemRowHolder>  {

    private ArrayList<Phim> itemsList;
    private Context mContext;

    public Section_List_Adapter(ArrayList<Phim> itemsList, Context mContext) {
        this.itemsList = itemsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_single_card, null, false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder holder, final int position) {
        final Phim phim = itemsList.get(position);
        holder.tvTitle.setText(phim.getTenPhim());
        Uri uri = Uri.parse(phim.getPosterPhim());
        Picasso.get().load(uri).into(holder.itemImage);
        final String id = phim.getIdPhim();

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(),DetailActivity.class);
                intent.putExtra("phim_UID", id);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView itemImage;

        private SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
        }
    }
}
