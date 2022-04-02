package com.wpam.carrental.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;

import java.util.List;

public class CarsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Car> carItems;

    public CarsListAdapter(Activity activity, List<Car> carItems) {
        this.activity = activity;
        this.carItems = carItems;
    }

    @Override
    public int getCount() {
        return carItems.size();
    }

    @Override
    public Object getItem(int location) {
        return carItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cars_list_row, null);
        }

        TextView model = convertView.findViewById(R.id.car_model);
        TextView make = convertView.findViewById(R.id.car_make);
        ImageView imageView = convertView.findViewById(R.id.car_img);



        Car car = carItems.get(position);
        model.setText(car.getCarModel().getName());
        make.setText(car.getCarModel().getMake().getName());
        Picasso.get().load("http://10.0.2.2:4000/images/car_" + car.getId() + ".jpg").into(imageView);


        return convertView;
    }


}
