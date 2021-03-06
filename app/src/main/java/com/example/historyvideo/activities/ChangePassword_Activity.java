package com.example.historyvideo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.historyvideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword_Activity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText edtCurrentPassword, edtNewPassword, edtReenterPassword;
    Button btnChange;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_);
        edtCurrentPassword = (EditText) findViewById(R.id.edtCurrenPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtReenterPassword = (EditText) findViewById(R.id.edtReenterPassword);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setEnabled(false);



        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentPass = edtCurrentPassword.getText().toString().trim();
                String newPass = edtNewPassword.getText().toString().trim();
                String reenterPass = edtReenterPassword.getText().toString().trim();

                btnChange.setEnabled(!currentPass.isEmpty() && !newPass.isEmpty() && !reenterPass.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edtCurrentPassword.addTextChangedListener(tw);
        edtNewPassword.addTextChangedListener(tw);
        edtReenterPassword.addTextChangedListener(tw);
    }

    private void DoiMatKhau() {
        String eMail = user.getEmail().toString();
        String currentPass = edtCurrentPassword.getText().toString().trim();
        final String newPass = edtNewPassword.getText().toString().trim();
        final String reenterPass = edtReenterPassword.getText().toString().trim();
        AuthCredential credential = EmailAuthProvider.getCredential(eMail, currentPass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (newPass.equals(reenterPass)) {
                        user.updatePassword(newPass)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePassword_Activity.this, "?????i M???t Kh???u Th??nh C??ng", Toast.LENGTH_SHORT).show();
                                        }
                                        closeProgressDialog();
                                    }
                                });
                    } else {
                        closeProgressDialog();
                        Toast.makeText(ChangePassword_Activity.this, "M???t Kh???u Nh???p L???i Ph???i Kh???p V???i M???t Kh???u M???i", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    closeProgressDialog();
                    Toast.makeText(ChangePassword_Activity.this, "M???t Kh???u Hi???n T???i Kh??ng ????ng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void clickChange(View view) {
        showProgressDialog();
        DoiMatKhau();
    }

    public void BackToPrevious(View view) {
        Intent intent = new Intent(ChangePassword_Activity.this, Home_Activity.class);
        startActivity(intent);
    }

    public void showProgressDialog() {
        try {
            if (pd == null) {
                pd = ProgressDialog.show(this, "Loading", "??ang X??? L?? D??? Li???u...", true, true);
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
