package ca.aequilibrium.weather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtils {
    public static String cityNameForCoordinates(Context context, LatLng latLng) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String locality = addresses.get(0).getLocality();
            if (locality == null || locality.isEmpty()) {
                locality = addresses.get(0).getSubAdminArea();
            }
//            String cityName = addresses.get(0).getAddressLine(0);
//            String stateName = addresses.get(0).getAddressLine(1);
//            String countryName = addresses.get(0).getAddressLine(2);
            return locality;
        } catch (Exception e) {
            return null;
        }
    }
}
