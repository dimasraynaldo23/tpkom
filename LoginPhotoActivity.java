package com.gawe.tpkom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.gawe.tpkom.Constant;

public class LoginPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    //Deklarasi View
    @BindView(R.id.email) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            TextInputEditText tvEmail;
    @BindView(R.id.password) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            TextInputEditText tvPassword;
    @BindView(R.id.email_sign_in_button) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            Button btnLogin;
    @BindView(R.id.tvReset) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            TextView tvResetPW;
    @BindView(R.id.dftr)
    TextView tvDftr;
    @BindView(R.id.optRoles)
    RadioGroup roles;
    @BindView(R.id.optAdm)
    RadioButton admin;
    @BindView(R.id.optAts)
    RadioButton atasan;
    @BindView(R.id.optSal)
    RadioButton sales;
    @BindView(R.id.optTek)
    RadioButton teknisi;
    private ProgressDialog pbDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_photo);
        ButterKnife.bind(this); //Binding ButterKnife pada activity
        pbDialog = new ProgressDialog(this);
        tvResetPW.setOnClickListener(this);
        tvDftr.setOnClickListener(this);
    }

    @OnClick(R.id.email_sign_in_button)
    public void login() {

        String email = tvEmail.getText().toString();
        String pass = tvPassword.getText().toString();

        //validasi kosong
        if (email.isEmpty()) {
            tvEmail.setError("Required");
            return;
        }
        //validasi kosong
        if (pass.isEmpty()) {
            tvPassword.setError("Required");
            return;
        }
        if (!sales.isChecked() && !teknisi.isChecked() && !admin.isChecked() && !atasan.isChecked()) {
            sales.setError("Choose one of these");
        }

        pbDialog.setMessage("Harap Tunggu");
        pbDialog.setIndeterminate(true);
        pbDialog.show();
//        loginUser(email, pass);
        loginProcess();
    }

    private void loginProcess() {
        final String str_email = tvEmail.getText().toString();
        final String str_password = tvPassword.getText().toString();

        int selectedId = roles.getCheckedRadioButtonId();
        String result = "";

        if (selectedId == sales.getId()) {
            result = "Sales";
        } else if (selectedId == teknisi.getId()) {
            result = "Teknisi";
        } else if (selectedId == admin.getId()) {
            result = "Admin";
        } else if (selectedId == atasan.getId()) {
            result = "Atasan";
        }

        //melakukan proses login menggunakan firebase
        final String finalResult = result;
        Constant.mAuth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (finalResult == "Admin" ) {
                                Intent a = new Intent(LoginPhotoActivity.this, ManageAtasanActivity.class);
                                startActivity(a);
                            } else if (finalResult == "Atasan") {
                                Intent a = new Intent(LoginPhotoActivity.this, AtasanActivity.class);
                                startActivity(a);
                            } else if (finalResult == "Sales" || finalResult == "Teknisi") {
                                Intent a = new Intent(LoginPhotoActivity.this, ListOnline.class);
                                startActivity(a);
                            } else {
                                Toast.makeText(LoginPhotoActivity.this, "Silahkan pilih terlebih dahulu", Toast.LENGTH_SHORT).show();
                            }
                            pbDialog.dismiss();
                            Log.d("", "signInWithEmail:success");
                            FirebaseUser curUser = Constant.mAuth.getCurrentUser(); //ambil informasi user yang login
                            Constant.currentUser = curUser; //set di variabel global
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPhotoActivity.this, "Akun belum terdaftar",
                                    Toast.LENGTH_SHORT).show();
                            pbDialog.dismiss();
                            //showProgress(false);
                        }
                    }
                });
    }


//    Intent a;

//    public void loginUser(final String userEmail, final String userLogin) {
//        Constant.mAuth.signInWithEmailAndPassword(userEmail, userLogin).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                    String registeredUser = currentUser.getUid();
//                    DatabaseReference userLoginDb = FirebaseDatabase.getInstance().getReference().child(registeredUser);
//                    userLoginDb.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String userType = dataSnapshot.child("Use").getValue().toString();
//                            if (userType.equals("Admin")) {
//                                a = new Intent(LoginPhotoActivity.this, AdminActivity.class);
//                            } else if (userType.equals("Atasan")) {
//                                a = new Intent(LoginPhotoActivity.this, AtasanActivity.class);
//                            } else {
//                                a = new Intent(LoginPhotoActivity.this, ListOnline.class);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReset:
                startActivity(new Intent(LoginPhotoActivity.this, PasswordActivity.class)); //panggil activity register
                break;

            case R.id.dftr:
                startActivity(new Intent(LoginPhotoActivity.this, RegisterData.class));
                break;
        }
    }
}
