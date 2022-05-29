package com.example.historyvideo.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Banner_Adapter;
import com.example.historyvideo.adapter.Recyclerview_Data_Adapter;
import com.example.historyvideo.model.Phim;
import com.example.historyvideo.model.QuangCao;
import com.example.historyvideo.model.SectionDataPhim;
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

import me.relex.circleindicator.CircleIndicator;

public class Home_Fragment extends Fragment {
    DatabaseReference mData;
    DatabaseReference mDataCate;
    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    Banner_Adapter adapter;
    ArrayList<QuangCao> arrayList;
    List<String> listKey;
    Runnable runnable;
    Handler handler;
    ArrayList<String> listTheLoai;
    ArrayList<SectionDataPhim> allSampleData;
    int currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.indicatorDefault);
        arrayList = new ArrayList<>();
        listKey = new ArrayList<>();
        adapter = new Banner_Adapter(getActivity(), arrayList);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        getDataQuangCao();
        populatePhim();
        return view;
    }

    private void populatePhim() {
        mDataCate = FirebaseDatabase.getInstance().getReference("TheLoai");
        mDataCate.orderByChild("TenTheLoai").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTheLoai = new ArrayList<>();
                if(dataSnapshot.exists() && dataSnapshot != null){
                    listTheLoai.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String tentl = ds.child("TenTheLoai").getValue(String.class);
                        listTheLoai.add(tentl);
                    }
                    Collections.sort(listTheLoai, new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return s.compareTo(t1);
                        }
                    });
                    for(int i = 0;i <listTheLoai.size(); i++){
                        initPhim(listTheLoai.get(i));
                    }
                    Log.d("TAGGG", ""+listTheLoai);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initPhim(String theloai) {

        allSampleData = new ArrayList<>();
        RecyclerView myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        myRecyclerView.setHasFixedSize(true);
        final Recyclerview_Data_Adapter adapter1 = new Recyclerview_Data_Adapter(view.getContext(), allSampleData);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setNestedScrollingEnabled(false);
        myRecyclerView.setAdapter(adapter1);

        final SectionDataPhim dm = new SectionDataPhim();

        dm.setHeaderTitle(theloai);

        final DatabaseReference mDataPhim;
        mDataPhim = FirebaseDatabase.getInstance().getReference("Phim");
        Query query = mDataPhim.orderByChild("theloaiPhim").equalTo(theloai);

        query.addValueEventListener(new ValueEventListener() {
            ArrayList<Phim> singleItem = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot!= null) {
                    singleItem.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Phim phim = snapshot.getValue(Phim.class);
                        singleItem.add(phim);
                        Collections.sort(singleItem, new Comparator<Phim>() {
                            @Override
                            public int compare(Phim phim, Phim t1) {
                                return phim.getTenPhim().compareTo(t1.getTenPhim());
                            }
                        });
                    }
                    dm.setAllPhimSections(singleItem);
                    allSampleData.add(dm);
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void getDataQuangCao() {
        mData = FirebaseDatabase.getInstance().getReference("QuangCao");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        mData.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                QuangCao qc = dataSnapshot.getValue(QuangCao.class);
                                arrayList.add(qc);
                                adapter.notifyDataSetChanged();
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


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItem = viewPager.getCurrentItem();
                currentItem++;
                if (currentItem >= viewPager.getAdapter().getCount()) {
                    currentItem = 0;
                }
                viewPager.setCurrentItem(currentItem, true);
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}