package com.wpam.carrental.ui.catalog;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.Adapter.catalog.MakeAdapter;
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.APIResultMessageBasic;
import com.wpam.carrental.model.Car;
import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.ModelCreation;
import com.wpam.carrental.model.enums.Body;
import com.wpam.carrental.model.enums.Fuel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddModelActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private static final String TAG_PAGE_TITLE = "Ad new model";
    private Spinner makeSpinner, fuelSpinner, bodySpinner;
    private Button addModelButton;
    private MakeAdapter adapter;
    private Make selectedMake;
    private TextView modelName, productionYear, enginePower;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private List<Make> makes = new ArrayList<>();

    String urlMakes = "http://10.0.2.2:4000/api/catalog/makes/";
    String url = "http://10.0.2.2:4000/api/catalog/models/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getStringExtra(TAG_PAGE_TITLE));
        setContentView(R.layout.acitivity_add_new_model);

        modelName = findViewById(R.id.inputModel);
        productionYear = findViewById(R.id.inputProductionYear);
        enginePower = findViewById(R.id.inputEnginePower);

        makeSpinner = findViewById(R.id.spinnerMake);

        adapter = new MakeAdapter(this,
                android.R.layout.simple_spinner_item,
                makes);
        makeSpinner.setAdapter(adapter);

        getMakes();

        fuelSpinner = findViewById(R.id.spinnerFuel);
        fuelSpinner.setAdapter(new ArrayAdapter<Fuel>(this, android.R.layout.simple_spinner_item, Fuel.values()));

        bodySpinner = findViewById(R.id.spinnerBody);
        bodySpinner.setAdapter(new ArrayAdapter<Body>(this, android.R.layout.simple_spinner_item, Body.values()));

        addModelButton = findViewById(R.id.buttonAddModel);

        modelCreation();

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                selectedMake = adapter.getItem(position);
                // Here you can do the action you want to...
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    private void modelCreation() {
        addModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelCreation model = new ModelCreation(
                    modelName.getText().toString(),
                    Fuel.valueOf(fuelSpinner.getSelectedItem().toString()),
                    Body.valueOf(bodySpinner.getSelectedItem().toString()),
                    Integer.parseInt(productionYear.getText().toString()),
                    Integer.parseInt(enginePower.getText().toString()),
                    (((Make) makeSpinner.getSelectedItem()).getId())
                );

                RequestBody body = RequestBody.create(new Gson().toJson(model), JSON);

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                        .post(body)
                        .build();
                postModelCreation(request);

            }
        });
    }

    public void postModelCreation(Request request) {
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
                JsonObject resultMessage = gson.fromJson(responseBody.string(), JsonObject.class);
                Type type = new TypeToken<APIResultMessageBasic>() {}.getType();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        APIResultMessageBasic resMsg = gson.fromJson(resultMessage, type);
                        final Toast toast = Toast.makeText(getBaseContext(), resMsg.message, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }


    private void getMakes() {
        Request request = new Request.Builder()
                .url(urlMakes)
                .build();
        makes.clear();
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
                System.out.println("SUCCESS");
                Gson gson = new Gson();
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("make").getAsJsonArray();
                Type type = new TypeToken<List<Make>>() {}.getType();
                List<Make> makesList = gson.fromJson(array, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Make make : makesList) {
                                makes.add(make);
                        }
                        adapter.notifyDataSetChanged();
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
}
