package com.mdgtaxi.dto;

public class StatusObjectDto {
    private Long id;
    private String libelle;
    private int score;

    public StatusObjectDto(Long id, String libelle, int score) {
        this.id = id;
        this.libelle = libelle;
        this.score = score;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
