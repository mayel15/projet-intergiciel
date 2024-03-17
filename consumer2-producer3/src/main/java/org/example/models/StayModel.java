package org.example.models;

public class StayModel {
    private String personId;
    private String numeroSejour;
    private String dateDebut;
    private String dateFin;

    public StayModel(String personId, String numeroSejour, String dateDebut, String dateFin) {
        this.personId = personId;
        this.numeroSejour = numeroSejour;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "StayModel{" +
                "personId='" + personId + '\'' +
                ", numeroSejour='" + numeroSejour + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                '}';
    }
}
