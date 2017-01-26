package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.TheMovieDatabaseJsonUtils;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView m_ivImage;
    private TextView m_tvErrorMessage;

    private ProgressBar m_bpLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_ivImage = (ImageView)findViewById(R.id.iv_image);
        m_tvErrorMessage = (TextView)findViewById(R.id.tv_error_message);
        m_bpLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        Context context = this;
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(m_ivImage);

        new FetchMoviesTask().execute(TheMovieDatabaseUtils.SortType.POPULAR);
    }

    public class FetchMoviesTask extends AsyncTask<TheMovieDatabaseUtils.SortType, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_bpLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(TheMovieDatabaseUtils.SortType... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            TheMovieDatabaseUtils.SortType sortType = params[0];
            URL moviesRequestUrl = TheMovieDatabaseUtils.buildUrl(sortType);

            try {
                String jsonMoviesResponse = TheMovieDatabaseUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                Movie[] simpleJsonMovieData = TheMovieDatabaseJsonUtils
                        .getMoviesFromJson(MainActivity.this, jsonMoviesResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            m_bpLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                //showMovieDataView();
                //mMoviesAdapter.setMoviesData(moviesData);
            } else {
                //showErrorMessage();
            }
        }
    }

}
