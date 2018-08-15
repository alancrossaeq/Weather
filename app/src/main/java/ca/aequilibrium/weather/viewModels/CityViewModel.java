package ca.aequilibrium.weather.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import ca.aequilibrium.weather.models.CityView;
import ca.aequilibrium.weather.models.FiveDayForecast;
import ca.aequilibrium.weather.models.Forecast;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.network.RequestSender;

public class CityViewModel extends ViewModel {
    private MutableLiveData<CityView> cityViewLive;

    public LiveData<CityView> getCityView(Context context, Location location) {
        if (cityViewLive == null) {
            cityViewLive = new MutableLiveData<>();
        }
        CityView cityView = new CityView();
        cityView.setLocation(location);
        cityViewLive.setValue(cityView);

        loadCityView(context);

        return cityViewLive;
    }

    public void loadCityView(Context context) {
        RequestSender requestSender = new RequestSender();
        requestSender.getForecast(cityViewLive.getValue().getLocation(), new RequestSender.RequestSenderCallback() {
            @Override
            public void onResponse(String result, String error) {
                Gson gson = new Gson();
                Forecast forecast = gson.fromJson(result, Forecast.class);
                CityView cityView = cityViewLive.getValue();
                cityView.setForecast(forecast);
                cityViewLive.setValue(cityView);
            }
        });

        requestSender.getFiveDayForecast(cityViewLive.getValue().getLocation(), new RequestSender.RequestSenderCallback() {
            @Override
            public void onResponse(String result, String error) {
                Gson gson = new Gson();
                FiveDayForecast fiveDayForecast = gson.fromJson(result, FiveDayForecast.class);
                CityView cityView = cityViewLive.getValue();
                cityView.setFiveDayForecast(fiveDayForecast);
                cityViewLive.setValue(cityView);
            }
        });

        // Do an asynchronous operation to fetch users.
//        new GetForecastsTask(context, this).execute();
    }

//    private static class GetForecastsTask extends AsyncTask<Void, Void, String> {
//
//        private Context appContext;
//        private CityViewModel cityViewModel;
//
//        // only retain a weak reference to the activity
//        GetForecastsTask(Context appContextIn, CityViewModel cityViewModelIn) {
//            appContext = appContextIn;
//            cityViewModel = cityViewModelIn;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            List<Location> favourites = AppDatabase.getAppDatabase(appContext).locationDao().getAll();
////            for (Location location : favourites) {
////
////            }
//            return favourites;
//        }
//
//        @Override
//        protected void onPostExecute(List<Location> favourites) {
//            favouritesViewModel.favourites.setValue(favourites);
//        }
//    }
}
