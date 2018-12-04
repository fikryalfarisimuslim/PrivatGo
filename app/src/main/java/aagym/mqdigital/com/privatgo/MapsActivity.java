package aagym.mqdigital.com.privatgo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import aagym.mqdigital.com.privatgo.fragment.PreviewProfile;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.footer_layout)
    RelativeLayout footerLayout;
    private GoogleMap mMap;

    // konstanta untuk menyimpan id permintaan izin untuk mengakses lokasi, disini kita isi nilainya dengan 99
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    // class Location digunakan untuk mendapatkan nilai latitude dan longitude
    Location lastLocation;

    private final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.container,new PreviewProfile()).commit();
    }

    private void getLocation() {

        // jika izin untuk mengakses lokasi belum diberikan
        if (!checkLocationPermission())
            // hentikan eksekusi kode program
            return;

        mMap.setMyLocationEnabled(true);

        // jika sudah, gunakan objek fusedLocationProviderClient untuk mendapatkan lokasi pengguna
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Mendapatkan lokasi, terkadang lokasi dapat bernilai null oleh karena itu kita buat handler nya supaya aplikasi tidak crash/force close ketika lokasi bernilai null
                        lastLocation = location;
                        // jika lokasi bernilai null
                        if (location == null) {
                            // hentikan eksekusi kode program
                            return;
                        }

                        // cek apakah geocoder tersedia
                        if (!Geocoder.isPresent()) {
                            Toast.makeText(MapsActivity.this,
                                    "Geocoder tidak tersedia",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        updateUI();
                        new GetAddress().execute();
                    }
                });
    }

    public void slideDown(View view) {
        // Prepare the View for the animation
        footerLayout.setVisibility(View.VISIBLE);
        footerLayout.setAlpha(1.0f);

        // Start the animation
        footerLayout.animate()
                .translationY(view.getHeight())
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        footerLayout.setVisibility(View.GONE);
                    }
                });

//        view.animate()
//                .translationY(0)
//                .alpha(0.0f)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        view.setVisibility(View.GONE);
//                    }
//                });
    }

    public void onClickOrder(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_order, null);
        dialogBuilder.setView(dialogView);

//        EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
//        editText.setText("test label");

        Button button = dialogView.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionSuccess();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void transactionSuccess() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.transaction_success, null);
        dialogBuilder.setView(dialogView);
        Button button = dialogView.findViewById(R.id.chat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View customView;
        private List<MarkerData> list;
        boolean notFirstTime;

        MyInfoWindowAdapter(List<MarkerData> list) {
            this.list = list;
            customView = getLayoutInflater().inflate(R.layout.map_info_window, null);
        }

        @Override
        public View getInfoContents(final Marker marker) {
            // int position = Integer.parseInt(marker.getSnippet());

            //Todo your code here

            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return customView;
        }

    }

    class MarkerData {
        double latitude;
        double longitude;
        String title;

        public MarkerData(double latitude, double longitude, String title) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    ArrayList<MarkerData> markersArray = new ArrayList<MarkerData>();


    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        int height = 50;
        int width = 78;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, height, width, false);

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromBitmap(smallBitmap)));
    }

    private void updateUI() {

        markersArray.add(new MarkerData(-6.863563, 107.589950, "Poin 1"));
        markersArray.add(new MarkerData(-6.872198, 107.588836, "Poin 1"));
        markersArray.add(new MarkerData(-6.861406, 107.593010, "Poin 1"));
        markersArray.add(new MarkerData(-6.929057, 107.612789, "Poin 1"));
        markersArray.add(new MarkerData(-6.903280, 107.608710, "Poin 1"));

        for (int i = 0; i < markersArray.size(); i++) {

            createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitle(), "Population: 4,627,300");
        }

        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel));
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(markersArray));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng latLon = marker.getPosition();

                //Cycle through places array
//                for(MarkerData place : markersArray){
//                    if (latLon.equals(new LatLng(place.getLatitude(),place.getLongitude()))){
//                        //match found!  Do something....
//                        slideUp();
//                    }
//
//                }
                slideUp();
            }
        });

        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void slideUp() {

        // Prepare the View for the animation
        footerLayout.setVisibility(View.VISIBLE);
        footerLayout.setAlpha(1.0f);

        // Start the animation
        footerLayout.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        footerLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    public boolean checkLocationPermission() {
        // jika izin untuk mengakses lokasi belum diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // tampilkan dialog permintaan izin untuk mengakses lokasi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    // method ini akan dipanggil setelah user memberikan respon pada dialog permintaan izin
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // jika izin tidak diberikan, maka isi array kosong ( grantResults.length == 0 )
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // jika izin untuk mengakses lokasi diberikan
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // panggil method getLocation()
                        getLocation();
                    }
                } else {
                    // jika izin tidak diberikan, hentikan aplikasi
                    finish();
                }
            }
        }
    }


    // class untuk menjalankan background proses
    public class GetAddress extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // membuat objek geocoder
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            // variable untuk menyimpan pesan error pada proses reverse geocoding
            String errorMessage = "";
            // list untuk menyimpan alamat yang didapatkan dari geocoder
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(),
                        // parameter ini adalah jumlah alamat yang akan kita ambil, pada contoh ini kita hanya akan mengambil satu alamat saja maka isi nilainya dengan 1
                        1);
            } catch (IOException ioException) {
                // handler jika terdapat kesalahan jaringan atau I/O
                errorMessage = "layanan tidak tersedia";
                // tampilkan pesan error di log
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // handler jika nilai latitude dan longitude tidak valid
                errorMessage = "nilai lat long tidak valid";
                // tampilkan nilai latitude dan longitude di log
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + lastLocation.getLatitude() +
                        ", Longitude = " +
                        lastLocation.getLongitude(), illegalArgumentException);
            }

            // handler jika tidak ada alamat yang ditemukan
            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "tidak ada alamat ditemukan";
                    // tampilkan pesan error di log
                    Log.e(TAG, errorMessage);
                }
                return errorMessage;
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // mengambil alamat dengan menggunakan getAddressLine()
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                // tampilkan string alamat ditemukan di log
                Log.i(TAG, "alamat ditemukan");
                // kembalikan alamat yang telah didapatkan
                return TextUtils.join(System.getProperty("line.separator"),
                        addressFragments);
            }
        }

        @Override
        protected void onPostExecute(String mAddressOutput) {
            super.onPostExecute(mAddressOutput);
            // ubah isi textview
            address.setText(mAddressOutput);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
