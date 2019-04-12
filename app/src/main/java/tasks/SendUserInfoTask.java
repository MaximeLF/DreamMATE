package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dreammate.CreateProfileActivity;
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

public class SendUserInfoTask extends AsyncTask<User, Void, Boolean>
{
    private WeakReference<CreateProfileActivity> actWeakRef;

    public SendUserInfoTask(CreateProfileActivity activity) {
        actWeakRef = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Boolean doInBackground(User... users) {
        User user = users[0];

        if (actWeakRef != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = actWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("users");
            builder.appendPath(user.id);
            user.id = null;

            try {
                Gson gson = new GsonBuilder().create();

                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestMethod("PUT");
                connection.setDoInput(true);

                String body = gson.toJson(user);

                Log.d("lua", "Sent user: " + body);

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
                    Log.d("lua", "Received user : " + answer);

                    User receivedUser = gson.fromJson(answer, User.class);

                    reader.close();
                    connection.disconnect();

                    return true;
                }
            }
            catch (Exception e) {
                Log.e("lua", e.toString());
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean worked) {
        super.onPostExecute(worked);
        if (actWeakRef != null) {
            actWeakRef.get().onUserInfoSent(worked);
        }
    }
}
