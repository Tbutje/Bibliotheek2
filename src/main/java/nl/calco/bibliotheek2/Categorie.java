/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.calco.bibliotheek2;

/**
 *
 * @author TKoole
 */
public class Categorie {
    private Integer categorieID;
    private String omschrijving;

    public Integer getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Integer categorieID) {
        this.categorieID = categorieID;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
       omschrijving = omschrijving.trim();
               
       // check if leeg. zo ja dan maak m null
        if (omschrijving.isEmpty()){
           this.omschrijving = null;
       } else{
        this.omschrijving = omschrijving;
       }
    }
}
