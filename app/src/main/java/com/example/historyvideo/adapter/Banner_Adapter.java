package com.example.historyvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.example.historyvideo.R;
import com.example.historyvideo.activities.DetailActivity;
import com.example.historyvideo.activities.ExoPlayerActivity;
import com.example.historyvideo.model.QuangCao;
import com.example.historyvideo.model.User_Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Banner_Adapter extends PagerAdapter {
    ArrayList<QuangCao> arrayListBanner;
    Context context;
    DatabaseReference mData;
    FirebaseUser user;
    ImageView imgAddPlaylist;
    List<String> listKey;
    String phim_UID;
    String tenphim;
    Long luotxem;
    private boolean mProcessWatchLater = false;

    public Banner_Adapter(Context context, ArrayList<QuangCao> arrayListBanner) {
        this.context = context;
        this.arrayListBanner = arrayListBanner;
    }

    @Override
    public int getCount() {
        return arrayListBanner.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.banner_item, null);
        imgAddPlaylist = (ImageView) view.findViewById(R.id.imgAddToPlaylist);
        ImageView img = (ImageView) view.findViewById(R.id.imgBackGroundBanner);
        ImageView imgPlay = (ImageView) view.findViewById(R.id.imgPlay);

        Picasso.get()
                .load(Uri.parse(arrayListBanner.get(position).getLinkAnh()))
                .into(img);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = arrayListBanner.get(position).getIdPhim();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("phim_UID", key);
                context.startActivity(intent);
            }
        });


        imgAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idPhim = arrayListBanner.get(position).getIdPhim();
                mProcessWatchLater = true;

                Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
                intent.putExtra("phim_UID", idPhim);
                context.startActivity(intent);
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = arrayListBanner.get(position).getIdPhim();
                mData = FirebaseDatabase.getInstance().getReference("Phim");
                mData.child(key).child("soluotXem").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String view = dataSnapshot.getValue().toString();
                        luotxem = Long.parseLong(view);
                        update_luotXem(key);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                mData.child(key).child("tenPhim").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference dataLog = FirebaseDatabase.getInstance().getReference();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = user.getUid();

                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = (cal.get(Calendar.MONTH) + 1);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);

                        String timeStamp = "Ngày " + day + " tháng " + month + " năm " + year + " lúc " + hour + "h:" + minute;
                        String movieWatched = dataSnapshot.getValue().toString();
                        String key = dataLog.child("UserLog").push().getKey();
                        Log.d("TAGGGGGG", "" + movieWatched);
                        User_Log user_log = new User_Log(timeStamp, movieWatched);
                        dataLog.child("UserLog").child(uid).child(key).setValue(user_log);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                mData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        String link = map.get("linkPhim");
                        String sub = map.get("linksub");

                        Intent intent = new Intent(context, ExoPlayerActivity.class);
                        intent.putExtra("Link", link);
                        intent.putExtra("Link_Sub", sub);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        return view;
    }

    public void update_luotXem(String id) {
        Long clicked = (luotxem + 1);

        FirebaseDatabase.getInstance().getReference("Phim").
                child(id).
                child("soluotXem").
                setValue(clicked);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void getData() {
        mData = FirebaseDatabase.getInstance().getReference("QuangCao");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey().toString();
                mData.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        String id_Phim = map.get("idPhim");
                        String link_Anh = map.get("linkAnh");

                        phim_UID = id_Phim;
                        listKey.add(id_Phim);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
