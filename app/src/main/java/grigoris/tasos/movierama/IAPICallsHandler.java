package grigoris.tasos.movierama;

import java.util.ArrayList;

public interface IAPICallsHandler {

    void fetchFirstPage();

    void loadMorePopular();

    void searchMovie(String text, int page);

    void fetchMovie(int movie_id);

    void fetchPopularMovies(ArrayList<Integer> ids);

    void setListener(APICallsListener listener);

}
