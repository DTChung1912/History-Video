package com.example.historyvideo.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Adapter_Category;
import com.example.historyvideo.model.TheLoai;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private ListView lvCate;
    //private FloatingActionButton btnAdd;
    DatabaseReference mData;

    ArrayList<TheLoai> arrayList;
    Adapter_Category adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window w = this.getWindow();
        w.setStatusBarColor(ContextCompat.getColor(this,R.color.gray_dark));
        
        initUI();
        arrayList = new ArrayList<>();
        adapter = new Adapter_Category(CategoryActivity.this,R.layout.cate_item_list,arrayList);
        lvCate.setAdapter(adapter);

        mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        mData.orderByChild("TenTheLoai").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        TheLoai theLoai = ds.getValue(TheLoai.class);
                        arrayList.add(theLoai);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initUI() {
        lvCate = (ListView)findViewById(R.id.lvCategory);
        //btnAdd = (FloatingActionButton)findViewById(R.id.btnAddCate);
    }

    public void clickBacktoMore(View view) {
        finish();
    }
}
