/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
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
public class MedewerkerEditBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private Medewerker medewerker;

    public Medewerker getMedewerker() {

        if (medewerker == null) {

            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            // if medewerker is in session map than load
            // if not than create new medewerker
            if (sessionMap.containsKey("medewerker")) {
                this.medewerker = (Medewerker) sessionMap.get("medewerker");
                sessionMap.remove("medewerker");
            } else {
                this.medewerker = new Medewerker();
            }

        }
        return medewerker;
    }

    private Boolean validate() {
        // returns true if input correct
        Boolean result = true;

        // check email
        if (!medewerker.getEmail().matches("\\w+@\\w+\\.\\w+")) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Email niet correct"));
            result = false;
        }
        return result;
    }

    public String opslaan() {

        // valideer eerrst input, email voornamelijk
        if (validate()) {
            // check if medewerker_id is null, zo niet dan bestaat die al
            if (medewerker.getMedewerker_ID() == null) {
                // hij bestaat nog niet dus insert
                try {
                    Database database = new Database();
                    database.insertMedewerker(medewerker);

                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                }
            } else {
                //bij bestaat al dus update
                try {
                    Database database = new Database();
                    database.updateMedewerker(medewerker);

                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                }

            }
            return "medewerkers";
        }
        return null;
    }

    public String annuleren() {
        return "medewerkers";
    }
}
