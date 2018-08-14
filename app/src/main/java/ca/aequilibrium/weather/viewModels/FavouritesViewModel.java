package ca.aequilibrium.weather.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.fragments.FavouritesFragment;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.utils.LocationUtils;

public class FavouritesViewModel extends ViewModel {
    private MutableLiveData<List<Location>> favourites;

    public LiveData<List<Location>> getFavourites(Context context) {
        if (favourites == null) {
            favourites = new MutableLiveData<>();
            loadFavourites(context);
        }
        return favourites;
    }

    public void loadFavourites(Context context) {
        // Do an asynchronous operation to fetch users.
        new GetFavouriteLocationsTask(context, this).execute();
    }

    private static class GetFavouriteLocationsTask extends AsyncTask<Void, Void, List<Location>> {

        private Context appContext;
        private FavouritesViewModel favouritesViewModel;

        // only retain a weak reference to the activity
        GetFavouriteLocationsTask(Context appContextIn, FavouritesViewModel favouritesViewModelIn) {
            appContext = appContextIn;
            favouritesViewModel = favouritesViewModelIn;
        }

        @Override
        protected List<Location> doInBackground(Void... params) {
            List<Location> favourites = AppDatabase.getAppDatabase(appContext).locationDao().getAll();
            return favourites;
        }

        @Override
        protected void onPostExecute(List<Location> favourites) {
            favouritesViewModel.favourites.setValue(favourites);
        }
    }
}
