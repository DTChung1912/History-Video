package com.example.historyvideo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Adapter_WatchLater;
import com.example.historyvideo.model.Phim;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Watch_Later_Activity extends AppCompatActivity {
    private GridView gvWatchLater;
    ArrayList<Phim> arrayList;
    Adapter_WatchLater adapter;
    DatabaseReference mDataUser;
    DatabaseReference mDataPhim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch__later_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        gvWatchLater = (GridView) findViewById(R.id.gvWatchLater);
        arrayList = new ArrayList<>();
        adapter = new Adapter_WatchLater(this, R.layout.custom_grid_item_wl, arrayList);
        gvWatchLater.setAdapter(adapter);
        gvWatchLater.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = arrayList.get(position).getIdPhim();
                Intent intent = new Intent(Watch_Later_Activity.this, DetailActivity.class);
                intent.putExtra("phim_UID", key);
                startActivity(intent);
            }
        });
        fillData();
    }

    private void fillData() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDataUser = FirebaseDatabase.getInstance().getReference("Users");
        mDataPhim = FirebaseDatabase.getInstance().getReference("Phim");
        mDataUser.child(userID).child("Watch_Later").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                if (dataSnapshot.exists() && dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final String idP = ds.getKey();
                        mDataPhim.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && dataSnapshot != null) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Phim phim = ds.getValue(Phim.class);
                                        if (phim.getIdPhim().equals(idP)) {
                                            arrayList.add(phim);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void clickBacktoMore(View view) {
        super.onBackPressed();
    }
}
