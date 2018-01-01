package il.ac.tau.cloudweb17a.hasorkim;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Intent service to fetch address from location
 */
public class FetchAddressIntentService extends IntentService {

    private static final String TAG = FetchAddressIntentService.class.getSimpleName();
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Set locale to Hebrew, Israel in order to get address in hebrew
        Locale locale = new Locale("iw", "IL");
        Geocoder geocoder = new Geocoder(this, locale);
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        String errorMessage = "";

        // Get the location passed to this service through an extra.
        LatLng target = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    target.latitude,
                    target.longitude,
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.no_network_error);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + target.latitude +
                    ", Longitude = " +
                    target.longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.address_not_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
        } else {
            Address address = addresses.get(0);
            Log.d(TAG, "Address found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, Address address) {
        Bundle bundle = new Bundle();
        if (address != null) {
            bundle.putString(Constants.STREET_NAME, address.getThoroughfare());
            bundle.putString(Constants.STREET_CITY, address.getLocality());
            bundle.putString(Constants.STREET_NUMBER, address.getSubThoroughfare());
        }
        mReceiver.send(resultCode, bundle);
    }


}
