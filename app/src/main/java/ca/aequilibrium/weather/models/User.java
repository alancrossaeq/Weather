package ca.aequilibrium.weather.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "favourites_locations")
    private List<Location> favouriteLocations;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<Location> getFavouriteLocations() {
        return favouriteLocations;
    }

    public void setFavouriteLocations(List<Location> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }
}
