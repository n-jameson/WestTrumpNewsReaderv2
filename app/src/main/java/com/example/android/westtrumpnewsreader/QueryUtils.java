package com.example.android.westtrumpnewsreader;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.westtrumpnewsreader.ReaderActivity.LOG_TAG;

public class QueryUtils {

    /**
     * Returns new URL object from the given string URL.
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // IF the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Creates a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Article> extractFeaturesFromJson(String articleJSON) {
        //If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON Response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            String responseJsonObject = baseJsonResponse.getString("response");

            JSONObject root = new JSONObject(responseJsonObject);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (articles).
            JSONArray articleArray = root.getJSONArray("results");

            // For each article in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                //Extract the string for the key called "Section Name"
                String sectionName= currentArticle.getString("sectionName");
                Log.e("Article "+ i + ":", "Section Name: " + sectionName);

                // Extract the string for the key called "webPublicationDate"
                String webPublicationDate = currentArticle.getString("webPublicationDate");
                Log.e("Article "+ i + ":", "webPublicationDate: " + webPublicationDate);

                // Extract the string for the key called "webTitle"
                String webTitle = currentArticle.getString("webTitle");
                Log.e("Article "+ i + ":", "webTitle: " + webTitle);

                // Extract the string for the key called "webUrl"
                String webUrl = currentArticle.getString("webUrl");

                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                Log.e("Article "+ i + ":", "tagsArray = " + tagsArray);

                if (tagsArray != null && tagsArray.length() > 0) {
                    JSONObject tagsObject = tagsArray.getJSONObject(0);
                    String contributor = tagsObject.getString("webTitle");
                    Article article = new Article(sectionName, webTitle, contributor, webPublicationDate, webUrl);
                    articles.add(article);
                    Log.e("Article "+ i + ":", "Contributor: " + contributor);
                } else {
                    // Create a new {@link Article} object with the sectionName, webPublicationDate,
                    // webTitle, and webUrl from the JSON response.
                    Article article = new Article(sectionName, webTitle, webPublicationDate, webUrl);
                    articles.add(article);
                }


            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }
        // Return the list of earthquakes
        return articles;
    }

    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract relevant fields from the JSON response and create a list of {@link Article}s
        List<Article> articles = extractFeaturesFromJson(jsonResponse);

        // Return the list of {@link Article}s
        return articles;
    }
}
