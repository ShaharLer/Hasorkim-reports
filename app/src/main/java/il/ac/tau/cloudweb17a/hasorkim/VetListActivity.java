package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static il.ac.tau.cloudweb17a.hasorkim.VetListActivity.QueryType.DISTANCE_SEARCH_ALL;
import static il.ac.tau.cloudweb17a.hasorkim.VetListActivity.QueryType.DISTANCE_SEARCH_OPEN;
import static il.ac.tau.cloudweb17a.hasorkim.VetListActivity.QueryType.NEARBY_SEARCH_ALL;
import static il.ac.tau.cloudweb17a.hasorkim.VetListActivity.QueryType.NEARBY_SEARCH_OPEN;

public class VetListActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG  = VetListActivity.class.getSimpleName();
    private static final double DEFAULT_LATITUDE  = 32.0820748;  // TODO handle this
    private static final double DEFAULT_LONGITUDE = 34.7717487; // TODO handle this

    public static final String PLACE_ID          = "com.example.hasorkim.place_id";
    public static final String NAME              = "com.example.hasorkim.name";
    public static final String ADDRESS           = "com.example.hasorkim.address";
    public static final String ORIGIN_LATITUDE  = "com.example.hasorkim.origin_latitude";
    public static final String ORIGIN_LONGITUDE = "com.example.hasorkim.origin_longitude";

    // TODO should I declare them here or inside OcCreate
    private ProgressBar progressBar;
    private RadioGroup vetTypeButtons;
    private RadioButton all_vets_button;
    private RadioButton open_vets_button;
    private LinearLayout vetListLayout;
    private RecyclerView vetListRecyclerView;
    private List<VeterinaryClinic> allVetsList;
    private List<VeterinaryClinic> openVetsList;
    private double currLatitude  = DEFAULT_LATITUDE;  // TODO handle this after checking GPS
    private double currLongitude = DEFAULT_LONGITUDE; // TODO handle this after checking GPS

    /********************************** New variables *****************************************/
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = false;
    /******************************************************************************************/

    Intent receivedIntent;

    public enum QueryType {
        NEARBY_SEARCH_ALL, NEARBY_SEARCH_OPEN, DISTANCE_SEARCH_ALL, DISTANCE_SEARCH_OPEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connecting to the XML widgets
        progressBar = findViewById(R.id.vet_list_progress_bar);
        vetTypeButtons = findViewById(R.id.vet_type_buttons_group);
        all_vets_button = findViewById(R.id.all_vets_button);
        open_vets_button = findViewById(R.id.open_vets_button);
        vetListLayout = findViewById(R.id.vet_list_layout);
        vetListRecyclerView = findViewById(R.id.vet_list_recycler_view);
        Button backToReport = findViewById(R.id.going_to_report_btn);

        // We use this setting to improve performance because changes
        // in content do not change the layout size of the RecyclerView
        vetListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LayoutManager vetListLayoutManager = new LinearLayoutManager(this);
        vetListRecyclerView.setLayoutManager(vetListLayoutManager);

        ListItemDecoration decoration = new ListItemDecoration(this, Color.LTGRAY, 1f);
        vetListRecyclerView.addItemDecoration(decoration);

        receivedIntent = getIntent();
        String sourceActivity = receivedIntent.getStringExtra("from");

        if (sourceActivity.equals("dialog")) {
            currLatitude = receivedIntent.getDoubleExtra("lat",DEFAULT_LATITUDE);
            currLongitude = receivedIntent.getDoubleExtra("long", DEFAULT_LONGITUDE);
            getNearbyVets(false);
        }
        else {
            backToReport.setVisibility(View.GONE);
            // Construct a FusedLocationProviderClient
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getLocationPermission();
        }
    }

    /**
     * The listener for the "Back to report" button
     */
    public void OnButtonClick(View v) {
        Intent newReportIntent = new Intent(this, NewEventMoreDetailsRequestActivity.class);
        newReportIntent.putExtra("lat", currLatitude);
        newReportIntent.putExtra("long", currLongitude);
        newReportIntent.putExtra("address", receivedIntent.getStringExtra("address"));
        startActivity(newReportIntent);
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        Context context = this.getApplicationContext();

        if ((ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)   == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)    ) {
                mLocationPermissionGranted = true;
                getDeviceLocation(); // TODO check is should stay here
        }
        else {
            ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        //mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

        getDeviceLocation(); // TODO check if should stay here (looks like it does)
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && (task.getResult() != null)) {
                            // Set the map's camera position to the current location of the device.
                            Location mLastKnownLocation = task.getResult();
                            currLatitude = mLastKnownLocation.getLatitude();
                            currLongitude = mLastKnownLocation.getLongitude();
                        }
                        else {
                            System.out.println("Current location is null. Using defaults.");
                            System.out.println("Exception: " + task.getException());
                        }

                        getNearbyVets(false);
                    }
                });
            }
            else
                // until getting permission
                getLocationPermission(); // TODO check if should stay at the end
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     *
     */
    private void getNearbyVets(boolean onlyOpen) {
        Uri.Builder url = new Uri.Builder()
                .scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("place")
                .appendPath("nearbysearch")
                .appendPath("json")
                .appendQueryParameter("location", (currLatitude + "," + currLongitude))
                .appendQueryParameter("type", "veterinary_care")
                .appendQueryParameter("rankby", "distance")
                .appendQueryParameter("language", "iw")
                .appendQueryParameter("key", getString(R.string.google_maps_key));


        if (onlyOpen)
            url.appendQueryParameter("opennow", "true");

        String currentUrl = url.build().toString();
        Log.d(TAG, currentUrl);

        try {
            if (onlyOpen)
                run(currentUrl, NEARBY_SEARCH_OPEN);
            else
                run(currentUrl, NEARBY_SEARCH_ALL);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * @param url
     * @param parseType
     * @throws IOException
     */
    private void run(String url, final QueryType parseType) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    String returnedString = responseBody.string();

                    // delete this 2 lines
                    System.out.println("RETURNED STRING IS:");
                    System.out.println(returnedString);

                    switch (parseType) {
                        case NEARBY_SEARCH_ALL:
                            allVetsList = DataParser.parsePlaces(returnedString);
                            run(distancesApiCall(allVetsList), DISTANCE_SEARCH_ALL);
                            break;

                        case DISTANCE_SEARCH_ALL:
                            DataParser.getDistances(returnedString, allVetsList);
                            getNearbyVets(true);
                            break;

                        case NEARBY_SEARCH_OPEN:
                            openVetsList = DataParser.parsePlaces(returnedString);
                            run(distancesApiCall(openVetsList), DISTANCE_SEARCH_OPEN);
                            break;

                        case DISTANCE_SEARCH_OPEN:
                            DataParser.getDistances(returnedString, openVetsList);
                            updateUI(); // calling to update the screen
                    }
                }
            }
        });
    }

    /**
     * @param vetList
     */
    private String distancesApiCall(List<VeterinaryClinic> vetList) {
        int listSize = vetList.size();
        Log.d(TAG, "Vet list size is: " + listSize);

        StringBuilder nearVetsID = new StringBuilder();
        for (int i = 0; i < listSize; i++) {
            nearVetsID.append("place_id:" + vetList.get(i).getPlaceId());

            if (i < (listSize - 1))
                nearVetsID.append("|");
        }

        Uri.Builder urlMaps = new Uri.Builder()
                .scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("distancematrix")
                .appendPath("json")
                .appendQueryParameter("origins", (Double.toString(currLatitude) + "," + Double.toString(currLongitude)))
                .appendQueryParameter("destinations", nearVetsID.toString())
                .appendQueryParameter("mode","driving")
                .appendQueryParameter("language", "iw")
                .appendQueryParameter("key", getString(R.string.google_maps_key));

        String currentUrlDistances = urlMaps.build().toString();
        Log.d(TAG, currentUrlDistances);

        return currentUrlDistances;
    }

    /**
     *
     */
    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final VeterinaryClinicAdapter.OnItemClickListener buttonsListener = new VeterinaryClinicAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(VeterinaryClinic vetClinic) {
                        Intent intent = new Intent(VetListActivity.this, VeterinaryClinicMoreDetailsActivity.class);
                        intent.putExtra(PLACE_ID, vetClinic.getPlaceId());
                        intent.putExtra(NAME, vetClinic.getName());
                        intent.putExtra(ADDRESS, vetClinic.getAddress());
                        intent.putExtra(ORIGIN_LATITUDE, Double.toString(currLatitude));
                        intent.putExtra(ORIGIN_LONGITUDE, Double.toString(currLongitude));
                        startActivity(intent);
                    }
                };

                vetTypeButtons.clearCheck();
                vetTypeButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.all_vets_button:
                                vetListRecyclerView.setAdapter(new VeterinaryClinicAdapter(allVetsList, buttonsListener));
                                break;

                            case R.id.open_vets_button:
                                vetListRecyclerView.setAdapter(new VeterinaryClinicAdapter(openVetsList, buttonsListener));
                        }
                    }
                });

                // updating the screen with the open clinics showed

                if (openVetsList.isEmpty())
                    all_vets_button.setChecked(true);
                else
                    open_vets_button.setChecked(true);

                progressBar.setVisibility(View.GONE);
                vetListLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /****************************************************************************************************/
    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MapsActivity.class));
        //finish(); // TODO what is this method???
    }
    */
}