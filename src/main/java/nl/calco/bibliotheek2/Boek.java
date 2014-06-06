/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.calco.bibliotheek2;

import java.io.Serializable;


/**
 *
 * @author TKoole
 */
public class Boek implements Serializable{
    private Integer boek_ID;
    private String boekNummer;
    private String titel;
    private String auteur;
    private String uitgeverij;
    private String isbn;
    private String locatie;
    private Integer categorie_ID;
    private String categorieOmschrijving;

    public String getCategorieOmschrijving() {
        return categorieOmschrijving;
    }

    public void setCategorieOmschrijving(String categorieOmschrijving) {
        this.categorieOmschrijving = categorieOmschrijving;
    }


    public Integer getBoek_ID() {
        return boek_ID;
    }

    public void setBoek_ID(Integer boek_ID) {
        this.boek_ID = boek_ID;
    }

    public String getBoekNummer() {
        return boekNummer;
    }

    public void setBoekNummer(String boekNummer) {
        this.boekNummer = boekNummer;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getUitgeverij() {
        return uitgeverij;
    }

    public void setUitgeverij(String uitgeverij) {
        this.uitgeverij = uitgeverij;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public Integer getCategorie_ID() {
        return categorie_ID;
    }

    public void setCategorie_ID(Integer categorie_ID) {
        this.categorie_ID = categorie_ID;
    }
    
}
