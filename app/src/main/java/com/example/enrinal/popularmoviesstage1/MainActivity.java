package com.example.enrinal.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterMovie.MovieAdapterOnClickerHandler {
    final static private String SORT = "SORT";

    private ProgressBar mLoadingProgressBar;
    private TextView mErrorTextView;
    private RecyclerView mMovieListRecyclerView;
    private AdapterMovie mAdapterMovie;
    private String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mErrorTextView = (TextView) findViewById(R.id.tv_list_error);
        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getString(SORT);
        }

        if (mSort == null) {
            mSort = DataFilm.POPULAR;
        }

        mMovieListRecyclerView.setLayoutManager(layoutManager);

        mAdapterMovie = new AdapterMovie(this, this);

        mMovieListRecyclerView.setAdapter(mAdapterMovie);

        loadMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT, mSort);
    }

    @Override
    public void ItemClicked(Entity Entity) {
        Context context = this;
        Class destinationActivity = MovieDetailActivity.class;

        Intent startMovieDetailIntent = new Intent(context, destinationActivity);

        startMovieDetailIntent.putExtra(IntentItem.MOVIE_ENTITY, Entity);

        startActivity(startMovieDetailIntent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Entity>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Entity> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String type = params[0];

            String json = DataFilm.getMovies(type);
            List<Entity> movieEntities = com.example.enrinal.popularmoviesstage1.json.parseJson(json);

            return movieEntities;
        }

        @Override
        protected void onPostExecute(List<Entity> movieEntities) {
            mAdapterMovie.setMovieData(movieEntities);
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();

        switch (id) {
            case R.id.action_top_rated:
                mSort = DataFilm.TOP_RATED;
                loadMovies();
                return true;
            case R.id.action_popular:
                mSort = DataFilm.POPULAR;
                loadMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        if(isOnline()) {
            showResults();
            new FetchMovieTask().execute(mSort);
        } else {
            showErrors();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showErrors() {
        mErrorTextView.setVisibility(View.VISIBLE);
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showResults() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
    }
}
