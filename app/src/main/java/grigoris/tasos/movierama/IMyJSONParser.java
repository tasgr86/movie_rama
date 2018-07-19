package grigoris.tasos.movierama;

import java.util.ArrayList;
import grigoris.tasos.movierama.POJOs.TheMovie;
import grigoris.tasos.movierama.POJOs.TheReview;

public interface IMyJSONParser {

    ArrayList<TheMovie> parseMovies(String response, int type);

    int getLastPopularPage();

    int getLastSearchPage();

    boolean hasNoMorePopularPages();

    boolean hasNoMoreSearchPages();

    int getLastType();

    TheMovie parseCredits(String response);

    ArrayList<TheReview> parseReviews(String response);

    ArrayList<TheMovie> parseSimilar(String response);

}
