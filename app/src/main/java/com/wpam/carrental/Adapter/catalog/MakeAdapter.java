package com.wpam.carrental.Adapter.catalog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;
import com.wpam.carrental.model.Make;

import java.util.List;

public class MakeAdapter extends ArrayAdapter<Make> {
    private LayoutInflater inflater;
    List<Make> makes;
    private Context context;


    public MakeAdapter(Context context, int textViewResourceId,
                       List<Make> makes) {
        super(context, textViewResourceId, makes);
        this.context = context;
        this.makes = makes;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(getItem(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getName());

        return label;
    }


}
