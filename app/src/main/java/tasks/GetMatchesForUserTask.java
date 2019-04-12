package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dreammate.DashboardActivity;
import com.dreammate.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import model.User;

public class GetMatchesForUserTask extends AsyncTask<String, Void, List<User>> {

    WeakReference<DashboardActivity> actWeakRef;

    public GetMatchesForUserTask(DashboardActivity act) {
        actWeakRef = new WeakReference<>(act);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<User> doInBackground(String... strings) {
        String userId = strings[0];

        if (actWeakRef != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = actWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("matches");
            builder.appendPath(userId);

            try {
                Gson gson = new GsonBuilder().create();

                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestMethod("GET");
                connection.setDoInput(true);


                Log.d("lua", "Asked for user with id: " + userId);



                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    Scanner s = new Scanner(reader).useDelimiter("\\A");

                    String answer = s.hasNext() ? s.next() : "";
                    Log.d("lua", "Received user : " + answer);

                    Type userListType = new TypeToken<List<User>>() {}.getType();

                    List<User> matches = gson.fromJson(answer, userListType);

                    reader.close();
                    connection.disconnect();

                    return matches;
                }
            }
            catch (Exception e) {
                Log.e("lua", e.toString());
            }
        }
    }

    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        if (actWeakRef != null) {
            actWeakRef.get().onMatchesComputed(users);
        }
    }
}
