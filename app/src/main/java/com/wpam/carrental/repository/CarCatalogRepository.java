package com.wpam.carrental.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.model.Car;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CarCatalogRepository {
    private List<Car> mRepos = new ArrayList<>();
    private Thread mThread;
    OkHttpClient client = new OkHttpClient();


    /**
     * @return mRepo    A MutableLiveData object casted as
     *                  a LiveData object.
     */
    public List<Car> getRepos(){
        return mRepos;
    }

    /**
     * Creates a new background thread and queries the Github API.
     * @param url
     */
    public void searchCarCatalog(final String url){
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                queryCarCatalog(url);
            }
        };

        // Stop the thread if its initialized
        // If the thread is not working interrupt will do nothing
        // If its working it stops the previous work and starts the
        // new runnable
        if (mThread != null){
            mThread.interrupt();
        }
        mThread = new Thread(fetchJsonRunnable);
        mThread.start();
    }

    private void queryCarCatalog(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
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
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("car").getAsJsonArray();
                Type type = new TypeToken<List<Car>>() {}.getType();
                List<Car> cars = gson.fromJson(array, type);

                mRepos = cars;
            }
        });
    }
}
