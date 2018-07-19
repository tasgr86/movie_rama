package grigoris.tasos.movierama.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import grigoris.tasos.movierama.APICallsHandler;
import grigoris.tasos.movierama.IAPICallsHandler;
import grigoris.tasos.movierama.IFavoritesManager;
import grigoris.tasos.movierama.MyJSONParser;
import grigoris.tasos.movierama.R;
import grigoris.tasos.movierama.Activities_Fragments.ShowMovie;
import grigoris.tasos.movierama.POJOs.TheMovie;
import grigoris.tasos.movierama.TimeParser;

public class PopularMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int                           TYPE_ITEM = 0;
    private final int                           TYPE_LOADING = 1;
    private Context                             context;
    private ArrayList<TheMovie>                 moviesToShow;
    private boolean                             isLoading, isFavorites;
    private RecyclerView                        rv;
    private IFavoritesManager                   favoritesManager;
    private IAPICallsHandler                    apiCallsHandler;

    public PopularMoviesAdapter(Context context, RecyclerView rv, LinearLayoutManager llm, ArrayList<TheMovie> movies,
                                boolean isFavorites, IFavoritesManager favoritesManager, IAPICallsHandler apiCallsHandler){

        this.context = context;
        this.rv = rv;
        this.isFavorites = isFavorites;
        this.favoritesManager = favoritesManager;
        this.apiCallsHandler = apiCallsHandler;

        moviesToShow = movies;

        if (!isFavorites)
            rv.addOnScrollListener(new MyScroll(llm));

    }

    public void updateView(ArrayList<TheMovie> movies){

        this.moviesToShow = movies;
        notifyDataSetChanged();

    }

    private void startLoading(ProgressBar bar) {

        isLoading = true;
        bar.setVisibility(View.VISIBLE);

    }

    private void finishLoading(ProgressBar bar) {

        isLoading = false;
        bar.setVisibility(View.GONE);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM)
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.popular_movie_row, parent, false));
        else
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.loading_bar, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemHolder) {

            TheMovie movie = moviesToShow.get(position);

            ((ItemHolder)holder).title.setText(movie.getTitle());
            ((ItemHolder)holder).date.setText(context.getString(R.string.release_date).concat(" : ").
                    concat(TimeParser.getFormattedTime(movie.getDate())));
            ((ItemHolder)holder).rating.setText(context.getString(R.string.rating).concat(" : ").concat(movie.getRating()));

            Picasso.get().load(APICallsHandler.IMAGE_URL.concat("w300/").concat(movie.getPoster())).
                    placeholder(AppCompatResources.getDrawable(context, R.drawable.placeholder)).into(((ItemHolder)holder).poster);


            if (!isFavorites) {

                if (favoritesManager.isFavorite(movie.getId()))
                    ((ItemHolder) holder).favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_selected));
                else
                    ((ItemHolder) holder).favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_unselected));

            } else {

                ((ItemHolder) holder).favorite.setVisibility(View.GONE);

            }

        }

    }

    @Override
    public int getItemViewType(int position) {

        return moviesToShow.size() == position ? TYPE_LOADING : TYPE_ITEM;

    }

    @Override
    public int getItemCount() {
        return moviesToShow.size() + 1;
    }


    class ItemHolder extends RecyclerView.ViewHolder{

        TextView title, date, rating;
        ImageView favorite, poster;

        ItemHolder(View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            rating = itemView.findViewById(R.id.rating);
            favorite = itemView.findViewById(R.id.favorite);
            poster = itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, ShowMovie.class);
                intent.putExtra("movie_id", moviesToShow.get(getAdapterPosition()).getId());
                context.startActivity(intent);

            });

            favorite.setOnClickListener((view) ->{

                TheMovie movie = moviesToShow.get(getAdapterPosition());

                if (favoritesManager.isFavorite(movie.getId())){

                    favoritesManager.removeFavorite(movie.getId());
                    favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_unselected));

                } else{

                    favoritesManager.addFavorite(movie.getId());
                    favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_selected));

                }

            });

        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        LoadingViewHolder(View view) {

            super(view);
            progressBar = view.findViewById(R.id.progress_bar);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);

        }
    }



    private class MyScroll extends OnScrollListener {

        LinearLayoutManager llm;
        MyJSONParser parser;

        MyScroll(LinearLayoutManager llm){

            this.llm = llm;
            parser = new MyJSONParser();

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int lastViewedItem = llm.findLastCompletelyVisibleItemPosition();

            if(lastViewedItem == moviesToShow.size() && !isLoading){

                RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(lastViewedItem);
                ProgressBar pBar = ((LoadingViewHolder)holder).progressBar;

                if (parser.getLastType() == 0){ // Load more popular pages

                    if(parser.hasNoMorePopularPages())
                        return;

                    startLoading(pBar);
                    apiCallsHandler.setListener((str) -> updateOnScrollCallback(str.get(0), 0, pBar));
                    apiCallsHandler.loadMorePopular();

                } else {                        // Load more search results pages

                    if(parser.hasNoMoreSearchPages())
                        return;

                    String query = ((SearchView)((Activity)context).findViewById(R.id.search_view)).getQuery().toString();

                    if (!query.isEmpty()) {

                        startLoading(pBar);
                        apiCallsHandler.setListener((str) -> updateOnScrollCallback(str.get(0), 1, pBar));
                        apiCallsHandler.searchMovie(query, parser.getLastSearchPage());

                    }
                }
            }
        }

        private void updateOnScrollCallback(String str, int type, ProgressBar pBar){

            moviesToShow.addAll(parser.parseMovies(str, type));
            notifyDataSetChanged();
            finishLoading(pBar);

        }

    }
}