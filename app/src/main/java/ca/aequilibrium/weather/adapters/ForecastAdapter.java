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
import ca.aequilibrium.weather.models.Forecast;
import ca.aequilibrium.weather.network.NetworkConstants;
import ca.aequilibrium.weather.utils.ImageLoader;

public class ForecastAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Forecast> mData;
    private Context context;

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public CardView mRootView;
        private TextView tvTemp;
        private TextView tvHumidity;
        private TextView tvRain;
        private TextView tvWinds;
        private TextView tvDate;
        public ImageView ivWeatherIcon;
        public ForecastViewHolder(CardView v) {
            super(v);
            mRootView = v;
            ivWeatherIcon = v.findViewById(R.id.iv_weather_icon);
            tvTemp = v.findViewById(R.id.tv_temp);
            tvHumidity = v.findViewById(R.id.tv_humidity);
            tvRain = v.findViewById(R.id.tv_rain);
            tvWinds = v.findViewById(R.id.tv_winds);
            tvDate = v.findViewById(R.id.tv_date);
        }
    }

    public ForecastAdapter(Context context, List<Forecast> favourites) {
        this.context = context;
        mData = favourites == null ? new ArrayList<>() : favourites;
    }

    public void swapItems(List<Forecast> newData) {
        newData = newData == null ? new ArrayList<>() : newData;

        // compute diffs
        final ForecastDiffCallback diffCallback = new ForecastDiffCallback(mData, newData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear contacts and add
        mData.clear();
        mData.addAll(newData);

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);

        ForecastViewHolder vh = new ForecastViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ForecastViewHolder forecastViewHolder = (ForecastViewHolder) holder;
        Forecast forecast = mData.get(position);
        ImageLoader.getInstance(context).loadImageFromUrl(forecastViewHolder.ivWeatherIcon, NetworkConstants.openWeatherImgUrl + forecast.getWeather().get(0).getIcon() + ".png");
        forecastViewHolder.tvTemp.setText(forecast.getMain().getTemp() + "°C");
        forecastViewHolder.tvHumidity.setText(context.getString(R.string.humidity) + ": " + forecast.getMain().getHumidity() + "%");
        forecastViewHolder.tvRain.setText(context.getString(R.string.rainfall) + ": " + (forecast.getRain() == null ? "0" : forecast.getRain().getThreeHourVolume()) + "ml (3hr)");
        forecastViewHolder.tvWinds.setText(context.getString(R.string.winds) + ": " + forecast.getWind().getSpeed() + "mph");
        forecastViewHolder.tvDate.setText(forecast.getDt_txt() + " UTC");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }
}
