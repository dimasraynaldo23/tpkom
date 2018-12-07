package com.gawe.tpkom;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import comm.example.HARRIMARGONO_1202151230_MODUL6.R;
import com.gawe.tpkom.Constant;
import com.gawe.tpkom.PhotoFragment;

public class MainPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_photo);
        ButterKnife.bind(this);  //Binding ButterKnife pada activity
        setTitle("Popotoan");

        //Mengatur tab dan fragment pada tab menggunakan fragment state pager itemadapter dari library SmartTabLayout
        FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(FragmentPagerItem.of("TERBARU", PhotoFragment.class, PhotoFragment.arguments("terbaru")))
                .add(FragmentPagerItem.of("FOTO SAYA", PhotoFragment.class, PhotoFragment.arguments("fotosaya")))
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);

        viewPager.setAdapter(adapter); //masukkan fragment pada adapter viewpager
        viewPagerTab.setViewPager(viewPager); //mengatur tab pada viewpager

//        if (Constant.currentUser == null) { //jika belum login
//            startActivity(new Intent(MainPhotoActivity.this, LoginActivity.class));
//            finish();
//        } else { //jika sudah login
//            viewPager.setAdapter(adapter); //masukkan fragment pada adapter viewpager
//            viewPagerTab.setViewPager(viewPager); //mengatur tab pada viewpager
//        }
    }

    //method untuk handling tombol add
    @OnClick(R.id.btnAdd)
    public void add() {
        startActivity(new Intent(MainPhotoActivity.this, AddPhotoActivity.class)); // panggil add photo activity
    }

    //method untuk implement menu pada activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // inflate atau memasukkan menu
        return super.onCreateOptionsMenu(menu);
    }

    //method untuk handling menu yang di klik dari daftar di menu yang di implement
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Constant.mAuth.signOut(); //logout firebase
                Constant.currentUser = null; //set global variable user null
                startActivity(new Intent(MainPhotoActivity.this, LoginActivity.class)); //panggil login activity
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
