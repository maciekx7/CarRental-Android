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

import java.util.List;
import java.util.Objects;

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
        TextView fuel = convertView.findViewById(R.id.car_fuel);
        TextView body = convertView.findViewById(R.id.car_body);
        TextView price = convertView.findViewById(R.id.car_cost);
        TextView availability = convertView.findViewById(R.id.car_avaliability);



        Car car = getItem(position);
        model.setText(car.getCarModel().getName());
        make.setText(car.getCarModel().getMake().getName());
        Picasso.get().load("http://10.0.2.2:4000/images/car_" + car.getId() + ".jpg").into(imageView);
        fuel.setText(car.getCarModel().getFuel().toString());
        body.setText(car.getCarModel().getBody().toString());
        price.setText(Integer.toString(car.getCost()));

        if(car.isAvailable()) {
            availability.setText("Available");
            availability.setTextColor(Color.GREEN);
        } else {
            availability.setText("Rented");
            availability.setTextColor(Color.RED);

        }

        return convertView;
    }


}
