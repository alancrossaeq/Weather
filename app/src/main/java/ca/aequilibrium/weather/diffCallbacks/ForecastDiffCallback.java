package ca.aequilibrium.weather.diffCallbacks;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.aequilibrium.weather.models.Forecast;

public class ForecastDiffCallback extends DiffUtil.Callback {

    private List<Forecast> mNewList;
    private List<Forecast> mOldList;

    public ForecastDiffCallback(List<Forecast> oldList, List<Forecast> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Forecast oldEntry = mOldList.get(oldItemPosition);
        Forecast newEntry = mNewList.get(newItemPosition);

        boolean same = oldEntry.getMain().getHumidity().doubleValue() == newEntry.getMain().getHumidity().doubleValue() &&
                ((oldEntry.getRain() == null && newEntry.getRain() == null) || (oldEntry.getRain().getThreeHourVolume() == null && newEntry.getRain().getThreeHourVolume() == null) || (oldEntry.getRain().getThreeHourVolume().doubleValue() == newEntry.getRain().getThreeHourVolume().doubleValue())) &&
                ((oldEntry.getWind()== null && newEntry.getWind() == null) || (oldEntry.getWind().getSpeed().doubleValue() == newEntry.getWind().getSpeed().doubleValue())) &&
                oldEntry.getMain().getTemp().doubleValue() == newEntry.getMain().getTemp().doubleValue();

        return same;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getDt().longValue() == mNewList.get(newItemPosition).getDt().longValue();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }
}
