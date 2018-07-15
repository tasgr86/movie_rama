package grigoris.tasos.movierama;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

// 99861?api_key=30842f7c80f80bb3ad8a2fb98195544d           reviews
// 99861/reviews?api_key=30842f7c80f80bb3ad8a2fb98195544d   reviews
// 99861/similar?api_key=30842f7c80f80bb3ad8a2fb98195544d   similar

public class ShowMovie extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView poster, favorite;
    private TextView date, rating, description, genres, reviewsLabel, similarMoviesLabel;
    private SpinKitView spin;
    private RecyclerView reviews_rv, similar_rv;
    private ReviewsAdapter reviewsAdapter;
    private FavoritesManager favoritesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int movie_id = getIntent().getIntExtra("movie_id", 0);

        String credits = MainActivity.BASE_URL.concat(String.valueOf(movie_id)).concat("?api_key=").
                concat(getString(R.string.appi_key));

        String reviews = MainActivity.BASE_URL.concat(String.valueOf(movie_id))
                .concat("/reviews").concat("?api_key=").concat(getString(R.string.appi_key));

        String similar = MainActivity.BASE_URL.concat(String.valueOf(movie_id))
                .concat("/similar").concat("?api_key=").concat(getString(R.string.appi_key));

        GET get = new GET();
        get.getListener = s -> {

            TheMovie movie = MyJSONParser.parseCredits(s.get(0));

            if (movie == null){

                showDismissDialog();
                return;

            }

            setUpViews(movie);
            fillMandatoryViews(movie);
            loadReviews(MyJSONParser.parseReviews(s.get(1)));
            loadSimilarMovies(MyJSONParser.parseSimilar(s.get(2)));

            spin.setVisibility(View.GONE);

        };
        get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, credits, reviews, similar);

    }


    private void fillMandatoryViews(TheMovie movie) {

        Picasso.get().load(MainActivity.IMAGE_URL.concat(movie.getPoster())).into(poster);
        date.setText(getString(R.string.release_date).concat(" : ").concat(TimeParser.getFormattedTime(movie.getDate())));
        rating.setText(getString(R.string.rating).concat(" : ").concat(movie.getRating()));
        genres.setText(movie.getGenre());
        description.setText(movie.getOverview());

    }


    private void loadReviews(ArrayList<TheReview> reviews){

        if(reviews == null || reviews.size() == 0){

            reviewsLabel.setVisibility(View.GONE);
            return;

        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        reviews_rv.addItemDecoration(divider);
        reviews_rv.setLayoutManager(llm);
        reviews_rv.setAdapter(reviewsAdapter);

    }


    private void loadSimilarMovies(ArrayList<TheMovie> similarMovies){

        if(similarMovies == null){

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

    private void setUpViews(TheMovie movie){

        setContentView(R.layout.show_movie);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(movie.getTitle());

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        favorite = findViewById(R.id.favorite);
        reviews_rv = findViewById(R.id.reviews_rv);
        similar_rv = findViewById(R.id.similar_rv);
        spin = findViewById(R.id.spin);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.description);
        genres = findViewById(R.id.genres);
        poster = findViewById(R.id.poster);
        reviewsLabel = findViewById(R.id.reviews_label);
        similarMoviesLabel = findViewById(R.id.similar_movies_label);

        spin.setIndeterminateDrawable(new Wave());
        spin.setVisibility(View.VISIBLE);

        favoritesManager = new FavoritesManager(this);

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
