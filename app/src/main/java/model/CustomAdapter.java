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

public class CustomAdapter extends ArrayAdapter<User> {

    private int layout;
    private Context context;
    public CustomAdapter(Context context, int resource, ArrayList<User> data){

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
        String description = getItem(position).getDescription();

        TextView tvFname = (TextView) view.findViewById(R.id.firstName);
        TextView tvLname = (TextView) view.findViewById(R.id.lastName);
        TextView tvDescription = (TextView) view.findViewById(R.id.description);

        tvFname.setText(fname);
        tvLname.setText(lname);
        tvDescription.setText(description);

        return view;
    }
}
