package grigoris.tasos.movierama;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.Holder> {

    private ArrayList<TheMovie> movies;
    private Context context;

    SimilarMoviesAdapter(Context context, ArrayList<TheMovie> movies){

        this.context = context;
        this.movies = movies;

    }


    @NonNull
    @Override
    public SimilarMoviesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.similar_movie_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarMoviesAdapter.Holder holder, int position) {

        String url = MainActivity.IMAGE_URL.concat(movies.get(position).getPoster());
        Picasso.get().load(url).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView poster;

        Holder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.similar_photo);

        }
    }
}
