package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dreammate.MyProfileActivity;
import com.dreammate.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import model.User;

public class GetUserInfoTask extends AsyncTask<String, Void, User>
{

    private WeakReference<MyProfileActivity> actWeakRef;

    public GetUserInfoTask(MyProfileActivity activity) {
        actWeakRef = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected User doInBackground(String... strings) {
        String userId = strings[0];

        if (actWeakRef != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = actWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("users");
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

                    User user = gson.fromJson(answer, User.class);

                    reader.close();
                    connection.disconnect();

                    return user;
                }
            }
            catch (Exception e) {
                Log.e("lua", e.toString());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (actWeakRef != null) {
            actWeakRef.get().onUserInfoReceived(user);
        }
    }
}
