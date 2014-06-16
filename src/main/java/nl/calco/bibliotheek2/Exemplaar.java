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
public class Exemplaar implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private Integer exemplaar_ID;
    private Integer boek_ID;
    private Integer exemplaarVolgnummer;
    private LocalDate datumAanschaf;
    private Boolean vermist;
    private Uitlening huidigeUitlening = null;
    private Boolean uitlening_set = false;
    private Boek boek = null;

    public Boek getBoek() {
        if (boek == null) {
            try {
                Database database = new Database();
                this.boek = database.getBoek(boek_ID);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
            
        }
        return boek;
    }

    public Uitlening getHuidigeUitlening() {
        if (huidigeUitlening == null && exemplaar_ID != null && !uitlening_set) {
            try {
                Database database = new Database();
                this.huidigeUitlening = database.getHuidigeUitlening(this.exemplaar_ID);
                this.uitlening_set = true;

            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());

            }
        }
        return huidigeUitlening;
    }

    public Integer getExemplaar_ID() {
        return exemplaar_ID;
    }

    public void setExemplaar_ID(Integer exemplaar_ID) {
        this.exemplaar_ID = exemplaar_ID;
    }

    public Integer getBoek_ID() {
        return boek_ID;
    }

    public void setBoek_ID(Integer boek_ID) {
        this.boek_ID = boek_ID;
    }

    public Integer getExemplaarVolgnummer() {
        return exemplaarVolgnummer;
    }

    public void setExemplaarVolgnummer(Integer exemplaarVolgnummer) {
        this.exemplaarVolgnummer = exemplaarVolgnummer;
    }

    public LocalDate getDatumAanschaf() {
        return datumAanschaf;
    }

    public void setDatumAanschaf(LocalDate datumAanschaf) {
        this.datumAanschaf = datumAanschaf;
    }

    public Boolean isVermist() {
        return vermist;
    }

    public void setVermist(Boolean vermist) {
        this.vermist = vermist;
    }

}
