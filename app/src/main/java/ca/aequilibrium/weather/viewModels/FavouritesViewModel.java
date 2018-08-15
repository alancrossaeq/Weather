package ca.aequilibrium.weather.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import ca.aequilibrium.weather.asyncTasks.GetFavouriteLocationsTask;
import ca.aequilibrium.weather.asyncTasks.SimpleCallback;
import ca.aequilibrium.weather.models.Location;

public class FavouritesViewModel extends ViewModel {
    private MutableLiveData<List<Location>> favourites;

    public LiveData<List<Location>> getFavourites(Context context) {
        if (favourites == null) {
            favourites = new MutableLiveData<>();
        }

        loadFavourites(context);

        return favourites;
    }

    public void loadFavourites(Context context) {
        new GetFavouriteLocationsTask(context, new SimpleCallback() {
            @Override
            public void onFinished(Object result) {
                List<Location> resultFavourites = (List<Location>) result;
                favourites.setValue(resultFavourites);
            }
        }).execute();
    }
}
