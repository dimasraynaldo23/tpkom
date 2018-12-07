package com.gawe.tpkom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;

    private final static int LOGIN_PERMISSION=1000;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent a = new Intent(MainActivity.this, ListOnline.class);
            startActivity(a);
        }


        btnLogin = (Button) findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                        .setAllowNewEmailAccounts(true).build() ,LOGIN_PERMISSION
                );
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_PERMISSION)
        {
            startNewActivity(resultCode,data);
        }
    }

    private void startNewActivity(int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            Toast.makeText(this, "Email anda sudah terdaftar, silahkan lakukan login",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show();
        }
    }
}
