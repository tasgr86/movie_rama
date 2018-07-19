package grigoris.tasos.movierama.Activities_Fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import javax.inject.Inject;
import dmax.dialog.SpotsDialog;
import grigoris.tasos.movierama.Adapters.PopularMoviesAdapter;
import grigoris.tasos.movierama.IAPICallsHandler;
import grigoris.tasos.movierama.IFavoritesManager;
import grigoris.tasos.movierama.IMyJSONParser;
import grigoris.tasos.movierama.MyApplication;
import grigoris.tasos.movierama.POJOs.TheMovie;
import grigoris.tasos.movierama.R;

public class MoviesFragment extends Fragment {

    private RecyclerView                rv;
    private ArrayList<TheMovie>         movies;
    private PopularMoviesAdapter        adapter;
    private SwipeRefreshLayout          swipeRefresh;
    private SearchView                  searchView;
    private View                        view;
    private TextView                    noMovies;
    @Inject IMyJSONParser               parser;
    @Inject IFavoritesManager           favoritesManager;
    @Inject IAPICallsHandler            apiCallsHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment, null);

        ((MyApplication)getActivity().getApplication())
                .getComponent()
                .inject(MoviesFragment.this);

        rv = view.findViewById(R.id.rv);
        swipeRefresh = view.findViewById(R.id.swipeContainer);
        noMovies = view.findViewById(R.id.no_movies);

        movies = getArguments().getParcelableArrayList("popular_movies");

        if (movies == null || movies.size() < 1)
            loadFirstPopularPage();
        else
            loadTheAdapter(movies);

        setUpSearchWidget();

        swipeRefresh.setOnRefreshListener(() -> {

            if (!searchView.getQuery().toString().isEmpty())
                searchView.setQuery("", false);

            searchView.clearFocus();
            loadFirstPopularPage();

        });

        return view;
    }


    private void loadFirstPopularPage(){

        Dialog loadingDialog = new SpotsDialog.Builder().setContext(getContext()).setMessage(getString(R.string.loading)).
                setCancelable(false).build();
        loadingDialog.show();

        apiCallsHandler.setListener((str -> {

            movies.clear();
            movies = parser.parseMovies(str.get(0), 0);
            loadTheAdapter(movies);
            swipeRefresh.setRefreshing(false);
            loadingDialog.dismiss();

        }));
        apiCallsHandler.fetchFirstPage();

    }


    private void loadTheAdapter(ArrayList<TheMovie> popularMovies){

        updateNoMoviesTextView();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        adapter = new PopularMoviesAdapter(getContext(), rv, llm, popularMovies, false, favoritesManager, apiCallsHandler);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }

    private void updateNoMoviesTextView(){

        if (movies == null || movies.size() < 1)
            noMovies.setVisibility(View.VISIBLE);
        else
            noMovies.setVisibility(View.GONE);

    }


    private void setUpSearchWidget() {

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = view.findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String query) {

                // If user has entered some text show the first page of Search results.
                // Otherwise load the first page of popular movies.

                if (!query.isEmpty()) {

                    apiCallsHandler.setListener((str) -> updateOnTextEntered(str, 1));
                    apiCallsHandler.searchMovie(query, 1);

                } else {

                    apiCallsHandler.setListener((str) -> updateOnTextEntered(str, 0));
                    apiCallsHandler.fetchFirstPage();

                }

                return false;
            }
        });

    }

    private void updateOnTextEntered(ArrayList<String> str, int type){

        movies.clear();
        movies = parser.parseMovies(str.get(0), type);
        adapter.notifyDataSetChanged();
        adapter.updateView(movies);

        if (type == 0)
            searchView.clearFocus();

        updateNoMoviesTextView();

    }
}