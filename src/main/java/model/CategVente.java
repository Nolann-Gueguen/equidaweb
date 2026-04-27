package model;

public class CategVente {

    // Correction : code est un varchar(10) dans la BDD, donc String et non int
    private String code;
    private String libelle;

    public CategVente() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}