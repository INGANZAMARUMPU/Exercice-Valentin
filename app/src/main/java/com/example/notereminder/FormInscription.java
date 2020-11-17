package com.example.notereminder;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
