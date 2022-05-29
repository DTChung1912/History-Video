package com.example.historyvideo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Reg_Activity extends AppCompatActivity {
    private EditText edtHoTen, edtEmail, edtMatKhau, edtSDT;
    private ProgressBar progressBar;
    public FirebaseAuth mAuth;
    DatabaseReference mData;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        edtHoTen = (EditText) findViewById(R.id.edtHoTen);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMatKhau = (EditText) findViewById(R.id.edtMatKhau);
        edtSDT = (EditText) findViewById(R.id.edtSDT);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
        }
    }

    private void DangKy() {
        pd = new ProgressDialog(Reg_Activity.this);
        pd.setTitle("Đang Xử Lý Dữ Liệu");
        pd.setMessage("Vui Lòng Chờ Trong Giây Lát...");
        pd.setCanceledOnTouchOutside(false);

        String eMail = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        if (!eMail.equals("") && !matKhau.equals("")) {
            pd.show();
            mAuth.createUserWithEmailAndPassword(eMail, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        pd.dismiss();
                        String hoTen = edtHoTen.getText().toString().trim();
                        String eMail = edtEmail.getText().toString().trim();
                        String SDT = edtSDT.getText().toString().trim();
                        if (!hoTen.equals("") && !eMail.equals("") && !SDT.equals("")) {
                            innerReg(hoTen, eMail, SDT);
                            pd.dismiss();
                        } else {
                            Toast.makeText(Reg_Activity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pd.dismiss();
                        switch (task.getException().toString()) {
                            case "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.":
                                Toast.makeText(Reg_Activity.this, "Email không hợp lệ, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                break;
                            case "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]":
                                Toast.makeText(Reg_Activity.this, "Mật khẩu phải chứa ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                                break;
                            case "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.":
                                Toast.makeText(Reg_Activity.this, "Email đã có người sử dụng", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBackToStart(View view) {
        Intent intent = new Intent(Reg_Activity.this, Start_Activity.class);
        startActivity(intent);
    }

    public void innerReg(String hovaten, String mail, String sdt) {
        Users users = new Users(hovaten, mail, sdt, "Default_Image", "Default", "Not Actived");
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid())
                .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Reg_Activity.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Reg_Activity.this, "Vui Lòng Kích Hoạt Email", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Reg_Activity.this, Start_Activity.class);
                                startActivity(intent);
                                checkOffline();
                                mAuth.signOut();
                                finish();
                            }
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(Reg_Activity.this, "Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickReg(View view) {
        DangKy();

    }

    private void checkOffline() {
        mData = FirebaseDatabase.getInstance().getReference("Users");
        String userID = mAuth.getCurrentUser().getUid();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = (cal.get(Calendar.MONTH) + 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (mAuth.getCurrentUser() != null) {
            mData.child(userID).child("Status").setValue("Offline");
            mData.child(userID).child("Last_Active").setValue("Ngày "
                    + day
                    + " tháng "
                    + month + " năm "
                    + year
                    + " lúc " + hour
                    + "h:" + minute);
        }
    }
}
