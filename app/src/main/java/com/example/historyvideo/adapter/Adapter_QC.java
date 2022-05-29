package com.example.historyvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.QuangCao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapter_QC extends ArrayAdapter<QuangCao> {
    private List<QuangCao> quangCaoList;
    private Context context;
    private LayoutInflater inflater;
    DatabaseReference mData;

    public Adapter_QC(@NonNull Context context, int resource, @NonNull List<QuangCao> objects) {
        super(context, resource, objects);
        this.quangCaoList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.banner_list_item, parent, false);
            holder.imgHinhBanner = convertView.findViewById(R.id.imgBanner);
            holder.idphimBanner = convertView.findViewById(R.id.tvidPhim);
            holder.tenphimBanner = convertView.findViewById(R.id.tvtenPhimQuangCao);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QuangCao quangCao = quangCaoList.get(position);

        String linkanh = quangCao.getLinkAnh();
        Uri url_Anh = Uri.parse(linkanh);
        String idPhim = quangCao.getIdPhim();

        Picasso.get().load(url_Anh).into(holder.imgHinhBanner);
        holder.idphimBanner.setText("ID phim: " + idPhim);
        mData = FirebaseDatabase.getInstance().getReference("Phim");
        mData.child(idPhim).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tenPhim = dataSnapshot.child("tenPhim").getValue(String.class);
                    holder.tenphimBanner.setText("Phim: " + tenPhim);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgHinhBanner;
        TextView idphimBanner;
        TextView tenphimBanner;
    }
}
