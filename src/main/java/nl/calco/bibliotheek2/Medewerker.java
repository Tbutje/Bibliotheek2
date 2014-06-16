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
public class Medewerker implements Serializable {

    private Integer medewerker_ID;
    private String achternaam;
    private String voornaam;
    private String tussenVoegsel;
    private String email;

    public Integer getMedewerker_ID() {
        return medewerker_ID;
    }

    public void setMedewerker_ID(Integer medewerker_ID) {
        this.medewerker_ID = medewerker_ID;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getTussenVoegsel() {
        return tussenVoegsel;
    }

    public void setTussenVoegsel(String tussenVoegsel) {
        this.tussenVoegsel = tussenVoegsel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String fullNaam() {
        if ( tussenVoegsel == null || tussenVoegsel.isEmpty() ) {
            return voornaam + " " + achternaam;
        } else {
            return voornaam + " " + tussenVoegsel + achternaam;
        }
    }

}
