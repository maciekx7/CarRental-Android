package com.wpam.carrental.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.Control;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wpam.carrental.R;
import com.wpam.carrental.databinding.FragmentProfileBinding;
import com.wpam.carrental.globalData.CurrentUser;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    TextView email, emailText, password, passwordText, outTitle;
    TextView inName, inEmail, inTitle, inPhone;
    ConstraintLayout inLayout, outLayout;
    Button loginBtn, editBtn, logoutBtn;

    OkHttpClient client = new OkHttpClient();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindElements();

        if(CurrentUser.getInstance().isUserLoggedIn()) {
            fillLoginUser();
        } else {
            clearLoginUser();
            fillLogoutUser();
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(CurrentUser.getInstance().isUserLoggedIn()) {
            fillLoginUser();
        }
    }





    public void fetchUser(String email, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        String url = "http://10.0.2.2:4000/api/auth/signin";
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("USER!---\nFAILURE");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("CODE: " + response.code());

                if(response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    assert responseBody != null;
                    String jsonObject = responseBody.string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CurrentUser user = CurrentUser.getInstance();
                                if(!user.isUserLoggedIn()) {
                                    user.setUser(jsonObject);
                                    System.out.println("USER!---\n" + user.toString());
                                    final Toast toast = Toast.makeText(getContext(), "LOGGED IN!", Toast.LENGTH_LONG);
                                    toast.show();
                                    BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
                                    navView.getMenu()
                                            .findItem(R.id.navigation_transactions)
                                            .setVisible(true);

                                    fillLoginUser();
                                } else {
                                    final Toast toast = Toast.makeText(getContext(), "ALREADY LOGGED IN!", Toast.LENGTH_LONG);
                                    toast.show();
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Toast toast = Toast.makeText(getContext(), "WRONG DATA!", Toast.LENGTH_LONG);
                            toast.show();

                        }
                    });
                }

            }
        });
    }



    private void fillLoginUser() {
        inName.setText(CurrentUser.getInstance().getName() + " " + CurrentUser.getInstance().getLastname());
        inEmail.setText(CurrentUser.getInstance().getEmail());
        inPhone.setText(CurrentUser.getInstance().getPhone());
        inTitle.setText("HELLO, " + inName.getText());

        enableInVisibility();
        disableOutVisibility();
        clearLoginForm();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillLogoutUser();
                CurrentUser.getInstance().clearUser();
                BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
                navView.getMenu()
                        .findItem(R.id.navigation_transactions)
                        .setVisible(false);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),
                        UserEditActivity.class);
                startActivity(intent);
            }
        });
    }





    private void fillLogoutUser() {
        enableOutVisibility();
        disableInVisibility();
        clearLoginUser();

        outTitle.setText("Login here");
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("---------LOGIN_DATA---------\n" + email.getText().toString() + " " + password.getText().toString());
                fetchUser(email.getText().toString(), password.getText().toString());
                clearLoginForm();
            }
        });
    }

    private void bindElements() {
        inLayout = binding.inConstraint;
        outLayout = binding.outConstraint;

        inTitle = binding.inTitle;
        outTitle = binding.outTitle;

        email = binding.inputUsername;
        emailText = binding.username;
        password = binding.inputPassword;
        passwordText = binding.password;

        inEmail = binding.inEmail;
        inPhone = binding.inPhone;
        inName = binding.inUsername;

        loginBtn = binding.loginButton;
        logoutBtn = binding.logoutButton;
        editBtn = binding.editButton;
    }

    public void disableInVisibility() {
        inLayout.setVisibility(View.GONE);

        inName.setVisibility(View.GONE);
        inEmail.setVisibility(View.GONE);
        inPhone.setVisibility(View.GONE);
        inTitle.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.GONE);

        inLayout.setVisibility(View.GONE);
    }

    public void enableInVisibility() {
        inLayout.setVisibility(View.VISIBLE);

        inName.setVisibility(View.VISIBLE);
        inEmail.setVisibility(View.VISIBLE);
        inPhone.setVisibility(View.VISIBLE);
        inTitle.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.VISIBLE);

        inLayout.setVisibility(View.VISIBLE);
    }

    public void disableOutVisibility() {
        outLayout.setVisibility(View.GONE);

        emailText.setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        passwordText.setVisibility(View.GONE);
        password.setVisibility(View.GONE);

        outTitle.setVisibility(View.GONE);

    }

    public void enableOutVisibility() {
        outLayout.setVisibility(View.VISIBLE);

        emailText.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);

        passwordText.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);

        outTitle.setVisibility(View.VISIBLE);
    }

    private void clearLoginUser() {
        inName.setText("");
        inEmail.setText("");
        inPhone.setText("");
        inTitle.setText("");
    }

    private void clearLoginForm() {
        password.setText("");
        email.setText("");
    }

}
