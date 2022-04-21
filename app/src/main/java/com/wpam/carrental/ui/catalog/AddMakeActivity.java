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
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.APIResultMessageBasic;
import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.MakeCreation;

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

public class AddMakeActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private static final String TAG_PAGE_TITLE = "Add new make";

    private Spinner makeSpinner;
    private MakeAdapter adapter;
    private List<Make> makes = new ArrayList<>();

    private Make selectedMake;


    private TextView makeInput;
    private Button addMakeButton, deleteMake;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String url = "http://10.0.2.2:4000/api/catalog/makes/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(TAG_PAGE_TITLE);
        setContentView(R.layout.activity_add_new_make);

        makeInput = findViewById(R.id.inputMake);
        addMakeButton = findViewById(R.id.buttonAddMake);
        deleteMake = findViewById(R.id.del_make);
        if(!CurrentUser.getInstance().isAdmin()) {
            addMakeButton.setVisibility(View.GONE);
        }

        getMakes();

        makeSpinner = findViewById(R.id.existing_makes_spinner);

        adapter = new MakeAdapter(this,
                android.R.layout.simple_spinner_item,
                makes);
        makeSpinner.setAdapter(adapter);

        makeCreation();

        deleteMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMake(url + selectedMake.getId());
            }
        });

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedMake = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        makes.clear();
        getMakes();
    }

    private void makeCreation() {
        addMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeCreation make = new MakeCreation(makeInput.getText().toString());

                RequestBody body = RequestBody.create(new Gson().toJson(make), JSON);

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                        .post(body)
                        .build();
                postMakeCreation(request);
            }
        });

    }

    public void postMakeCreation(Request request) {
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
                .url(url)
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
                        makes.clear();
                        makes.addAll(makesList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }



    private void deleteMake(String url) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("x-access-token",  CurrentUser.getInstance().getToken())
                .build();
        deleteRequest(request);
    }

    private void deleteRequest(Request request) {
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
                final int statusCode = response.code();

                JsonObject resultMessage = gson.fromJson(responseBody.string(), JsonObject.class);
                Type type = new TypeToken<APIResultMessageBasic>() {}.getType();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        APIResultMessageBasic resMsg = gson.fromJson(resultMessage, type);
                        final Toast toast = Toast.makeText(getBaseContext(), resMsg.message, Toast.LENGTH_LONG);
                        toast.show();
                        if((statusCode == 200 || statusCode == 201)) {
                            finish();
                        }
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
