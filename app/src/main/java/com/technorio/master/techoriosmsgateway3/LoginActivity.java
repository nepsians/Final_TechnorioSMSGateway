package com.technorio.master.techoriosmsgateway3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technorio.master.techoriosmsgateway3.Main.MainActivity;
import com.technorio.master.techoriosmsgateway3.Utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView sign_in;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.login);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Logging in...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    mProgress.show();
                    doLogin();
                }
            }
        });
    }

    private boolean isValid() {

        if (username.getText().toString().isEmpty()) {
            username.setError("Please enter Username");
            username.requestFocus();
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Please enter Password");
            password.requestFocus();
            return false;
        }

        return true;
    }

    private void doLogin() {
        String url = "http://nepsian.000webhostapp.com/technorio_sms.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                if (response.trim().equals("Success")) {

                    SharedPrefManager.getInstance(getApplicationContext()).setUserStatus(true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                Toast.makeText(LoginActivity.this, "Login Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Username", username.getText().toString().trim());
                params.put("Password", password.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(request);
    }

}

