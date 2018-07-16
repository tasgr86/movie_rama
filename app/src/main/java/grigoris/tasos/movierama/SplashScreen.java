package grigoris.tasos.movierama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    APICallsHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        handler = new APICallsHandler(this);
        handler.apiCallsListener = this::startMainActivity;
        handler.fetchFirstPage(false);

    }

    private void startMainActivity(ArrayList<String> str){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra("popular_movies", MyJSONParser.parseMovies(str.get(0), 0));
        startActivity(intent);
        finish();

    }
}