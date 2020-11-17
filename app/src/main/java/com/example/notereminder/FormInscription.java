package com.example.notereminder;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FormInscription extends Dialog {

    AppCompatActivity context;
    private TextView field_username, field_password, field_no_inscription;
    private String username, password, no_inscription;
    private Button btn_product_cancel, btn_product_submit;

    public FormInscription(AppCompatActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_inscription);
        this.context = context;

        field_username = findViewById(R.id.field_username);
        field_password = findViewById(R.id.field_password);
        field_no_inscription = findViewById(R.id.field_no_inscription);

        btn_product_cancel = findViewById(R.id.btn_inscr_cancel);
        btn_product_submit = findViewById(R.id.btn_inscr_submit);

        btn_product_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        btn_product_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void submit(){
        if(validateFields()) {
            String username = field_username.getText().toString(),
                    password = field_password.getText().toString(),
                    inscription = field_no_inscription.getText().toString();

            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(
                    Host.URL + "/register/"+username+"/"+password+"/"+inscription)
                    .newBuilder();
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder().url(url).get().build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        final JSONObject j = new JSONObject(json);
                        if(j.getString("code").trim().equals("200")) {
                            dismiss();
                        } else {
                            final String message = j.getString("message");
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }
    private Boolean validateFields() {
        username = field_username.getText().toString().trim();
        password = field_password.getText().toString().trim();
        no_inscription = field_no_inscription.getText().toString().trim();
        if(username.isEmpty()){
            field_username.setError("vous devez completer ici");
            return false;
        }
        if(password.isEmpty()){
            field_password.setError("vous devez completer ici");
            return false;
        }
        if(no_inscription.isEmpty()){
            field_password.setError("vous devez completer ici");
            return false;
        }
        return true;
    }
}
