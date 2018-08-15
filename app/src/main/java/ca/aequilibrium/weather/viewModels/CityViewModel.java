package ca.aequilibrium.weather.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.google.gson.Gson;

import ca.aequilibrium.weather.enums.UnitType;
import ca.aequilibrium.weather.models.CityView;
import ca.aequilibrium.weather.models.FiveDayForecast;
import ca.aequilibrium.weather.models.Forecast;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.network.RequestSender;
import ca.aequilibrium.weather.utils.PreferencesHelper;

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
        UnitType unitType = PreferencesHelper.readUnitSystemType(context);

        RequestSender requestSender = new RequestSender();
        requestSender.getForecast(cityViewLive.getValue().getLocation(), unitType, new RequestSender.RequestSenderCallback() {
            @Override
            public void onResponse(String result, String error) {
                Gson gson = new Gson();
                Forecast forecast = gson.fromJson(result, Forecast.class);
                CityView cityView = cityViewLive.getValue();
                cityView.setForecast(forecast);
                cityViewLive.setValue(cityView);
            }
        });

        requestSender.getFiveDayForecast(cityViewLive.getValue().getLocation(), unitType, new RequestSender.RequestSenderCallback() {
            @Override
            public void onResponse(String result, String error) {
                Gson gson = new Gson();
                FiveDayForecast fiveDayForecast = gson.fromJson(result, FiveDayForecast.class);
                CityView cityView = cityViewLive.getValue();
                cityView.setFiveDayForecast(fiveDayForecast);
                cityViewLive.setValue(cityView);
            }
        });
    }
}
