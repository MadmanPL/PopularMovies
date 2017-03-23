package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Michał on 2017-03-19.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE)
                .build();

        public static final String TABLE_NAME = "favourite_movies";

        public static final String COLUMN_EXTERNAL_ID = "external_id";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_POSTER_BLOB = "posterBlob";
        public static final String COLUMN_ORGINAL_TITLE = "orginalTitle";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";

        public static Uri buildFavouriteMoviesUri() {
            return CONTENT_URI.buildUpon()
                    .build();
        }

        public static String getSqlSelectByExternalId(int externalId) {
            return MovieEntry.COLUMN_EXTERNAL_ID + " = " + String.valueOf(externalId);
        }
    }
}