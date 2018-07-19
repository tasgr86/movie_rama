package grigoris.tasos.movierama;

import android.app.Application;

public class MyApplication extends Application {

    private MyComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = createMyComponent();
    }

    public MyComponent getComponent() {
        return component;
    }

    private MyComponent createMyComponent(){

        return DaggerMyComponent
                .builder()
                .myModule(new MyModule(getBaseContext()))
                .build();

    }
}