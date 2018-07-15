package grigoris.tasos.movierama;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GET extends AsyncTask<String, Void, ArrayList<String>> {

    public GETListener getListener;

    public interface GETListener{

        void getResponse(ArrayList<String> s);

    }

    @Override
    protected ArrayList<String> doInBackground(String... str) {

        ArrayList<String> responses = new ArrayList<>();

        for (String aStr : str) {

            responses.add(fetch(aStr));

        }

        return responses;

    }


    @Override
    protected void onPostExecute(ArrayList<String> results) {
        super.onPostExecute(results);

        System.out.println("response: " + results);

        getListener.getResponse(results);

    }

    private String fetch(String url){

        try {

            URL mUrl = new URL(url);

            System.out.println("fetching: " + mUrl);

            HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(10000);

            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }
}
