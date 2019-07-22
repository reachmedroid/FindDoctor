package com.example.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.API.APIService;
import com.example.myapplication.API.RestApiFactory;
import com.example.myapplication.Models.LoginResponse;
import com.example.myapplication.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This AuthenticationActivity is a responsible for authentication
 * of the username and password(though hardcoded) and internally used to
 * get the access token for further API interaction
 *
 */

public class AuthenticationActivity extends AppCompatActivity {
    @BindView(R.id.btn_login) Button btnLogin;

    private Callback<LoginResponse> callback;

    {
        callback = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {

                    Log.e("Response Successful: ", " " + response.body().access_token);
                    Log.e("Response Successful: ", " " + response.body().token_type);
                    saveAccessTokenPreference(response.body().access_token);
                    navigateToSearchDoctor();
                } else {
                    Log.e("Request failed: ", " " + response);
                    Toast.makeText(AuthenticationActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Error fetching repos", t.getMessage());
                Toast.makeText(AuthenticationActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void saveAccessTokenPreference(String accessToken) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ACCESS_PREFERENCE", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("access_token", accessToken);
        editor.commit();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUsernamePassword();
            }
        });

    }

    /**
     * Finish up the login activity & move to search doctor activity
     */
    private void navigateToSearchDoctor() {
        Intent intent = new Intent(AuthenticationActivity.this, SearchMyDoctorActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Responsible for authenticating the user
     * taking input of username & password.
     */

    private void authenticateUsernamePassword() {
        APIService restApiFactory  = RestApiFactory.createService(APIService.class,"Basic", "aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==");
        Call<LoginResponse> call =  restApiFactory.authenticateUser("androidChallenge@vivy.com","88888888");
        call.enqueue(callback);
    }
}
