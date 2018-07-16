package grigoris.tasos.movierama;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MyJSONParser {

    private static int lastPopularPage;
    private static int lastSearchPage;
    private static boolean hasNoMorePopularPages;
    private static boolean hasNoMoreSearchPages;
    private static int lastType;

    public static ArrayList<TheMovie> parseMovies(String response, int type){  // 0 for popular, 1 for search

        ArrayList<TheMovie> popularMovies = new ArrayList<>();
        lastType = type;

        try {

            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("results");

            if (type == 0){

                lastPopularPage = obj.getInt("page");
                lastSearchPage = 0;

                hasNoMorePopularPages = obj.getInt("total_pages") <= obj.getInt("page");
                hasNoMoreSearchPages = false;

            }else {

                lastSearchPage = obj.getInt("page");
                lastPopularPage = 0;

                hasNoMoreSearchPages = obj.getInt("total_pages") <= obj.getInt("page");
                hasNoMorePopularPages = false;

            }


            for(int i=0; i<array.length(); i++){

                JSONObject temp_obj = array.getJSONObject(i);

                int movie_id = temp_obj.getInt("id");
                String title = temp_obj.getString("title");
                String release_date = temp_obj.getString("release_date");
                String rating = temp_obj.getString("vote_average");
                String poster = temp_obj.getString("poster_path");

                popularMovies.add(new TheMovie(movie_id, poster, title, release_date, rating));

            }

            print();

            return popularMovies;

        } catch (JSONException e) {

            e.printStackTrace();
            return null;

        }
    }

    public static int getLastPopularPage() {
        return lastPopularPage + 1;
    }

    public static int getLastSearchPage() {
        return lastSearchPage + 1;
    }

    public static boolean hasNoMorePopularPages() {

        return hasNoMorePopularPages;

    }

    public static boolean hasNoMoreSearchPages() {

        return hasNoMoreSearchPages;

    }

    public static int getLastType() {

        return lastType;
    }

    public static TheMovie parseCredits(String response){

        try {

            JSONObject obj = new JSONObject(response);

            JSONArray genres = obj.getJSONArray("genres");
            String genre = "";

            for(int i=0; i<genres.length(); i++){

                genre = genres.getJSONObject(i).getString("name");

            }

            int id = obj.getInt("id");
            String title = obj.getString("original_title");
            String poster = obj.getString("poster_path");
            String overview = obj.getString("overview");
            String date = obj.getString("release_date");
            String rating = obj.getString("vote_average");

            return new TheMovie(id, title, genre, poster, overview, date, rating);

        } catch (JSONException e) {

            e.printStackTrace();
            return null;

        }
    }


    public static ArrayList<TheReview> parseReviews(String response){

        ArrayList<TheReview> reviews = new ArrayList<>();

        try {

            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("results");

            int length = array.length() > 2 ? 2 : array.length();

            for(int i=0; i<length; i++){

                JSONObject temp_ob = array.getJSONObject(i);
                String _id = temp_ob.getString("id");
                String content = temp_ob.getString("content");
                String url = temp_ob.getString("url");
                String author = temp_ob.getString("author");

                reviews.add(new TheReview(_id, content, url, author));

            }

            return reviews;

        } catch (JSONException e) {

            e.printStackTrace();
            return null;

        }
    }


    public static ArrayList<TheMovie> parseSimilar(String response){

        ArrayList<TheMovie> similarMovies = new ArrayList<>();

        try {

            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("results");

            for(int i=0; i<array.length(); i++){

                JSONObject temp_ob = array.getJSONObject(i);
                int id = temp_ob.getInt("id");
                String title = temp_ob.getString("original_title");
                String poster = temp_ob.getString("poster_path");

                similarMovies.add(new TheMovie(id, title, poster));

            }

            return similarMovies;

        } catch (JSONException e) {

            e.printStackTrace();
            return null;

        }
    }


    private static void print(){

        System.out.println("last popular page " + lastPopularPage);
        System.out.println("last search page " + lastSearchPage);
        System.out.println("last type " + lastType);
        System.out.println("has no more popular pages " + hasNoMorePopularPages());
        System.out.println("has no more search pages " + hasNoMoreSearchPages());

    }
}