package grigoris.tasos.movierama.Activities_Fragments;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import java.util.ArrayList;

import grigoris.tasos.movierama.MyApplication;
import grigoris.tasos.movierama.POJOs.TheMovie;
import grigoris.tasos.movierama.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<TheMovie>         movies;
    private Toolbar                     toolbar;
    private DrawerLayout                drawer;
    private FrameLayout                 frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawer                  = findViewById(R.id.drawer_layout);
        toolbar                 = findViewById(R.id.main_toolbar);
        frameLayout             = findViewById(R.id.frame);

        ((MyApplication)getApplication())
                .getComponent()
                .inject(MainActivity.this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        movies = getIntent().getParcelableArrayListExtra("popular_movies");

        loadFragment(getPopularMoviesFragment());

    }

    private void loadFragment(Fragment fragment){

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fManager.beginTransaction();
        mFragmentTransaction.replace(frameLayout.getId(), fragment).commit();

    }

    private Fragment getPopularMoviesFragment(){

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("popular_movies", movies);
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(bundle);

        return fragment;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        new Handler().postDelayed(() -> {

            if (id == R.id.search_movies)
                loadFragment(getPopularMoviesFragment());
            else if (id == R.id.favorites_movies)
                loadFragment(new FavoritesFragment());

        }, 300);

        drawer.closeDrawer(GravityCompat.START);

        return false;
    }
}