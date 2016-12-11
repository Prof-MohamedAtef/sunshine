package com.example.prof_mohamed.sunshine_udacity_startdate.data;

/**
 * Created by Prof-Mohamed on 7/27/2016.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
// device.
    public static final String CONTENT_AUTHORITY = "com.example.prof_mohamed.sunshine_udacity_startdate";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
// the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_WEATHER = "weather";
    public static final String PATH_LOCATION = "location";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

                        public static final String CONTENT_TYPE =
                                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
                public static final String CONTENT_ITEM_TYPE =
                                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String TABLE_NAME = "location";

        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_CITY_NAME = "city_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

        public static Uri buildLocationUri(long id) {
                        return ContentUris.withAppendedId(CONTENT_URI, id);
                    }
        public void testBuildWeatherLocation() {
                    Uri locationUri = WeatherEntry.buildWeatherLocation(TestWeatherContract.TEST_WEATHER_LOCATION);
                    assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                                                    "WeatherContract.",
                                    locationUri);
                    assertEquals("Error: Weather location not properly appended to the end of the Uri",
                                    TestWeatherContract.TEST_WEATHER_LOCATION, locationUri.getLastPathSegment());
                    assertEquals("Error: Weather location Uri doesn't match our expected result",
                                    locationUri.toString(),
                                    "content://com.example.android.sunshine.app/weather/%2FNorth%20Pole");
                }
    }
}
