package com.example.historyvideo.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historyvideo.R;
import com.example.historyvideo.activities.DetailActivity;
import com.example.historyvideo.adapter.Phim_Adapter;
import com.example.historyvideo.model.Phim;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Kid_Fragment extends Fragment {
    DatabaseReference mData;
    View view;
    private GridView gridviewKid;
    private ArrayList<Phim> arrayList;
    private Phim_Adapter adapter;
    private RecyclerView rcRelated;
    private List<String> listKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_kid, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gridviewKid = (GridView) view.findViewById(R.id.gvKidz);

        arrayList = new ArrayList<>();
        adapter = new Phim_Adapter(getContext(), R.layout.custom_grid_item, arrayList);
        gridviewKid.setAdapter(adapter);
        readData();


        gridviewKid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent manhinhDetail = new Intent(getActivity(), DetailActivity.class);
                String key = arrayList.get(position).getIdPhim();
                for (int i = 0; i < arrayList.size(); i++) {
                    manhinhDetail.putExtra("phim_UID", key);
                }
                startActivity(manhinhDetail);
            }
        });


        return view;
    }


    public void readData() {
        mData = FirebaseDatabase.getInstance().getReference("Phim");
        Query query = mData.orderByChild("theloaiPhim").equalTo("Hoạt Hình");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Phim phim = snapshot.getValue(Phim.class);
                        arrayList.add(phim);
                        Collections.sort(arrayList, new Comparator<Phim>() {
                            @Override
                            public int compare(Phim phim, Phim t1) {
                                return phim.getTenPhim().compareTo(t1.getTenPhim());
                            }
                        });
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
