package ca.aequilibrium.weather.enums;

import java.util.HashMap;
import java.util.Map;

public enum UnitType {
    METRIC(0), IMPERIAL(1);

    private int value;
    private static Map<Integer, UnitType> map = new HashMap<>();

    private UnitType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    static {
        for (UnitType type : UnitType.values()) {
            map.put(type.value, type);
        }
    }

    public static UnitType valueOf(Integer type) {
        UnitType mappedType = map.get(type);
        return mappedType != null ? mappedType : METRIC;
    }
}
