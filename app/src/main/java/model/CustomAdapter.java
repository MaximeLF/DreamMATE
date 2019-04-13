package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreammate.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<User> {

    private int layout;
    private Context context;
    public CustomAdapter(Context context, int resource, List<User> data){

        super(context, resource, data);
        this.layout = resource;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View view = convertView;
        if(view == null){

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(this.layout, parent, false);
        }

        //ImageButton image = getItem(position).getImage();
        String fname = getItem(position).getFirstName();
        String lname = getItem(position).getLastName();
        String age = getItem(position).getAge();
        String description = getItem(position).getDescription();
        String profileInfo = fname + " " + lname + " " + age;

        TextView tvDescription = (TextView) view.findViewById(R.id.description);
        TextView tvProfileInfo = (TextView) view.findViewById(R.id.profileInfo);

        tvProfileInfo.setText(profileInfo);
        tvDescription.setText(description);

        return view;
    }

}
