package com.gawe.tpkom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gawe.tpkom.Model.UserDetailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDataActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputName, inputDivisi;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        // Displaying toolbar icon

        inputName = (EditText) findViewById(R.id.et_nama);
        btnSave = (Button) findViewById(R.id.btn_save);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("locations");
        mFirebaseDatabase2 = mFirebaseInstance.getReference("lastOnline");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String name = inputName.getText().toString();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(inputName.getText().toString())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateDataActivity.this, "Nama telah diubah, silahkan lakukan re-join", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                openUserDetailActivity();

            }
        });
        toggleButton();

    }
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        UserDetailModel userDetail = new UserDetailModel(name);

        mFirebaseDatabase.child(userId).setValue(userDetail);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetailModel user = dataSnapshot.getValue(UserDetailModel.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name);

//                // Display newly updated name and email
//                txtDetails.setText(user.name + ", " + user.divisi);

                // clear edit text
                inputDivisi.setText("");
                inputName.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    public void openUserDetailActivity() {
        Intent intent = new Intent(this, ListOnline.class);
        startActivity(intent);
    }

    private void updateUser(String name) {
        // updating the user via child nodes
//        mFirebaseDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nama").setValue(name);
//        mFirebaseDatabase2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nama").setValue(name);





//        if (!TextUtils.isEmpty(divisi))
//            mFirebaseDatabase.child(userId).child("divisi").setValue(divisi);
    }

}
