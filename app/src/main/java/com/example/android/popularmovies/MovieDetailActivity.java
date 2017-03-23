package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.extras.Extras;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.utilities.TheMovieDatabaseJsonUtils;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity
        implements VideosAdapter.VideosAdapterOnClickHandler, ReviewsAdapter.ReviewsAdapterOnClickHandler {

    private ImageView m_ivPoster;
    private ImageView m_ivFav;
    private TextView m_tvTitleAndYear;
    private ProgressBar m_pbUserScore;
    private TextView m_tvOverview;

    private RecyclerView m_rvVideos;
    private VideosAdapter m_videosAdapter;
    private TextView m_tvVideosErrorMessage;
    private ProgressBar m_bpVideosLoadingIndicator;

    private RecyclerView m_rvReviews;
    private ReviewsAdapter m_reviewsAdapter;
    private TextView m_tvReviewsErrorMessage;
    private ProgressBar m_bpReviewsLoadingIndicator;

    private String m_sMovieTitle = "";
    private String m_sMovieOriginalTitle = "";
    private String m_sMovieReleaseDate = "";
    private String m_sMoviePosterPath = "";
    private Integer m_iMovieExternalId = 0;
    private String m_sMovieOverview = "";
    private Double m_dMovieVoteAverage = 0.0;


    private boolean m_bIsFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        m_ivPoster= (ImageView) findViewById(R.id.iv_poster);
        m_ivFav= (ImageView) findViewById(R.id.iv_fav);
        m_tvTitleAndYear= (TextView) findViewById(R.id.tv_title_and_year);
        m_pbUserScore= (ProgressBar) findViewById(R.id.pb_user_score);
        m_tvOverview= (TextView) findViewById(R.id.tv_overview);

        m_rvVideos = (RecyclerView) findViewById(R.id.rv_videos);
        m_tvVideosErrorMessage = (TextView)findViewById(R.id.tv_videos_error_message);
        m_bpVideosLoadingIndicator = (ProgressBar) findViewById(R.id.pb_videos_loading_indicator);

        m_rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        m_tvReviewsErrorMessage = (TextView)findViewById(R.id.tv_reviews_error_message);
        m_bpReviewsLoadingIndicator = (ProgressBar) findViewById(R.id.pb_reviews_loading_indicator);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_TITLE)) {
                m_sMovieTitle = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_TITLE);
            }
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_ORIGINAL_TITLE)) {
                m_sMovieOriginalTitle = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_ORIGINAL_TITLE);
            }
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_RELEASE_DATE)) {
                m_sMovieReleaseDate = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_RELEASE_DATE);
            }
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_ID)) {
                m_iMovieExternalId = intentThatStartedThisActivity.getIntExtra(Extras.MOVIE_ID, 0);
            }

            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_OVERVIEW)) {
                m_sMovieOverview = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_OVERVIEW);
            }
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_VOTE_AVERAGE)) {
                m_dMovieVoteAverage = intentThatStartedThisActivity.getDoubleExtra(Extras.MOVIE_VOTE_AVERAGE, 0.0);
            }

            if (m_sMovieReleaseDate.length() >= 4) {
                m_tvTitleAndYear.setText(m_sMovieTitle + " (" + m_sMovieReleaseDate.substring(0, 4) + ")");
            } else {
                m_tvTitleAndYear.setText(m_sMovieTitle);
            }

            String sMovieOverview = "";
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_OVERVIEW)) {
                sMovieOverview = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_OVERVIEW);
            }
            m_tvOverview.setText(sMovieOverview);

            Double dMovieVoteAverage = 0d;
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_VOTE_AVERAGE)) {
                dMovieVoteAverage = intentThatStartedThisActivity.getDoubleExtra(Extras.MOVIE_VOTE_AVERAGE, 0d);
            }
            dMovieVoteAverage = dMovieVoteAverage * 10d;
            int iMovieVoteAverage = (int)Math.round(dMovieVoteAverage);
            m_pbUserScore.setProgress(iMovieVoteAverage);

            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_POSTER_PATH)) {
                m_sMoviePosterPath = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_POSTER_PATH);
            }

            Context context = this;
            URL imageUrl = TheMovieDatabaseUtils.buildImageUrl(m_sMoviePosterPath);
            Picasso.with(context).load(imageUrl.toString()).into(m_ivPoster);

            LinearLayoutManager videosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            m_rvVideos.setLayoutManager(videosLayoutManager);
            m_rvVideos.setHasFixedSize(true);

            m_videosAdapter = new VideosAdapter(this, this);
            m_rvVideos.setAdapter(m_videosAdapter);

            LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            m_rvReviews.setLayoutManager(reviewsLayoutManager);
            m_rvReviews.setHasFixedSize(true);

            m_reviewsAdapter = new ReviewsAdapter(this, this);
            m_rvReviews.setAdapter(m_reviewsAdapter);

            if (isFavourite()) {
                m_bIsFavourite = true;
                m_ivFav.setImageResource(R.drawable.fav);
            } else {
                m_bIsFavourite = false;
                m_ivFav.setImageResource(R.drawable.nofav);
            }

            Integer iMovieId = 0;
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_ID)) {
                iMovieId = intentThatStartedThisActivity.getIntExtra(Extras.MOVIE_ID, 0);
            }
            new MovieDetailActivity.FetchVideosTask().execute(iMovieId);
            new MovieDetailActivity.FetchReviewsTask().execute(iMovieId);
        }

    }

    private void showVideosDataView() {
        m_tvVideosErrorMessage.setVisibility(View.INVISIBLE);
        m_rvVideos.setVisibility(View.VISIBLE);
    }

    private void showVideosErrorMessage() {
        m_rvVideos.setVisibility(View.INVISIBLE);
        m_tvVideosErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showReviewsDataView() {
        m_tvReviewsErrorMessage.setVisibility(View.INVISIBLE);
        m_rvReviews.setVisibility(View.VISIBLE);
    }

    private void showReviewsErrorMessage() {
        m_rvReviews.setVisibility(View.INVISIBLE);
        m_tvReviewsErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Video video) {
        watchYoutubeVideo(video.Key);
    }

    public void watchYoutubeVideo(String id){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        startActivity(webIntent);
    }

    @Override
    public void onClick(Review review) {

    }

    private Boolean isFavourite() {
        String[] projectionColumns = {MovieContract.MovieEntry._ID};
        String selectionStatement = MovieContract.MovieEntry
                .getSqlSelectByExternalId(m_iMovieExternalId);

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projectionColumns, selectionStatement, null, null);

        Boolean result = false;
        if (null != cursor && cursor.getCount() > 0) {
            result = true;
        }

        cursor.close();

        return result;
    }

    public void favButtonClick(View view) {
        if (m_bIsFavourite) {
            String[] selectionArgs=new String[]{String.valueOf(m_iMovieExternalId)};
            String selection=""+MovieContract.MovieEntry.COLUMN_EXTERNAL_ID+"=?";

            getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, selectionArgs);

            m_bIsFavourite = false;
            m_ivFav.setImageResource(R.drawable.nofav);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, m_sMovieTitle);
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORGINAL_TITLE, m_sMovieOriginalTitle);
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, m_sMovieReleaseDate);
            contentValues.put(MovieContract.MovieEntry.COLUMN_EXTERNAL_ID, m_iMovieExternalId);
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, m_sMoviePosterPath);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, m_dMovieVoteAverage);
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, m_sMovieOverview);
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            m_bIsFavourite = true;
            m_ivFav.setImageResource(R.drawable.fav);
        }

        //change button image
    }

    public class FetchVideosTask extends AsyncTask<Integer, Void, Video[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_bpVideosLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Video[] doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            int movieId = params[0];
            URL moviesRequestUrl = TheMovieDatabaseUtils.buildVideosUrl(movieId);

            try {
                String jsonVideosResponse = TheMovieDatabaseUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                Video[] simpleJsonVideoData = TheMovieDatabaseJsonUtils
                        .getTrailersFromJson(MovieDetailActivity.this, jsonVideosResponse);

                return simpleJsonVideoData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Video[] videosData) {
            m_bpVideosLoadingIndicator.setVisibility(View.INVISIBLE);
            if (videosData != null) {
                showVideosDataView();
                m_videosAdapter.setVideosData(videosData);
            } else {
                showVideosErrorMessage();
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<Integer, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_bpReviewsLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Review[] doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            int movieId = params[0];
            URL reviewsRequestUrl = TheMovieDatabaseUtils.buildReviewsUrl(movieId);

            try {
                String jsonVideosResponse = TheMovieDatabaseUtils
                        .getResponseFromHttpUrl(reviewsRequestUrl);

                Review[] simpleJsonReviewData = TheMovieDatabaseJsonUtils
                        .getReviewsFromJson(MovieDetailActivity.this, jsonVideosResponse);

                return simpleJsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviewsData) {
            m_bpReviewsLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviewsData != null) {
                showReviewsDataView();
                m_reviewsAdapter.setReviewsData(reviewsData);
            } else {
                showReviewsErrorMessage();
            }
        }
    }
}
