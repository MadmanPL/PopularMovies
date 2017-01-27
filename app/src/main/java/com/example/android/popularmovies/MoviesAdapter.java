package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Integra on 2017-01-26.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private Movie[] mMoviesData;
    
    private final MoviesAdapterOnClickHandler mClickHandler;
    private Context mContext;
    
    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }
    
    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }
    
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        //int height = viewGroup.getMeasuredHeight() / 2;
        //view.setMinimumHeight(height);
        return new MoviesAdapterViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder MoviesAdapterViewHolder, int position) {
        Movie movie = mMoviesData[position];
        URL imageUrl = TheMovieDatabaseUtils.buildImageUrl(movie.PosterPath);
        Picasso.with(mContext).load(imageUrl.toString()).into(MoviesAdapterViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }
    
    public void setMoviesData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
