package com.example.historyvideo.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.historyvideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String provider = String.valueOf(user.getProviderData().get(0));
    String UID = user.getUid();
    String eMail = user.getEmail();
    DatabaseReference userDatabase;
    String download;
    private static final int GALLERY_PICK = 1;

    private StorageReference imageStorageData;

    private ProgressDialog pd;

    CircleImageView profileImage;
    EditText edtDisplayName, edtDisplayEmail, edtPhone, edtPassword;
    TextInputLayout tilPhone, tilPassword;
    TextView tvPhoto,tvTitle;
    ImageButton btnChangePass;
    Button btnSave;

    static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    static final String FILE_NAMING_PREFIX = "JPEG_";
    static final String FILE_NAMING_SUFFIX = "_";
    static final String FILE_FORMAT = ".jpg";
    static final String AUTHORITY_SUFFIX = ".cropper.fileprovider";

    private Uri outputUri;

    private final ActivityResultLauncher<Uri> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), this::onTakePictureResult);

    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), this::onCropImageResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        mData = FirebaseDatabase.getInstance().getReference("Users");
        initUI();


        Log.d("PROVIDERRRR", "" + provider);

        if (provider.equals("facebook.com")) {

            fillInfoFacebook();
        }
        else if(provider.equals("password")) {

            fillInfo();
        }
    }

    private void dialogChangePass() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changepassword);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        ImageButton tvThoat = (ImageButton) dialog.findViewById(R.id.tvThoat);
        final EditText edtMKHT = (EditText) dialog.findViewById(R.id.edtMKHT);
        final EditText edtMKM = (EditText) dialog.findViewById(R.id.edtMKM);
        final EditText edtNLMK = (EditText) dialog.findViewById(R.id.edtNLMK);
        final Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);


        final String mkhientai = edtMKHT.getText().toString().trim();
        final String mkmoi = edtMKM.getText().toString().trim();
        final String mknhaplai = edtNLMK.getText().toString().trim();

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = edtMKHT.getText().toString();
                String newpw = edtMKM.getText().toString();
                String repw = edtNLMK.getText().toString();
                if (!current.isEmpty() && !newpw.isEmpty() && !repw.isEmpty()) {
                    String currentPass = edtMKHT.getText().toString().trim();

                    AuthCredential credential = EmailAuthProvider.getCredential(eMail, currentPass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String newPass = edtMKM.getText().toString().trim();
                            String reenterPass = edtNLMK.getText().toString().trim();

                            if (task.isSuccessful()) {
                                final ProgressDialog pd = new ProgressDialog(AccountActivity.this);
                                pd.setMessage("Loading...");
                                pd.show();
                                if (newPass.equals(reenterPass)) {
                                    user.updatePassword(newPass)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        pd.dismiss();
                                                        Toast.makeText(AccountActivity.this, "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                                        dialog.hide();
                                                    }

                                                }
                                            });
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(AccountActivity.this, "Mật Khẩu Nhập Lại Phải Khớp Với Mật Khẩu Mới", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AccountActivity.this, "Mật Khẩu Hiện Tại Không Đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    edtMKHT.requestFocus();
                }
            }
        });
        tvThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });


        dialog.show();

    }

    private void fillInfoFacebook(){
        tvTitle.setText("Bạn Đang Đăng Nhập Với Tài Khoản Facebook");
        tvTitle.setTextSize(15f);
        String displayName = user.getDisplayName();
        String email =user.getEmail();
        Uri image_url = user.getPhotoUrl();
        String linkAnh = image_url+"?height=400";
        Uri profile_Image = Uri.parse(linkAnh);

        btnSave.setVisibility(View.GONE);
        edtPassword.setVisibility(View.GONE);
        edtPhone.setVisibility(View.GONE);
        profileImage.setClickable(false);
        edtDisplayName.setClickable(false);
        edtDisplayName.setFocusable(false);
        tvPhoto.setVisibility(View.GONE);
        btnChangePass.setVisibility(View.GONE);
        tilPhone.setVisibility(View.GONE);
        tilPassword.setVisibility(View.GONE);
        tvPhoto.setVisibility(View.GONE);
        tvPhoto.setClickable(false);
        btnSave.setVisibility(View.GONE);
        edtDisplayName.setText(displayName);
        edtDisplayEmail.setText(email);
        Picasso.get()
                .load(profile_Image)
                .placeholder(R.drawable.person)
                .into(profileImage);
    }
    private void fillInfo() {


        String displayName = user.getDisplayName();
        String displayEmail = user.getEmail();
        String phone = user.getPhoneNumber();


        mData.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String name = map.get("HoTen");
                String email = map.get("Email");
                String sdt = map.get("SDT");
                String uri = map.get("Image");
                Uri uri_image = Uri.parse(uri);

                Log.v("VALUEEEEE", "" + name);
                Log.v("VALUEEEEE", "" + email);
                Log.v("VALUEEEEE", "" + sdt);
                edtDisplayName.setText(name);
                edtPhone.setText(sdt);
                edtDisplayEmail.setText(email);
                btnSave.setVisibility(View.VISIBLE);
                edtPassword.setVisibility(View.VISIBLE);
                tvPhoto.setClickable(true);
                tvPhoto.setVisibility(View.VISIBLE);
                Picasso.get().load(uri).placeholder(R.drawable.person).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initUI() {
        profileImage = (CircleImageView) findViewById(R.id.profile_Image);
        edtDisplayName = (EditText) findViewById(R.id.edtDisplayName);
        edtDisplayEmail = (EditText) findViewById(R.id.edtDisplayEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tilPhone = (TextInputLayout) findViewById(R.id.tilPhone);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        tvPhoto = (TextView) findViewById(R.id.tveditPhoto);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnChangePass = (ImageButton)findViewById(R.id.btnChangePass);
        edtPassword.setFocusable(false);
        edtPassword.setClickable(false);
        edtDisplayEmail.setFocusable(false);
        edtDisplayEmail.setClickable(false);

        imageStorageData = FirebaseStorage.getInstance().getReference();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    }

    public void clickShowDialog(View view) {
        dialogChangePass();
    }

    public void clickSave(View view) {
        String eMailMoi = edtDisplayEmail.getText().toString().trim();
        String hotenMoi = edtDisplayName.getText().toString().trim();
        String sdtMoi = edtPhone.getText().toString().trim();
        setnewUserData(eMailMoi, hotenMoi, sdtMoi);
    }

    private void setnewUserData(final String eMail, final String hoten, final String sdt) {
        ProgressDialog pd = new ProgressDialog(AccountActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        mData = FirebaseDatabase.getInstance().getReference("Users");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        pd.dismiss();

        mData.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> postValues = new HashMap<String,Object>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postValues.put(snapshot.getKey(),snapshot.getValue());
                }
                postValues.put("Email",eMail);
                postValues.put("HoTen",hoten);
                postValues.put("SDT",sdt);
                mData.child(uid).updateChildren(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AccountActivity.this, "Cập Nhật Thông Tin Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void BackToPrevious(View view) {
        super.onBackPressed();
    }

    public void clickEditPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "SELECT_IMAGE"), GALLERY_PICK);

        /*CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AccountActivity.this);
                */
    }

    public void startCameraWithUri() {
        CropImageContractOptions options = new CropImageContractOptions(outputUri, new CropImageOptions())
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .setMaxZoom(4)
                .setAutoZoomEnabled(true)
                .setMultiTouchEnabled(true)
                .setCenterMoveEnabled(true)
                .setShowCropOverlay(true)
                .setAllowFlipping(true)
                .setSnapRadius(3f)
                .setTouchRadius(48f)
                .setInitialCropWindowPaddingRatio(0.1f)
                .setBorderLineThickness(3f)
                .setBorderLineColor(Color.argb(170, 255, 255, 255))
                .setBorderCornerThickness(2f)
                .setBorderCornerOffset(5f)
                .setBorderCornerLength(14f)
                .setBorderCornerColor(R.color.white)
                .setGuidelinesThickness(1f)
                .setGuidelinesColor(R.color.white)
                .setBackgroundColor(Color.argb(119, 0, 0, 0))
                .setMinCropWindowSize(24, 24)
                .setMinCropResultSize(20, 20)
                .setMaxCropResultSize(99999, 99999)
                .setActivityTitle("")
                .setActivityMenuIconColor(0)
                .setOutputUri(null)
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setOutputCompressQuality(90)
                .setRequestedSize(0, 0)
                .setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setInitialCropWindowRectangle(null)
                .setInitialRotation(90)
                .setAllowCounterRotation(false)
                .setFlipHorizontally(false)
                .setFlipVertically(false)
                .setCropMenuCropButtonTitle(null)
                .setCropMenuCropButtonIcon(0)
                .setAllowRotation(true)
                .setNoOutputImage(false)
                .setFixAspectRatio(false);
        cropImage.launch(options);
    }

    public void showErrorMessage(@NotNull String message) {
        Log.e("Camera Error:", message);
        Toast.makeText(this, "Crop failed: " + message, Toast.LENGTH_SHORT).show();
    }

    private void startTakePicture() {
        try {
            Context ctx = this;
            String authorities = ctx.getPackageName() + AUTHORITY_SUFFIX;
            outputUri = FileProvider.getUriForFile(ctx, authorities, createImageFile());
            takePicture.launch(outputUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCropImageResult(@NotNull String uri) {
//        SampleResultScreen.Companion.start(this, null, Uri.parse(uri), null);
        pd = new ProgressDialog(AccountActivity.this);
        pd.setTitle("Đang Tải Ảnh Lên");
        pd.setMessage("Vui Lòng Đợi Trong Giây Lát");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Uri resultUri = Uri.parse(uri);

        final StorageReference filePath = imageStorageData.child("profile_images").child(user.getUid() + ".jpg");

        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String download_uri = uri.toString();
                        download = download_uri;
                        userDatabase.child("Image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Toast.makeText(AccountActivity.this, "Hoàn Tất", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AccountActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private File createImageFile() throws IOException {
        SimpleDateFormat timeStamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                FILE_NAMING_PREFIX + timeStamp + FILE_NAMING_SUFFIX,
                FILE_FORMAT,
                storageDir
        );
    }

    public void onCropImageResult(@NonNull CropImageView.CropResult result) {
        if (result.isSuccessful()) {
            handleCropImageResult(Objects.requireNonNull(result.getUriContent())
                    .toString()
                    .replace("file:", ""));
        } else if (result.equals(CropImage.CancelledResult.INSTANCE)) {
            showErrorMessage("cropping image was cancelled by the user");
        } else {
            showErrorMessage("cropping image failed");
        }
    }

    public void onTakePictureResult(boolean success) {
        if (success) { startCameraWithUri(); }
        else { showErrorMessage("taking picture failed"); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

//            String imageUri = data.getDataString();
//            CropImage.activity(Uri.parse(imageUri))
//                    .setAspectRatio(1, 1)
//                    .start(this);
            outputUri = data.getData();
            startTakePicture();
        }

//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                pd = new ProgressDialog(AccountActivity.this);
//                pd.setTitle("Đang Tải Ảnh Lên");
//                pd.setMessage("Vui Lòng Đợi Trong Giây Lát");
//                pd.setCanceledOnTouchOutside(false);
//                pd.show();
//
//                Uri resultUri = result.getUri();
//
//                final StorageReference filePath = imageStorageData.child("profile_images").child(user.getUid() + ".jpg");
//
//                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                String download_uri = uri.toString();
//                                download = download_uri;
//                                userDatabase.child("Image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            pd.dismiss();
//                                            Toast.makeText(AccountActivity.this, "Hoàn Tất", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(AccountActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }
}
