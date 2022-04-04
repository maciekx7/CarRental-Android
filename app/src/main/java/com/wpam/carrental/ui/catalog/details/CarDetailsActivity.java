package com.wpam.carrental.ui.catalog.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CarDetailsActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    TextView make, model, fuel;
    ImageView imageView;
    Button rentButton;
    private Car car;

    private int carId;

    public static final String TAG_ID = "id";
    public static final String TAG_PAGE_TITLE = "detail_view_title";

    String url = "http://10.0.2.2:4000/api/catalog/cars/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getStringExtra(TAG_PAGE_TITLE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_details);


        make = findViewById(R.id.car_make);
        model = findViewById(R.id.car_model);
        fuel = findViewById(R.id.car_fuel);
        imageView = findViewById(R.id.car_img);
        rentButton = findViewById(R.id.rent_button);
        carId = getIntent().getIntExtra(TAG_ID,0);

        Picasso.get().load("http://10.0.2.2:4000/images/car_" + carId + ".jpg").into(imageView);


        String endpoint = url + carId;
        getData(endpoint);

    }

    private void getData(String url) {
        Request request = new Request.Builder()
                .url(url)
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
                JsonObject jsonCar = gson.fromJson(responseBody.string(), JsonObject.class).get("car").getAsJsonObject();
                Type type = new TypeToken<Car>() {}.getType();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        car = gson.fromJson(jsonCar, type);
                        make.setText(car.getCarModel().getMake().getName());
                        model.setText(car.getCarModel().getName());
                        fuel.setText(car.getCarModel().getFuel().getCode());
                        setRentButtonVisibility(car.isAvailability());
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void setRentButtonVisibility(boolean carAvailability) {
        if(carAvailability) {
            rentButton.setVisibility(View.VISIBLE);
        } else {
            rentButton.setVisibility(View.GONE);
        }
    }
}

