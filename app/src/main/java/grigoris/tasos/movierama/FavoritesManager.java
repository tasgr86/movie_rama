package grigoris.tasos.movierama;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class FavoritesManager {

    private Context context;
    private SharedPreferences prefs;
    private Gson gson;
    private Type type;

    public FavoritesManager(Context context){

        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        type = new TypeToken<ArrayList<Integer>>() {}.getType();

    }


    public void addFavorite(int newFavorite){

        ArrayList<Integer> favorites = getFavorites();
        favorites.add(newFavorite);

        String str_to_serialize = gson.toJson(favorites, type);
        prefs.edit().putString("favorites", str_to_serialize).apply();

        postFavorites();

    }

    public void removeFavorite(int favoriteToRemove){

        ArrayList<Integer> favorites = getFavorites();
        favorites.removeAll(Arrays.asList(favoriteToRemove));

        System.out.println("before");
        postFavorites();

        String str_to_serialize = gson.toJson(favorites);
        prefs.edit().putString("favorites", str_to_serialize).apply();

        System.out.println("after");
        postFavorites();

    }


    public ArrayList<Integer> getFavorites(){

        String str = prefs.getString("favorites", "");

        if (str.isEmpty())
            return new ArrayList<>();
        else
            return gson.fromJson(str, type);

    }

    public boolean isFavorite(int id_to_check){

        ArrayList<Integer> favorites = getFavorites();

        for(Integer favorite : favorites){

            if (favorite == id_to_check)
                return true;

        }

        return false;

    }


    public void postFavorites(){

        ArrayList<Integer> favorites = getFavorites();

        for(Integer favorite : favorites){

            System.out.println("favorites : " + favorite);

        }

    }

}