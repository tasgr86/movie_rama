package grigoris.tasos.movierama;

import java.util.ArrayList;

public interface IFavoritesManager {

    void addFavorite(int newFavorite);

    void removeFavorite(int favoriteToRemove);

    ArrayList<Integer> getFavorites();

    boolean isFavorite(int id_to_check);

}
