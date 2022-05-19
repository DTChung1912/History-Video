package com.example.historyvideo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmail_Activity extends AppCompatActivity {
    EditText edtEmailLogin, edtPasswordLogin;
    CheckBox cbRememberMe;
    TextView tvForgotPassword;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        edtEmailLogin = (EditText) findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = (EditText) findViewById(R.id.edtMatKhauLogin);
        tvForgotPassword = (TextView) findViewById(R.id.tvQuenMatKhau);
        doimau();

    }

    private void doimau() {
        SpannableString ss = new SpannableString("Quên Mật Khẩu? Nhấn Vào Đây");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginEmail_Activity.this, ForgotPassword_Activity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan, 15, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 15, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvForgotPassword.setText(ss);
        tvForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void clickLogin(View view) {
        Login();
    }

    private void Login() {
        mAuth = FirebaseAuth.getInstance();
        String Email = edtEmailLogin.getText().toString().trim();
        String Password = edtPasswordLogin.getText().toString().trim();
        pd = new ProgressDialog(this);
        pd.setMessage("Đang Tải Dữ Liệu...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                Toast.makeText(LoginEmail_Activity.this, "You're Logged In", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginEmail_Activity.this, Home_Activity.class);
                                pd.dismiss();
                                startActivity(intent);
                                finish();
                            }else{
                                pd.dismiss();
                                Toast.makeText(LoginEmail_Activity.this, "Tài khoản chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginEmail_Activity.this, "Tài Khoản Hoặc Mật Khẩu Không Đúng.", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }

                    }
                });

    }

    public void clickBackToStart1(View view) {
        Intent intent = new Intent(LoginEmail_Activity.this, Start_Activity.class);
        startActivity(intent);
    }


}
