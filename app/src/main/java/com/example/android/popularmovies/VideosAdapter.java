package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Video;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Michał on 2017-03-18.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder> {

    private Video[] mVideosData;

    private final VideosAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface VideosAdapterOnClickHandler {
        void onClick(Video video);
    }

    public VideosAdapter(VideosAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextView;

        public VideosAdapterViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_video_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Video video = mVideosData[adapterPosition];
            mClickHandler.onClick(video);
        }
    }

    @Override
    public VideosAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        //int height = viewGroup.getMeasuredHeight() / 2;
        //view.setMinimumHeight(height);
        return new VideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideosAdapterViewHolder videoAdapterViewHolder, int position) {
        Video video = mVideosData[position];
        videoAdapterViewHolder.mTextView.setText(video.Name);
    }


    @Override
    public int getItemCount() {
        if (null == mVideosData) return 0;
        return mVideosData.length;
    }

    public void setVideosData(Video[] videosData) {
        mVideosData = videosData;
        notifyDataSetChanged();
    }
}