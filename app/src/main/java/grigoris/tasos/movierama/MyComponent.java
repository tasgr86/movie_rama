package grigoris.tasos.movierama;

import dagger.Component;
import grigoris.tasos.movierama.Activities_Fragments.FavoritesFragment;
import grigoris.tasos.movierama.Activities_Fragments.MainActivity;
import grigoris.tasos.movierama.Activities_Fragments.MoviesFragment;
import grigoris.tasos.movierama.Activities_Fragments.ShowMovie;
import grigoris.tasos.movierama.Activities_Fragments.SplashScreen;

@Component(modules = MyModule.class)
public interface MyComponent {

    void inject(MainActivity mainActivity);

    void inject(ShowMovie showMovie);

    void inject(SplashScreen splashScreen);

    void inject(MoviesFragment moviesFragment);

    void inject(FavoritesFragment favoritesFragment);

}
