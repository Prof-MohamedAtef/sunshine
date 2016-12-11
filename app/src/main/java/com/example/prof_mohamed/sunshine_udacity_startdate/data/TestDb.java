package com.example.prof_mohamed.sunshine_udacity_startdate.data;

/*  Created by Prof-Mohamed on 7/28/2016.*/


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestDb extends AndroidTestCase {

    public static String TEST_CITY_NAME = "North Pole";
    public static String TEST_LOCATION = "99705";
    public static String TEST_DATE = "20141205";

    String testLocationSetting = "99705";
    String testCityName = "North Pole";
    double testLatitude = 64.7488;
    double testLongitude = -147.353;

//    public void testLocationTable() {
//        insertLocation();
//    }

    public static final String LOG_TAG = TestDb.class.getSimpleName();


 /*   public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        long locationRowId = insertLocation();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", locationRowId == -1L);
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Weather): Create weather values
                ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

                        // Third Step (Weather): Insert ContentValues into database and get a row ID back
                                long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
                assertTrue(weatherRowId != -1);

                        // Fourth Step: Query the database and receive a Cursor back
                                // A cursor is your primary interface to the query results.
                                        Cursor weatherCursor = db.query(
                                WeatherEntry.TABLE_NAME,  // Table to Query
                                null, // leaving "columns" null just returns all the columns.
                                null, // cols for "where" clause
                                null, // values for "where" clause
                                null, // columns to group by
                                null, // columns to filter by row groups
                                null  // sort order
                                );

        // Move the cursor to the first valid database row and check to see if we have any rows
                assertTrue( "Error: No Records returned from location query", weatherCursor.moveToFirst() );

                        // Fifth Step: Validate the location Query
                                TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                                                weatherCursor, weatherValues);

                        // Move the cursor to demonstrate that there is only one record in the database
                                assertFalse( "Error: More than one record returned from weather query",
                                                weatherCursor.moveToNext() );

                        // Sixth Step: Close cursor and database
                                weatherCursor.close();
                dbHelper.close();
            }
*/
    public void testLocationTable() {

                // First step: Get reference to writable database
                        // If there's an error in those massive SQL table creation Strings,
                                // errors will be thrown here when you try to get a writable database.
                                        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create ContentValues of what you want to insert
                // Second Step: Create ContentValues of what you want to insert
                // (you can use the createNorthPoleLocationValues if you wish)

                // Insert ContentValues into database and get a row ID back

                // Query the database and receive a Cursor back

                // Move the cursor to a valid database row

                // Validate data in resulting Cursor with the original ContentValues
                ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

                // Third Step: Insert ContentValues into database and get a row ID back
                                long locationRowId;
                locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

                        // Verify we got a row back.
                                assertTrue(locationRowId != -1);

                        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
                                // the round trip.

                // Fourth Step: Query the database and receive a Cursor back
                // A cursor is your primary interface to the query results.
                Cursor cursor = db.query(
                                WeatherContract.LocationEntry.TABLE_NAME,  // Table to Query
                                null, // all columns
                                null, // Columns for the "where" clause
                                null, // Values for the "where" clause
                                null, // columns to group by
                                null, // columns to filter by row groups
                                null // sort order
                                );

                        // Move the cursor to a valid database row and check to see if we got any records back
                                // from the query
                                        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

                        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
                        // (you can use the validateCurrentRecord function in TestUtilities to validate the
                        // query if you like)
                                TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                                                cursor, testValues);

                // Finally, close the cursor and database
                        // Move the cursor to demonstrate that there is only one record in the database
                                assertFalse( "Error: More than one record returned from location query",
                                                cursor.moveToNext() );

                // Sixth Step: Close Cursor and Database
                        cursor.close();
                db.close();
    }

    public void testCreateDb() throws Throwable {
                // build a HashSet of all of the table names we wish to look for
                        // Note that there will be another table in the DB that stores the
                                // Android metadata (db version information)
                                        final HashSet<String> tableNameHashSet = new HashSet<String>();
                tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
                tableNameHashSet.add(WeatherEntry.TABLE_NAME);

                        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
                SQLiteDatabase db = new WeatherDbHelper(
                                this.mContext).getWritableDatabase();
                assertEquals(true, db.isOpen());

                         // have we created the tables we want?
                                 Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                        assertTrue("Error: This means that the database has not been created correctly",
                                        c.moveToFirst());

                        // verify that the tables have been created
                                do {
                        tableNameHashSet.remove(c.getString(0));
                    } while( c.moveToNext() );

                        // if this fails, it means that your database doesn't contain both the location entry
                                // and weather entry tables
                                        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                                                        tableNameHashSet.isEmpty());

                        // now, do our tables contain the correct columns?
                                c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                                null);

                        assertTrue("Error: This means that we were unable to query the database for table information.",
                                        c.moveToFirst());

                        // Build a HashSet of all of the column names we want to look for
                                final HashSet<String> locationColumnHashSet = new HashSet<String>();
                locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
                locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
                locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
                locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
                locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

                        int columnNameIndex = c.getColumnIndex("name");
                do {
                        String columnName = c.getString(columnNameIndex);
                        locationColumnHashSet.remove(columnName);
                    } while(c.moveToNext());

                        // if this fails, it means that your database doesn't contain all of the required location
                                // entry columns
                                        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                                                        locationColumnHashSet.isEmpty());
                db.close();
            }




/*        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.*/

    static ContentValues createWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        //weatherValues.put(WeatherEntry.COLUMN_DATETEXT, TEST_DATE);
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        return weatherValues;
    }

    static ContentValues createLocationValues() {
        ContentValues values = new ContentValues();
        values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, TEST_LOCATION);
        values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, TEST_CITY_NAME);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, 64.7488);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, -147.353);

        return values;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse(index == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(index));
        }
    }

    public void testInsertReadDb() {
        // If there's an error in those massive SQL table creation Strings,
        // errors will be throw here when you try to get a writable database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Create a new map of values, where columns names are the keys
        ContentValues values = createLocationValues();

        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);

        // Verify we got a row back
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,   // Table to query
                null,       // Columns for the query to get
                null,       // Columns for the "where" clause
                null,       // Values for the "where" clause
                null,       // Columns to group by
                null,       // Columns to filter by row groups
                null        // sort order
        );

        validateCursor(cursor, values);
        cursor.close();

        ContentValues weatherValues = createWeatherValues(locationRowId);

        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null,  weatherValues);

        assertTrue(weatherRowId != -1);
        Log.d(LOG_TAG, "Weather Row Id is " + weatherRowId);

        Cursor weatherTableCursor = db.query(
                WeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        validateCursor(weatherTableCursor, weatherValues);
        cursor.close();

        dbHelper.close();
    }

  /*  public long insertLocation() {
            // First step: Get reference to writable database
            // If there's an error in those massive SQL table creation Strings,
            // errors will be thrown here when you try to get a writable database.
            WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
            cursor.close();
            db.close();
            return locationRowId;
        }*/
}