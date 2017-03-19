package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Vector;

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
        final String OWM_OVERVIEW = "overview";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";

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
            movie.Overview = movieJson.getString(OWM_OVERVIEW);
            movie.VoteAverage = movieJson.getDouble(OWM_VOTE_AVERAGE);
            movie.ReleaseDate = movieJson.getString(OWM_RELEASE_DATE);

            parsedMovieData[i] = movie;
        }

        return parsedMovieData;
    }

    public static Video[] getVideosFromJson(Context context, String jsonStr)
            throws JSONException {


        final String OWM_RESULTS = "results";

        final String OWM_ID = "id";
        final String OWM_KEY = "key";
        final String OWM_NAME = "name";
        final String OWM_SITE = "site";
        final String OWM_SIZE = "size";
        final String OWM_TYPE = "type";

        Video[] parsedVideoData = null;

        JSONObject videoJson = new JSONObject(jsonStr);

        JSONArray videosArray = videoJson.getJSONArray(OWM_RESULTS);

        parsedVideoData = new Video[videosArray.length()];

        for (int i = 0; i < videosArray.length(); i++) {
            Video video = new Video();

            JSONObject movieJson = videosArray.getJSONObject(i);

            video.Id = movieJson.getString(OWM_ID);
            video.Key = movieJson.getString(OWM_KEY);
            video.Name = movieJson.getString(OWM_NAME);
            video.Site = movieJson.getString(OWM_SITE);
            video.Size = movieJson.getInt(OWM_SIZE);
            video.Type = movieJson.getString(OWM_TYPE);

            parsedVideoData[i] = video;
        }

        return parsedVideoData;
    }

    public static Video[] getTrailersFromJson(Context context, String jsonStr)
            throws JSONException {

        Video[] videos = getVideosFromJson(context, jsonStr);

        Vector trailersList = new Vector();
        for(int i = 0; i<videos.length; i++){
            if(videos[i].Type.equals("Trailer"))
                trailersList.addElement(videos[i]);
        }

        Video[] videosArray = new Video[trailersList.size()];
        trailersList.copyInto(videosArray);

        return videosArray;
    }

    public static Review[] getReviewsFromJson(Context context, String jsonStr)
            throws JSONException {


        final String OWM_RESULTS = "results";

        final String OWM_ID = "id";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";

        Review[] parsedReviewData = null;

        JSONObject reviewJson = new JSONObject(jsonStr);

        JSONArray reviewsArray = reviewJson.getJSONArray(OWM_RESULTS);

        parsedReviewData = new Review[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {
            Review review = new Review();

            JSONObject movieJson = reviewsArray.getJSONObject(i);

            review.Id = movieJson.getString(OWM_ID);
            review.Author = movieJson.getString(OWM_AUTHOR);
            review.Content = movieJson.getString(OWM_CONTENT);

            parsedReviewData[i] = review;
        }

        return parsedReviewData;
    }
}
