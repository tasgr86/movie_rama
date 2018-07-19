package grigoris.tasos.movierama.Activities_Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import javax.inject.Inject;
import dmax.dialog.SpotsDialog;
import grigoris.tasos.movierama.APICallsHandler;
import grigoris.tasos.movierama.Adapters.ReviewsAdapter;
import grigoris.tasos.movierama.Adapters.SimilarMoviesAdapter;
import grigoris.tasos.movierama.IAPICallsHandler;
import grigoris.tasos.movierama.IFavoritesManager;
import grigoris.tasos.movierama.IMyJSONParser;
import grigoris.tasos.movierama.MyApplication;
import grigoris.tasos.movierama.POJOs.TheMovie;
import grigoris.tasos.movierama.POJOs.TheReview;
import grigoris.tasos.movierama.R;
import grigoris.tasos.movierama.TimeParser;

public class ShowMovie extends AppCompatActivity {

    private Toolbar                     toolbar;
    private ImageView                   poster, favorite;
    private TextView                    date, rating, description, genres, reviewsLabel, similarMoviesLabel;
    private RecyclerView                reviews_rv, similar_rv;
    private ReviewsAdapter              reviewsAdapter;
    private Dialog                      loadingDialog;
    @Inject
    IFavoritesManager favoritesManager;
    @Inject
    IAPICallsHandler apiCallsHandler;
    @Inject
    IMyJSONParser parser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((MyApplication)getApplication())
                .getComponent()
                .inject(ShowMovie.this);

        int movie_id = getIntent().getIntExtra("movie_id", 0);

        loadingDialog = new SpotsDialog.Builder().setContext(this).setMessage(getString(R.string.loading)).
                setCancelable(false).build();
        loadingDialog.show();

        apiCallsHandler.setListener((str) -> {

            TheMovie movie = parser.parseCredits(str.get(0));

            if (movie == null){

                loadingDialog.dismiss();
                showDismissDialog();
                return;

            }

            setUpViews();
            setUpToolbar(movie);
            setUpFavorites(movie);
            fillMandatoryViews(movie);
            loadReviews(parser.parseReviews(str.get(1)));
            loadSimilarMovies(parser.parseSimilar(str.get(2)));
            loadingDialog.dismiss();

        });
        apiCallsHandler.fetchMovie(movie_id);

    }


    private void fillMandatoryViews(TheMovie movie) {

        Picasso.get().load(APICallsHandler.IMAGE_URL.concat("original/").concat(movie.getPoster())).
                placeholder(R.drawable.placeholder).into(poster);
        date.setText(getString(R.string.release_date).concat(" : ").concat(TimeParser.getFormattedTime(movie.getDate())));
        rating.setText(getString(R.string.rating).concat(" : ").concat(movie.getRating()));
        genres.setText(movie.getGenre());
        description.setText(movie.getOverview());

    }


    private void loadReviews(ArrayList<TheReview> reviews){

        if(reviews == null || reviews.size() < 1){

            reviewsLabel.setVisibility(View.GONE);
            return;

        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        reviews_rv.addItemDecoration(divider);
        reviews_rv.setLayoutManager(llm);
        reviews_rv.setAdapter(reviewsAdapter);
        reviews_rv.setNestedScrollingEnabled(false);

    }


    private void loadSimilarMovies(ArrayList<TheMovie> similarMovies){

        if(similarMovies == null || similarMovies.size() < 1){

            similarMoviesLabel.setVisibility(View.GONE);
            return;

        }

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        SimilarMoviesAdapter similarMoviesAdapter = new SimilarMoviesAdapter(this, similarMovies);
        similar_rv.setLayoutManager(llm);
        similar_rv.setAdapter(similarMoviesAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }

    private void setUpViews(){

        setContentView(R.layout.show_movie);

        favorite                        = findViewById(R.id.favorite);
        reviews_rv                      = findViewById(R.id.reviews_rv);
        similar_rv                      = findViewById(R.id.similar_rv);
        date                            = findViewById(R.id.date);
        rating                          = findViewById(R.id.rating);
        description                     = findViewById(R.id.description);
        genres                          = findViewById(R.id.genres);
        poster                          = findViewById(R.id.poster);
        reviewsLabel                    = findViewById(R.id.reviews_label);
        similarMoviesLabel              = findViewById(R.id.similar_movies_label);

    }

    private void setUpToolbar(TheMovie movie){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(movie.getTitle());

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private void setUpFavorites(TheMovie movie){

        if (favoritesManager.isFavorite(movie.getId()))
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_selected));
        else
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_unselected));

        favorite.setOnClickListener((view) -> {

            if (favoritesManager.isFavorite(movie.getId())){

                favoritesManager.removeFavorite(movie.getId());
                favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_unselected));

            } else{

                favoritesManager.addFavorite(movie.getId());
                favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_selected));

            }
        });
    }


    private void showDismissDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).
                setTitle(getString(R.string.error)).
                setMessage(getString(R.string.error_message)).
                setNegativeButton(getString(R.string.dismiss),
                        (dialog, which) -> finish());
        builder.show();

    }
}
