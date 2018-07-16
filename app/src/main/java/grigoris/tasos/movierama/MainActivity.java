package grigoris.tasos.movierama;

import android.app.Dialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.ArrayList;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POPURAL_URL = "movie/popular";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String SEARCH_MOVIE = "search/movie";
    private RecyclerView rv;
    private ArrayList<TheMovie> popularMovies;
    private PopularMoviesAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private APICallsHandler apiCallsHandler;

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null)
            adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        apiCallsHandler = new APICallsHandler(this);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swipeRefresh = findViewById(R.id.swipeContainer);
        rv = findViewById(R.id.rv);

        swipeRefresh.setOnRefreshListener(() -> {

            apiCallsHandler.apiCallsListener = (str -> {

                popularMovies.clear();
                popularMovies = MyJSONParser.parseMovies(str.get(0), 0);
                loadTheAdapter(popularMovies);
                swipeRefresh.setRefreshing(false);

            });
            apiCallsHandler.fetchFirstPage(true);

        });

        popularMovies = getIntent().getParcelableArrayListExtra("popular_movies");
        loadTheAdapter(popularMovies);


    }



    private void loadTheAdapter(ArrayList<TheMovie> popularMovies){

        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new PopularMoviesAdapter(this, rv, llm, popularMovies);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        new Handler().postDelayed(() -> {

            if (id == R.id.search_movies) {

            }else if (id == R.id.favorites_movies){


            }

        }, 300);

        drawer.closeDrawer(GravityCompat.START);

        return false;
    }
}