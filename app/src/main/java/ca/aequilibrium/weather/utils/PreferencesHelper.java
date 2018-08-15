package ca.aequilibrium.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ca.aequilibrium.weather.enums.UnitType;

public class PreferencesHelper {

    private static final String PREFERENCES_NAME = "weather.app.prefs";
    private static final String PREF_UNIT_SYSTEM_TYPE = "pref.unitSystemType";

    public static UnitType readUnitSystemType(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        int value = sharedPref.getInt(PREF_UNIT_SYSTEM_TYPE, UnitType.METRIC.getValue());

        return UnitType.valueOf(value);
    }

    public static void setUnitSystemType(Context context, UnitType unitType) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_UNIT_SYSTEM_TYPE, unitType.getValue());
        editor.commit();
    }
}
