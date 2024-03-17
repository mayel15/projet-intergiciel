package org.example.models;

public class MovementModel {
    private String personId;
    private String numeroSejour;
    private String service;
    private String dateMouvement;

    public MovementModel(String personId, String numeroSejour, String service, String dateMouvement) {
        this.personId = personId;
        this.numeroSejour = numeroSejour;
        this.service = service;
        this.dateMouvement = dateMouvement;
    }

    @Override
    public String toString() {
        return "MovementModel{" +
                "personId='" + personId + '\'' +
                ", numeroSejour='" + numeroSejour + '\'' +
                ", service='" + service + '\'' +
                ", dateMouvement='" + dateMouvement + '\'' +
                '}';
    }
}
