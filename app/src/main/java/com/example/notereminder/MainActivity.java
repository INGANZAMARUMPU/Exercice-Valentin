package com.example.notereminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> notes;
    private RecyclerView recycler;
    private AdapterNote adaptateur;
    String reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reminder = getIntent().getStringExtra("reminder");

        recycler = findViewById(R.id.recycler_reminders);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        notes = new ArrayList<>();
        adaptateur = new AdapterNote(this, notes);
        recycler.setAdapter(adaptateur);
        new ItemTouchHelper(movement).attachToRecyclerView(recycler);
        chargerNotes();
    }

    private void chargerNotes() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL + "/list").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", reminder)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray json_array = new JSONObject(json).getJSONArray("data");
                    for (int i=0; i<json_array.length(); i++){
                        JSONObject json_obj = json_array.getJSONObject(i);
                        Note note = new Note(
                                json_obj.getString("id"),
                                json_obj.getString("tache"),
                                json_obj.getString("couleur"),
                                json_obj.getString("echeance"),
                                json_obj.getInt("ordre")
                        );
                        notes.add(note);
                    }
                    MainActivity.this.adaptateur.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_deconnexion) {
            Toast.makeText(this, "deconnexion...", Toast.LENGTH_SHORT).show();

            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL + "/deconnect").newBuilder();
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder().url(url).get().build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
            reminder = "";
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNote(View view) {
        new FormNote(this).show();
    }
    public void removeNote(Note note) {

    }
    public void updateNote(Note note) {
        for(Note n: notes){
            if(n.id.equals(note.id)){
                notes.set(notes.indexOf(n), note);
                break;
            }
        }
        adaptateur.notifyDataSetChanged();
    }
    public void pushNote(Note note) {
        notes.add(0, note);
        adaptateur.notifyDataSetChanged();
    }
    ItemTouchHelper.SimpleCallback movement = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP|ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(notes, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}