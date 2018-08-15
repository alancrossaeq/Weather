package ca.aequilibrium.weather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtils {
    public static List<Address> addressesForCoordinates(Context context, LatLng latLng) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addresses;
        } catch (Exception e) {
            return null;
        }
    }
}
