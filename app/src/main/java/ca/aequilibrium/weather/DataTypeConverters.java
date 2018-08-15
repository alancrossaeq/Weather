package ca.aequilibrium.weather;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class DataTypeConverters {

    @TypeConverter
    public static String latLngToString(LatLng latLng) {
        Gson gson = new Gson();
        return gson.toJson(latLng);
    }

    @TypeConverter
    public static LatLng stringToLatLng(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, LatLng.class);
    }
}
