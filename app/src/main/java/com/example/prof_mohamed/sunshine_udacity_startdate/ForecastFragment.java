package com.example.prof_mohamed.sunshine_udacity_startdate;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Prof-Mohamed on 7/12/2016.
 */

public class ForecastFragment extends Fragment {

    public ForecastFragment() {

    }

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

// view menu items and overflow icon in action bar horizontally and vertically view
        // stack over flow at http://stackoverflow.com/questions/21544501/overflow-icon-in-action-bar-invisible
        //ForecastFragment forcast=new ForecastFragment();
        /*try {
            ViewConfiguration config = ViewConfiguration.get((getContext()));
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }*/


      //  runOnUiThread();

      }

    ArrayAdapter<String> mForecastAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, content,false);

        /*String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP IN WEATHER STATION - 60/51",
                "Sun - Sunny - 80/68"
        };*/

        /*ArrayList<String> weekForecast = new ArrayList<String>(
        Arrays.asList(forecastArray));*/

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast ,
                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                new ArrayList<String>());
//        runOnUiThread();
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mForecastAdapter.getItem(position);
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

    //    if (mNotifyOnChange) notifyDataSetChanged();
        //mForecastAdapter.notifyDataSetChanged();
       // runOnUiThread();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.forecastfragment, menu);
        //inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();

            return true;
        }
        if (id == R.id.settings_click) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        if (id == R.id.contact_us_3) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        SharedPreferences sharedPrefs =
                                PreferenceManager.getDefaultSharedPreferences(getActivity());
                String location = sharedPrefs.getString(
                                getString(R.string.pref_location_key),
                                getString(R.string.pref_location_default));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                                .appendQueryParameter("q", location)
                                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

        //Context context=new Context() ;

        //List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "Couldn't call geoLocation at: " + location + ", no receiving apps installed!");
                    }
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

   /* public class FetchWeatherTask extends AsyncTask<String,Void,String[]> {


         private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


         private String getReadableDateString(long time){
             // Because the API returns a unix timestamp (measured in seconds),
             // it must be converted to milliseconds in order to be converted to valid date.
             Date date=new Date(time*1000);
             SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
             return format.format(date).toString();
         }


         private String formatHighLows(double high, double low,String unitType) {

             SharedPreferences sharedPrefs =
                                         PreferenceManager.getDefaultSharedPreferences(getActivity());
                         unitType = sharedPrefs.getString(
                                         getString(R.string.pref_units_key),
                                         getString(R.string.pref_units_metric));

             // For presentation, assume the user doesn't care about tenths of a degree.
             if (unitType.equals(getString(R.string.pref_units_imperial))){
                 high=(high*1.8)+32;
                 low=(low*1.8)+32;
             }else if(!unitType.equals(getString(R.string.pref_units_metric))){
                 Log.d(LOG_TAG,"Unit type not found: "+unitType);
             }

             long roundedHigh = Math.round(high);
             long roundedLow = Math.round(low);
             String highLowStr = roundedHigh + "/" + roundedLow;
             return highLowStr;
         }


         private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                 throws JSONException {

             // These are the names of the JSON objects that need to be extracted.
             final String OWM_LIST = "list";
             final String OWM_WEATHER = "weather";
             final String OWM_TEMPERATURE = "temp";
             final String OWM_MAX = "max";
             final String OWM_MIN = "min";
             final String OWM_DESCRIPTION = "main";

             JSONObject forecastJson = new JSONObject(forecastJsonStr);
             JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

             // OWM returns daily forecasts based upon the local time of the city that is being
             // asked for, which means that we need to know the GMT offset to translate this data
             // properly.

             // Since this data is also sent in-order and the first day is always the
             // current day, we're going to take advantage of that to get a nice
             // normalized UTC date for all of our weather.

             Time dayTime = new Time();
             dayTime.setToNow();

             // we start at the day returned by local time. Otherwise this is a mess.
             int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

             // now we work exclusively in UTC
             dayTime = new Time();

             String[] resultStrs = new String[numDays];
             for(int i = 0; i < weatherArray.length(); i++) {
                 // For now, using the format "Day, description, hi/low"
                 String day;
                 String description;
                 String highAndLow;

                 // Get the JSON object representing the day
                 JSONObject dayForecast = weatherArray.getJSONObject(i);

                 // The date/time is returned as a long.  We need to convert that
                 // into something human-readable, since most people won't read "1400356800" as
                 // "this saturday".
                 long dateTime;
                 // Cheating to convert this to UTC time, which is what we want anyhow
                 dateTime = dayTime.setJulianDay(julianStartDay+i);
                 day = getReadableDateString(dateTime);

                 // description is in a child array called "weather", which is 1 element long.
                 JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                 description = weatherObject.getString(OWM_DESCRIPTION);

                 // Temperatures are in a child object called "temp".  Try not to name variables
                 // "temp" when working with temperature.  It confuses everybody.
                 JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                 double high = temperatureObject.getDouble(OWM_MAX);
                 double low = temperatureObject.getDouble(OWM_MIN);

                 SharedPreferences sharedPrefs =
                                             PreferenceManager.getDefaultSharedPreferences(getActivity());
                             String unitType = sharedPrefs.getString(
                                             getString(R.string.pref_units_key),
                                             getString(R.string.pref_units_metric));
                 highAndLow = formatHighLows(high, low, unitType);
                 //highAndLow = formatHighLows(high, low,unitType);
                 resultStrs[i] = day + " - " + description + " - " + highAndLow;
             }

             for (String s : resultStrs) {
                 Log.v(LOG_TAG, "Forecast entry: " + s);
             }
             return resultStrs;
         }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                   return null;
                }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                final String FORECAST_BASE_URL =
                "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";
                //final String apikey="ea2f2f3b31cbc318071a99d9a7b5de08";
                //final String apikey="6c0fbc3c6cf80a15cc1458bd74441157";
                final String apikey="420c1005471290db0bf2dcd09d7f86ad";


                        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, apikey)
                        .build();

                        URL url = new URL(builtUri.toString());

                        Log.v(LOG_TAG, "Built URI " + builtUri.toString());



                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG,"Forecast JSON String: "+forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error here Exactly ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                        }
                }
            }
            try{
                return getWeatherDataFromJson(forecastJsonStr,numDays);
            }catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }

         @Override
         protected void onPostExecute(String[] result) {
            if (result != null) {
                    mForecastAdapter.clear();
                        for(String dayForecastStr : result) {
                            mForecastAdapter.add(dayForecastStr);
                            }
                             // New data is back from the server.  Hooray!
                        }
                     }

     }*/
}