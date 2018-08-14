package ca.aequilibrium.weather.diffCallbacks;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.aequilibrium.weather.models.Location;

public class LocationDiffCallback extends DiffUtil.Callback {

    private List<Location> mNewList;
    private List<Location> mOldList;

    public LocationDiffCallback(List<Location> oldList, List<Location> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Location oldEntry = mOldList.get(oldItemPosition);
        Location newEntry = mNewList.get(newItemPosition);

        boolean same = oldEntry.getLatLng().latitude == newEntry.getLatLng().latitude &&
                oldEntry.getLatLng().longitude == newEntry.getLatLng().longitude &&
                oldEntry.getName().equals(newEntry.getName());

        return same;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // add a unique ID property on Contact and expose a getId() method
        return mOldList.get(oldItemPosition).getUid() == mNewList.get(newItemPosition).getUid();
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
