package com.example.historyvideo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Phim_InfoActivity extends AppCompatActivity {
    EditText edtID, edtTen, edtLink, edtSub, edtPoster, edtCate, edtDes, edtActor;
    Spinner spTheLoai;
    DatabaseReference mData;
    String key;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    private String theloai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phim__info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        InitUI();

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter(Phim_InfoActivity.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheLoai.setAdapter(adapter);

        edtID.setFocusable(false);
        edtID.setClickable(false);

        Intent intent = getIntent();
        key = intent.getStringExtra("Phim_UID");
        mData = FirebaseDatabase.getInstance().getReference("Phim").child(key);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayList.add(0, theloai);
                if (dataSnapshot.exists()) {
                    String id = dataSnapshot.child("idPhim").getValue().toString();
                    String ten = dataSnapshot.child("tenPhim").getValue().toString();
                    String link = dataSnapshot.child("linkPhim").getValue().toString();
                    String sub = dataSnapshot.child("linksub").getValue().toString();
                    String poster = dataSnapshot.child("posterPhim").getValue().toString();
                    String mota = dataSnapshot.child("motaPhim").getValue().toString();
                    theloai = dataSnapshot.child("theloaiPhim").getValue().toString();
                    String dienvien = dataSnapshot.child("dienvienPhim").getValue().toString();

                    edtID.setText(id);
                    edtTen.setText(ten);
                    edtLink.setText(link);
                    edtSub.setText(sub);
                    edtPoster.setText(poster);
                    edtDes.setText(mota);
                    edtActor.setText(dienvien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        fillCate();
        spTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tl = arrayList.get(i);
                theloai = tl;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void fillCate() {
        DatabaseReference mDataCate = FirebaseDatabase.getInstance().getReference("TheLoai");
        mDataCate.orderByChild("TenTheLoai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayList.add(0, theloai);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String tl = ds.child("TenTheLoai").getValue().toString();
                        if (tl != null && !tl.equals(theloai)) {
                            arrayList.add(tl);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InitUI() {
        edtID = (EditText) findViewById(R.id.edtID);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtLink = (EditText) findViewById(R.id.edtLink);
        edtSub = (EditText) findViewById(R.id.edtLinkSub);
        edtPoster = (EditText) findViewById(R.id.edtPoster);
        spTheLoai = (Spinner) findViewById(R.id.spTheLoai1);
        edtDes = (EditText) findViewById(R.id.edtMota);
        edtActor = (EditText) findViewById(R.id.edtDienVien);
    }

    public void clickBackToAdmin(View view) {
        finish();
    }

    public void clickClear(View view) {
        edtID.setText("");
        edtTen.setText("");
        edtLink.setText("");
        edtSub.setText("");
        edtPoster.setText("");
        edtDes.setText("");
        edtActor.setText("");
    }

    public void clickSavePhim(View view) {
        String name = edtTen.getText().toString().trim();
        String link = edtLink.getText().toString().trim();
        String sub = edtSub.getText().toString().trim();
        String poster = edtPoster.getText().toString().trim();

        String des = edtDes.getText().toString().trim();
        String actor = edtActor.getText().toString().trim();

        mData.child("tenPhim").setValue(name);
        mData.child("linkPhim").setValue(link);
        mData.child("linksub").setValue(sub);
        mData.child("posterPhim").setValue(poster);
        mData.child("motaPhim").setValue(des);
        mData.child("theloaiPhim").setValue(theloai);
        mData.child("dienvienPhim").setValue(actor);

        Toast.makeText(this, "Cập Nhật Thông Tin Phim Thành Công", Toast.LENGTH_SHORT).show();
    }
}
