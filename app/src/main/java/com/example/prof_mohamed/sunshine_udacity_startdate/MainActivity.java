package com.example.prof_mohamed.sunshine_udacity_startdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ForecastFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menu.clear();
      //  getMenuInflater().inflate(R.menu.main, menu);
        /*
        MenuItem menuItem=(MenuItem) findView
        ById(R.id.settingsd);
        ImageSpan imagespan=new ImageSpan(this,R.drawable.twitter_e);
        CharSequence title=" "+menuItem.getTitle();
        SpannableString spannablestring=new SpannableString(title);
        spannablestring.setSpan(imagespan,0,1,0);
        menuItem.setTitle(spannablestring);*/
        /*MenuItem item = menu.findItem(R.id.settings_id);
        SpannableStringBuilder builder = new SpannableStringBuilder("Share");
        // replace "*" with icon
        builder.setSpan(new ImageSpan(this, R.drawable.twitter_e), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);*/
     //   return true;
    //}

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            ForecastFragment weatherTask = new ForecastFragment();
            weatherTask.execute("94043");
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
            //      FetchWeatherTask WeatherTask= new FetchWeatherTask();
            //    WeatherTask.execute();
            //return true;
        }
        if (id == R.id.contact_us_1) {
            return true;
        }
        if (id == R.id.contact_us_2) {
            return true;
        }
        if (id == R.id.contact_us_3) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}