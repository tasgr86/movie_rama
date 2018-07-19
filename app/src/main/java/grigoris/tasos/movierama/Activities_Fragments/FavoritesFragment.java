package grigoris.tasos.movierama.Activities_Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoritesFragment extends Fragment {

    private RecyclerView                rv;
    private PopularMoviesAdapter        adapter;
    private View                        view;
    private TextView                    noMovies;
    @Inject IAPICallsHandler            handler;
    @Inject IMyJSONParser               parser;
    @Inject IFavoritesManager           favoritesManager;

    @Override
    public void onResume() {
        super.onResume();

        getFavorites();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment, null);

        ((MyApplication)getActivity().getApplication())
                .getComponent()
                .inject(FavoritesFragment.this);

        rv = view.findViewById(R.id.rv);
        noMovies = view.findViewById(R.id.no_movies);

        view.findViewById(R.id.search_view).setVisibility(View.GONE);
        view.findViewById(R.id.swipeContainer).setEnabled(false);

        return view;
    }

    private void getFavorites(){

        Dialog loadingDialog = new SpotsDialog.Builder().setContext(getContext()).setMessage(getString(R.string.loading)).
                setCancelable(false).build();
        loadingDialog.show();

        handler.setListener(responses -> {

            ArrayList<TheMovie> favoriteMovies = new ArrayList<>();

            for(int i=0; i<responses.size(); i++){

                TheMovie movie = parser.parseCredits(responses.get(i));
                if (movie != null)
                    favoriteMovies.add(movie);

            }

            loadingDialog.dismiss();
            loadAdapter(favoriteMovies);

        });
        handler.fetchPopularMovies(favoritesManager.getFavorites());

    }


    private void loadAdapter(ArrayList<TheMovie> favoriteMovies){

        updateNoMoviesTextView(favoriteMovies);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        adapter = new PopularMoviesAdapter(getContext(), rv, llm, favoriteMovies, true, null, handler);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

    }

    private void updateNoMoviesTextView(ArrayList<TheMovie> favoriteMovies){

        if (favoriteMovies == null || favoriteMovies.size() < 1)
            noMovies.setVisibility(View.VISIBLE);
        else
            noMovies.setVisibility(View.GONE);

    }


}
