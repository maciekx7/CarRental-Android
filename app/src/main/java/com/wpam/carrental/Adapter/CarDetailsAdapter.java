package com.wpam.carrental.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;

import java.util.Objects;

public class CarDetailsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private Car car;

    public CarDetailsAdapter(Activity activity, Car car) {
        this.activity = activity;
        this.car = car;
    }

    @Override
    public int getCount() {
        if(Objects.isNull(car)) {
            return 0;
        }
        return 1;
    }

    @Override
    public Object getItem(int location) {
        return car;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.car_details, null);
        }

        TextView make = convertView.findViewById(R.id.car_make);
        TextView model = convertView.findViewById(R.id.car_model);
        TextView fuel = convertView.findViewById(R.id.car_fuel);

        make.setText(car.getCarModel().getMake().getName());
        model.setText(car.getCarModel().getName());
        fuel.setText(car.getCarModel().getFuel().getCode());

        return convertView;
    }
}
