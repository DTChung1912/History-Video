package com.example.historyvideo.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Phim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerAddPhim_Activity extends AppCompatActivity {
    DatabaseReference mData;
    EditText edtTenPhim, edtLinkPhim, edtLinkSub, edtLinkPoster, edtMoTa, edtDienVien;
    Spinner spTheLoai;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    private String theLoai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_phim);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        initUI();

        arrayList= new ArrayList<>();
        adapter = new ArrayAdapter(ManagerAddPhim_Activity.this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheLoai.setAdapter(adapter);

        fillCategory();
        spTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tl = arrayList.get(i);
                theLoai = tl;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fillCategory() {
        DatabaseReference mDataCate = FirebaseDatabase.getInstance().getReference("TheLoai");
        mDataCate.orderByChild("TenTheLoai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayList.add(0,"- Chọn Thể Loại -");
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String tl = ds.child("TenTheLoai").getValue().toString();
                        arrayList.add(tl);
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
        edtTenPhim = (EditText) findViewById(R.id.edtTen);
        edtLinkPhim = (EditText) findViewById(R.id.edtLink);
        edtLinkSub = (EditText) findViewById(R.id.edtLinkSub);
        edtLinkPoster = (EditText) findViewById(R.id.edtPoster);
        edtMoTa = (EditText) findViewById(R.id.edtMota);
        edtDienVien = (EditText) findViewById(R.id.edtDienVien);
        spTheLoai = (Spinner)findViewById(R.id.spTheLoai);

    }

    public void clickUp(View view) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Đang xử lý dữ liệu...");
        pd.show();
        themPhim();
        pd.dismiss();
    }

    private void themPhim() {

        String tenPhim = edtTenPhim.getText().toString().trim();
        String linkPhim = edtLinkPhim.getText().toString().trim();
        String linkSub = edtLinkSub.getText().toString().trim();
        String linkPoster = edtLinkPoster.getText().toString().trim();

        String moTa = edtMoTa.getText().toString().trim();
        String dienVien = edtDienVien.getText().toString().trim();
        Long luotXem = Long.parseLong("0");


        mData = FirebaseDatabase.getInstance().getReference("Phim");
        mData.push();
        String idPhim = mData.push().getKey();
        Phim phim = new Phim(idPhim, tenPhim, linkPhim, linkSub, linkPoster, theLoai, moTa, dienVien, luotXem);


        mData.child(idPhim).setValue(phim).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    clearText();
                    Toast.makeText(ManagerAddPhim_Activity.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearText() {
        edtTenPhim.setText("");
        edtLinkPhim.setText("");
        edtLinkSub.setText("");
        edtLinkPoster.setText("");

        edtMoTa.setText("");
        edtDienVien.setText("");

    }

    public void clickClear(View view) {
        clearText();
    }

    public void clickBackToMain(View view) {
        finish();
    }
}
