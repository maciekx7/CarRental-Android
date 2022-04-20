package com.wpam.carrental.ui.catalog;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.wpam.carrental.Adapter.catalog.ModelAdapter;
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.APIResultMessageBasic;
import com.wpam.carrental.model.CarCreation;
import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.Model;
import com.wpam.carrental.model.enums.Body;
import com.wpam.carrental.model.enums.Fuel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddCarActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private static final String TAG_PAGE_TITLE = "Ad new car";

    private Spinner makeSpinner, modelSpinner;
    private Button addCarButton;
    private TextView costText, VINText;

    private MakeAdapter makeAdapter;
    private ModelAdapter modelAdapter;


    private List<Make> makes = new ArrayList<>();
    private List<Model> models = new ArrayList<>();

    private Make selectedMake;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    String urlMakes = "http://10.0.2.2:4000/api/catalog/makes/";
    String urlModels = "http://10.0.2.2:4000/api/catalog/models/";
    String urlTransaction = "http://10.0.2.2:4000/api/catalog/cars/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getStringExtra(TAG_PAGE_TITLE));
        setContentView(R.layout.activity_add_new_car);

        modelSpinner = findViewById(R.id.spinnerModelSelector);
        makeSpinner = findViewById(R.id.spinnerMakeSelector);

        addCarButton = findViewById(R.id.buttonAddCar);

        costText = findViewById(R.id.inputCost);
        VINText = findViewById(R.id.inputVIN);

        makeAdapter = new MakeAdapter(this,
                android.R.layout.simple_spinner_item,
                makes);
        makeSpinner.setAdapter(makeAdapter);

        modelAdapter = new ModelAdapter(this,
                android.R.layout.simple_spinner_item,
                models);
        modelSpinner.setAdapter(modelAdapter);

        getMakes();

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedMake = makeAdapter.getItem(position);
                getModels();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        carCreation();

    }

    private void getMakes() {
        Request request = new Request.Builder()
                .url(urlMakes)
                .build();
        makes.clear();
        fetchMakes(request);
    }

    private void fetchMakes(Request request) {
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
                        makes.addAll(makesList);
                        makeAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    private void getModels() {
        Request request = new Request.Builder()
                .url(urlModels)
                .build();
        models.clear();
        fetchModels(request);
    }

    private void fetchModels(Request request) {
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
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("model").getAsJsonArray();
                Type type = new TypeToken<List<Model>>() {}.getType();
                List<Model> modelsList = gson.fromJson(array, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List newModelsList;
                        newModelsList = modelsList.stream()
                                .filter(o -> o.getMakeId() == selectedMake.getId())
                                .collect(Collectors.toList());
                        models.addAll(newModelsList);
                        modelAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    private void carCreation() {
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarCreation model = new CarCreation(
                        Integer.parseInt(costText.getText().toString()),
                        Long.parseLong(VINText.getText().toString()),
                        true,
                        (((Model) modelSpinner.getSelectedItem()).getId())
                );

                RequestBody body = RequestBody.create(new Gson().toJson(model), JSON);

                Request request = new Request.Builder()
                        .url(urlTransaction)
                        .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                        .post(body)
                        .build();
                postCarCreation(request);

            }
        });
    }

    public void postCarCreation(Request request) {
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
