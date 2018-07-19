package grigoris.tasos.movierama.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import grigoris.tasos.movierama.R;
import grigoris.tasos.movierama.POJOs.TheReview;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.Holder> {

    private Context context;
    private ArrayList<TheReview> reviews;

    public ReviewsAdapter(Context context, ArrayList<TheReview> reviews){

        this.context = context;
        this.reviews = reviews;

    }

    @NonNull
    @Override
    public ReviewsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.review_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.Holder holder, int position) {

        TheReview review = reviews.get(position);

        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView review, author;

        Holder(View itemView) {
            super(itemView);

            review = itemView.findViewById(R.id.review);
            author = itemView.findViewById(R.id.author);

        }
    }
}
