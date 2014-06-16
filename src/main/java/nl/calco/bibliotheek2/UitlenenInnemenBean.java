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
public class UitlenenInnemenBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());
    private Boek boek = null;
    private List<Exemplaar> exemplaren = null;
    private Exemplaar geselecteerdExemplaar = null;

    public Exemplaar getGeselecteerdExemplaar() {
        return geselecteerdExemplaar;
    }

    public void setGeselecteerdExemplaar(Exemplaar geselecteerdExemplaar) {
        this.geselecteerdExemplaar = geselecteerdExemplaar;
    }

    public Boek getBoek() {
        if (boek == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.boek = (Boek) sessionMap.get("boek");
            sessionMap.remove("boek");
        }
        return boek;
    }

    public List<Exemplaar> getExemplaren() {

        if (exemplaren == null) {
            // get boek if null
            if (boek == null) {
                this.getBoek();
            }

            try {
                Database database = new Database();
                exemplaren = database.getExemplaren(boek.getBoek_ID());
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

        }
        return exemplaren;
    }

    public void annuleren() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void innemen() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void uitlenen() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void bewerken() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void verwijderen() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void exemplaarToevoegen() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public void wijzigen() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

    public Medewerker getMedewerker(Integer medewerker_id) {
        Medewerker medewerker = new Medewerker();
        try {
            Database database = new Database();
            medewerker = database.getMedewerker(medewerker_id);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        return medewerker;
    }

    public Medewerker getMedewerker(String medewerker_id) {
        Integer medewerker_id_int = Integer.parseInt(medewerker_id);
        Medewerker medewerker = new Medewerker();
        try {
            Database database = new Database();
            medewerker = database.getMedewerker(medewerker_id_int);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        return medewerker;
    }
}
