package ca.aequilibrium.weather.asyncTasks;

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.utils.LocationUtils;

public class SaveFavouriteLocationTask extends AsyncTask<LatLng, Void, Location> {

    private Context context;
    private SimpleCallback simpleCallback;

    public SaveFavouriteLocationTask(Context context, SimpleCallback simpleCallback) {
        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected Location doInBackground(LatLng... params) {

        LatLng latLng = params[0];

        List<Address> addresses = LocationUtils.addressesForCoordinates(context, latLng);

        String locality = addresses.get(0).getLocality();
        if (locality == null || locality.isEmpty()) {
            locality = addresses.get(0).getSubAdminArea();
        }
        if (locality == null || locality.isEmpty()) {
            locality = addresses.get(0).getAdminArea();
        }
        String countryName = addresses.get(0).getCountryName();

        Location location = new Location();
        location.setLatLng(latLng);
        location.setName(locality);
        location.setCountry(countryName);
        AppDatabase.getAppDatabase(context).locationDao().insertAll(location);

        return location;
    }

    @Override
    protected void onPostExecute(Location location) {
        simpleCallback.onFinished(location);
    }
}
