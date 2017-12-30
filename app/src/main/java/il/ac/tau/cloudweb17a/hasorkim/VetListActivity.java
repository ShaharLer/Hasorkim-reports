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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private static final String DEFAULT_LATITUDE  = "32.0820748";  // TODO handle this
    private static final String DEFAULT_LONGITUDE = "34.7717487"; // TODO handle this

    public static final String PLACE_ID          = "com.example.hasorkim.place_id";
    public static final String NAME              = "com.example.hasorkim.name";
    public static final String ADDRESS           = "com.example.hasorkim.address";
    public static final String ORIGIN_LATITUDE  = "com.example.hasorkim.origin_latitude";
    public static final String ORIGIN_LONGITUDE = "com.example.hasorkim.origin_longitude";

    // TODO should I declare them here or inside OcCreate
    private ProgressBar progressBar;
    private RadioGroup vetTypeButtons;
    private RadioButton open_vets_button;
    private LinearLayout vetListLayout;
    private RecyclerView vetListRecyclerView;
    private List<VeterinaryClinic> allVetsList;
    private List<VeterinaryClinic> openVetsList;
    private String currLatitude  = DEFAULT_LATITUDE;
    private String currLongitude = DEFAULT_LONGITUDE;

    /********************************** New variables *****************************************/
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = false;
    /******************************************************************************************/

    public enum QueryType {
        NEARBY_SEARCH_ALL, NEARBY_SEARCH_OPEN, DISTANCE_SEARCH_ALL, DISTANCE_SEARCH_OPEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);

        // Connecting to the XML widgets
        progressBar = findViewById(R.id.vet_list_progress_bar);
        vetTypeButtons = findViewById(R.id.vet_type_buttons_group);
        open_vets_button = findViewById(R.id.open_vets_button);
        vetListLayout = findViewById(R.id.vet_list_layout);
        vetListRecyclerView = findViewById(R.id.vet_list_recycler_view);

        // We use this setting to improve performance because changes
        // in content do not change the layout size of the RecyclerView
        vetListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LayoutManager vetListLayoutManager = new LinearLayoutManager(this);
        vetListRecyclerView.setLayoutManager(vetListLayoutManager);

        VetListItemDecoration decoration = new VetListItemDecoration(this, Color.LTGRAY, 1f);
        vetListRecyclerView.addItemDecoration(decoration);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
    }

    /**
     * The listener for the "Back to report" button
     */
    public void OnButtonClick(View v) {
        Toast.makeText(getApplicationContext(), "Will return to report", Toast.LENGTH_SHORT).show();
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
                System.out.println("There is permission");
                getDeviceLocation(); // TODO check is should stay here
        }
        else {
            System.out.println("There is NOT permission");
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

        getDeviceLocation(); // TODO check is should stay here
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
                            currLatitude = Double.toString(mLastKnownLocation.getLatitude());
                            currLongitude = Double.toString(mLastKnownLocation.getLongitude());
                        }
                        else {
                            System.out.println("Current location is null. Using defaults.");
                            System.out.println("Exception: " + task.getException());
                        }

                        /*
                        getAllNearbyVets();  // Getting closest vets
                        getOpenNearbyVets(); // getting the closest open vets
                        */
                        getNearbyVets(false);
                        getNearbyVets(true);
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
                .appendQueryParameter("key", getString(R.string.google_places_key));


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
     * @param vetList
     */
    private void distancesApiCall(List<VeterinaryClinic> vetList, QueryType distanceSearchType) {
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
                .appendQueryParameter("origins", (currLatitude + "," + currLongitude))
                .appendQueryParameter("destinations", nearVetsID.toString())
                .appendQueryParameter("mode","driving")
                .appendQueryParameter("language", "iw")
                .appendQueryParameter("key", getString(R.string.google_places_key));

        String currentUrlDistances = urlMaps.build().toString();
        Log.d(TAG, currentUrlDistances);

        try {
            run(currentUrlDistances, distanceSearchType);
        } catch (IOException e) {
            e.printStackTrace();
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
                            distancesApiCall(allVetsList, DISTANCE_SEARCH_ALL);
                            break;

                        case NEARBY_SEARCH_OPEN:
                            openVetsList = DataParser.parsePlaces(returnedString);
                            distancesApiCall(openVetsList, DISTANCE_SEARCH_OPEN);
                            break;

                        case DISTANCE_SEARCH_ALL:
                            DataParser.getDistances(returnedString, allVetsList);
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
                        intent.putExtra(ORIGIN_LATITUDE, currLatitude);
                        intent.putExtra(ORIGIN_LONGITUDE, currLongitude);
                        startActivity(intent);
                    }
                };

                vetListRecyclerView.setAdapter(new VeterinaryClinicAdapter(allVetsList, buttonsListener));

                vetTypeButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        System.out.println("Got inside cause radio button changed");

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
                vetTypeButtons.clearCheck();
                open_vets_button.setChecked(true);
                progressBar.setVisibility(View.GONE);
                vetListLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /****************************************************************************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VetListActivity.this, ReportEventActivity.class));
        finish();
    }
}