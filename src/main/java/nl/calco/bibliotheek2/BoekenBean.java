/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class BoekenBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private List<Boek> boeken = null;
    private String filter = "";
    private Boek geselecteerdBoek;

    public Boek getGeselecteerdBoek() {
        return geselecteerdBoek;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Boek> getBoeken() {
        if (boeken == null) {
            try {
                Database database = new Database();
                this.boeken = database.getBoeken(this.filter);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
        }
        return this.boeken;
    }

    public void reloadBoeken() {
        try {
            Database database = new Database();
            this.boeken = database.getBoeken(this.filter);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }
    }

    public void resetBoeken() {
        this.filter = "";
        //     this.boeken = null;
        this.geselecteerdBoek = null;
       this.reloadBoeken();
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public String uitlenenInnemen() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("boek", this.geselecteerdBoek);
        return "uitleneninnemen";
    }

    // voeg een exemplaar toe aan het geselecteerde boek
    public String exemplaarToevoegen() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("boek", this.geselecteerdBoek);
        return "exemplaartoevoegen";
    }

    public String titelToevoegen() {
        return "titeltoevoegen";
    }

    public String categorien() {
        return "categorieen";
    }

    public void overzichten() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Dit is nog niet geimplementeerd"));
    }

    public String medewerkers() {
        return "medewerkers";
    }

    // deze functie is om een boek te selecteren
    public void selecteer(Integer boek_ID) {
        // gebruik dit om een omschrijving te selecteren

        for (Boek boek : boeken) {
            if (boek.getBoek_ID().equals(boek_ID)) {
                this.geselecteerdBoek = boek;
                break;
            }
        }
    }

}
