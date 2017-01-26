package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Integra on 2017-01-26.
 */

public final class TheMovieDatabaseJsonUtils {
    public static Movie[] getMoviesFromJson(Context context, String jsonStr)
            throws JSONException {


        final String OWM_RESULTS = "results";

        final String OWM_ID = "id";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_TITLE = "title";
        final String OWM_ORIGINAL_TITLE = "original_title";

        Movie[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(jsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

        parsedMovieData = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            Movie movie = new Movie();

            JSONObject movieJson = moviesArray.getJSONObject(i);

            movie.Id = movieJson.getInt(OWM_ID);
            movie.PosterPath = movieJson.getString(OWM_POSTER_PATH);
            movie.Title = movieJson.getString(OWM_TITLE);
            movie.OrginalTitle = movieJson.getString(OWM_ORIGINAL_TITLE);

            parsedMovieData[i] = movie;
        }

        return parsedMovieData;
    }
}
