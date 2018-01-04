package il.ac.tau.cloudweb17a.hasorkim;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataParser {
    private static final int WEEKDAYS_NUMBER = 7;
    private static final int SUNDAY = 0;
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final String TAG = DataParser.class.getSimpleName();
    private static final int MAXIMUM_PLACES = 10;

    static List<VeterinaryClinic> parsePlaces(String jsonData) {
        List<VeterinaryClinic> vetsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            int maximumVetPlaces = Math.min(jsonArray.length(), MAXIMUM_PLACES);

            for (int i = 0; i < maximumVetPlaces; i++)
                vetsList.add(getPlace((JSONObject) jsonArray.get(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Vet list size is: " + vetsList.size());
        return vetsList;
    }

    private static VeterinaryClinic getPlace(JSONObject googlePlaceJson) {
        VeterinaryClinic vetPlace = null;
        String place_id = null;
        String placeName = null;
        String vicinity = null;

        try {
            if (!googlePlaceJson.isNull("place_id"))
                place_id = googlePlaceJson.getString("place_id");

            if (!googlePlaceJson.isNull("name"))
                placeName = googlePlaceJson.getString("name");

            if (!googlePlaceJson.isNull("vicinity"))
                vicinity = googlePlaceJson.getString("vicinity");

            vetPlace = new VeterinaryClinic(place_id, placeName, vicinity);
            Log.d(TAG, vetPlace.getPlaceId());
            Log.d(TAG, vetPlace.getName());
            Log.d(TAG, vetPlace.getAddress());
        } catch (JSONException e) {
            Log.d(TAG, "Error");
            e.printStackTrace();
        }

        return vetPlace;
    }

    static void getDistances(String jsonData, List<VeterinaryClinic> vetList) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = (JSONObject)(jsonArray.get(i));
                String distance = element.getJSONObject("duration").getString("value");
                vetList.get(i).setDistanceFromOrigin(Math.round(Double.parseDouble(distance) / 60));
            }

            Collections.sort(vetList); // sort according to distance
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static Map<String, Object> parsePlaceDetails(String jsonData) {
        Map<String, Object> placeDetails = new HashMap<>();
        String vetLatitude = null, vetLongitude = null, phoneNumber = null;
        String[] openingHours = null;
        String website = null;

        try {
            JSONObject placeDetailsJSON = (new JSONObject(jsonData)).getJSONObject("result");

            if (!placeDetailsJSON.isNull("geometry")) {
                JSONObject LatLngJSON = placeDetailsJSON.getJSONObject("geometry");

                if (!LatLngJSON.isNull("location")) {
                    LatLngJSON = LatLngJSON.getJSONObject("location");

                    if (!LatLngJSON.isNull("lat") && !LatLngJSON.isNull("lng")) {
                        vetLatitude = LatLngJSON.getString("lat");
                        vetLongitude = LatLngJSON.getString("lng");
                    }
                }
            }

            if (!placeDetailsJSON.isNull("formatted_phone_number"))
                phoneNumber = placeDetailsJSON.getString("formatted_phone_number");

            if (!placeDetailsJSON.isNull("website"))
                website = placeDetailsJSON.getString("website");

            if (!placeDetailsJSON.isNull("opening_hours")) {
                placeDetailsJSON = placeDetailsJSON.getJSONObject("opening_hours");

                if (!placeDetailsJSON.isNull("weekday_text")) {
                    JSONArray weekdayHoursJsonArray = placeDetailsJSON.getJSONArray("weekday_text");
                    openingHours = new String[WEEKDAYS_NUMBER];
                    int hoursArrayLength = weekdayHoursJsonArray.length();
                    String currParsedString;

                    for (int i = 0; i < hoursArrayLength; i++) {
                        openingHours[i] = "";
                        currParsedString = (String)weekdayHoursJsonArray.get(i);

                        if ((currParsedString == null) || (!currParsedString.contains(":")))
                            continue;

                        openingHours[i] = getOpeningHours(currParsedString, i);
                    }
                }
            }

            placeDetails.put(VeterinaryClinicMoreDetailsActivity.VET_LATITUDE, vetLatitude);
            placeDetails.put(VeterinaryClinicMoreDetailsActivity.VET_LONGITUDE, vetLongitude);
            placeDetails.put(VeterinaryClinicMoreDetailsActivity.PHONE_NUMBER, phoneNumber);
            placeDetails.put(VeterinaryClinicMoreDetailsActivity.WEBSITE, website);
            placeDetails.put(VeterinaryClinicMoreDetailsActivity.OPENING_HOURS, openingHours);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }

        return placeDetails;
    }

    private static String getOpeningHours(String currParsedString, int index) {
        switch (index) {
            case SUNDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                currParsedString = currParsedString.substring(11);
                break;
            case MONDAY:
            case SATURDAY:
                currParsedString = currParsedString.substring(9);
                break;
            case FRIDAY:
                currParsedString = currParsedString.substring(10);
                break;
            default:
                currParsedString = null;
        }

        boolean keepParsing = (currParsedString.contains(":") && currParsedString.contains("–"));

        if (!keepParsing)
            return currParsedString;

        // TODO change to String Builder
        String parsedHours = "";
        int specialCharIndex;

        while (keepParsing) {
            specialCharIndex = currParsedString.indexOf("–");
            parsedHours = (currParsedString.substring(0, specialCharIndex) + parsedHours);
            parsedHours = ("–" + parsedHours);
            currParsedString = currParsedString.substring(specialCharIndex + 1);

            if (!currParsedString.contains(",")) {
                parsedHours = (currParsedString.substring(0) + parsedHours);
                keepParsing = false;
            } else {
                specialCharIndex = currParsedString.indexOf(",");
                parsedHours = (currParsedString.substring(0, specialCharIndex) + parsedHours);
                parsedHours = ("  ," + parsedHours);
                currParsedString = currParsedString.substring(specialCharIndex + 2);
            }
        }

        return parsedHours;
    }
}