package com.example.notereminder;

public class Note {
    public String id, description, couleur, echeance;
    Integer priorite;

    public Note(String id, String description, String couleur, String echeance, Integer priorite) {
        this.id = id;
        this.description = description;
        this.couleur = couleur;
        this.echeance = echeance;
        this.priorite = priorite;
    }

    public String strPriorite(){
        String str = "";
        switch (priorite){
            case 0:
                str = "trop bas";
                break;
            case 1:
                str = "bas";
                break;
            case 2:
                str = "moyenne";
                break;
            case 3:
                str = "elevée";
                break;
            case 4:
                str = "trop élevee";
                break;
        }
        return "priorité " + str;
    }
}