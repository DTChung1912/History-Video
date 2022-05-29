package com.example.historyvideo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.historyvideo.R;
import com.example.historyvideo.model.QuangCao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditBannerActivity extends AppCompatActivity {
    EditText edtLinkPoster;
    Spinner spTenPhim;
    Button btnHuy, btnSua;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    private String ten;
    private String tenPhim;
    private String key;

    DatabaseReference mDataQC;
    DatabaseReference mDataPhim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_banner);
        Window w = this.getWindow();
        w.setStatusBarColor(getResources().getColor(R.color.gray_dark));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initUI();
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(EditBannerActivity.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTenPhim.setAdapter(adapter);
        Intent intent = getIntent();
        final String idPhim = intent.getStringExtra("idPhim");
        mDataQC = FirebaseDatabase.getInstance().getReference("QuangCao");
        mDataQC.orderByChild("idPhim").equalTo(idPhim).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    mDataQC.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            QuangCao qc = dataSnapshot.getValue(QuangCao.class);
                            String linkanh = qc.getLinkAnh();
                            edtLinkPoster.setText(linkanh);
                            String id = qc.getIdPhim();
                            mDataPhim.child(id).child("tenPhim").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ten = dataSnapshot.getValue().toString();
                                    arrayList.add(0, ten);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDataPhim = FirebaseDatabase.getInstance().getReference("Phim");
        mDataPhim.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String tp = ds.child("tenPhim").getValue().toString();
                    if (!tp.equals(ten)) {
                        arrayList.add(tp);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        spTenPhim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tenPhim = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBanner(idPhim);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void editBanner(final String idp) {

        mDataQC.orderByChild("idPhim").equalTo(idp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String linkAnh = edtLinkPoster.getText().toString().trim().toLowerCase();
                    String key = ds.getKey();
                    DatabaseReference mDataBannerRef = FirebaseDatabase.getInstance().getReference("QuangCao").child(key);

                    mDataBannerRef.child("idPhim").setValue(idp);
                    mDataBannerRef.child("linkAnh").setValue(linkAnh);
                    Toast.makeText(EditBannerActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditBannerActivity.this, BannerManagerActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initUI() {
        edtLinkPoster = (EditText) findViewById(R.id.edtlinkAnh);
        spTenPhim = (Spinner) findViewById(R.id.sptenPhim);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        btnSua = (Button) findViewById(R.id.btnThem);
    }
}
