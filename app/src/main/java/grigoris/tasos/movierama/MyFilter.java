package grigoris.tasos.movierama;

import android.widget.Filter;
import java.util.ArrayList;

public class MyFilter extends Filter {

    private PopularMoviesAdapter adapter;
    private ArrayList<ThePopularMovie> movies;

    MyFilter(PopularMoviesAdapter adapter, ArrayList<ThePopularMovie> movies){
        this.adapter = adapter;
        this.movies = movies;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        ArrayList<ThePopularMovie> filteredList = new ArrayList<>();
        FilterResults filter = new FilterResults();

        if (constraint != null && constraint.length() > 0){

            for (ThePopularMovie movie : movies){

                if (movie.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){

                    filteredList.add(movie);

                }

            }

            filter.count = filteredList.size();
            filter.values = filteredList;

        }else {

            filter.count = movies.size();
            filter.values = movies;

        }

        return filter;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.moviesToShow = (ArrayList<ThePopularMovie>) results.values;
        adapter.notifyDataSetChanged();

    }

}
