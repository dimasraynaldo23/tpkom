package com.gawe.tpkom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.gawe.tpkom.Model.User;
import com.gawe.tpkom.manageatasan.ManageAtasanAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import butterknife.ButterKnife;

public class ManageTeknisiActivity extends AppCompatActivity implements com.gawe.tpkom.manageatasan.ManageAtasanAdapter.IOnItemClickListener {
    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    //    private RecyclerView.Adapter adapter;
    private com.gawe.tpkom.manageatasan.ManageAtasanAdapter ManageAtasanAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference dataRef;
    private ArrayList<User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_akun);
        ButterKnife.bind(this);//used for bind the view to object
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarACC);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Manage Akun Teknisi");
        recyclerView = findViewById(R.id.listAkun);
        dataRef = FirebaseDatabase.getInstance().getReference();
        MyRecyclerView();
        GetData();
    }

    private void GetData() {
        Toast.makeText(getApplicationContext(), "Mengambil Data", Toast.LENGTH_LONG).show();
        //Mendapatkan Referensi Database
        dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.child("Pre-User").child("Teknisi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        userData = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek
//                            replaceCurrentUserIfGetAllLongValue(); //read comment bellow method implementation
                            User users = snapshot.getValue(User.class);
                            userData.add(users);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
//                            users.setId(snapshot.getKey());
//                            userData.add(users);
                        }

                        //so if there is change in user data, it will update the adapter without changing the adapter
                        //only change the data
                        ManageAtasanAdapter.setData(userData);

                    }

                    private void replaceCurrentUserIfGetAllLongValue() {
                        //DataSnapshot { key = 5, value = {password=29121997, role=Atasan, atasan=C, statusAcc=0, name=Sebi, id=5, email=sebi@gmail.com} }
//                            String email = snapshot.child("email").getValue(String.class).toString();
////                            String status;
////                            String time;
//                            String id = snapshot.child("id").getValue(String.class).toString();
//                            String atasan = snapshot.child("atasan").getValue(String.class).toString();
//                            String name = snapshot.child("name").getValue(String.class).toString();
//                            String role = snapshot.child("role").getValue(String.class).toString();
//                            String password = snapshot.child("password").getValue(String.class).toString();;//TODO if this is important just add the code
//                            int statusAcc = snapshot.child("statusAcc").getValue(int.class);
//                            User user = new User();
//                            user.setId(id);
//                            user.setEmail(email);
//                            user.setAtasan(atasan);
//                            user.setName(name);
//                            user.setRole(role);
//                            user.setStatusAcc(statusAcc);
//                            user.setPassword(password);
//                            snapshot.getRef().removeValue();
//                            snapshot.getRef().setValue(user);
//                          TODO dont forget to detach listener / uninstall because its make infinite loop
//                          if you not detach the listener, use / learn recommended pattern
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                        Toast.makeText(getApplicationContext(), "Pengambilan Data Gagal", Toast.LENGTH_LONG).show();
                        Log.e("ManageTeknisiActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView() {
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ManageAtasanAdapter = new ManageAtasanAdapter(ManageTeknisiActivity.this);
        recyclerView.setAdapter(ManageAtasanAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                break;
            case R.id.manage_sales:
                manageSales();
                break;
            case R.id.manage_teknisi:
                manageTeknisi();
                break;
            case R.id.manage_atasan:
                manageAtasan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Intent a;

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ManageTeknisiActivity.this, LoginPhotoActivity.class));
        finish();
    }

    public void manageAtasan() {
        a = new Intent(this, ManageTeknisiActivity.class);
        startActivity(a);
    }

    public void manageSales() {
        a = new Intent(this, ManageSalesActivity.class);
        startActivity(a);
    }

    public void manageTeknisi() {
        a = new Intent(this, ManageTeknisiActivity.class);
        startActivity(a);
    }

    @Override
    public void onItemManageAtasanClickListener(boolean activate, User user) {
        DatabaseReference refAdd = dataRef.child("User").child("Teknisi").child(user.getId());
        DatabaseReference refDel = dataRef.child("Pre-User").child("Teknisi").child(user.getId());
        if (activate) {
            Toast.makeText(getApplicationContext(), "Aktivasi Akun sedang diproses", Toast.LENGTH_LONG).show();
            Constant.mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
            refAdd.setValue(user);
            refDel.removeValue();
            Toast.makeText(getApplicationContext(), "Aktivasi Akun Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            refDel.removeValue();
            Toast.makeText(getApplicationContext(), "Permintaan Akun telah dihapus", Toast.LENGTH_SHORT).show();
        }
    }
}