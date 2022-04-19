package com.wpam.carrental.Adapter.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;

import java.util.List;

public class CatalogAdapter extends ArrayAdapter<Car> {
    private LayoutInflater inflater;

    public CatalogAdapter(Context context, List<Car> cars) {
        super(context, 0, cars);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cars_list_row, null);
        }

        TextView model = convertView.findViewById(R.id.car);
        TextView make = convertView.findViewById(R.id.car_make);
        ImageView imageView = convertView.findViewById(R.id.car_img);



        Car car = getItem(position);
        model.setText(car.getCarModel().getName());
        make.setText(car.getCarModel().getMake().getName());
        Picasso.get().load("http://10.0.2.2:4000/images/car_" + car.getId() + ".jpg").into(imageView);


        return convertView;
    }


}
