package ca.aequilibrium.weather.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.weather.R;
import ca.aequilibrium.weather.diffCallbacks.ForecastDiffCallback;
import ca.aequilibrium.weather.enums.UnitType;
import ca.aequilibrium.weather.models.Forecast;
import ca.aequilibrium.weather.network.NetworkConstants;
import ca.aequilibrium.weather.utils.ImageLoader;
import ca.aequilibrium.weather.utils.PreferencesHelper;

public class ForecastAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Forecast> items;
    private Context context;

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public CardView cvRootView;
        private TextView tvTemp;
        private TextView tvHumidity;
        private TextView tvRain;
        private TextView tvWinds;
        private TextView tvDate;
        public ImageView ivWeatherIcon;
        public ForecastViewHolder(CardView v) {
            super(v);
            cvRootView = v;
            ivWeatherIcon = v.findViewById(R.id.iv_weather_icon);
            tvTemp = v.findViewById(R.id.tv_temp);
            tvHumidity = v.findViewById(R.id.tv_humidity);
            tvRain = v.findViewById(R.id.tv_rain);
            tvWinds = v.findViewById(R.id.tv_winds);
            tvDate = v.findViewById(R.id.tv_date);
        }
    }

    public ForecastAdapter(Context context, List<Forecast> newItems) {
        this.context = context;
        items = newItems == null ? new ArrayList<>() : newItems;
    }

    public void swapItems(List<Forecast> newItems) {
        newItems = newItems == null ? new ArrayList<>() : newItems;

        // compute diffs
        final ForecastDiffCallback diffCallback = new ForecastDiffCallback(items, newItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear contacts and add
        items.clear();
        items.addAll(newItems);

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);

        ForecastViewHolder vh = new ForecastViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UnitType unitType = PreferencesHelper.readUnitSystemType(context);
        String speedMetric = unitType == UnitType.METRIC ? context.getString(R.string.unit_kph) : context.getString(R.string.unit_mph);
        String tempMetric = unitType == UnitType.METRIC ? context.getString(R.string.unit_celsius) : context.getString(R.string.unit_fahrenheit);
        String volumeMetric = unitType == UnitType.METRIC ? context.getString(R.string.unit_ml) : context.getString(R.string.unit_ounces);

        final ForecastViewHolder forecastViewHolder = (ForecastViewHolder) holder;
        Forecast forecast = items.get(position);
        ImageLoader.getInstance(context).loadImageFromUrl(forecastViewHolder.ivWeatherIcon, NetworkConstants.openWeatherImgUrl + forecast.getWeather().get(0).getIcon() + ".png");
        forecastViewHolder.tvTemp.setText(forecast.getMain().getTemp() + tempMetric);
        forecastViewHolder.tvHumidity.setText(context.getString(R.string.humidity) + ": " + forecast.getMain().getHumidity() + "%");
        forecastViewHolder.tvRain.setText(context.getString(R.string.rainfall) + ": " + ((forecast.getRain() == null || forecast.getRain().getThreeHourVolume() == null) ? "0" : forecast.getRain().getThreeHourVolume()) + volumeMetric + " (3hr)");
        forecastViewHolder.tvWinds.setText(context.getString(R.string.winds) + ": " + forecast.getWind().getSpeed() + speedMetric);
        forecastViewHolder.tvDate.setText(forecast.getDt_txt() + " UTC");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
