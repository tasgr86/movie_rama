package grigoris.tasos.movierama;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class PopularMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 0;
    private final int TYPE_LOADING = 1;
    private Context context;
    public static ArrayList<TheMovie> moviesToShow;
    private boolean isLoading;
    private SearchView searchView;
    private RecyclerView rv;
    private FavoritesManager favoritesManager;
    private LinearLayoutManager llm;
    APICallsHandler apiCallsHandler;

    public PopularMoviesAdapter(Context context, RecyclerView rv, LinearLayoutManager llm, ArrayList<TheMovie> movies){

        this.context = context;
        moviesToShow = movies;
        this.llm = llm;

        this.rv = rv;
        setUpSearchWidget();
        rv.addOnScrollListener(new MyScroll(llm));
        favoritesManager = new FavoritesManager(context);
        apiCallsHandler = new APICallsHandler(context);

    }

    public void startLoading(ProgressBar bar) {

        isLoading = true;
        bar.setVisibility(View.VISIBLE);

    }

    public void finishLoading(ProgressBar bar) {

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

            System.out.println("list size: " + moviesToShow.size() + " position: " + position + " parsing " +
                    moviesToShow.get(position).getTitle() + " " + moviesToShow.get(position).getDate());

            TheMovie movie = moviesToShow.get(position);

            ((ItemHolder)holder).title.setText(movie.getTitle());
            ((ItemHolder)holder).date.setText(context.getString(R.string.release_date).concat(" : ").
                    concat(TimeParser.getFormattedTime(movie.getDate())));
            ((ItemHolder)holder).rating.setText(context.getString(R.string.rating).concat(" : ").concat(movie.getRating()));

            Picasso.get().load(MainActivity.IMAGE_URL.concat("w300/").concat(movie.getPoster())).
                    placeholder(R.drawable.placeholder).into(((ItemHolder)holder).poster);

            if (favoritesManager.isFavorite(movie.getId()))
                ((ItemHolder)holder).favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_selected));
            else
                ((ItemHolder)holder).favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_unselected));

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

    private void setUpSearchWidget(){

        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchView = ((Activity)context).findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(((Activity)context).getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String query) {

                if (!query.isEmpty()){

                    apiCallsHandler.apiCallsListener = (str) -> {

                        moviesToShow.clear();
                        moviesToShow = MyJSONParser.parseMovies(str.get(0), 0);
                        notifyDataSetChanged();

                    };
                    apiCallsHandler.searchMovie(query, 1);


                }else {

                    apiCallsHandler.apiCallsListener = (str -> {

                        moviesToShow.clear();
                        moviesToShow = MyJSONParser.parseMovies(str.get(0), 0);
                        notifyDataSetChanged();


                    });
                    apiCallsHandler.fetchFirstPage(true);

                }

                return false;
            }
        });

    }



    private class MyScroll extends OnScrollListener {

        LinearLayoutManager llm;

        MyScroll(LinearLayoutManager llm){

            this.llm = llm;

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

                if (MyJSONParser.getLastType() == 0){

                    startLoading(pBar);

                    apiCallsHandler.apiCallsListener = (str) -> {

                        moviesToShow.addAll(MyJSONParser.parseMovies(str.get(0), 0));
                        finishLoading(pBar);
                        notifyDataSetChanged();

                    };
                    apiCallsHandler.loadMorePopular();

                } else {

                    System.out.println("loading more SEARCH movies ... " + MyJSONParser.hasNoMoreSearchPages());

                    if(MyJSONParser.hasNoMoreSearchPages())
                        return;

                    String query = searchView.getQuery().toString();

                    if (!query.isEmpty()) {

                        startLoading(pBar);

                        apiCallsHandler.apiCallsListener = (str) -> {

                            moviesToShow.addAll(MyJSONParser.parseMovies(str.get(0),1));
                            notifyDataSetChanged();
                            finishLoading(pBar);

                        };
                        apiCallsHandler.searchMovie(query, MyJSONParser.getLastSearchPage());

                    }
                }
            }
        }
    }
}