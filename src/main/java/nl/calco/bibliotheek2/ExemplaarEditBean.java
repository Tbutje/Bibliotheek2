/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class ExemplaarEditBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private String exemplaar_aantal = null;
    private String exemplaar_aantal_oud = null;
    private List<Exemplaar> exemplaren = null;
    private Boek boek = null;

    public void setExemplaar_aantal(String exemplaar_aantal) {
        this.exemplaar_aantal = exemplaar_aantal;
    }

    // lazy getter
    public String getExemplaar_aantal() {
        if (exemplaar_aantal == null) {
            try {
                Database database = new Database();
                exemplaren = database.getExemplaren(boek.getBoek_ID());
                // get last row of exemplaren to get highest number
                exemplaar_aantal = exemplaren.get(exemplaren.size() - 1).getExemplaarVolgnummer().toString();
                exemplaar_aantal_oud = exemplaar_aantal;

            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

        }

        return exemplaar_aantal;
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

    public String exemplaarToevoegen() {

        FacesContext context = FacesContext.getCurrentInstance();

        // check eerst of er iets veranderd is
        if (!exemplaar_aantal.equals(exemplaar_aantal_oud)) {
            //check if het nieuwe getal niet lager is

            // waarde is veranderd en niet klijner. doorvoerne dus
            try {
                // check niet negatief
                if (Integer.parseInt(exemplaar_aantal) < Integer.parseInt(exemplaar_aantal_oud)) {
                    context.addMessage(null, new FacesMessage("Nieuwe waarde moet groter zijn dan oude waarde"));
                } else {
                    //alles in orde. toevoegen die handel
                    try {
                        Database database = new Database();
                        database.insertExemplaar(boek.getBoek_ID(),
                                Integer.parseInt(exemplaar_aantal_oud) + 1,
                                Integer.parseInt(exemplaar_aantal) - Integer.parseInt(exemplaar_aantal_oud)
                        );
                        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                        sessionMap.remove("boek");
                        return "naarboeken";
                    } catch (SQLException | NamingException ex) {
                        LOGGER.log(Level.SEVERE, "Error {0}", ex);
                        System.out.println(ex.getMessage());
                    }
                }

            } catch (NumberFormatException ex) {
                //          FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Aantal exemplaren moet een positief heel getal zijn, geen text of float"));
            }
        }
        return null;

    }

    public String annuleren() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.remove("boek");
        return "naarboeken";
    }

}
