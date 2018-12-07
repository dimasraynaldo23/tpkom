package com.gawe.tpkom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gawe.tpkom.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterData extends AppCompatActivity {
    //Deklarasi View dengan ButterKnife
    @BindView(R.id.koding_id)
    TextInputEditText tvId;
    @BindView(R.id.name)
    TextInputEditText tvName;
    @BindView(R.id.email)
    TextInputEditText tvEmail;
    @BindView(R.id.password)
    TextInputEditText tvPass;
    @BindView(R.id.btn_regisdata)
    Button btnRegisDt;
    @BindView(R.id.atasan)
    TextInputEditText tvAtasan;
    @BindView(R.id.radioBtn)
    RadioGroup radioGroup;
    @BindView(R.id.radioAdmin)
    RadioButton rAdmin;
    @BindView(R.id.radioSales)
    RadioButton rSales;
    @BindView(R.id.radioTeknisi)
    RadioButton rTeknisi;
    @BindView(R.id.radioAtsn)
    RadioButton rAtasan;
    private ProgressDialog pbDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerdata);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        pbDialog = new ProgressDialog(this);
        Intent i = this.getIntent();
    }

    //method handling btnregister
    @OnClick(R.id.btn_regisdata)

    public void regisData() {
        //validasi kosong
        if (tvId.getText().toString().isEmpty()) {
            tvId.setError("Required");
            return;
        }
        if (tvName.getText().toString().isEmpty()) {
            tvName.setError("Required");
            return;
        }
        if (tvPass.getText().toString().isEmpty()) {
            tvPass.setError("Required");
            return;
        }
        if (tvAtasan.getText().toString().isEmpty()) {
            tvAtasan.setError("Required");
        }
        if (!rSales.isChecked() && !rTeknisi.isChecked() && !rAdmin.isChecked() && !rAtasan.isChecked()) {
            rSales.setError("Choose one of these");
        }

        pbDialog.setMessage("Proses Mendaftarkan");
        pbDialog.setIndeterminate(true);
        pbDialog.show();

        addData();

        Toast.makeText(RegisterData.this, "Berhasil menambhkan data!", Toast.LENGTH_SHORT).show();
    }

    protected void addData() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String result = "";

        if (selectedId == rSales.getId()) {
            result = "Sales";
        } else if (selectedId == rTeknisi.getId()) {
            result = "Teknisi";
        } else if (selectedId == rAdmin.getId()) {
            result = "Admin";
        } else if (selectedId == rAtasan.getId()) {
            result = "Atasan";
        }

        //sending data to fb
        DatabaseReference db2;
        db2 = FirebaseDatabase.getInstance().getReference();
        String id = tvId.getText().toString();
        String name = tvName.getText().toString();
        String email = tvEmail.getText().toString();
        String password = tvPass.getText().toString();
        String role = result;
        String atasan = tvAtasan.getText().toString();
        User User = new User(id, atasan, name, email, password, role);

        if (result == "Sales") {
            db2.child("Pre-User").child("Sales").child(id).setValue(User);
            Intent i = new Intent(RegisterData.this, LoginPhotoActivity.class);
            startActivity(i);
        } else if (result == "Teknisi") {
            db2.child("Pre-User").child("Teknisi").setValue(User);
            Intent i = new Intent(RegisterData.this, LoginPhotoActivity.class);
            startActivity(i);
        } else if (result == "Admin") {
            createUser();
            db2.child("User").child("Admin").child(id).setValue(User);
            Intent i = new Intent(RegisterData.this, ManageAtasanActivity.class);
            startActivity(i);
        } else if (result == "Atasan") {
            db2.child("Pre-User").child("Atasan").child(id).setValue(User);
            Intent i = new Intent(RegisterData.this, LoginPhotoActivity.class);
            startActivity(i);
        }
    }

    //method untuk register user, dengan fitur email authentication di firebase
    protected void createUser() {
        final String email = tvEmail.getText().toString();
        String password = tvPass.getText().toString();

        Constant.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pbDialog.dismiss();
                            Log.d("", "createUserWithEmail:success");
                            Constant.currentUser = Constant.mAuth.getCurrentUser(); //simpan data user
                        } else {
                            // If sign in fails, display a message to the user.
                            pbDialog.dismiss();
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterData.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}