package grigoris.tasos.movierama;

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
import android.view.View;
import android.widget.ProgressBar;
import com.github.ybq.android.spinkit.style.Wave;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String POPURAL_URL = "popular";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private RecyclerView rv;
    private ArrayList<ThePopularMovie> popularMovies;
    private PopularMoviesAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private Toolbar toolbar;
    private DrawerLayout drawer;

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
        progressBar = findViewById(R.id.spin);
        progressBar.setIndeterminateDrawable(new Wave());
        rv = findViewById(R.id.rv);

        fetchFirstPage(false);

        progressBar.setVisibility(View.VISIBLE);
        swipeRefresh.setOnRefreshListener(() -> fetchFirstPage(true));
        
    }


    private void fetchFirstPage(boolean isSwipeRefresh){

        GET get = new GET();
        get.getListener = (str) -> {

            popularMovies = new ArrayList<>();
            popularMovies.addAll(MyJSONParser.parserPopularMovies(str.get(0)));
            loadTheAdapter(popularMovies);
            addListener();
            progressBar.setVisibility(View.GONE);

            if (isSwipeRefresh)
                swipeRefresh.setRefreshing(false);

        };

        get.execute(BASE_URL.concat(POPURAL_URL).concat("?api_key=").concat(getString(R.string.appi_key)));

    }

    private void loadTheAdapter(ArrayList<ThePopularMovie> popularMovies){

        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new PopularMoviesAdapter(this, rv, llm, popularMovies);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }


    private void addListener(){

        adapter.setOnLoadMoreListener((bar) -> {

            GET get2 = new GET();
            get2.getListener = (str) -> {

                new Handler().postDelayed(() -> {

                    popularMovies.addAll(MyJSONParser.parserPopularMovies(str.get(0)));
                    adapter.setLoaded(bar);
                    adapter.notifyDataSetChanged();

                }, 1000);

            };
            get2.execute(BASE_URL.concat(POPURAL_URL).concat("?api_key=").
                    concat(getString(R.string.appi_key)).concat("&page=").
                    concat(String.valueOf(MyJSONParser.getPopularPage())));

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_movies) {

        }else if (id == R.id.favorites_movies){


        }


        drawer.closeDrawer(GravityCompat.START);


        return false;
    }
}