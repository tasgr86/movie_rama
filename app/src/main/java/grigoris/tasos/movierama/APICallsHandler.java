package grigoris.tasos.movierama;

import android.content.Context;
import android.os.AsyncTask;
import java.util.ArrayList;

public class APICallsHandler implements IAPICallsHandler{

    public static final String          BASE_URL = "https://api.themoviedb.org/3/";
    public static final String          POPURAL_URL = "movie/popular";
    public static final String          SEARCH_MOVIE = "search/movie";
    public static final String          IMAGE_URL = "https://image.tmdb.org/t/p/";
    private Context                     context;
    public APICallsListener             apiCallsListener;
    private MyJSONParser                parser;

    public APICallsHandler(Context context){

        this.context = context;
        parser = new MyJSONParser();

    }

    public void setListener(APICallsListener listener) {
        apiCallsListener = listener;
    }

    public void fetchFirstPage(){

        GET get = new GET();
        get.getListener = (str) -> apiCallsListener.fetchedResults(str);
        get.execute(BASE_URL.concat(POPURAL_URL).concat("?api_key=").concat(context.getString(R.string.appi_key)));

    }


    public void loadMorePopular(){

        GET get2 = new GET();
        get2.getListener = (str) -> apiCallsListener.fetchedResults(str);
        get2.execute(BASE_URL.concat(POPURAL_URL).concat("?api_key=").concat(context.getString(R.string.appi_key)).
                concat("&page=").concat(String.valueOf(parser.getLastPopularPage())));


    }


    public void searchMovie(String text, int page){

        GET get = new GET();
        get.getListener = (str) -> apiCallsListener.fetchedResults(str);
        get.execute(BASE_URL.concat(SEARCH_MOVIE).concat("?api_key=").concat(context.getString(R.string.appi_key)).
                concat("&query=").concat(text).concat("&page=").concat(String.valueOf(page)));
    }




    public void fetchMovie(int movie_id){

        String credits = BASE_URL.concat("movie/").concat(String.valueOf(movie_id)).concat("&append_to_response=credits").concat("?api_key=").
                concat(context.getString(R.string.appi_key));

        String reviews = BASE_URL.concat("movie/").concat(String.valueOf(movie_id))
                .concat("/reviews").concat("?api_key=").concat(context.getString(R.string.appi_key));

        String similar = BASE_URL.concat("movie/").concat(String.valueOf(movie_id))
                .concat("/similar").concat("?api_key=").concat(context.getString(R.string.appi_key));

        GET get = new GET();
        get.getListener = s -> apiCallsListener.fetchedResults(s);
        get.execute(credits, reviews, similar);

    }



    public void fetchPopularMovies(ArrayList<Integer> ids){

        String urls[] = new String[ids.size()];

        for(int i=0; i<ids.size(); i++){

            urls[i] = BASE_URL.concat("movie/").concat(String.valueOf(ids.get(i))).concat("?api_key=").
                    concat(context.getString(R.string.appi_key));

        }

        GET get = new GET();
        get.getListener = s -> apiCallsListener.fetchedResults(s);
        get.execute(urls);

    }
}