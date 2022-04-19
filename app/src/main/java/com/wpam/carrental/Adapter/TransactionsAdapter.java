package com.wpam.carrental.Adapter;

import android.annotation.SuppressLint;
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
import com.wpam.carrental.model.Transaction;

import java.util.List;
import java.util.Objects;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {
    private LayoutInflater inflater;
    private TextView car, rentDate, returnDate, status;

    public TransactionsAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.transactions_list_row, null);
        }

        ImageView imageView = convertView.findViewById(R.id.car_img);

        car = convertView.findViewById(R.id.car);
        returnDate = convertView.findViewById(R.id.return_date);
        status = convertView.findViewById(R.id.status);


        Transaction transaction = getItem(position);

        car.setText(transaction.getCar().getCarModel().getMake().getName() + " " +  transaction.getCar().getCarModel().getName());
        if(!Objects.isNull(transaction.getReturnDate())) {
            returnDate.setText(transaction.getRentDate() + " - " + transaction.getReturnDate());
            status.setText("Returned");
            status.setTextColor(Color.GREEN);
        } else {
            returnDate.setText("");
            status.setText(transaction.getRentDate());
        }

        String imgUrl = "http://10.0.2.2:4000/images/car_" + transaction.getCarId() + ".jpg";
        Picasso.get().load(imgUrl).into(imageView);

        return convertView;
    }
}
