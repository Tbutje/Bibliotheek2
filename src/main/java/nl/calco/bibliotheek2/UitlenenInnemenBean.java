/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
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


    
    public void selecteerExemplaar(Integer exemplaar_id){
         try {
                Database database = new Database();
                this.geselecteerdExemplaar = database.getExemplaar(exemplaar_id);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
    }

    public Boek getBoek() {
        if (boek == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.boek = (Boek) sessionMap.get("boek");
            //         sessionMap.remove("boek");
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

    public String annuleren() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

        sessionMap.remove("boek");
        return "naarboeken";
    }

    public void innemen() {
        Uitlening uitlening = new Uitlening();
        uitlening.setExemplaar_ID(geselecteerdExemplaar.getExemplaar_ID());
        uitlening.setMedewerker_ID(geselecteerdExemplaar.getHuidigeUitlening().getMedewerker_ID());
        uitlening.setDatumUitleen(geselecteerdExemplaar.getHuidigeUitlening().getDatumUitleen());
        uitlening.setDatumInleveren(LocalDate.now());

        try {
            Database database = new Database();
            database.updateUitlening(uitlening);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }
        geselecteerdExemplaar.refresh();
        //  refresh();
    }

    public String uitlenen() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("exemplaar", this.geselecteerdExemplaar);
        return "naaruitlenen";
    }

    public String bewerken() {
        // dit wijzigt boek + exemplaar
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("exemplaar", this.geselecteerdExemplaar);
        sessionMap.put("boek", this.boek);
        sessionMap.put("vanwaar", "vanuitlenen");
        return "boekedit";
    }

    public void verwijderen() {
        // verwijderen eerst uitleenhistory

        try {
            Integer exemplaar_id = this.geselecteerdExemplaar.getExemplaar_ID();

            Database database = new Database();
            database.verwijderUitleningen(exemplaar_id);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        //verwijder dan exemplaar
        try {
            Database database = new Database();
            database.verwijderExemplaar(geselecteerdExemplaar);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        //refresh gegevens
        geselecteerdExemplaar.refresh();
        this.refresh();
    }

    public void exemplaarToevoegen() {
        // voeg 1 exemplaar toe

        try {
            Database database = new Database();
            List<Exemplaar> exemplaren_tmp = database.getExemplaren(boek.getBoek_ID());
            Integer exemplaar_aantal = Integer.parseInt(exemplaren_tmp.get(exemplaren_tmp.size() - 1).getExemplaarVolgnummer().toString());

            // vang af als er nog geen exemplaren zijn
            if (exemplaar_aantal < 1) {
                database.insertExemplaar(boek.getBoek_ID(), 1);
            } else {
                database.insertExemplaar(boek.getBoek_ID(),
                        exemplaar_aantal + 1,
                        1);
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        //en refresh als niet leeg is
        if (geselecteerdExemplaar != null) {
            geselecteerdExemplaar.refresh();
        }
        this.refresh();

    }

    public String wijzigen() {
        // dit wijzigt boek
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("boek", this.boek);
        sessionMap.put("vanwaar", "vanuitlenen");
        return "boekedit";
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

    private void refresh() {
        exemplaren = null;
        geselecteerdExemplaar = null;
    }
}
