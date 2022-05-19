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
import com.example.historyvideo.model.Related_Phim;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Related_Adapter extends RecyclerView.Adapter<Related_Adapter.SingleItemRowHolder>{
    private ArrayList<Related_Phim> itemsList;
    private Context mContext;

    public Related_Adapter(ArrayList<Related_Phim> itemsList, Context mContext) {
        this.itemsList = itemsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_single_card,null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int position) {
        final Related_Phim phim = itemsList.get(position);
        holder.tvRelatedTitle.setText(phim.getTenPhim());
        Uri uri = Uri.parse(phim.getPosterPhim());
        Picasso.get().load(uri).into(holder.imgRelatedImage);
        final String id = phim.getIdPhim();

        holder.imgRelatedImage.setOnClickListener(new View.OnClickListener() {
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

    public class SingleItemRowHolder extends RecyclerView.ViewHolder{

        private TextView tvRelatedTitle;
        private ImageView imgRelatedImage;


        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.tvRelatedTitle = (TextView)itemView.findViewById(R.id.tvTitleRelated);
            this.imgRelatedImage = (ImageView) itemView.findViewById(R.id.itemImageRelated);
        }
    }
}
