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
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.AdapterSearch;
import com.example.historyvideo.model.Phim;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {
    GridView gvSearch;
    SearchView tvSearch;
    AdapterSearch adapterSearch;
    ArrayList<Phim> listPhim;
    String searchtext;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        gvSearch = (GridView) findViewById(R.id.gvSearch);
        tvSearch = (SearchView) findViewById(R.id.tvSearch);

        listPhim = new ArrayList<>();
        adapterSearch = new AdapterSearch(this, R.layout.custom_gridview_item_search, listPhim);
        gvSearch.setAdapter(adapterSearch);
        fillDataAll();

        tvSearch.setIconified(false);
        tvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fillData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fillData(newText);
                return false;
            }
        });

    }

    private void fillDataAll() {
        mData = FirebaseDatabase.getInstance().getReference("Phim");

        Query query = mData.orderByChild("tenPhim");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPhim.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Phim phim = snapshot.getValue(Phim.class);
                        listPhim.add(phim);
                    }
                    adapterSearch.notifyDataSetChanged();
                }
                gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent manhinhDetail = new Intent(Search_Activity.this, DetailActivity.class);
                        String key = listPhim.get(position).getIdPhim();
                        manhinhDetail.putExtra("phim_UID", key);
                        startActivity(manhinhDetail);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);
    }

    private void fillData(final String q) {

        mData = FirebaseDatabase.getInstance().getReference("Phim");
        Query query = mData.orderByChild("tenPhim");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPhim.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Phim phim = snapshot.getValue(Phim.class);
                        String tenthuong = phim.getTenPhim().toLowerCase();
                        if (tenthuong.contains(q)) {
                            listPhim.add(phim);
                        }
                    }
                    adapterSearch.notifyDataSetChanged();
                }
                gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent manhinhDetail = new Intent(Search_Activity.this, DetailActivity.class);
                        String key = listPhim.get(position).getIdPhim();
                        manhinhDetail.putExtra("phim_UID", key);
                        startActivity(manhinhDetail);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(valueEventListener);
    }

    public void clickcloseSearch(View view) {
        super.onBackPressed();
    }
}
