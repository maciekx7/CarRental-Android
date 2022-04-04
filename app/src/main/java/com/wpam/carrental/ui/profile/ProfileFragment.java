package com.wpam.carrental.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.wpam.carrental.databinding.FragmentCatalogBinding;
import com.wpam.carrental.databinding.FragmentProfileBinding;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.Car;
import com.wpam.carrental.model.Make;
import com.wpam.carrental.model.Model;
import com.wpam.carrental.model.enums.Body;
import com.wpam.carrental.model.enums.Fuel;

import org.w3c.dom.Text;

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

    OkHttpClient client = new OkHttpClient();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //        CatalogViewModel catalogViewModel =
//                new ViewModelProvider(this).get(CatalogViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView email = binding.inputUsername;
        final TextView password = binding.inputPassword;
        final Button submit = binding.submitButton;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("---------LOGIN_DATA---------\n" + email.getText().toString() + " " + password.getText().toString());
                fetchUser(email.getText().toString(), password.getText().toString());
            }
        });

        return root;
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
                                if(user.isUserLoggedIn()) {
                                    user.setUser(jsonObject);
                                    System.out.println("USER!---\n" + user.toString());
                                    final Toast toast = Toast.makeText(getContext(), "LOGGED IN!", Toast.LENGTH_LONG);
                                    toast.show();
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
}
