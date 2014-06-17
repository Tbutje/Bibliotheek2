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
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class BoekUitlenenBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private Exemplaar exemplaar;
    private Medewerker geselecteerdeMedewerker;

    public Medewerker getGeselecteerdeMedewerker() {
        return geselecteerdeMedewerker;
    }

    public void setGeselecteerdeMedewerker(Medewerker geselecteerdeMedewerker) {
        this.geselecteerdeMedewerker = geselecteerdeMedewerker;
    }
    private List<Medewerker> medewerkers = null;

    public List<Medewerker> getMedewerkers() {
        if (medewerkers == null) {
            try {
                Database database = new Database();
                this.medewerkers = database.getMedewerkers();
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
        }
        return medewerkers;
    }

    public Exemplaar getExemplaar() {

        if (exemplaar == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.exemplaar = (Exemplaar) sessionMap.get("exemplaar");
            sessionMap.remove("exemplaar");
        }
        return exemplaar;
    }

    public String terug() {
        return "naaruitleneninnemen";
    }

    public String uitlenen() {
        Uitlening uitlening = new Uitlening();
        uitlening.setExemplaar_ID(exemplaar.getExemplaar_ID());
        uitlening.setMedewerker_ID(geselecteerdeMedewerker.getMedewerker_ID());
        uitlening.setDatumUitleen(LocalDate.now());

        try {
            Database database = new Database();
            database.insertUitlening(uitlening);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }
        return "naaruitleneninnemen";
    }
}
