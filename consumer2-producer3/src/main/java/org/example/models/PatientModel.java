package org.example.models;

public class PatientModel {
    private String ipp;
    private String nom;
    private String prenom;
    private String naissance;
    private String sexe;

    public PatientModel(String ipp, String nom, String prenom, String naissance, String sexe) {
        this.ipp = ipp;
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = naissance;
        this.sexe = sexe;
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "ipp='" + ipp + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", naissance='" + naissance + '\'' +
                ", sexe='" + sexe + '\'' +
                '}';
    }
}
