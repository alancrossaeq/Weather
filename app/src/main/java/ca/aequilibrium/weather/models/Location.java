package ca.aequilibrium.weather.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

@Entity
public class Location implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "latLng")
    private LatLng latLng;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "country")
    private String country;

    public Location() {}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    protected Location(Parcel in) {
        uid = in.readInt();
        latLng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        name = in.readString();
        country = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeValue(latLng);
        dest.writeString(name);
        dest.writeString(country);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
