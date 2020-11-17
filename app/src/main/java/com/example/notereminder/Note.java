package com.example.notereminder;

public class Note {
    public String id, description, couleur, echeance, priorite;

    public Note(String id, String description, String couleur, String echeance, String priorite) {
        this.id = id;
        this.description = description;
        this.couleur = couleur;
        this.echeance = echeance;
        this.priorite = priorite;
    }
}