package com.gawe.tpkom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.gawe.tpkom.Model.dataPelanggan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InputDataPlg extends AppCompatActivity {

    @BindView(R.id.in_kdsales)
    EditText kdSales;
    @BindView(R.id.in_nmrmobi)
    EditText nomorMobi;
    @BindView(R.id.in_namaplg)
    EditText namaPlg;
    @BindView(R.id.in_nomorplg)
    EditText nomorPlg;
    @BindView(R.id.in_nomoralt)
    EditText nomorAlt;
    @BindView(R.id.in_relasiplg)
    EditText relasiPlg;
    @BindView(R.id.in_tglinstal)
    EditText tglInstall;
    @BindView(R.id.in_almtinstl)
    EditText almtInstall;
    @BindView(R.id.in_ptknalamat)
    EditText ptknAlamat;
    @BindView(R.id.imageKTP)
    ImageView imageKTP;
    @BindView(R.id.submitdataPlg)
    Button submitdataPlg;
//    @BindView(R.id.btn_photoKtp)
//    Button photoKtp;
    private StorageReference refPhotoProfile;
    private Uri photoUrl;
    ProgressDialog ptDialog;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sales);
        ButterKnife.bind(this);
        ptDialog = new ProgressDialog(this);

    }

    //method handling btn_submitdataPlg
    @OnClick(R.id.submitdataPlg)

    public void regisData() {
        //validasi kosong
        if (kdSales.getText().toString().isEmpty()) {
            kdSales.setError("Required");
            return;
        }
        if (nomorMobi.getText().toString().isEmpty()) {
            nomorMobi.setError("Required");
            return;
        }
        Toast.makeText(InputDataPlg.this, "Berhasil menambhkan data!", Toast.LENGTH_SHORT).show();
        addData();
        Intent i = new Intent(InputDataPlg.this, ListOnline.class);
        startActivity(i);
    }

    protected void addData() {
        String vKdSales, vNomorMobi, vNamaPlg, vNomorPlg, vNomorAlt, vRelasiPlg, vTglInstall, vAlamatPlg, vPtknAlamat;
        //sending data to fb
        DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference();
        vKdSales = kdSales.getText().toString();
        vNomorMobi = nomorMobi.getText().toString();
        vNamaPlg = namaPlg.getText().toString();
        vNomorPlg = nomorPlg.getText().toString();
        vNomorAlt = nomorAlt.getText().toString();
        vRelasiPlg = relasiPlg.getText().toString();
        vTglInstall = tglInstall.getText().toString();
        vAlamatPlg = almtInstall.getText().toString();
        vPtknAlamat = ptknAlamat.getText().toString();
        dataPelanggan dataPelanggan = new dataPelanggan(vKdSales, vNomorMobi, vNamaPlg, vNomorPlg, vNomorAlt, vRelasiPlg, vTglInstall, vAlamatPlg, vPtknAlamat);
        db.child("Data Pelanggan").child(vKdSales).push().setValue(dataPelanggan);

    }

//        @Override
//        public void onClick (View view){
//            switch (view.getId()) {
//                case R.id.btn_photoKtp: //tombol choose (pilih gambar)
//                    //ImagePicker library untuk menampilkan dialog memilih gambar pada gallery/camera
//                    ImagePicker.create(this)
//                            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
//                            .folderMode(true) // folder mode (false by default)
//                            .toolbarFolderTitle("Folder") // folder selection title
//                            .toolbarImageTitle("Tap to select") // image selection title
//                            .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
//                            .single() // single mode
//                            .limit(1) // max images can be selected (99 by default)
//                            .showCamera(true) // show camera or not (true by default)
//                            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//                            .enableLog(false) // disabling log
//                            .start(); // start image picker activity with request code
//                    break;
//
//                case R.id.submitdataPlg:
//                    if (!isPhotoKTP) {
//                        Toast.makeText(this, "Harap pilih gambar!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ptDialog.setMessage("Uploading..");
//                    ptDialog.setIndeterminate(true);
//                    ptDialog.show();
//
//                    //melakukan proses update foto
//                    refPhotoProfile = Constant.storageRef.child("KTP Pelanggan/" + System.currentTimeMillis() + ".jpg"); //akses path dan filename storage di firebase untuk menyimpan gambar
//                    StorageReference photoImagesRef = Constant.storageRef.child("KTP Pelanggan/" + System.currentTimeMillis() + ".jpg");
//                    refPhotoProfile.getName().equals(photoImagesRef.getName());
//                    refPhotoProfile.getPath().equals(photoImagesRef.getPath());
//
//                    //mengambil gambar dari imageview yang sudah di set menjadi selected image sebelumnya
//                    imageKTP.setDrawingCacheEnabled(true);
//                    imageKTP.buildDrawingCache();
//                    Bitmap bitmap = imageKTP.getDrawingCache(); //convert imageview ke bitmap
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //convert bitmap ke bytearray
//                    byte[] data = baos.toByteArray();
//
//                    UploadTask uploadTask = refPhotoProfile.putBytes(data); //upload image yang sudah dalam bentuk bytearray ke firebase storage
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                            photoUrl = refPhotoProfile.getDownloadUrl().getResult(); //setelah selesai upload, ambil url gambar
//                            key = Constant.refPhoto.push().getKey(); //ambil key dari node firebase
//                        }
//                    });
//                    break;
//            }
//        }
//
//        protected void uploadKTP(){
//            //push atau insert data ke firebase database
//            Constant.refPhoto.child(key).setValue(new dataPelanggan(key, photoUrl.toString()));
//            ptDialog.dismiss();
//            Toast.makeText(InputDataPlg.this, "Uploaded!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//    boolean isPhotoKTP = false;
//
//    //method untuk handling result dari activity lain contoh disini adalah imagepicker
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) { // jika ada data dipilih
//            Image image = ImagePicker.getFirstImageOrNull(data); //ambil first image
//            File imgFile = new File(image.getPath()); // dapatkan lokasi gambar yang dipilih
//            if (imgFile.exists()) { //jika ditemukan
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //convert file ke bitmap
//                imageKTP.setImageBitmap(myBitmap); //set imageview dengan gambar yang dipilih
//                isPhotoKTP = true; // ubah state menjadi true untuk menandakan gambar telah dipilih
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}