package com.example.notereminder;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder> {

    ArrayList<Note> notes;

    public AdapterNote(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Note note = notes.get(position);
        holder.lbl_description.setText(note.description);
        holder.lbl_echeance.setText(note.echeance);
        holder.lbl_priorite.setText(note.strPriorite());
        holder.view.setCardBackgroundColor(Color.parseColor(note.couleur));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_description, lbl_echeance, lbl_priorite;
        CardView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (CardView) itemView;
            lbl_description = itemView.findViewById(R.id.lbl_description);
            lbl_echeance = itemView.findViewById(R.id.lbl_echeance);
            lbl_priorite = itemView.findViewById(R.id.lbl_priorite);
        }
    }
}
