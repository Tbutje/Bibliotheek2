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
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class MedewerkersBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private List<Medewerker> medewerkers = null;
    private Medewerker geselecteerdeMedewerker = null;

    public List<Medewerker> getMedewerkers() {

        if (medewerkers == null) {

            try {
                Database database = new Database();
                medewerkers = database.getMedewerkers();
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
        }

        return medewerkers;
    }

    public Medewerker getGeselecteerdeMedewerker() {
        return geselecteerdeMedewerker;
    }

    public String medewerkerWijzigen() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("medewerker", this.geselecteerdeMedewerker);
        return "medewerkeredit";
    }

    public String toonBoeken() {
        // ga naar boeken overzicht van de medewerker
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("medewerker", this.geselecteerdeMedewerker);
        return "toonboeken";
    }

    public String medewerkerToevoegen() {
       return "medewerkeredit";

    }

    public void selecteerMedewerker(Medewerker medewerker) {
        this.geselecteerdeMedewerker = medewerker;
    }

    public String terug() {
        return "naarboeken";
    }
}
