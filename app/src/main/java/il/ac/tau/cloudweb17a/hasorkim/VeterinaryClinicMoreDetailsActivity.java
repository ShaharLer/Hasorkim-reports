package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VeterinaryClinicMoreDetailsActivity extends AppCompatActivity {
    public  static final String VET_LATITUDE  = "vet longitude";
    public  static final String VET_LONGITUDE = "vet latitude";
    public  static final String PHONE_NUMBER  = "phone number";
    public  static final String WEBSITE       = "website";
    public  static final String OPENING_HOURS = "opening hours";
    private static final String UNKNOWN       = "לא ידוע";
    private static final String SUNDAY        = "יום ראשון";
    private static final String MONDAY        = "יום שני";
    private static final String TUESDAY       = "יום שלישי";
    private static final String WEDNESDAY     = "יום רביעי";
    private static final String THURSDAY      = "יום חמישי";
    private static final String FRIDAY        = "יום שישי";
    private static final String SATURDAY      = "יום שבת";
    private static final String DASH          = "–";
    private static final String OPEN_WITH     = "פתח באמצעות";
    private static final String TAG           = VetListActivity.class.getSimpleName();
    private static final OkHttpClient client  = new OkHttpClient();

    private ProgressBar progressBar;
    private RelativeLayout vetListLayout;
    private TextView phoneNumberTextView;
    private TextView websiteTextView;
    private String parsedVetPhoneNumber;
    private String originLatitude;
    private String originLongitude;
    private String destLatitude;
    private String destLongitude;
    private LinearLayout phoneCallLayout;
    private LinearLayout websiteLayout;
    private LinearLayout routeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary_clinic_more_details);
        Intent intent = getIntent();
        originLatitude  = intent.getStringExtra(VetListActivity.ORIGIN_LATITUDE);
        originLongitude = intent.getStringExtra(VetListActivity.ORIGIN_LONGITUDE);

        progressBar = findViewById(R.id.vet_more_details_progress_bar);
        vetListLayout = findViewById(R.id.vet_more_details_layout);

        TextView nameTextView = findViewById(R.id.vet_more_details_name);
        phoneNumberTextView = findViewById(R.id.vet_more_details_phone_number);

        TextView addressTextView = findViewById(R.id.vet_more_details_address);
        websiteTextView = findViewById(R.id.vet_more_details_website);

        // Setting the received text for the text views
        nameTextView.setText(intent.getStringExtra(VetListActivity.NAME));
        addressTextView.setText(intent.getStringExtra(VetListActivity.ADDRESS));

        phoneCallLayout = findViewById(R.id.call_button_layout);
        websiteLayout   = findViewById(R.id.website_button_layout);
        routeLayout     = findViewById(R.id.route_button_layout);

        /*************************************/
        /*** Run Google Places for Details ***/
        /*************************************/
        Uri.Builder urlPlaces = new Uri.Builder()
                .scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("place")
                .appendPath("details")
                .appendPath("json")
                .appendQueryParameter("placeid", intent.getStringExtra(VetListActivity.PLACE_ID))
                .appendQueryParameter("language", "iw")
                .appendQueryParameter("key", getString(R.string.google_places_key));

        String currentUrlPlaces = urlPlaces.build().toString();
        Log.d(TAG, currentUrlPlaces);

        try {
            run(currentUrlPlaces);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @throws IOException
     */
    // TODO consider to take this function to another class
    private void run(String url) throws IOException {
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

                    System.out.println("RETURNED STRING IS:");
                    String returnedString = responseBody.string();
                    System.out.println(returnedString);

                    Map<String, Object> placeDetails = DataParser.parsePlaceDetails(returnedString);
                    updateTextViews(placeDetails);
                }
            }
        });
    }

    /**
     * @param placeDetails
     */
    private void updateTextViews(final Map<String,Object> placeDetails) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String phoneNumber = (String) placeDetails.get(PHONE_NUMBER);
                if (phoneNumber != null) {
                    phoneNumberTextView.setText(phoneNumber);
                    parsedVetPhoneNumber = parsePhoneNumber(phoneNumber);
                    // TODO change visibility to VIEW.VISIBLE
                }
                else {
                    phoneNumberTextView.setText(UNKNOWN);
                    phoneCallLayout.setVisibility(View.GONE);
                }

                String website = (String) placeDetails.get(WEBSITE);
                if (website != null)
                    websiteTextView.setText(website);
                else {
                    websiteTextView.setText(UNKNOWN);
                    websiteLayout.setVisibility(View.GONE);
                }

                String[] openingHours = (String[]) placeDetails.get(OPENING_HOURS);
                if (openingHours != null)
                    setOpeningHoursTextViews(openingHours);
                else {
                    TextView openingHoursUnknownTextView = findViewById(R.id.vet_opening_hours_unknown);
                    openingHoursUnknownTextView.setText(UNKNOWN);
                }

                String vetLatitude  = (String) placeDetails.get(VET_LATITUDE);
                String vetLongitude = (String) placeDetails.get(VET_LONGITUDE);
                if ((vetLatitude != null) && (vetLongitude != null)) {
                    destLatitude = vetLatitude;
                    destLongitude = vetLongitude;
                }
                else
                    routeLayout.setVisibility(View.GONE);

                // change visibility after information is ready
                progressBar.setVisibility(View.GONE);
                vetListLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * @param phoneNumber
     * @return
     */
    private String parsePhoneNumber(String phoneNumber) {
        // parsing the phone number
        StringBuilder parsedNumber = new StringBuilder(phoneNumber);

        boolean keepParsing = (parsedNumber.indexOf(DASH) >= 0);

        while (keepParsing) {
            parsedNumber.deleteCharAt(parsedNumber.indexOf(DASH));
            keepParsing = (parsedNumber.indexOf(DASH) >= 0);
        }

        return parsedNumber.toString();
    }

    /**
     * @param openingHours
     */
    private void setOpeningHoursTextViews(String[] openingHours) {
        // Text views for the days labels
        TextView sundayLabelTextView    = findViewById(R.id.sundayLabel);
        TextView mondayLabelTextView    = findViewById(R.id.mondayLabel);
        TextView tuesdayLabelTextView   = findViewById(R.id.tuesdayLabel);
        TextView wednesdayLabelTextView = findViewById(R.id.wednesdayLabel);
        TextView thursdayLabelTextView  = findViewById(R.id.thursdayLabel);
        TextView fridayLabelTextView    = findViewById(R.id.fridayLabel);
        TextView saturdayLabelTextView  = findViewById(R.id.saturdayLabel);

        // Text views for the days opening hours
        TextView openingHoursSundayTextView    = findViewById(R.id.vet_opening_hours_sunday);
        TextView openingHoursMondayTextView    = findViewById(R.id.vet_opening_hours_monday);
        TextView openingHoursTuesdayTextView   = findViewById(R.id.vet_opening_hours_tuesday);
        TextView openingHoursWednesdayTextView = findViewById(R.id.vet_opening_hours_wednesday);
        TextView openingHoursThursdayTextView  = findViewById(R.id.vet_opening_hours_thursday);
        TextView openingHoursFridayTextView    = findViewById(R.id.vet_opening_hours_friday);
        TextView openingHoursSaturdayTextView  = findViewById(R.id.vet_opening_hours_saturday);

        // Setting the text for the days labels text views
        sundayLabelTextView.setText(SUNDAY);
        mondayLabelTextView.setText(MONDAY);
        tuesdayLabelTextView.setText(TUESDAY);
        wednesdayLabelTextView.setText(WEDNESDAY);
        thursdayLabelTextView.setText(THURSDAY);
        fridayLabelTextView.setText(FRIDAY);
        saturdayLabelTextView.setText(SATURDAY);

        // Setting the text for the days opening hours text views
        openingHoursSundayTextView.setText(openingHours[0]);
        openingHoursMondayTextView.setText(openingHours[1]);
        openingHoursTuesdayTextView.setText(openingHours[2]);
        openingHoursWednesdayTextView.setText(openingHours[3]);
        openingHoursThursdayTextView.setText(openingHours[4]);
        openingHoursFridayTextView.setText(openingHours[5]);
        openingHoursSaturdayTextView.setText(openingHours[6]);
    }

    /**
     * @param view
     */
    public void OnCallButtonClick(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + parsedVetPhoneNumber));
        if (callIntent.resolveActivity(getPackageManager()) != null)
            startActivity(callIntent);
    }

    /**
     * @param view
     */
    public void OnWebsiteButtonClick(View view) {
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteTextView.getText().toString()));
        if (websiteIntent.resolveActivity(getPackageManager()) != null)
            startActivity(websiteIntent);
    }

    /**
     * @param view
     */
    public void OnRouteButtonClick(View view) {
        // TODO take it off after debug (directly goes to google maps

        Uri.Builder urlRoute = new Uri.Builder()
                .scheme("https")
                .authority("maps.google.com")
                .appendPath("maps")
                //.appendQueryParameter("f", "d")
                //.appendQueryParameter("hl", "iw")
                .appendQueryParameter("saddr",(originLatitude + "," + originLongitude))
                .appendQueryParameter("daddr", (destLatitude + "," + destLongitude));

        System.out.println("The URL is: " + urlRoute.toString());
        Intent routeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlRoute.toString()));

        // TODO got problem with merging google maps and waze in this case
        //Uri nav = Uri.parse("geo:" + destLatitude + "," + destLongitude);                 // works perfect for Waze
        //Uri nav = Uri.parse("google.navigation:q=" + destLatitude + "," + destLongitude); // works good for Google Maps (default is driving)
        //Intent routeIntent = new Intent(Intent.ACTION_VIEW, nav);

        //Intent chooser = Intent.createChooser(routeIntent, OPEN_WITH); // Create intent to show chooser

        // Verify the intent will resolve to at least one activity
        if (routeIntent.resolveActivity(getPackageManager()) != null) {
            //startActivity(chooser);
            startActivity(routeIntent);
        }
        else
            Toast.makeText(getApplicationContext(), "זמנית לא ניתן לספק מסלול", Toast.LENGTH_LONG).show();
    }
}