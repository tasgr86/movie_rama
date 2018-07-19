package grigoris.tasos.movierama.Activities_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;
import grigoris.tasos.movierama.IAPICallsHandler;
import grigoris.tasos.movierama.IMyJSONParser;
import grigoris.tasos.movierama.MyApplication;

public class SplashScreen extends AppCompatActivity {

    @Inject
    IMyJSONParser parser;
    @Inject
    IAPICallsHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((MyApplication)getApplication())
                .getComponent()
                .inject(SplashScreen.this);

        handler.setListener(this::startMainActivity);
        handler.fetchFirstPage();

    }

    private void startMainActivity(ArrayList<String> str){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra("popular_movies", parser.parseMovies(str.get(0), 0));
        startActivity(intent);
        finish();

    }
}