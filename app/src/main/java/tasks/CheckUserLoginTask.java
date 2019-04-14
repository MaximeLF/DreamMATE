package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dreammate.LoginActivity;
import com.dreammate.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import model.User;

public class CheckUserLoginTask extends AsyncTask<User, Void, User>
{
    private WeakReference<LoginActivity> loginWeakRef;

    public CheckUserLoginTask(LoginActivity loginActivity) {
        loginWeakRef = new WeakReference<>(loginActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(User... users) {
        User user = users[0];

        if (loginWeakRef != null)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = loginWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("authentication");
            builder.appendPath(user.email);
            builder.appendPath(user.encrypted);

            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestMethod("GET");


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    Scanner s = new Scanner(reader).useDelimiter("\\A");

                    String answer = s.hasNext() ? s.next() : "";
                    Log.d("lua", "Received : " + answer);

                    User receivedUser = gson.fromJson(answer, User.class);

                    reader.close();
                    connection.disconnect();

                    return receivedUser;
                }
                else {
                    Log.d("lua", "Response code was NOT OK");
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
        if (loginWeakRef != null) {
            loginWeakRef.get().confirmLogin(user);
        }
    }
}
