package com.wpam.carrental.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.R;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.APIResultMessageBasic;
import com.wpam.carrental.model.ApiPostModels.RegisterUser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterUserActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private static final String TAG_PAGE_TITLE = "Register";
    private TextView password, confirmPassword, name, lastname, phone, email;
    private Button confirm;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String url = "http://10.0.2.2:4000/api/auth/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(TAG_PAGE_TITLE);
        setContentView(R.layout.activity_register_user);

        bind();
        confirmInit();
    }

    private void bind() {
        confirmPassword = findViewById(R.id.register_repeate_password);
        password = findViewById(R.id.register_password);
        name = findViewById(R.id.register_name);
        lastname = findViewById(R.id.register_lastname);
        phone = findViewById(R.id.register_phone);
        email = findViewById(R.id.register_email);
        confirm = findViewById(R.id.register_btn);
    }

    private void confirmInit() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(password.getText().toString());
                System.out.println(confirmPassword.getText().toString());

                if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    final Toast toast = Toast.makeText(getBaseContext(), "Passwords must be the same!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(name.getText().toString()) ||
                    TextUtils.isEmpty(lastname.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||
                    TextUtils.isEmpty(phone.getText().toString())) {
                    final Toast toast = Toast.makeText(getBaseContext(), "You have to fill up all data!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                RegisterUser registerUser = new RegisterUser(
                        name.getText().toString(),
                        lastname.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());

                RequestBody body = RequestBody.create(new Gson().toJson(registerUser), JSON);


                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                registerUser(request);
            }

            private void registerUser(Request request) {
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
