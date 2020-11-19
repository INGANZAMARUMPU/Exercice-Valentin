package com.example.notereminder;

import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormNote extends Dialog {
    private MainActivity context;
    private EditText field_description;
    private Button btn_cancel, btn_submit, btn_delete;
    private DatePicker date_echeance;
    SeekBar seek_green, seek_red, seek_blue, seek_priorite;
    TextView colors_preview, lbl_priorite;
    private Note note;
    private boolean edition = false;
    MutableLiveData<Integer> int_rouge = new MutableLiveData<>();
    MutableLiveData<Integer> int_vert = new MutableLiveData<>();
    MutableLiveData<Integer> int_bleu = new MutableLiveData<>();
    MutableLiveData<Integer> int_priorite = new MutableLiveData<>();

    public FormNote(MainActivity context) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_note);
        this.context = context;

        field_description = findViewById(R.id.field_description);
        seek_green = findViewById(R.id.seek_green);
        seek_red = findViewById(R.id.seek_red);
        seek_blue = findViewById(R.id.seek_blue);
        colors_preview = findViewById(R.id.colors_preview);
        lbl_priorite = findViewById(R.id.lbl_priorite);
        seek_priorite = findViewById(R.id.seek_priorite);
        date_echeance = findViewById(R.id.date_echeance);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.btn_delete);
        btn_submit = findViewById(R.id.btn_submit);

        int_vert.setValue(0); int_bleu.setValue(0); int_rouge.setValue(0); int_priorite.setValue(0);
        initAll(); watchColors();
        seek_green.setProgress((int) (Math.random()*255));
        seek_blue.setProgress((int) (Math.random()*255));
        seek_red.setProgress((int) (Math.random()*255));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edition) edit(); else add();
            }
        });
    }

    private void watchColors() {
        int_bleu.observe(context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                colors_preview.setBackgroundColor(
                        Color.rgb(int_rouge.getValue(), int_vert.getValue(), int_bleu.getValue())
                );
            }
        });
        int_rouge.observe(context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                colors_preview.setBackgroundColor(
                        Color.rgb(int_rouge.getValue(), int_vert.getValue(), int_bleu.getValue())
                );
            }
        });
        int_vert.observe(context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                colors_preview.setBackgroundColor(
                        Color.rgb(int_rouge.getValue(), int_vert.getValue(), int_bleu.getValue())
                );
            }
        });
        int_priorite.observe(context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                integer = integer+1;
                lbl_priorite.setText(integer.toString());
            }
        });
    }
    private void initAll() {
        seek_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int_bleu.setValue(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seek_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int_rouge.setValue(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seek_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int_vert.setValue(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seek_priorite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int_priorite.setValue(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void add() {
        final Note note = new Note(
                "0", field_description.getText().toString(), getHexColor(),
                Host.getDate(date_echeance), seek_priorite.getProgress()
        );
        RequestBody body = new FormBody.Builder()
                .add("texte", note.description)
                .add("e", note.echeance)
                .add("o", note.priorite.toString())
                .add("c", note.couleur)
                .build();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL + "/add").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", context.reminder)
                .post(body)
                .build();
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
                    JSONObject json_obj = new JSONObject(json).getJSONObject("data");
                    note.id = json_obj.getString("id");
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            context.pushNote(note);
                        }
                    });
                    FormNote.this.dismiss();
                } catch (JSONException e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Ajout échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void edit() {
        final Note new_note = new Note(
                "0", field_description.getText().toString(), getHexColor(),
                Host.getDate(date_echeance), seek_priorite.getProgress()
        );
        RequestBody body = new FormBody.Builder()
                .add("texte", new_note.description)
                .add("e", new_note.echeance)
                .add("o", new_note.priorite.toString())
                .add("c", new_note.couleur)
                .build();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL + "/change/"+note.id).newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", context.reminder)
                .post(body)
                .build();
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
                    JSONObject json_obj = new JSONObject(json).getJSONObject("data");
                    new_note.id = json_obj.getString("id");
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            context.updateNote(new_note);
                        }
                    });
                    FormNote.this.dismiss();
                } catch (JSONException e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Edition échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void delete() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL + "/remove/"+note.id).newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .addHeader("Cookie", context.reminder)
                .url(url).get().build();
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
                if (!json.trim().isEmpty()){
                    try {
                        JSONObject j = new JSONObject(json);
                        if(j.getString("code").trim().equals("200")) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    context.removeNote(note);
                                    Toast.makeText(context, "Suppression éffectuée", Toast.LENGTH_SHORT).show();
                                }
                            });
                            FormNote.this.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String getHexColor() {
        return "#" +
            String.format("%02X", seek_red.getProgress())+
            String.format("%02X", seek_green.getProgress()) +
            String.format("%02X", seek_blue.getProgress());
    }

    public void setColor(String color) {
        color = color.replace("#","");
        seek_red.setProgress(Integer.decode("0x"+color.subSequence(0,2)));
        seek_green.setProgress(Integer.decode("0x"+color.subSequence(2,4)));
        seek_blue.setProgress(Integer.decode("0x"+color.subSequence(4,6)));
    }

    public void setEdition(Note note) {
        edition = true;
        this.note = note;
        setColor(note.couleur);
        field_description.setText(note.description);
        btn_delete.setVisibility(View.VISIBLE);
    }
}