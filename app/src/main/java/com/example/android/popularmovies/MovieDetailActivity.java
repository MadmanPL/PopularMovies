package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.extras.Extras;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView m_ivPoster;
    private TextView m_tvTitleAndYear;
    private ProgressBar m_pbUserScore;
    private TextView m_tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        m_ivPoster= (ImageView) findViewById(R.id.iv_poster);
        m_tvTitleAndYear= (TextView) findViewById(R.id.tv_title_and_year);
        m_pbUserScore= (ProgressBar) findViewById(R.id.pb_user_score);
        m_tvOverview= (TextView) findViewById(R.id.tv_overview);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            String sMovieTitle = "";
            String sMovieReleaseDate = "";
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_TITLE)) {
                sMovieTitle = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_TITLE);
            }
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_RELEASE_DATE)) {
                sMovieReleaseDate = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_RELEASE_DATE);
            }

            if (sMovieReleaseDate.length() >= 4) {
                m_tvTitleAndYear.setText(sMovieTitle + " (" + sMovieReleaseDate.substring(0, 4) + ")");
            } else {
                m_tvTitleAndYear.setText(sMovieTitle);
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

            String sMoviePosterPath = "";
            if (intentThatStartedThisActivity.hasExtra(Extras.MOVIE_POSTER_PATH)) {
                sMoviePosterPath = intentThatStartedThisActivity.getStringExtra(Extras.MOVIE_POSTER_PATH);
            }

            Context context = this;
            URL imageUrl = TheMovieDatabaseUtils.buildImageUrl(sMoviePosterPath);
            Picasso.with(context).load(imageUrl.toString()).into(m_ivPoster);
        }

    }
}
