package com.wpam.carrental.globalData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.model.enums.Role;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

public class CurrentUser {

    private static CurrentUser INSTANCE;
    private User user;
    private boolean loggedIn = false;


    private CurrentUser() { }

    public static CurrentUser getInstance() {
        if (INSTANCE == null)
            synchronized (CurrentUser.class) {
                if (INSTANCE == null)
                    INSTANCE = new CurrentUser();
            }
        return INSTANCE;
    }

    public void updateData(String name, String lastname, String phone) {
        this.user.name = name;
        this.user.lastName = lastname;
        this.user.phone = phone;
    }

    public boolean isUserLoggedIn() {
        return loggedIn;
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

    public String getPhone() {return user.phone; }

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
        this.loggedIn = true;
        return this;
    }

    public boolean isAdmin() {
        if(this.isUserLoggedIn()) {
            return user.role.equals(Role.admin);
        }
        return false;
    }

    public boolean isUser() {
        if(this.isUserLoggedIn()) {
            return user.role.equals(Role.user);
        }
        return false;
    }

    public CurrentUser clearUser() {
        user = null;
        loggedIn = false;
        return this;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                '}';
    }
}