package model;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreammate.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<User> {

    private int layout;
    private Context context;

    public CustomAdapter(Context context, int resource, List<User> data)
    {
        super(context, resource, data);
        this.layout = resource;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(this.layout, parent, false);
        }

        String gender = getItem(position).getGender();
        String fname = getItem(position).getFirstName();
        String lname = getItem(position).getLastName();
        String age = Integer.toString(getItem(position).getAge());
        String description = getItem(position).getDescription();
        String profileInfo = fname + " " + lname + " " + age;
        TextView tvDescription = view.findViewById(R.id.description);
        TextView tvProfileInfo = view.findViewById(R.id.profileInfo);

        tvProfileInfo.setText(profileInfo);
        tvDescription.setText(description);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);

        if(gender.equals("Female")){
            avatar.setImageResource(R.drawable.girl_avatar);
        }

        else if(gender.equals("Male")){
            avatar.setImageResource(R.drawable.male_avatar);
        }

        else{
            avatar.setImageResource(R.drawable.other_avatar);
        }

        return view;
    }

}
