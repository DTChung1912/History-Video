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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class Start_Activity extends AppCompatActivity {
    private TextView tvReg, loginFacebook;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private static final String TAG = "FACE_LOG";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        tvReg = (TextView) findViewById(R.id.tvReg);
        loginFacebook = (TextView) findViewById(R.id.loginFacebook);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        SpannableString ss = new SpannableString("Bạn chưa có tài khoản? Đăng Ký");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Start_Activity.this, Reg_Activity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvReg.setText(ss);
        tvReg.setMovementMethod(LinkMovementMethod.getInstance());


        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginwithFacebook();
            }
        });

    }

    private void loginwithFacebook() {
        showProgressDialog();
        LoginManager.getInstance().logInWithReadPermissions(Start_Activity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                closeProgressDialog();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                closeProgressDialog();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            
                            updateUI();
                            createDatabase();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Start_Activity.this, "Login Failed !", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void createDatabase() {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user= mAuth.getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();
        String name = user.getDisplayName().toString();
        String photo_url = user.getPhotoUrl().toString();
        String uri = photo_url +"?height=400";

        mData.child(uid).child("Email").setValue(email);
        mData.child(uid).child("HoTen").setValue(name);
        mData.child(uid).child("Image").setValue(uri);
        mData.child(uid).child("SDT").setValue("facebook_default_phone_number");

    }

    private void updateUI() {
            Toast.makeText(this, "You're Logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Start_Activity.this, Home_Activity.class);
            startActivity(intent);
            finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void clickLoginWithEmail(View view) {
        Intent intent = new Intent(Start_Activity.this, LoginEmail_Activity.class);
        startActivity(intent);
    }
    public void showProgressDialog() {
        try {
            if (pd == null) {
                pd = ProgressDialog.show(this, "Loading", "Đang Xử Lý Dữ Liệu...", true, true);
            }
        } catch (Exception e) {
            Log.e("Error", "" + e.getMessage());
        }
    }
    public void closeProgressDialog() {
        try {
            if (pd != null) {
                pd.dismiss();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}
