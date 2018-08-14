package ca.aequilibrium.weather.models;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private int threeHourVolume;

    public int getThreeHourVolume() {
        return threeHourVolume;
    }

    public void setThreeHourVolume(int threeHourVolume) {
        this.threeHourVolume = threeHourVolume;
    }
}
