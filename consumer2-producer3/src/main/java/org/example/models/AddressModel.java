package org.example.models;

public class AddressModel {
    private String personId;
    private String address;
    private String ville;
    private String codePostale;
    private String pays;

    public AddressModel(String personId, String address, String ville, String codePostale, String pays) {
        this.personId = personId;
        this.address = address;
        this.ville = ville;
        this.codePostale = codePostale;
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "personId='" + personId + '\'' +
                ", address='" + address + '\'' +
                ", ville='" + ville + '\'' +
                ", codePostale='" + codePostale + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }
}
