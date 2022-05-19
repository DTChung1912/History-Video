package com.example.historyvideo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Related_Adapter;
import com.example.historyvideo.model.Related_Phim;
import com.example.historyvideo.model.User_Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {
    private ImageView imgPoster;
    private TextView tvTenPhim;
    private TextView tvTheLoai;
    private TextView tvDienVien;
    private TextView tvViews;
    private ImageButton btnAdd, btnShare;
    private ArrayList<Related_Phim> arrayList;
    private Related_Adapter adapter;
    private ExpandableTextView expandableTextView;
    private LinearLayout layout;
    private RecyclerView rcRelated;
    private DatabaseReference mDataLink;
    private String tp;

    Context context;
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Phim");

    Long luotxem;
    String theLoai;
    String phimID;
    String link_Phim;
    String link_Sub;
    private String link;
    private boolean processWatchLater = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window ww = getWindow(); // in Activity's onCreate() for instance
            ww.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        context = this;

        khoiTao();
        arrayList = new ArrayList<>();
        adapter = new Related_Adapter(arrayList, context);
        rcRelated.setAdapter(adapter);
        rcRelated.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        fillDetail();
        searchtheLoai();
        checkWatch_Later();
        mDataLink = FirebaseDatabase.getInstance().getReference();
        mDataLink.child("Link").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& dataSnapshot != null){
                    link = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Morning Of Owl");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Hãy tải ứng dụng tại link "+link+"\n để xem phim "+tp+" nhé !");
                startActivity(Intent.createChooser(shareIntent, "Chia Sẻ"));
            }
        });
    }

    private void checkWatch_Later() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        final String idPhim = intent.getStringExtra("phim_UID");
        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Watch_Later");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(idPhim)) {
                    btnAdd.setImageDrawable(getDrawable(R.drawable.ic_baseline_playlist_add_check_24px));
                    btnAdd.setBackground(getDrawable(R.drawable.elip_button_with_opacity_added));
                } else {
                    btnAdd.setImageDrawable(getDrawable(R.drawable.ic_baseline_playlist_add_24px));
                    btnAdd.setBackground(getDrawable(R.drawable.elip_button_with_opacity));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void fillDetail() {

        Intent intent = getIntent();
        String phim_UID = intent.getStringExtra("phim_UID");
        phimID = phim_UID;
        mData.child(phim_UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                Map<Long, Long> view = (Map<Long, Long>) dataSnapshot.getValue();

                String ten = map.get("tenPhim");
                tp = ten;
                String linkPhim = map.get("linkPhim");
                link_Phim = linkPhim;
                String linkSub = map.get("linksub");
                link_Sub = linkSub;
                String link_poster = map.get("posterPhim");
                String mota = map.get("motaPhim");
                String dienvien = map.get("dienvienPhim");
                String theloai = map.get("theloaiPhim");
                Long views = view.get("soluotXem");
                luotxem = views;
                tvTenPhim.setText(ten);
                tvTheLoai.setText(theloai);
                if(theloai.equals("Hoạt Hình")){
                    tvDienVien.setText("Lồng Tiếng: " + dienvien);
                }else{
                    tvDienVien.setText("Diễn Viên: " + dienvien);
                }
                expandableTextView.setText(mota);
                tvViews.setText("Lượt Xem: " + views);

                Picasso.get().load(link_poster)
                        .into(imgPoster);


                Picasso.get().load(link_poster).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        layout.setBackground(new BitmapDrawable(bitmap));
                        Bitmap blurImage = BlurImage(bitmap);

                        layout.setBackground(new BitmapDrawable(blurImage));
                        PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        layout.getBackground().setColorFilter(greyFilter);

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }


                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Bitmap BlurImage(Bitmap input) {
        RenderScript rsScript = RenderScript.create(context);
        Allocation alloc = Allocation.createFromBitmap(rsScript, input);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
        blur.setRadius(25);
        blur.setInput(alloc);

        Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);
        blur.forEach(outAlloc);
        outAlloc.copyTo(result);

        rsScript.destroy();
        return result;
    }



    public void khoiTao() {
        btnShare = (ImageButton)findViewById(R.id.btnShare);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        tvTenPhim = (TextView) findViewById(R.id.tvName);
        tvTheLoai = (TextView) findViewById(R.id.tvTheLoai);
        tvDienVien = (TextView) findViewById(R.id.tvDienVien);
        tvViews = (TextView) findViewById(R.id.tvSoLuotXem);
        layout = (LinearLayout) findViewById(R.id.layoutDetail);
        expandableTextView = (ExpandableTextView) findViewById(R.id.expandable_textView);
        rcRelated = (RecyclerView) findViewById(R.id.relatedRecycler);
    }

    private void searchtheLoai() {
        Intent intent = getIntent();
        String phim_UID = intent.getStringExtra("phim_UID");
        phimID = phim_UID;
        mData.child(phim_UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String theloai = map.get("theloaiPhim");
                readData(theloai);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readData(String tl) {

        Query query = mData.orderByChild("theloaiPhim").equalTo(tl).limitToLast(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String idPhim = snapshot.child("idPhim").getValue().toString();
                    String tenPhim = snapshot.child("tenPhim").getValue().toString();
                    String linkPhim = snapshot.child("linkPhim").getValue().toString();
                    String linkSub = snapshot.child("linksub").getValue().toString();
                    String posterPhim = snapshot.child("posterPhim").getValue().toString();
                    String theloaiPhim = snapshot.child("theloaiPhim").getValue().toString();
                    String motaPhim = snapshot.child("motaPhim").getValue().toString();
                    String dienvienPhim = snapshot.child("dienvienPhim").getValue().toString();
                    String luotxem = snapshot.child("soluotXem").getValue().toString();


                    if (!snapshot.child("idPhim").getValue().toString().equals(phimID)) {
                        Collections.sort(arrayList, new Comparator<Related_Phim>() {
                            @Override
                            public int compare(Related_Phim related_phim, Related_Phim t1) {
                                return related_phim.getTenPhim().compareTo(t1.getTenPhim());
                            }
                        });
                        arrayList.add(new Related_Phim(idPhim, tenPhim, linkPhim, linkSub, posterPhim, theloaiPhim, motaPhim, dienvienPhim, Long.parseLong(luotxem)));
                    }
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void clickplayPhim(View view) {
        Intent intent = new Intent(DetailActivity.this, ExoPlayerActivity.class);
        intent.putExtra("Link", link_Phim);
        intent.putExtra("Link_Sub", link_Sub);
        startActivity(intent);
        update_luotXem();
        ghiLog();
    }



    public void ghiLog(){
        DatabaseReference dataLog = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = (cal.get(Calendar.MONTH) + 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String timeStamp = "Ngày "+day+" tháng "+month+" năm "+ year+ " lúc "+hour+"h:"+minute;
        String movieWatched = tvTenPhim.getText().toString();
//        dataLog.child("UserLog").child(uid).child("dateTime").setValue(timeStamp);
//        dataLog.child("UserLog").child(uid).child("movieWatched").setValue(tvTenPhim.getText().toString());
       String key= dataLog.child("UserLog").push().getKey();
        User_Log user_log = new User_Log(timeStamp,movieWatched);
       dataLog.child("UserLog").child(uid).child(key).setValue(user_log).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){

               }
           }
       });
    }


    public void update_luotXem() {
        Intent intent = getIntent();
        final String key = intent.getStringExtra("phim_UID");

        FirebaseDatabase.getInstance().getReference("Phim").
                child(key).
                child("soluotXem").
                setValue(luotxem+1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void clickBackToSomeThing(View view) {
        super.onBackPressed();
    }

    public void clickXemSau(View view) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        final String idPhim = intent.getStringExtra("phim_UID");
        final String tenPhim = tvTenPhim.getText().toString();
        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Watch_Later");

        processWatchLater = true;


        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (processWatchLater) {
                    if (dataSnapshot.hasChild(idPhim)) {
                        mData.child(idPhim).removeValue();
                        btnAdd.setImageDrawable(getDrawable(R.drawable.ic_baseline_playlist_add_24px));
                        btnAdd.setBackground(getDrawable(R.drawable.elip_button_with_opacity));
                        Toast.makeText(context, "Đã xóa "+tenPhim+" ra khỏi danh sách xem sau", Toast.LENGTH_SHORT).show();
                        processWatchLater = false;
                    } else {
                        mData.child(idPhim).setValue(idPhim);
                        btnAdd.setImageDrawable(getDrawable(R.drawable.ic_baseline_playlist_add_check_24px));
                        btnAdd.setBackground(getDrawable(R.drawable.elip_button_with_opacity_added));
                        Toast.makeText(context, "Đã thêm "+tenPhim+" vào danh sách xem sau", Toast.LENGTH_SHORT).show();
                        processWatchLater = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

