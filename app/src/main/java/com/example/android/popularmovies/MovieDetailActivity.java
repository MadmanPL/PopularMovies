package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    }
}
