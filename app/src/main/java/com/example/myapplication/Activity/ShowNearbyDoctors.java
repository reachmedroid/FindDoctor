package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.API.APIService;
import com.example.myapplication.API.RestApiFactory;
import com.example.myapplication.Adapters.SearchResultViewAdapter;
import com.example.myapplication.Models.DoctorsFeed;
import com.example.myapplication.Models.SearchResultResponse;
import com.example.myapplication.R;
import com.example.myapplication.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowNearbyDoctors  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultViewAdapter recyclerViewAdapter;
    private List<DoctorsFeed> values = new ArrayList<>();
    boolean isLoading = false;
    private ProgressDialog progressBar;
    private ProgressBar bottomProgressBar;
    private String nextDataKey=null;
    private String searchText=null;


    private APIService restApiFactory;
    private Callback<SearchResultResponse> callback = new Callback<SearchResultResponse>() {
        @Override
        public void onResponse(Call<SearchResultResponse> call, Response<SearchResultResponse> response) {
            progressBar.cancel();
            bottomProgressBar.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                Log.e("Response Successful: ", " "+response.body().doctorsList);
                nextDataKey=response.body().nextKey;
                if(response.body().doctorsList.size()>0) {
                    List<DoctorsFeed> body = response.body().doctorsList;
                    for (DoctorsFeed repo : body) {
                        values.add(new DoctorsFeed(repo.getName(), repo.getAddress(),repo.getPhotoId()));
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                    isLoading = false;
                }
                else if(response.body().doctorsList.isEmpty() ){
                Toast.makeText(ShowNearbyDoctors.this,getString(R.string.txt_no_doctors),
                        Toast.LENGTH_LONG).show();
                ShowNearbyDoctors.this.finish();
                }
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
        if(bundle.containsKey(CommonUtils.BUNDEL_SEARCH_TEXT)) {
            startProgressDialogPopUp();
            searchText = bundle.getString(CommonUtils.BUNDEL_SEARCH_TEXT);
            initRestCall(searchText);
            initScrollListener();
        }
    }

    private void initRestCall(String searchText) {
        restApiFactory = RestApiFactory.createService(APIService.class,CommonUtils.AUTH_BEARER,getCurrentAccessToken(), CommonUtils.BASE_URL);
        Call<SearchResultResponse> call =  restApiFactory.findNearestDoctor(searchText);
        call.enqueue(callback);
    }

    private void fetchMoreDoctorsData(String searchText,String lastKey) {

        restApiFactory = RestApiFactory.createService(APIService.class,CommonUtils.AUTH_BEARER,getCurrentAccessToken(), CommonUtils.BASE_URL);
        Call<SearchResultResponse> call =  restApiFactory.fetchMoreDoctorsData(searchText,lastKey);
        call.enqueue(callback);
    }

    private void initAdapter() {
        bottomProgressBar = findViewById(R.id.progressBarLoading);
        recyclerView = findViewById(R.id.rv_doctorResult);
        recyclerViewAdapter = new SearchResultViewAdapter(values);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == values.size() - 1) {
                        if(nextDataKey!=null){
                            bottomProgressBar.setVisibility(View.VISIBLE);
                            fetchMoreDoctorsData(searchText,nextDataKey);
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    private String getCurrentAccessToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(CommonUtils.PREFERENCE_FILE, 0);
        return pref.getString(CommonUtils.SAVED_ACCESS_NAME,"");
    }

    private void startProgressDialogPopUp(){
        progressBar = new ProgressDialog(ShowNearbyDoctors.this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getString(R.string.txt_loading_doctors));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

}
