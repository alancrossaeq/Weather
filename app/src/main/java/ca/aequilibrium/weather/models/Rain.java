package ca.aequilibrium.weather.models;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private Double threeHourVolume;

    public Double getThreeHourVolume() {
        return threeHourVolume;
    }

    public void setThreeHourVolume(Double threeHourVolume) {
        this.threeHourVolume = threeHourVolume;
    }
}
