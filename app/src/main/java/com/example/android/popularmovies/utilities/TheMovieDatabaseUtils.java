package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.TheMovieDBApiKey;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Integra on 2017-01-26.
 */

public class TheMovieDatabaseUtils {
    private static final String TAG = TheMovieDatabaseUtils.class.getSimpleName();

    private static final String STATIC_URL = "https://api.themoviedb.org/3";
    private static final String STATIC_PATH_MOVIE = "movie";
    private static final String STATIC_PATH_POPULAR = "popular";
    private static final String STATIC_PATH_TOP_RATED = "top_rated";
    private static final String STATIC_PATH_VIDEOS = "videos";
    private static final String STATIC_PATH_REVIEWS = "reviews";
    private static final String APIKEY_PARAM = "api_key";

    private static final String STATIC_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String STATIC_IMAGE_PATH_SIZE = "w185";


    public enum SortType {
        POPULAR, TOP_RATED
    }

    public static URL buildUrl(SortType sortType) {
        Uri.Builder builder = Uri.parse(STATIC_URL).buildUpon();
        builder.appendPath(STATIC_PATH_MOVIE);
        if (sortType == SortType.POPULAR) {
            builder.appendPath(STATIC_PATH_POPULAR);
        } else {
            builder.appendPath(STATIC_PATH_TOP_RATED);
        }
        builder.appendQueryParameter(APIKEY_PARAM, TheMovieDBApiKey.GetApiKey());

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildVideosUrl(int movieId) {
        Uri.Builder builder = Uri.parse(STATIC_URL).buildUpon();
        builder.appendPath(STATIC_PATH_MOVIE);
        builder.appendPath(String.valueOf(movieId));
        builder.appendPath(STATIC_PATH_VIDEOS);

        builder.appendQueryParameter(APIKEY_PARAM, TheMovieDBApiKey.GetApiKey());

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewsUrl(int movieId) {
        Uri.Builder builder = Uri.parse(STATIC_URL).buildUpon();
        builder.appendPath(STATIC_PATH_MOVIE);
        builder.appendPath(String.valueOf(movieId));
        builder.appendPath(STATIC_PATH_REVIEWS);

        builder.appendQueryParameter(APIKEY_PARAM, TheMovieDBApiKey.GetApiKey());

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildImageUrl(String imagePath) {
        Uri.Builder builder = Uri.parse(STATIC_IMAGE_URL).buildUpon();
        builder.appendPath(STATIC_IMAGE_PATH_SIZE);
        imagePath = imagePath.replace("/", "");
        builder.appendPath(imagePath);

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
