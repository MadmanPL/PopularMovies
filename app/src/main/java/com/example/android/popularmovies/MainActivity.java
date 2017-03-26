package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.extras.Extras;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.utilities.TheMovieDatabaseJsonUtils;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String FIRST_VISIBLE_POSITION = "FIRST_VISIBLE_POSITION";
    private static final String SORT_TYPE = "SORT_TYPE";
    private String m_sSortType = "POPULAR";
    private int m_iFirstVisiblePosition = -1;


    private RecyclerView m_rvMovies;
    private MoviesAdapter m_moviesAdapter;

    private TextView m_tvErrorMessage;

    private ProgressBar m_bpLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_rvMovies= (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        m_rvMovies.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        m_rvMovies.setHasFixedSize(true);


        m_tvErrorMessage = (TextView)findViewById(R.id.tv_error_message);
        m_bpLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        Context context = this;
        m_moviesAdapter = new MoviesAdapter(this, context);

        m_rvMovies.setAdapter(m_moviesAdapter);

        m_sSortType = "POPULAR";
        if (savedInstanceState != null) {
            m_sSortType = savedInstanceState.getString(SORT_TYPE);
            m_iFirstVisiblePosition = savedInstanceState.getInt(FIRST_VISIBLE_POSITION);

        }
        if (m_sSortType == "POPULAR") {
            loadMoviesData(TheMovieDatabaseUtils.SortType.POPULAR);
        } else if (m_sSortType == "TOP_RATED") {
            loadMoviesData(TheMovieDatabaseUtils.SortType.TOP_RATED);
        } else {
            loadMoviesData(TheMovieDatabaseUtils.SortType.FAVORITE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putString(SORT_TYPE, m_sSortType);

        View firstChild = m_rvMovies.getChildAt(0);
        int firstVisiblePosition = m_rvMovies.getChildAdapterPosition(firstChild);

        state.putInt(FIRST_VISIBLE_POSITION, firstVisiblePosition);

        super.onSaveInstanceState(state);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            m_moviesAdapter.setMoviesData(null);
            loadMoviesData(TheMovieDatabaseUtils.SortType.POPULAR);
            m_sSortType = "POPULAR";
            return true;
        }
        else if (id == R.id.action_sort_top_rated) {
            m_moviesAdapter.setMoviesData(null);
            loadMoviesData(TheMovieDatabaseUtils.SortType.TOP_RATED);
            m_sSortType = "TOP_RATED";
            return true;
        } else if (id == R.id.action_sort_favorites) {
            m_moviesAdapter.setMoviesData(null);
            loadMoviesData(TheMovieDatabaseUtils.SortType.FAVORITE);
            m_sSortType = "FAVORITE";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMoviesData(TheMovieDatabaseUtils.SortType sortType) {
        showMoviesDataView();

        new FetchMoviesTask().execute(sortType);
    }

    private void showMoviesDataView() {
        m_tvErrorMessage.setVisibility(View.INVISIBLE);
        m_rvMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        m_rvMovies.setVisibility(View.INVISIBLE);
        m_tvErrorMessage.setVisibility(View.VISIBLE);
    }



    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_ORIGINAL_TITLE, movie.OrginalTitle);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_ID, movie.Id);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_POSTER_PATH, movie.PosterPath);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_TITLE, movie.Title);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_OVERVIEW, movie.Overview);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_RELEASE_DATE, movie.ReleaseDate);
        intentToStartDetailActivity.putExtra(Extras.MOVIE_VOTE_AVERAGE, movie.VoteAverage);

        startActivity(intentToStartDetailActivity);

    }

    public class FetchMoviesTask extends AsyncTask<TheMovieDatabaseUtils.SortType, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_bpLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(TheMovieDatabaseUtils.SortType... params) {
            if (params.length == 0) {
                return null;
            }

            TheMovieDatabaseUtils.SortType sortType = params[0];
            if (sortType == TheMovieDatabaseUtils.SortType.FAVORITE) {
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

                Vector movieList = new Vector();
                if (null != cursor && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int externalIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_EXTERNAL_ID);
                    int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                    int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                    int orginalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORGINAL_TITLE);
                    int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                    int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                    int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);

                    for (int i =0; i<cursor.getCount();i++) {
                        Movie m = new Movie();
                        m.Id = cursor.getInt(externalIdIndex);
                        if (!cursor.isNull(posterPathIndex)) {
                            m.PosterPath = cursor.getString(posterPathIndex);
                        }
                        if (!cursor.isNull(orginalTitleIndex)) {
                            m.OrginalTitle = cursor.getString(orginalTitleIndex);
                        }
                        if (!cursor.isNull(releaseDateIndex)) {
                            m.ReleaseDate = cursor.getString(releaseDateIndex);
                        }
                        if (!cursor.isNull(voteAverageIndex)) {
                            m.VoteAverage = cursor.getDouble(voteAverageIndex);
                        }
                        if (!cursor.isNull(overviewIndex)) {
                            m.Overview = cursor.getString(overviewIndex);
                        }
                        if (!cursor.isNull(titleIndex)) {
                            m.Title = cursor.getString(titleIndex);
                        }
                        movieList.add(m);

                        cursor.moveToNext();
                    }
                }

                cursor.close();

                Movie[] result = new Movie[movieList.size()];
                movieList.copyInto(result);

                return result;

            } else {
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
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            m_bpLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                m_moviesAdapter.setMoviesData(moviesData);
                if (m_iFirstVisiblePosition > 0) {
                    m_rvMovies.scrollToPosition(m_iFirstVisiblePosition);
                }
            } else {
                showErrorMessage();
            }
        }
    }

}
