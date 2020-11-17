package com.example.notereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addNote(View view) {
        new FormNote(this).show();
    }

    public void editNote(Note note) {
    }

    public void removeNote(Note note) {

    }

    public void pushNote(Note note) {

    }
}