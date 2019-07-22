package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMyDoctorActivity extends AppCompatActivity {

    @BindView(R.id.btn_search) Button btnSearch;
    @BindView(R.id.searchText) EditText searchDoctorText;

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setUpUIControls();
    }

    private void setUpUIControls() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDoctorText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if(searchDoctorText.getText().toString().trim().length()>3) {
                    findNearbyDoctors(searchDoctorText.getText().toString().trim());
                }
                else {
                    searchDoctorText.setError("Please enter the doctor name");
                }
            }
        });
    }

    private void findNearbyDoctors(String doctorName) {

        Intent intent = new Intent(SearchMyDoctorActivity.this, ShowNearbyDoctors.class);
        Bundle bundle = new Bundle();
        bundle.putString("SEARCH_TEXT", doctorName);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
