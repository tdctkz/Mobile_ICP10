package com.example.vijaya.earthquakeapp;

import android.icu.util.ICUUncheckedIOException;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static List<Earthquake> fetchEarthquakeData2(String requestUrl) {
        // An empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();
        //  URL object to store the url for a given string
        URL url = null;
        // A string to store the response obtained from rest call in the form of string
        String jsonResponse = "";
        try {
            //TODO: 1. Create a URL from the requestUrl string and make a GET request to it

            //creating new url and attempting to access information here
            url = new URL (requestUrl);
            HttpURLConnection url_connect = (HttpURLConnection) url.openConnection();
            url_connect.setReadTimeout(10000);
            url_connect.setConnectTimeout(15000);
            url_connect.setRequestMethod("GET");
            url_connect.connect();

            //TODO: 2. Read from the Url Connection and store it as a string(jsonResponse)
            StringBuilder output = new StringBuilder();
            if(url_connect.getResponseCode() == 200){//checks if the connection was successful
                InputStream in = url_connect.getInputStream();//retrieves input stream.
                if (in != null){
                    InputStreamReader inputStreamReader = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    System.out.println("Output");
                    String line = reader.readLine();
                    while(line != null){
                        output.append(line);//concatenates info to string.
                        line = reader.readLine();
                    }
                }
                System.out.println(output);
            }

            /*TODO: 3. Parse the jsonResponse string obtained in step 2 above into JSONObject to extract the values of
                "mag","place","time","url"for every earth quake and create corresponding Earthquake objects with them
                 Add each earthquake object to the list(earthquakes) and return it.
             */
            JSONObject eqinfo  = new JSONObject(String.valueOf(output));
            JSONArray myinfo = eqinfo.getJSONArray("features");
            for (int i = 0; i< myinfo.length(); i++){//creeats the jsonobjects line by line
                JSONObject currentEQ = myinfo.getJSONObject(i);
                JSONObject properties = currentEQ.getJSONObject("properties");
                double mag = properties.getDouble("mag");//earthquake magnitured
                String location = properties.getString("place");//earthquake locations
                long time = properties.getLong("time");//time
                String quake_url = properties.getString("url");//link to earthquake info
                Earthquake earthquake = new Earthquake(mag,location,time,quake_url);//create new variable to store info
                earthquakes.add(earthquake);//adds to earthquake array
                System.out.println(earthquakes);//displays info
            }

        } catch (Exception e) {//error handling
            Log.e(LOG_TAG, "Exception:  ", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }
}