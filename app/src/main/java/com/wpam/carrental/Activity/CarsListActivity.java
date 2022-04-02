package com.wpam.carrental.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.Adapter.CarsListAdapter;
import com.wpam.carrental.R;
import com.wpam.carrental.model.Car;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CarsListActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    String url = "http://10.0.2.2:4000/api/catalog/cars";
    private CarsListAdapter adapter;

    List<Car> carsList = new ArrayList<Car>();

    ListView listView;

    public static final String TAG_ID = "id";
    public static final String TAG_PAGE_TITLE = "detail_view_title";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list);
        listView = findViewById(R.id.list);
        Request request = new Request.Builder()
                .url(url)
                .build();


        adapter = new CarsListAdapter(CarsListActivity.this, carsList);
        listView.setAdapter(adapter);

        carsList.clear();

        Call call = client.newCall(request);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view,
                                   int position, long id)  {
                Intent intent = new Intent(CarsListActivity.this, CarDetailsActivity.class);
                intent.putExtra(TAG_ID, carsList.get(position).getId());
                intent.putExtra(TAG_PAGE_TITLE,
                        carsList.get(position).getCarModel().getMake().getName() + " " + carsList.get(position).getCarModel().getName());
                startActivity(intent);
           }
       });


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("FAIL");
                System.out.println(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("SUCCESS");
                Gson gson = new Gson();
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("car").getAsJsonArray();
                Type type = new TypeToken<List<Car>>() {}.getType();
                List<Car> cars = gson.fromJson(array, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Car car : cars) {
                            System.out.println(car.toString());
                            carsList.add(car);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }
}