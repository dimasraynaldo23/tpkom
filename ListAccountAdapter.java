package com.gawe.tpkom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gawe.tpkom.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;

public class ListAccountAdapter extends RecyclerView.Adapter<ListAccountAdapter.ViewHolder> {

    private ArrayList<User> userData;
    private Context context;

    public ListAccountAdapter(ArrayList<User> userData, Context context) {
        this.userData = userData;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView idAkun, namaAkun, emailAkun, roleAkun;
        private LinearLayout listItem;

        public ViewHolder(View itemView) {
            super(itemView);
            idAkun = itemView.findViewById(R.id.id_ACC);
            namaAkun = itemView.findViewById(R.id.nama_ACC);
            emailAkun = itemView.findViewById(R.id.email_ACC);
            roleAkun = itemView.findViewById(R.id.role_ACC);
            listItem = itemView.findViewById(R.id.listAkun);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String id = userData.get(position).getId();
        final String nama = userData.get(position).getName();
        final String email = userData.get(position).getEmail();
        final String role = userData.get(position).getRole();


        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.idAkun.setText("ID\n"+id);
        holder.namaAkun.setText("Nama\n"+nama);
        holder.emailAkun.setText("Email\n"+email);
        holder.roleAkun.setText("Role\n"+role);
//
//        holder.listItem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                /*
//                  Kodingan untuk membuat fungsi Edit dan Delete,
//                  yang akan dibahas pada Tutorial Berikutnya.
//                 */
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return userData.size();
    }

}