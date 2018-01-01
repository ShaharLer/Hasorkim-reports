package il.ac.tau.cloudweb17a.hasorkim;

import android.support.annotation.NonNull;

public class VeterinaryClinic implements Comparable<VeterinaryClinic> {

    private String place_id;
    private String name;
    private String address;
    private long distanceFromOrigin;

    VeterinaryClinic(String place_id, String name, String address) {
        this.place_id = place_id;
        this.name = name;
        this.address = address;
    }

    String getPlaceId() {
        return this.place_id;
    }

    String getName() {
        return this.name;
    }

    String getAddress() {
        return this.address;
    }

    long getDistanceFromOrigin() {
        return this.distanceFromOrigin;
    }

    void setDistanceFromOrigin(long distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    @Override
    public int compareTo(@NonNull VeterinaryClinic o) {
        return Long.compare(this.getDistanceFromOrigin(), o.getDistanceFromOrigin());
    }
}
