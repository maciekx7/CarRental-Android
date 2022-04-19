package com.wpam.carrental.ui.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wpam.carrental.Adapter.TransactionsAdapter;
import com.wpam.carrental.databinding.FragmentTransactionsBinding;
import com.wpam.carrental.globalData.CurrentUser;
import com.wpam.carrental.model.Transaction;

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

public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;
    private List<Transaction> transactionsList = new ArrayList<>();
    private TransactionsAdapter adapter;
    OkHttpClient client = new OkHttpClient();

    public static final String TAG_ID = "id";
    public static final String TAG_PAGE_TITLE = "detail_view_title";



    String url;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(CurrentUser.getInstance().isAdmin()) {
            url = "http://10.0.2.2:4000/api/transactions/all";
        } else {
            url = "http://10.0.2.2:4000/api/transactions/";
        }

        final ListView list = binding.list;

        adapter = new TransactionsAdapter(getContext(), transactionsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)  {
                Intent intent = new Intent(getActivity(), TransactionDetailsActivty.class);
                intent.putExtra(TAG_ID, transactionsList.get(position).getId());
                intent.putExtra(TAG_PAGE_TITLE,
                        transactionsList.get(position).getCar().getCarModel().getMake().getName() + " " + transactionsList.get(position).getCar().getCarModel().getName());
                startActivity(intent);
            }
        });


        getData(url);



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(url);
    }

    private void getData(String url) {
        CurrentUser user = CurrentUser.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-access-token",  user.getToken())
                .build();
        transactionsList.clear();
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
               // System.out.println("TRANSACTIONS-----\n" + responseBody.string());
                JsonArray array = gson.fromJson(responseBody.string(), JsonObject.class).get("transaction").getAsJsonArray();
                Type type = new TypeToken<List<Transaction>>() {}.getType();
                List<Transaction> transactions = gson.fromJson(array, type);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Transaction transaction : transactions) {
                            System.out.println(transaction.toString());
                            transactionsList.add(transaction);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
