package com.wpam.carrental.globalData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.model.Car;
import com.wpam.carrental.model.enums.Role;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CurrentUser {

    private static CurrentUser INSTANCE;
    private User user;


    private CurrentUser() { }

    public static CurrentUser getInstance() {
        if (INSTANCE == null)
            synchronized (CurrentUser.class) {
                if (INSTANCE == null)
                    INSTANCE = new CurrentUser();
            }
        return INSTANCE;
    }

    public boolean isUserLoggedIn() {
        return !Objects.isNull(user);
    }

    public String getEmail() {
        return user.email;
    }

    public String getName() {
        return user.name;
    }

    public String getLastname() {
        return user.lastName;
    }

    public Role getRole() {
        return user.role;
    }

    public String getToken() {
        return user.accessToken;
    }

    public boolean isUserSet() {
        return Objects.isNull(user);
    }

    public CurrentUser setUser(String responseBody) throws IOException {
        Gson gson = new Gson();
        if(Objects.isNull(user)) {
            JsonObject jsonUser = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject();
            Type type = new TypeToken<User>() {}.getType();
            user = gson.fromJson(jsonUser, type);
        }
        return this;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                '}';
    }
}