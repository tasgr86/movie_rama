package grigoris.tasos.movierama;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import dmax.dialog.SpotsDialog;

public class APICallsHandler {

    private Context context;
    public APICallsListener apiCallsListener;
    private Dialog loadingDialog;

    public APICallsHandler(Context context){

        this.context = context;
        loadingDialog = new SpotsDialog.Builder().setContext(context).
                setMessage(context.getString(R.string.loading)).setCancelable(false).build();

    }


    public void fetchFirstPage(boolean showLoadingDialog){

        if(showLoadingDialog)
            loadingDialog.show();

        GET get = new GET();
        get.getListener = (str) -> {

            apiCallsListener.fetchedResults(str);

            if (showLoadingDialog)
                loadingDialog.dismiss();

        };

        get.execute(MainActivity.BASE_URL.concat(MainActivity.POPURAL_URL).concat("?api_key=").
                concat(context.getString(R.string.appi_key)));

    }


    public void loadMorePopular(){

        GET get2 = new GET();
        get2.getListener = (str) -> apiCallsListener.fetchedResults(str);
        get2.execute(MainActivity.BASE_URL.concat(MainActivity.POPURAL_URL).concat("?api_key=").
                concat(context.getString(R.string.appi_key)).concat("&page=").
                concat(String.valueOf(MyJSONParser.getLastPopularPage())));


    }


    public void searchMovie(String text, int page){

        GET get = new GET();
        get.getListener = (str) -> apiCallsListener.fetchedResults(str);

        get.execute(MainActivity.BASE_URL.concat(MainActivity.SEARCH_MOVIE).concat("?api_key=").
                concat(context.getString(R.string.appi_key)).concat("&query=").concat(text).
                concat("&page=").concat(String.valueOf(page)));
    }




    public void fetchMovie(int movie_id){

        loadingDialog.show();

        String credits = MainActivity.BASE_URL.concat("movie/").concat(String.valueOf(movie_id)).concat("?api_key=").
                concat(context.getString(R.string.appi_key));

        String reviews = MainActivity.BASE_URL.concat("movie/").concat(String.valueOf(movie_id))
                .concat("/reviews").concat("?api_key=").concat(context.getString(R.string.appi_key));

        String similar = MainActivity.BASE_URL.concat("movie/").concat(String.valueOf(movie_id))
                .concat("/similar").concat("?api_key=").concat(context.getString(R.string.appi_key));

        GET get = new GET();
        get.getListener = s -> {

            apiCallsListener.fetchedResults(s);
            loadingDialog.dismiss();
            loadingDialog.dismiss();

        };
        get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, credits, reviews, similar);

    }
}