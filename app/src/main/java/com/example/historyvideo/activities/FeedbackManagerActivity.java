package com.example.historyvideo.activities;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Adapter_Feedback;
import com.example.historyvideo.model.FeedBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackManagerActivity extends AppCompatActivity {
    ArrayList<FeedBack> arrayList;
    Adapter_Feedback adapter;
    ListView lvFeedBack;
    SearchView searchView;

    DatabaseReference mData;

    private String from;
    private String date;
    private String content;
    private String contentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_manager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        InitUI();
        arrayList = new ArrayList<>();
        adapter = new Adapter_Feedback(FeedbackManagerActivity.this, R.layout.feedback_list_item, arrayList);
        lvFeedBack.setAdapter(adapter);
        mData = FirebaseDatabase.getInstance().getReference("FeedBack");

        ReloadData();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFB(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFB(newText);
                return false;
            }
        });

        lvFeedBack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FeedBack fb = arrayList.get(i);
                String key = fb.Key;
                from = fb.Email;
                date = fb.At;
                content = fb.Content;

                if(from != null && date != null && content != null) {
                    diaLogDetail();
                }
                mData.child(key).child("Status").setValue("Seen");

            }
        });

    }

    private void searchFB(final String q){
        mData = FirebaseDatabase.getInstance().getReference("FeedBack");
        Query query = mData;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot != null){
                    arrayList.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        FeedBack fb = ds.getValue(FeedBack.class);
                        if(fb.Content.contains(q)){
                            arrayList.add(fb);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListener);

    }

    private void diaLogDetail() {
        final Dialog dialog = new Dialog(FeedbackManagerActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback_content);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        Button btnHuy = (Button) dialog.findViewById(R.id.btnDong);
        TextView tvFrom= (TextView)dialog.findViewById(R.id.tvFrom);
        TextView tvDate=(TextView)dialog.findViewById(R.id.tvDate);
        EditText edtContent = (EditText)dialog.findViewById(R.id.edtContent);


        tvFrom.setText("<"+from+">");
        tvDate.setText(date);
        edtContent.setText(content);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ReloadData() {
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FeedBack fb = ds.getValue(FeedBack.class);
                    arrayList.add(fb);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitUI() {
        lvFeedBack = (ListView) findViewById(R.id.lvFeedback);
        searchView = (SearchView) findViewById(R.id.searchFeedBack);
    }

    public void clickBackToAdmin1(View view) {
        finish();
    }
}
