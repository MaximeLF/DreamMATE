package tasks;

import com.dreammate.R;
import com.dreammate.RegisterActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import model.User;


public class RegisterUserTask extends AsyncTask<User, Void, User>
{
    private WeakReference<RegisterActivity> regUserWeakRef;

    public RegisterUserTask (RegisterActivity registerActivity) {
        regUserWeakRef = new WeakReference<>(registerActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(User... users) {

        User baseUser = users[0];

        if (regUserWeakRef != null)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = regUserWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("users");
            builder.appendPath("");

            try {
                Gson gson = new GsonBuilder().create();

                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestMethod("POST");
                connection.setDoInput(true);

                String body = gson.toJson(baseUser);

                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    Scanner s = new Scanner(reader).useDelimiter("\\A");

                    String answer = s.hasNext() ? s.next() : "";
                    Log.d("lua", "Received : " + answer);

                    User user = gson.fromJson(answer, User.class);

                    reader.close();
                    connection.disconnect();

                    Log.d("lua", (user == null) ? "Null user!" : user.toString());

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
        if (regUserWeakRef != null) {
            regUserWeakRef.get().confirmRegistration(user);
        }
    }
}
