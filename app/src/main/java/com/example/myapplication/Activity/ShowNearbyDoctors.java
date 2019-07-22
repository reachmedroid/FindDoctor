package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.API.APIService;
import com.example.myapplication.API.RestApiFactory;
import com.example.myapplication.Adapters.SearchResultViewAdapter;
import com.example.myapplication.Models.DoctorsFeed;
import com.example.myapplication.Models.PaginationItem;
import com.example.myapplication.Models.SearchResultResponse;
import com.example.myapplication.R;
import com.example.myapplication.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowNearbyDoctors  extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchResultViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private List<PaginationItem> values = new ArrayList<>();
    boolean isLoading = false;
    private ProgressDialog progressBar;


    private APIService restApiFactory;
    private Callback<SearchResultResponse> callback = new Callback<SearchResultResponse>() {
        @Override
        public void onResponse(Call<SearchResultResponse> call, Response<SearchResultResponse> response) {
            progressBar.cancel();
            if (response.isSuccessful()) {

                Log.e("Response Successful: ", " "+response.body().doctorsList);

                List<DoctorsFeed> body = response.body().doctorsList;
                for (DoctorsFeed repo : body) {
                    Log.e("Doctor Name ",""+repo.getName());
                    //values.add(new PaginationItem(repo.getId(), repo.getName()));
                    rowsArrayList.add(repo.getName());
                }
                recyclerViewAdapter.notifyDataSetChanged();
                fetchReposNextPage(response);
            } else {
                Log.e("Request failed: ", " "+response);
            }
        }

        @Override
        public void onFailure(Call<SearchResultResponse> call, Throwable t) {
            progressBar.cancel();
            Log.e("Error fetching repos", t.getMessage());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle bundle = getIntent().getExtras();
        initAdapter();
        if(bundle.containsKey("SEARCH_TEXT")) {
            startProgressDialogPopUp();
            String searchText = bundle.getString("SEARCH_TEXT");
            initRestCall(searchText);
            initScrollListener();
        }
    }

    private void initRestCall(String searchText) {

        restApiFactory = RestApiFactory.createService(APIService.class,CommonUtils.AUTH_BEARER,getCurrentAccessToken(), CommonUtils.BASE_URL);
        Call<SearchResultResponse> call =  restApiFactory.findNearestDoctor();
        call.enqueue(callback);
    }


    private void initAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new SearchResultViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private String getCurrentAccessToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ACCESS_PREFERENCE", 0);
        return pref.getString("access_token","");
    }

    private void startProgressDialogPopUp(){
        progressBar = new ProgressDialog(ShowNearbyDoctors.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading nearby doctors ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
    private void fetchReposNextPage(Response<SearchResultResponse> response)
    {
        /*GitHubPagelinksUtils pagelinksUtils =
                new GitHubPagelinksUtils(response.headers());
        String next = pagelinksUtils.getNext();*/

        //Log.d("Header", response.headers().get("Link"));

        /*if (TextUtils.isEmpty(next)) {
            return; // nothing to do
        }*/

        /*Call<List<DoctorsFeed>> call = restApiFactory.reposForUserPaginate(next);
        call.enqueue(callback);*/
    }
}
