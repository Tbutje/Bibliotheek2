/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class MedBoekenBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private Medewerker medewerker;
    private List<Uitlening> uitleningen = null;
    private Uitlening geselecteerdeUitlening = null;

    public Uitlening getGeselecteerdeUitlening() {
        return geselecteerdeUitlening;
    }

    public Medewerker getMedewerker() {

        if (medewerker == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.medewerker = (Medewerker) sessionMap.get("medewerker");
            sessionMap.remove("medewerker");
        }
        return medewerker;
    }

    public List<Uitlening> getUitleningen() {

        if (uitleningen == null && medewerker != null) {
            try {
                Database database = new Database();
                uitleningen = database.getUitleningen(medewerker.getMedewerker_ID());

            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
        }

        return uitleningen;
    }

    public void selecteer(Uitlening uitlening) {
        this.geselecteerdeUitlening = uitlening;
    }

    public void refresh() {
        uitleningen = null;
        geselecteerdeUitlening = null;
    }

    //***** Buttons for functions**********************************************

    public void boekInnemen() {
        // bij innemen wordt systeem datum op huidige datum gezet
        try {
            geselecteerdeUitlening.setDatumInleveren(LocalDate.now());
            Database database = new Database();
            database.updateUitlening(geselecteerdeUitlening);

        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }
        refresh();
    }

    public void innemenTerugDraaien() {
        // hier wordt datum op null gezet
        try {
            geselecteerdeUitlening.setDatumInleveren(null);
            Database database = new Database();
            database.updateUitlening(geselecteerdeUitlening);

        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }
        refresh();
    }

    public String terug() {
        return "naarmedewerkers";
    }

    //***** END Buttons for functions******************************************
}
