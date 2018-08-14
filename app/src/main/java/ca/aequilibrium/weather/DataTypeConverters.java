package ca.aequilibrium.weather;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ca.aequilibrium.weather.models.Location;

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

    @TypeConverter
    public static String favouriteLocationsToString(List<Location> favouriteLocations) {
        Gson gson = new Gson();
        return gson.toJson(favouriteLocations);
    }

    @TypeConverter
    public static List<Location> stringToFavouriteLocations(String string) {
        if (string == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Location>>() {
        }.getType();

        return gson.fromJson(string, type);
    }
}
