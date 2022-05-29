package com.example.historyvideo.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.historyvideo.R;
import com.example.historyvideo.activities.AccountActivity;
import com.example.historyvideo.activities.DetailBaseByCategoryActivity;
import com.example.historyvideo.activities.Start_Activity;
import com.example.historyvideo.activities.Watch_Later_Activity;
import com.example.historyvideo.adapter.Expandablelist_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class More_Fragment extends Fragment {
    private ExpandableListView listView;
    private Expandablelist_Adapter adapter;
    private List<String> listHeader;
    private HashMap<String, List<String>> listHash;
    private FirebaseAuth mAuth;
    private DatabaseReference mData;
    private FirebaseUser mUser;

    private String tentl;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        listView = (ExpandableListView) view.findViewById(R.id.lvMore);
        initData();
        adapter = new Expandablelist_Adapter(getContext(), listHeader, listHash);
        listView.setAdapter(adapter);
        listView.expandGroup(0);
        listView.expandGroup(2);
        final Intent intent = new Intent(getActivity(), DetailBaseByCategoryActivity.class);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                Intent intentWL = new Intent(getContext(), Watch_Later_Activity.class);
                                startActivity(intentWL);
                                break;
                            case 1:
                                Intent intentAccount = new Intent(getContext(), AccountActivity.class);
                                startActivity(intentAccount);
                                break;
                            case 2:
                                checkOffline();
                                mAuth.signOut();
                                Toast.makeText(getContext(), "Vui Lòng Đăng Nhập Để Tiếp Tục Sử Dụng", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getContext(), Start_Activity.class);
                                startActivity(intent1);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 1:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 2:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 3:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 4:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 5:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 6:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 7:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 8:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 9:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                            case 10:
                                truyenIntent(childPosition);
                                intent.putExtra("tenTL", tentl);
                                startActivity(intent);
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                dialogFeedback();
                                break;
                        }
                        break;
                }
                return true;
            }
        });
        return view;
    }

    private void truyenIntent(int cp) {
        tentl = listHash.get(listHeader.get(1)).get(cp);
    }

    private void checkOffline() {
        DatabaseReference mDataUser = FirebaseDatabase.getInstance().getReference("Users");
        String userID = mAuth.getCurrentUser().getUid();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = (cal.get(Calendar.MONTH) + 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (mAuth.getCurrentUser() != null) {
            mDataUser.child(userID).child("Status").setValue("Offline");
            mDataUser.child(userID).child("Last_Active").setValue("Ngày "
                    + day
                    + " tháng "
                    + month + " năm "
                    + year
                    + " lúc " + hour
                    + "h:" + minute);
        }
    }

    private void dialogFeedback() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = FirebaseAuth.getInstance().getCurrentUser();

                EditText edtContent = (EditText) dialog.findViewById(R.id.edtContent);
                String feedbackContent = edtContent.getText().toString();
                if (feedbackContent.equals("")) {
                    Toast.makeText(getContext(), "Nội dung feedback không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    submitFeedback(feedbackContent);
                    edtContent.setText("");
                    Toast.makeText(getContext(), "Cảm ơn bạn đã đóng góp ý kiến", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void submitFeedback(final String content) {
        DatabaseReference mDataFeedBack;
        final DatabaseReference mData;
        FirebaseUser user;
        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = mUser.getEmail();
        final String userID = user.getUid();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month_of_year = cal.get(Calendar.MONTH);
        int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String pushKey = mData.child("FeedBack").push().getKey();
        mDataFeedBack = mData.child("FeedBack").child(pushKey);

        mDataFeedBack.child("Content").setValue(content);
        mDataFeedBack.child("Email").setValue(email);
        mDataFeedBack.child("At").setValue("Ngày "
                + day_of_month
                + " tháng "
                + month_of_year + " năm "
                + year
                + " lúc " + hour
                + "h:" + minute);
        mDataFeedBack.child("Status").setValue("Sent");
        mDataFeedBack.child("Key").setValue(pushKey);
    }

    private void initData() {
        listHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listHeader.add("Tài Khoản");
        listHeader.add("Phim");
        listHeader.add("Hỗ Trợ");


        List<String> taiKhoan = new ArrayList<>();
        taiKhoan.add("Danh Sách Xem Sau");
        taiKhoan.add("Thông Tin");
        taiKhoan.add("Đăng Xuất");

        final ArrayList<String> theloaiPhim = new ArrayList<>();
        mData.orderByChild("TenTheLoai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    theloaiPhim.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String tl = snapshot.child("TenTheLoai").getValue().toString();
                        theloaiPhim.add(tl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        List<String> hotro = new ArrayList<>();
        hotro.add("Feedback");
        hotro.add("About Us");

        listHash.put(listHeader.get(0), taiKhoan);
        listHash.put(listHeader.get(1), theloaiPhim);
        listHash.put(listHeader.get(2), hotro);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String email = firebaseUser.getEmail();
    }
}
