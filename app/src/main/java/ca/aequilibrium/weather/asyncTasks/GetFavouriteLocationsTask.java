package ca.aequilibrium.weather.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.models.Location;

public class GetFavouriteLocationsTask extends AsyncTask<Void, Void, List<Location>> {

    private Context context;
    private SimpleCallback simpleCallback;

    public GetFavouriteLocationsTask(Context context, SimpleCallback simpleCallback) {
        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected List<Location> doInBackground(Void... params) {
        List<Location> favourites = AppDatabase.getAppDatabase(context).locationDao().getAll();
        return favourites;
    }

    @Override
    protected void onPostExecute(List<Location> favourites) {
        simpleCallback.onFinished(favourites);
    }
}
