package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dreammate.CreateProfileActivity;
import com.dreammate.R;

import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetCountryListTask extends AsyncTask<Void, Void, List<String>> {

    private WeakReference<CreateProfileActivity> actWeakRef;

    public GetCountryListTask(CreateProfileActivity act) {
        actWeakRef = new WeakReference<>(act);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<String> doInBackground(Void... voids)
    {
        if (actWeakRef != null)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            String server = actWeakRef.get().getApplicationContext().getResources().getString(R.string.server_url);
            builder.authority(server);
            builder.appendPath("dev");
            builder.appendPath("countries_list");

            try {
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    Scanner s = new Scanner(reader).useDelimiter("\\A");

                    String answer = s.hasNext() ? s.next() : "";

                    reader.close();
                    connection.disconnect();

                    Log.d("lua", "Received countries: " + answer);

                    answer = answer.substring(2, answer.length() - 2); // remove '["' at the beginning and '"]' at the end
                    List<String> countries = new ArrayList<>();
                    String[] words = answer.split("\",\"");

                    for (int i = 0; i < words.length; i++) {
                        String clean = words[i].trim();
                        countries.add(clean);
                    }

                    return countries;
                }
                else {
                    Log.d("lua", "Response code was NOT OK");
                }
            }
            catch (Exception e)
            {
                Log.e("lua", e.toString());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> countries) {
        super.onPostExecute(countries);
        if (actWeakRef != null) {
           actWeakRef.get().onCountriesResultComputed(countries); // PUT HERE THE CODE THAT PUTS THE GIVEN LIST IN THE ACTIVITY
        }
    }
}
