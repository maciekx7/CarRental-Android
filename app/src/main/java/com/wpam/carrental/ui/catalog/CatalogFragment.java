package com.wpam.carrental.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.Adapter.catalog.CatalogAdapter;
import com.wpam.carrental.databinding.FragmentCatalogBinding;
import com.wpam.carrental.model.Car;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CatalogFragment extends Fragment {
    private FragmentCatalogBinding binding;
    String url = "http://10.0.2.2:4000/api/catalog/cars";
    OkHttpClient client = new OkHttpClient();
    Button filterButton, carButton, modelButton, makeButton;

    public static final String TAG_ID = "id";
    public static final String TAG_PAGE_TITLE = "detail_view_title";



    private CatalogAdapter adapter;
    private List<Car> carsList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //        CatalogViewModel catalogViewModel =
//                new ViewModelProvider(this).get(CatalogViewModel.class);

        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filterButton = binding.filterButton;
        carButton = binding.addCarButton;
        modelButton = binding.addModelButton;
        makeButton = binding.addMakeButton;
        final ListView list = binding.list;
        adapter = new CatalogAdapter(getContext(), carsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)  {
                Intent intent = new Intent(getActivity(), CarDetailsActivity.class);
                intent.putExtra(TAG_ID, carsList.get(position).getId());
                intent.putExtra(TAG_PAGE_TITLE,
                        carsList.get(position).getCarModel().getMake().getName() + " " + carsList.get(position).getCarModel().getName());
                startActivity(intent);
            }
        });

        getData(url);
        setButtonAction();

        return root;
    }

    private void setButtonAction() {
        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMakeActivity.class);
                startActivity(intent);
            }
        });

        modelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddModelActivity.class);
                startActivity(intent);
            }
        });

        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCarActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getData(url);
    }

    private void getData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        carsList.clear();
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
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("car").getAsJsonArray();
                Type type = new TypeToken<List<Car>>() {}.getType();
                List<Car> cars = gson.fromJson(array, type);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Car car : cars) {
                            System.out.println(car.toString());
                            if(car.isAvailable()) {
                                carsList.add(car);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

//    public void fetchUser(String email, String password) {
//        RequestBody formBody = new FormBody.Builder()
//                .add("email", email)
//                .add("password", password)
//                .build();
//
//        String url = "http://10.0.2.2:4000/api/auth/signin";
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                System.out.println("USER!---\nFAILURE");
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                ResponseBody responseBody = response.body();
//                assert responseBody != null;
//                String jsonObject = responseBody.string();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            CurrentUser user = CurrentUser.getInstance().setUser(jsonObject);
//                            System.out.println("USER!---\n" + user.toString());
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//    }


}
