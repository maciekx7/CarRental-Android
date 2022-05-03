package com.wpam.carrental.ui.catalog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.Adapter.catalog.MakeAdapter;
import com.wpam.carrental.Adapter.catalog.ModelAdapter;
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.APIResultCarCreation;
import com.wpam.carrental.model.APIResultMessageBasic;
import com.wpam.carrental.model.CarCreation;
import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.Model;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddCarActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private static final String TAG_PAGE_TITLE = "Add new car";

    private File imgFile;
    private Uri uri;
    private Bitmap bitmap;

    private Spinner makeSpinner, modelSpinner;
    private Button addCarButton, deleteMake, deleteModel, addMake, addModel;
    private TextView costText, VINText;

    private ImageButton imageButton;


    private MakeAdapter makeAdapter;
    private ModelAdapter modelAdapter;


    private List<Make> makes = new ArrayList<>();
    private List<Model> models = new ArrayList<>();

    private Make selectedMake;
    private Model selectedModel;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    String urlMakes = "http://10.0.2.2:4000/api/catalog/makes/";
    String urlModels = "http://10.0.2.2:4000/api/catalog/models/";
    String urlTransaction = "http://10.0.2.2:4000/api/catalog/cars/";
    String urlImage = "http://10.0.2.2:4000/upload/jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(TAG_PAGE_TITLE);
        setContentView(R.layout.activity_add_new_car);

        modelSpinner = findViewById(R.id.spinnerModelSelector);
        makeSpinner = findViewById(R.id.spinnerMakeSelector);

        addCarButton = findViewById(R.id.buttonAddCar);
        deleteMake = findViewById(R.id.car_make_delete);
        deleteModel = findViewById(R.id.car_model_delete);
        addMake = findViewById(R.id.car_make_add);
        addModel = findViewById(R.id.car_model_add);

        costText = findViewById(R.id.inputCost);
        VINText = findViewById(R.id.inputVIN);

        imageButton = findViewById(R.id.image_btn);

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

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedModel = modelAdapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        deleteMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(urlMakes + selectedMake.getId());

            }
        });

        deleteModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(urlModels + selectedModel.getId());

            }
        });

        addMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMakeActivity.class);
                startActivity(intent);
            }
        });

        addModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddModelActivity.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        carCreation();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // There are no request codes
                        uri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageButton.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    @Override
    public void onResume() {
        super.onResume();
        getMakes();
    }

    private void showPictureDialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
//        startActivity(Intent.createChooser(intent, "Select Picture"));

    }


    private void getMakes() {
        Request request = new Request.Builder()
                .url(urlMakes)
                .build();
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
                        makes.clear();
                        makes.addAll(makesList);
                        makeAdapter.notifyDataSetChanged();
                        if(!makes.isEmpty()) {
                            selectedMake = makeAdapter.getItem(0);
                            makeSpinner.setSelection(0);
                            getModels();
                        }
                    }
                });
            }
        });
    }


    private void getModels() {
        Request request = new Request.Builder()
                .url(urlModels)
                .build();
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
                            models.clear();
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
                int code = response.code();
                JsonObject resultMessage = gson.fromJson(responseBody.string(), JsonObject.class);
                Type type = new TypeToken<APIResultCarCreation>() {}.getType();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        APIResultCarCreation resMsg = gson.fromJson(resultMessage, type);
                        final Toast toast = Toast.makeText(getBaseContext(), resMsg.message, Toast.LENGTH_LONG);
                        toast.show();
                        if(code == 200 || code == 201) {
                            try {
                                addPhoto(resMsg.getCar().getId());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    public void addPhoto(int carId) throws IOException {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", "car_" + String.valueOf(carId) + ".jpg")  // Upload parameters
                .addFormDataPart(
                        "file",
                        "car_" + String.valueOf(carId) + ".jpg",
                        RequestBody.create(encodedImage, MEDIA_TYPE_PNG)
                ).build();

        Request request = new Request.Builder()
                .url(urlImage)
                .post(req)
                .build();
        sendPhoto(request);
    }

    public void sendPhoto(Request request) {
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
                int code = response.code();
                JsonObject resultMessage = gson.fromJson(responseBody.string(), JsonObject.class);
                Type type = new TypeToken<APIResultMessageBasic>() {}.getType();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        APIResultMessageBasic resMsg = gson.fromJson(resultMessage, type);
                        final Toast toast = Toast.makeText(getBaseContext(), resMsg.message, Toast.LENGTH_LONG);
                        toast.show();
                        if(code == 200 || code == 201) {
                            getMakes();
                        }
                    }
                });
            }
        });
    }



    ////////////DELETE---------------------
    private void delete(String url) {
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
                        if(statusCode >=200 && statusCode<300) {
                            getMakes();
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
