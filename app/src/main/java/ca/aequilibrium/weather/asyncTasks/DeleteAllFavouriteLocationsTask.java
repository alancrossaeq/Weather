package ca.aequilibrium.weather.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import ca.aequilibrium.weather.AppDatabase;

public class DeleteAllFavouriteLocationsTask extends AsyncTask<String, Void, String> {

    private Context context;
    private SimpleCallback simpleCallback;

    public DeleteAllFavouriteLocationsTask(Context context, SimpleCallback simpleCallback) {
        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected String doInBackground(String... params) {

        AppDatabase.getAppDatabase(context).locationDao().deleteAll();

        return "task finished";
    }

    @Override
    protected void onPostExecute(String result) {
        simpleCallback.onFinished(null);
    }
}
