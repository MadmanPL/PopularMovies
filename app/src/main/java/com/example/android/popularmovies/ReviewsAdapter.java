package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utilities.TheMovieDatabaseUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Michał on 2017-03-18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private Review[] mReviewsData;

    private final ReviewsAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface ReviewsAdapterOnClickHandler {
        void onClick(Review review);
    }

    public ReviewsAdapter(ReviewsAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextViewAuthor;
        public final TextView mTextViewContent;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            mTextViewAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            mTextViewContent = (TextView) view.findViewById(R.id.tv_review_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = mReviewsData[adapterPosition];
            mClickHandler.onClick(review);
        }
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        //int height = viewGroup.getMeasuredHeight() / 2;
        //view.setMinimumHeight(height);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mReviewsData[position];
        reviewAdapterViewHolder.mTextViewAuthor.setText(review.Author);
        reviewAdapterViewHolder.mTextViewContent.setText(review.Content);
    }


    @Override
    public int getItemCount() {
        if (null == mReviewsData) return 0;
        return mReviewsData.length;
    }

    public void setReviewsData(Review[] reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }
}