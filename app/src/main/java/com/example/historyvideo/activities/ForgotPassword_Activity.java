package com.example.historyvideo.activities;

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

import com.example.historyvideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_Activity extends AppCompatActivity {
    EditText edtForgot;
    ProgressBar pgBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Window w = this.getWindow();
        w.setStatusBarColor(getResources().getColor(R.color.gray_dark));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edtForgot = (EditText) findViewById(R.id.edtEmailForgot);
        pgBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void BackToStart(View view) {
        Intent intent = new Intent(ForgotPassword_Activity.this, Start_Activity.class);
        startActivity(intent);
    }

    public void clickReset(View view) {
        pgBar.setVisibility(View.VISIBLE);
        if (!edtForgot.getText().toString().equals("")) {
            reset();
        } else {
            Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
            edtForgot.requestFocus();
            pgBar.setVisibility(View.GONE);
        }

    }

    private void reset() {
        mAuth = FirebaseAuth.getInstance();
        String Email = edtForgot.getText().toString().trim();
        mAuth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ForgotPassword_Activity.this, "Hoàn Tất Quá Trình, Vui Lòng Kiểm Tra Email Để Cập Nhật Mật Khẩu Mới.", Toast.LENGTH_SHORT).show();
                        pgBar.setVisibility(View.GONE);
                    }
                });
    }
}
