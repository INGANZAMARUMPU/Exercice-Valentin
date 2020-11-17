package com.example.notereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText field_login_username, field_login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        field_login_username = findViewById(R.id.field_login_username);
        field_login_password = findViewById(R.id.field_login_password);
    }

    public void performLogin(View view) {
        Toast.makeText(LoginActivity.this, "connexion...", Toast.LENGTH_SHORT).show();
        String username = field_login_username.getText().toString(),
            password = field_login_password.getText().toString();

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(
                Host.URL + "/connect/"+username+"/"+password)
                .newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject j = new JSONObject(json);
                    if(j.getString("code").trim().equals("200")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,
                                        "les infos semblent incorrect", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void showInscription(View view) {
        new FormInscription(this).show();
    }
}