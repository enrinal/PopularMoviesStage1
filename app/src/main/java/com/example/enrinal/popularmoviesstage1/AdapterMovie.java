package com.example.enrinal.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enrinal on 7/6/2017.
 */

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.MovieAdapterViewHolder>{
    public List<Entity> mMovieData;
    public Context mContext;

    final private MovieAdapterOnClickerHandler mClickHandler;

    interface MovieAdapterOnClickerHandler {
        void ItemClicked(Entity id);
    }

    public AdapterMovie(MovieAdapterOnClickerHandler onClickerHandler, Context context) {
        mMovieData = new ArrayList<Entity>();
        mClickHandler = onClickerHandler;
        mContext = context;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mListMoviePosterImageView;
        public final View mView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mView = view;
            mListMoviePosterImageView = (ImageView) view.findViewById(R.id.iv_list_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Entity Entity = mMovieData.get(position);
            mClickHandler.ItemClicked(Entity);
        }
}

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        Integer layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Entity Entity = mMovieData.get(position);
        Picasso.with(holder.mView.getContext())
                .load(Entity.getMoviePoster())
                .into(holder.mListMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }


    public void setMovieData(List<Entity> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
