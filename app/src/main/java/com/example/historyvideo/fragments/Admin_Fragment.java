package com.example.historyvideo.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.historyvideo.R;
import com.example.historyvideo.activities.All_UserActivity;
import com.example.historyvideo.activities.BannerManagerActivity;
import com.example.historyvideo.activities.CategoryActivity;
import com.example.historyvideo.activities.FeedbackManagerActivity;
import com.example.historyvideo.activities.VideoManagement_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Fragment extends Fragment {
    LinearLayout linearPhim, linearUser, linearAd, linearFeedback, linearTheLoai;
    TextView tvFeedCount;
    DatabaseReference mData;
    View view;
    private int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        InitUI();

        mData = FirebaseDatabase.getInstance().getReference("FeedBack");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.exists()) {
                        String status = ds.child("Status").getValue(String.class);
                        if (status != null && status.equals("Sent")) {
                            i++;
                        }
                        if (i > 0) {
                            tvFeedCount.setVisibility(View.VISIBLE);
                            tvFeedCount.setText("" + i);
                        } else {
                            tvFeedCount.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        linearPhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), VideoManagement_Activity.class);
                startActivity(intent);
            }
        });

        linearUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), All_UserActivity.class);
                startActivity(intent);
            }
        });

        linearAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BannerManagerActivity.class);
                startActivity(intent);
            }
        });

        linearFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FeedbackManagerActivity.class);
                startActivity(intent);
            }
        });

        linearTheLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void InitUI() {
        linearPhim = (LinearLayout) view.findViewById(R.id.linearPhim);
        linearUser = (LinearLayout) view.findViewById(R.id.linearUser);
        linearAd = (LinearLayout) view.findViewById(R.id.linearAd);
        linearFeedback = (LinearLayout) view.findViewById(R.id.linearFeedback);
        linearTheLoai = (LinearLayout) view.findViewById(R.id.linearCategory);
        tvFeedCount = (TextView) view.findViewById(R.id.tvnotiFeedbackCount);
    }
}
