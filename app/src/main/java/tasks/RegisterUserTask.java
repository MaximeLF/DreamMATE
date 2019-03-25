package tasks;

import com.dreammate.RegisterActivity;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;

import model.UserInfo;


public class RegisterUserTask extends AsyncTask<UserInfo, Void, UserInfo> {
    private WeakReference<RegisterActivity> regUserWeakRef;

    public RegisterUserTask (RegisterActivity registerActivity) {
        regUserWeakRef = new WeakReference<>(registerActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected UserInfo doInBackground(UserInfo... userInfos) {

        UserInfo baseInfo = userInfos[0];

        if (regUserWeakRef != null) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            builder.authority("api.forismatic.com");
            builder.appendPath("api");
            builder.appendPath("1.0");
            builder.appendPath("");

            try {
                Gson gson = new Gson();
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);

                String body = gson.toJson(baseInfo);

                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                connection.setRequestMethod("POST");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                    UserInfo userInfo = gson.fromJson(reader, UserInfo.class);

                    reader.close();
                    connection.disconnect();

                    return userInfo;
                }
            }
            catch (Exception e) {

            }
            finally {

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(UserInfo userInfo) {
        super.onPostExecute(userInfo);
        if (regUserWeakRef != null) {
            regUserWeakRef.get().confirmRegistration(userInfo);
        }
    }
}
