/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
public class Uitlening implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());
    private Integer exemplaar_ID;
    private Integer medewerker_ID;
    private LocalDate datumUitleen;
    private Medewerker medewerker = null;
    private LocalDate datumInleveren;

    //voeg ook boek toe voor extra info
    private Boek boek = null;
    private Exemplaar exemplaar = null;

    public Exemplaar getExemplaar() {

        if (exemplaar == null) {
            try {
                Database database = new Database();
                this.exemplaar = database.getExemplaar(exemplaar_ID);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

        }
        return exemplaar;
    }

    public Boek getBoek() {
        if (boek == null) {
            if(exemplaar == null){
                getExemplaar();
            }
            try {
                Database database = new Database();
                this.boek = database.getBoek(exemplaar.getBoek_ID());
 
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

        }
        return boek;
    }

    public LocalDate getDatumInleveren() {
        return datumInleveren;
    }

    public void setDatumInleveren(LocalDate datumInleveren) {
        this.datumInleveren = datumInleveren;
    }

    public Medewerker getMedewerker() {
        if (medewerker == null && medewerker_ID != null) {
            try {
                Database database = new Database();
                this.medewerker = database.getMedewerker(medewerker_ID);

            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());

            }

        }
        return medewerker;
    }

    public Integer getExemplaar_ID() {
        return exemplaar_ID;
    }

    public void setExemplaar_ID(Integer exemplaar_ID) {
        this.exemplaar_ID = exemplaar_ID;
    }

    public Integer getMedewerker_ID() {
        return medewerker_ID;
    }

    public void setMedewerker_ID(Integer medewerker_ID) {
        this.medewerker_ID = medewerker_ID;
    }

    public LocalDate getDatumUitleen() {
        return datumUitleen;
    }

    public void setDatumUitleen(LocalDate datumUitleen) {
        this.datumUitleen = datumUitleen;
    }

}
