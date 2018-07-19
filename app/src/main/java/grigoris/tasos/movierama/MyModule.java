package grigoris.tasos.movierama;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class MyModule {

    private Context context;

    MyModule(Context context){ this.context = context; }

    @Provides
    IMyJSONParser provideMyJSONParser(){ return new MyJSONParser(); }

    @Provides
    IAPICallsHandler provideAPICallsHandler(){ return new APICallsHandler(context); }

    @Provides
    IFavoritesManager provideFavoritesManager(){ return new FavoritesManager(context); }

}
