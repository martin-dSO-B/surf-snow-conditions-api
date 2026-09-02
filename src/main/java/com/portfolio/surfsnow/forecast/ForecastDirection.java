package com.portfolio.surfsnow.forecast;

final class ForecastDirection {

    private static final String[] COMPASS_POINTS = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    private ForecastDirection() {
    }

    static String toCompass(Integer degrees) {
        if (degrees == null) {
            return null;
        }
        int normalized = Math.floorMod(degrees, 360);
        int index = (int) Math.round(normalized / 45.0) % COMPASS_POINTS.length;
        return COMPASS_POINTS[index];
    }
}
