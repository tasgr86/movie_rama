package grigoris.tasos.movierama.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import grigoris.tasos.movierama.APICallsHandler;
import grigoris.tasos.movierama.R;
import grigoris.tasos.movierama.Activities_Fragments.ShowMovie;
import grigoris.tasos.movierama.POJOs.TheMovie;

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.Holder> {

    private ArrayList<TheMovie> movies;
    private Context context;

    public SimilarMoviesAdapter(Context context, ArrayList<TheMovie> movies){

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

        String url = APICallsHandler.IMAGE_URL.concat("w300/").concat(movies.get(position).getPoster());
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.poster);

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

            itemView.setOnClickListener((view) -> {

                int movie_id = movies.get(getAdapterPosition()).getId();

                Intent intent = new Intent(context, ShowMovie.class);
                intent.putExtra("movie_id", movie_id);
                context.startActivity(intent);
                ((Activity)context).finish();

            });

        }
    }
}
