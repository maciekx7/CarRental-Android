package com.wpam.carrental.Adapter.catalog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.Model;

import java.util.List;

public class ModelAdapter extends ArrayAdapter<Model> {
    private LayoutInflater inflater;
    List<Model> models;
    private Context context;

    public ModelAdapter(Context context, int textViewResourceId,
                       List<Model> models) {
        super(context, textViewResourceId, models);
        this.context = context;
        this.models = models;
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
        Model model = getItem(position);
        label.setText(model.getMake().getName() + " " + model.getName() + " (" +
                model.getFuel() + ")" + " /" + model.getBody() + "/");

        return label;
    }

}
