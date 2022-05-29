package com.example.historyvideo.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Adapter_Banner;
import com.example.historyvideo.model.Phim;
import com.example.historyvideo.model.QuangCao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BannerManagerActivity extends AppCompatActivity {
    ListView listBanner;
    ArrayList<QuangCao> arrayList;
    ArrayList<Phim> listPhim;
    Adapter_Banner adapter;
    FloatingActionButton btnAdd;
    DatabaseReference mData;
    SearchView searchBanner;
    private Dialog dialogHoi = null;

    private String ten;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_manager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        InitUI();

        arrayList = new ArrayList<>();
        listPhim = new ArrayList<>();
        adapter = new Adapter_Banner(BannerManagerActivity.this, R.layout.banner_list_item, arrayList);
        listBanner.setAdapter(adapter);

        reloadQC();

        searchBanner.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchBanner(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchBanner(newText);
                return false;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddBanner();
            }
        });

        listBanner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final String idPhim = arrayList.get(i).getIdPhim();

                mData = FirebaseDatabase.getInstance().getReference();

                dialogHoi = new Dialog(BannerManagerActivity.this);
                dialogHoi.setCanceledOnTouchOutside(false);
                dialogHoi.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogHoi.setContentView(R.layout.dialog_remove_video);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogHoi.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialogHoi.getWindow().setAttributes(lp);

                Button btnHuy = (Button) dialogHoi.findViewById(R.id.btnHuy);
                Button btnSua = (Button) dialogHoi.findViewById(R.id.btnSua);
                Button btnSubmit = (Button) dialogHoi.findViewById(R.id.btnSubmit);

                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(BannerManagerActivity.this, EditBannerActivity.class);

                        String idPhim = arrayList.get(i).getIdPhim();
                        intent.putExtra("idPhim", idPhim);
                        startActivity(intent);
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHoi.dismiss();
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BannerManagerActivity.this);
                        builder.setTitle("Xóa Banner");
                        builder.setMessage("Bạn Muốn Xóa Banner Này?");

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mData.child("QuangCao").orderByChild("idPhim").equalTo(idPhim).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            String key = d.getKey();
                                            mData.child("QuangCao").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(BannerManagerActivity.this, "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                                                        dialogHoi.dismiss();
                                                        reloadQC();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        });

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        dialogHoi.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
                dialogHoi.show();
            }
        });
    }

    private void reloadQC() {
        mData = FirebaseDatabase.getInstance().getReference("QuangCao");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot != null) {
                    arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        QuangCao qc = ds.getValue(QuangCao.class);
                        arrayList.add(qc);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void dialogAddBanner() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_banner);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Spinner sptenPhim = (Spinner) dialog.findViewById(R.id.sptenPhim);
        final EditText edtlinkPoster = (EditText) dialog.findViewById(R.id.edtlinkAnh);
        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;
        final DatabaseReference mDataQC;

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter(dialog.getContext(), android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptenPhim.setAdapter(adapter);

        arrayList.add(0, "- Tên Phim -");

        mDataQC = FirebaseDatabase.getInstance().getReference();
        mDataQC.child("Phim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String tenphim = ds.child("tenPhim").getValue().toString();
                    arrayList.add(tenphim);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        sptenPhim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("- Tên Phim -")) {

                } else {
                    ten = adapterView.getItemAtPosition(i).toString();
                    mDataQC.child("Phim").orderByChild("tenPhim").equalTo(ten).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                key = ds.getKey();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkPoster = edtlinkPoster.getText().toString().trim();
                QuangCao qc = new QuangCao(linkPoster, key);
                mDataQC.child("QuangCao").push().setValue(qc).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BannerManagerActivity.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                            reloadQC();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(BannerManagerActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void searchBanner(final String q) {
        mData = FirebaseDatabase.getInstance().getReference("QuangCao");
        Query query = mData;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final QuangCao qc = ds.getValue(QuangCao.class);
                        String idPhim = qc.getIdPhim();
                        final DatabaseReference dataPhim = FirebaseDatabase.getInstance().getReference("Phim");
                        dataPhim.child(idPhim).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Phim phim = dataSnapshot.getValue(Phim.class);
                                    String tenphim = phim.getTenPhim().toLowerCase();
                                    if (tenphim.contains(q)) {
                                        arrayList.add(qc);
                                    }
                                }
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
        };

        query.addValueEventListener(valueEventListener);
    }

    private void InitUI() {
        listBanner = (ListView) findViewById(R.id.lvBanner);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAddBanner);
        searchBanner = (SearchView) findViewById(R.id.searchBanner);
    }

    public void clickBackToAdmin1(View view) {
        finish();
    }
}
