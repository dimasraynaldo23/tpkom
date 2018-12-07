package com.gawe.tpkom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gawe.tpkom.Model.Tracking;
import com.gawe.tpkom.Model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ListOnline extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    //Firebase
    DatabaseReference onlineRef,currentUserRef,counterRef,locations;
    FirebaseRecyclerAdapter<User,ListOnlineViewHolder> adapter;

    FusedLocationProviderClient mFusedLocationClient; //fused location client

    //View
    RecyclerView listOnline;
    RecyclerView.LayoutManager layoutManager;

    //Button
    private Button kirimFoto;

    //Location
    private static final int MY_PERMISSION_REQUEST_CODE=7171;
    private static final int PLAY_SERVICES_RES_REQUEST=7172;
    private LocationRequest mlocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FirebaseAuth mAuth;

    public double latDoub;
    public double lngDoub;

    private TextView latitude ;
    private TextView longitude ;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);


//        latitude = (TextView) findViewById(R.id.txt_lat);
//        longitude = (TextView) findViewById(R.id.txt_lng);


        //On click listener



        //InitView
        listOnline = findViewById(R.id.listOnline);
        listOnline.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOnline.setLayoutManager(layoutManager);

        //Set toolbar and layout / join menu
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("TPkom Presensi");
        setSupportActionBar(toolbar);

        //Firebase
        locations = FirebaseDatabase.getInstance().getReference("locations");
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline"); //bikin child baru namanya last online
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()); //bikin child didalem last online dengan uid
        
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else
        {
            if(checkPlayServices())
            {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }

        setupSystem();
        //After setup system load all user from counterRef and display on RecyclerView
        //This is online list
        updateList();

        mAuth = FirebaseAuth.getInstance();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
            }
            break;
        }
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); //fusedLocationProviderClient
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time = "Aktif Terakhir " + format.format(calendar.getTime());

                TextView textJam = findViewById(R.id.jam);
                textJam.setText(time);

                String valueJam = time;

                if (location != null)
                {
                    locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                    String.valueOf(location.getLatitude()),
                                    String.valueOf(location.getLongitude())
                                    ,valueJam ));
                }
                else
                {
//                    Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show();
                    Log.d("Location Status","Could not get location");
                }
            }
        });
    }

    private void createLocationRequest() {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationRequest.setSmallestDisplacement(DISTANCE);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RES_REQUEST).show();

            }
            else
            {
                Toast.makeText(this,"This Device is not supported",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void signOut() {
        currentUserRef.removeValue();
        mAuth.signOut();
        startActivity(new Intent(ListOnline.this, LoginPhotoActivity.class));
        finish();

    }

    public void openPhotoActivity() {
        Intent intent = new Intent(this, MainPhotoActivity.class);
        startActivity(intent);
    }

    public void openUserDetailActivity() {
        Intent intent = new Intent(this, UpdateDataActivity.class);
        startActivity(intent);
    }

    public void openInputData(){
        Intent intent= new Intent(this, InputDataPlg.class);
        startActivity(intent);
    }


    private void updateList() {
//        latDoub = mLastLocation.getLatitude();
//        lngDoub = mLastLocation.getLongitude();

        FirebaseRecyclerOptions<User> userOptions = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(counterRef,User.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<User, ListOnlineViewHolder>(userOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ListOnlineViewHolder viewHolder, int position, @NonNull final User model) {

                if (model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    viewHolder.txtEmail.setText("Email : "+ model.getEmail() + "(Me)");
                    viewHolder.txtNama.setText("Nama : " + model.getName());
                    viewHolder.txtJam.setText(model.getTime());
                }
                else {
                    viewHolder.txtEmail.setText("Email : "+ model.getEmail());
                    viewHolder.txtJam.setText(model.getTime());
                    viewHolder.txtNama.setText("Nama : " + model.getName());
                }

                //We need implement item click of recycler view

                viewHolder.itemClickListener = new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //if model is current user , not set click event

                        Intent map = new Intent(ListOnline.this,MapTracking.class);

                        switch (view.getId()) {
                            case R.id.cardView : map = new Intent(ListOnline.this,MapTracking.class);
                                if(!model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    map.putExtra("email", model.getEmail());
                                    map.putExtra("lat", mLastLocation.getLatitude());
                                    map.putExtra("lng", mLastLocation.getLongitude());
                                    startActivity(map);
                                }
                        }
                    }
                };
            }

            @NonNull
            @Override
            public ListOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getBaseContext())
                        .inflate(R.layout.user_layout,parent,false);
                return new ListOnlineViewHolder(itemView);
            }
        };
        adapter.startListening();
        listOnline.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time = "Aktif Terakhir " + format.format(calendar.getTime());

                TextView textJam = findViewById(R.id.jam);
                textJam.setText(time);

                if(dataSnapshot.getValue(Boolean.class))
                {
                    currentUserRef.onDisconnect().removeValue(); //Delete value lama
                    //set online user di list
                    String valueJam = time;
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),"Online", valueJam ));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    User user = postSnapshot.getValue (User.class);
                    Log.d("LOG",""+user.getEmail()+" is "+user.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = "Login Terakhir " + format.format(calendar.getTime());

        TextView textJam = findViewById(R.id.jam);
        textJam.setText(time);

        String valueJam = time;

        switch (item.getItemId())
        {
            case R.id.action_join:
                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),"Online",valueJam));

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            break;
            case R.id.action_logout:

                    signOut();

                break;
            case R.id.action_photos:

                openPhotoActivity();

                break;
            case R.id.action_update_info:

                openUserDetailActivity();

                break;

            case R.id.action_input_data:

                openInputData();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
    mLastLocation = location;
    displayLocation();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    displayLocation();
    startLocationUpdates();
    }

    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return ;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mlocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkPlayServices();
    }
}