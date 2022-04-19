package com.wpam.carrental.ui.transactions;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.ApiPostModels.TransactionClosingModel;
import com.wpam.carrental.model.ApiPostModels.TransactionInitializationModel;
import com.wpam.carrental.model.Car;
import com.wpam.carrental.model.Transaction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TransactionDetailsActivty extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    TextView make, model, fuel, date, cost, email;
    ImageView imageView;
    Button returnButton;
    private Transaction transaction;

    public static final String TAG_ID = "id";
    public static final String TAG_PAGE_TITLE = "detail_view_title";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    String url = "http://10.0.2.2:4000/api/transactions/";
    String returnUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getStringExtra(TAG_PAGE_TITLE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);


        make = findViewById(R.id.car_make);
        model = findViewById(R.id.car);
        fuel = findViewById(R.id.car_fuel);
        imageView = findViewById(R.id.car_img);
        returnButton = findViewById(R.id.return_button);
        int transactionId = getIntent().getIntExtra(TAG_ID, 0);
        cost = findViewById(R.id.cost);
        email = findViewById(R.id.email);
        date = findViewById(R.id.rentDate);

        String endpoint = url + transactionId;
        getData(endpoint);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnUrl = "http://10.0.2.2:4000/api/transactions/" + transactionId;
                returnCar(transaction, returnUrl);
            }
        });

    }

    //-----GET transaction data-----
    private void getData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                .build();
        fetchData(request);
    }

    private void fetchData(Request request) {
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("FAIL");
                System.out.println(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                //System.out.println(responseBody.string());
                JsonObject jsonCar = gson.fromJson(responseBody.string(), JsonObject.class).get("transaction").getAsJsonObject();
                Type type = new TypeToken<Transaction>() {}.getType();
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        transaction = gson.fromJson(jsonCar, type);
                        Picasso.get().load("http://10.0.2.2:4000/images/car_" + transaction.getCarId() + ".jpg").into(imageView);
                        make.setText(transaction.getCar().getCarModel().getMake().getName());
                        model.setText(transaction.getCar().getCarModel().getName());
                        fuel.setText(transaction.getCar().getCarModel().getFuel().getCode());
                        if(Objects.isNull(transaction.getReturnDate())) {
                            date.setText(transaction.getRentDate() + " - ");
                        } else {
                            date.setText(transaction.getRentDate() + " - " + transaction.getReturnDate());
                            date.setTextColor(Color.GREEN);
                        }
                        cost.setText(Integer.toString(transaction.getCost()));
                        if(CurrentUser.getInstance().isAdmin()) {
                            email.setVisibility(View.VISIBLE);
                            email.setText(transaction.getUser().getEmail());
                        } else {
                            email.setVisibility(View.GONE);
                        }
                        setReturnButtonVisibility(transaction);
                    }
                });
            }
        });
    }

    //-----POST rent car-----

    public void returnCar(Transaction transaction, String url) {

        LocalDate rentDate = LocalDate.parse(transaction.getRentDate());
        Period period = Period.between(rentDate, LocalDate.now());
        int days = period.getDays();
        if(days < 1) {
            days = 1;
        }
        int cost = transaction.getCar().getCost() * days ;

        TransactionClosingModel model = new TransactionClosingModel(
                cost,
                LocalDate.now().toString()
        );
        RequestBody body = RequestBody.create(new Gson().toJson(model), JSON);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                .put(body)
                .build();
        postRentCar(request);
    }

    public void postRentCar(Request request) {
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("FAIL");
                System.out.println(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setReturnButtonVisibility(transaction);
                        final Toast toast = Toast.makeText(getBaseContext(), "Car returned!", Toast.LENGTH_LONG);
                        toast.show();
                        getData(url + transaction.getId());
                        setReturnButtonVisibility(transaction);
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setReturnButtonVisibility(Transaction transaction) {
        if(Objects.isNull(transaction.getReturnDate()) && CurrentUser.getInstance().isAdmin()) {
            returnButton.setVisibility(View.VISIBLE);
        } else {
            returnButton.setVisibility(View.GONE);
        }
    }



}
