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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    @BindView(R.id.tvSignUp) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            TextView tvDaftar;
    @BindView(R.id.tvReset) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
            TextView tvResetPW;
    private ProgressDialog pbDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_photo);
        ButterKnife.bind(this); //Binding ButterKnife pada activity
        pbDialog = new ProgressDialog(this);
        tvDaftar.setOnClickListener(this);
        tvResetPW.setOnClickListener(this);

    }




    @OnClick(R.id.email_sign_in_button)
    public void login() {
        //validasi kosong
        if (tvEmail.getText().toString().isEmpty()) {
            tvEmail.setError("Required");
            return;
        }
        //validasi kosong
        if (tvPassword.getText().toString().isEmpty()) {
            tvPassword.setError("Required");
            return;
        }

        pbDialog.setMessage("Harap Tunggu");
        pbDialog.setIndeterminate(true);
        pbDialog.show();
        loginProcess();
    }

    private void loginProcess() {
        final String str_email = tvEmail.getText().toString();
        final String str_password = tvPassword.getText().toString();

        //melakukan proses login menggunakan firebase
        Constant.mAuth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pbDialog.dismiss();
                            Log.d("", "signInWithEmail:success");
                            FirebaseUser curUser = Constant.mAuth.getCurrentUser(); //ambil informasi user yang login
                            Constant.currentUser = curUser; //set di variabel global
                            startActivity(new Intent(LoginPhotoActivity.this, MainActivity.class)); //panggil activity main
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSignUp:
                startActivity(new Intent(LoginPhotoActivity.this, RegisterActivity.class)); //panggil activity register
                break;

            case R.id.tvReset:
                startActivity(new Intent(LoginPhotoActivity.this, PasswordActivity.class)); //panggil activity register
                break;

        }
    }
}
