package ca.aequilibrium.weather.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import ca.aequilibrium.weather.AppDatabase;
import ca.aequilibrium.weather.models.Location;

public class DeleteFavouriteLocationTask extends AsyncTask<Location, Void, String> {

    private Context context;
    private SimpleCallback simpleCallback;

    public DeleteFavouriteLocationTask(Context context, SimpleCallback simpleCallback) {
        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected String doInBackground(Location... params) {

        Location location = params[0];
        AppDatabase.getAppDatabase(context).locationDao().delete(location);

        return "task finished";
    }

    @Override
    protected void onPostExecute(String result) {
        simpleCallback.onFinished(null);
    }
}
